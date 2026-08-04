from dataclasses import dataclass
from datetime import datetime, timedelta

from app.security import generate_reset_token, hash_password, hash_reset_token, verify_password


@dataclass
class ServiceResult:
    success: bool
    message: str
    username: str | None = None


class AuthService:
    def __init__(self, store, notification_sink, clock=None):
        self.store = store
        self.notification_sink = notification_sink
        self.clock = clock or datetime.utcnow

    def now(self):
        return self.clock()

    def login(self, username, password):
        username = username.strip()
        password = password.strip()

        if not username or not password:
            return ServiceResult(False, "Username and password are required.")

        user = self.store.get_by_username(username)
        if not user:
            return ServiceResult(False, "Invalid username or password.")

        current_time = self.now()
        self._release_lock_if_expired(user, current_time)
        if user.locked_until and user.locked_until > current_time:
            return ServiceResult(False, "Account is locked. Try again later.")

        if not verify_password(user.password_hash, password):
            user.failed_login_attempts += 1
            if user.failed_login_attempts >= 2:
                user.locked_until = current_time + timedelta(seconds=60)
                return ServiceResult(False, "Account locked after 2 failed attempts.")
            return ServiceResult(False, "Invalid username or password.")

        user.failed_login_attempts = 0
        user.locked_until = None
        return ServiceResult(True, "Login successful.", username=user.username)

    def request_password_reset(self, identifier, request_root):
        identifier = identifier.strip()
        user = self.store.get_by_username(identifier) or self.store.get_by_email(identifier)
        if not user:
            return ServiceResult(True, "If the account exists, a reset link has been sent.")

        reset_token = generate_reset_token()
        token_hash = hash_reset_token(reset_token)
        user.reset_token_hash = token_hash
        user.reset_token_expires_at = self.now() + timedelta(seconds=60)

        reset_link = f"{request_root}/reset-password?token={reset_token}"
        self.notification_sink.send_reset_link(user.email, reset_link)
        return ServiceResult(True, "If the account exists, a reset link has been sent.")

    def validate_reset_token(self, token):
        if not token:
            return ServiceResult(False, "Reset token is required.")

        user = self.store.get_by_reset_token(token)
        if not user:
            return ServiceResult(False, "Reset token is invalid.")

        current_time = self.now()
        if not user.reset_token_expires_at or user.reset_token_expires_at <= current_time:
            return ServiceResult(False, "Reset token has expired.")

        return ServiceResult(True, "Reset token is valid.", username=user.username)

    def reset_password(self, token, new_password):
        token = token.strip()
        password_errors = self.password_policy_errors(new_password)
        if password_errors:
            return ServiceResult(False, " ".join(password_errors))

        token_result = self.validate_reset_token(token)
        if not token_result.success:
            return token_result

        user = self.store.get_by_reset_token(token)
        user.password_hash = hash_password(new_password)
        user.reset_token_hash = None
        user.reset_token_expires_at = None
        return ServiceResult(True, "Password reset successful.", username=user.username)

    def password_policy_errors(self, password):
        errors = []
        if len(password) < 8:
            errors.append("Password must be at least 8 characters long.")
        if not any(character.isupper() for character in password):
            errors.append("Password must include an uppercase letter.")
        if not any(character.islower() for character in password):
            errors.append("Password must include a lowercase letter.")
        if not any(character.isdigit() for character in password):
            errors.append("Password must include a digit.")
        if not any(not character.isalnum() for character in password):
            errors.append("Password must include a special character.")
        return errors

    def _release_lock_if_expired(self, user, current_time):
        if user.locked_until and user.locked_until <= current_time:
            user.locked_until = None
            user.failed_login_attempts = 0