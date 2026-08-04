from dataclasses import dataclass
from datetime import datetime

from app.security import hash_password, hash_reset_token


@dataclass
class UserRecord:
    username: str
    email: str
    password_hash: str
    failed_login_attempts: int = 0
    locked_until: datetime | None = None
    reset_token_hash: str | None = None
    reset_token_expires_at: datetime | None = None


class InMemoryAuthStore:
    def __init__(self, users):
        self.users = users

    @classmethod
    def seeded(cls):
        return cls(
            users={
                "scrum50": UserRecord(
                    username="scrum50",
                    email="scrum50@example.com",
                    password_hash=hash_password("Password1!"),
                )
            }
        )

    def get_by_username(self, username):
        return self.users.get(username)

    def get_by_email(self, email):
        for user in self.users.values():
            if user.email.lower() == email.lower():
                return user
        return None

    def get_by_reset_token(self, token):
        token_hash = hash_reset_token(token)
        for user in self.users.values():
            if user.reset_token_hash == token_hash:
                return user
        return None