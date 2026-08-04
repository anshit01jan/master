import shutil
import tempfile
import unittest
from datetime import datetime, timedelta
from pathlib import Path
from urllib.parse import parse_qs, urlparse

from app import create_app
from app.notifications import InMemoryNotificationSink
from app.store import InMemoryAuthStore


class FixedClock:
    def __init__(self, now):
        self.current = now

    def __call__(self):
        return self.current

    def advance(self, seconds):
        self.current += timedelta(seconds=seconds)


class FailingNotificationSink:
    def clear(self):
        raise OSError("notification log unavailable")


class Scrum50Tests(unittest.TestCase):
    def setUp(self):
        self.clock = FixedClock(datetime(2026, 8, 3, 12, 0, 0))
        self.store = InMemoryAuthStore.seeded()
        self.notifications = InMemoryNotificationSink()
        self.app = create_app(
            {
                "TESTING": True,
                "AUTH_STORE": self.store,
                "NOTIFICATION_SINK": self.notifications,
                "CLOCK": self.clock,
            }
        )
        self.client = self.app.test_client()

    def test_login_redirects_to_dashboard_on_success(self):
        response = self.client.post(
            "/login",
            data={"username": "scrum50", "password": "Password1!"},
            follow_redirects=False,
        )
        self.assertEqual(response.status_code, 302)
        self.assertIn("/dashboard", response.headers["Location"])

    def test_login_requires_mandatory_fields(self):
        response = self.client.post("/login", data={"username": "", "password": ""})
        self.assertIn(b"Username and password are required.", response.data)

    def test_invalid_credentials_are_denied(self):
        response = self.client.post(
            "/login",
            data={"username": "scrum50", "password": "WrongPass1!"},
        )
        self.assertIn(b"Invalid username or password.", response.data)

    def test_account_locks_after_two_failed_attempts_and_unlocks_after_sixty_seconds(self):
        first = self.client.post("/login", data={"username": "scrum50", "password": "WrongPass1!"})
        second = self.client.post("/login", data={"username": "scrum50", "password": "WrongPass1!"})
        locked = self.client.post("/login", data={"username": "scrum50", "password": "Password1!"})

        self.assertIn(b"Invalid username or password.", first.data)
        self.assertIn(b"Account locked after 2 failed attempts.", second.data)
        self.assertIn(b"Account is locked. Try again later.", locked.data)

        self.clock.advance(60)
        unlocked = self.client.post("/login", data={"username": "scrum50", "password": "Password1!"})
        self.assertEqual(unlocked.status_code, 302)

    def test_forgot_password_accepts_username_or_email_and_sends_reset_link(self):
        response = self.client.post("/forgot-password", data={"identifier": "scrum50"}, follow_redirects=True)
        self.assertIn(b"If the account exists, a reset link has been sent.", response.data)
        self.assertEqual(len(self.notifications.sent_notifications), 1)

        second = self.client.post("/forgot-password", data={"identifier": "scrum50@example.com"})
        self.assertIn(b"If the account exists, a reset link has been sent.", second.data)

    def test_only_one_active_reset_token_exists_per_user(self):
        self.client.post("/forgot-password", data={"identifier": "scrum50"})
        first_link = self.notifications.sent_notifications[-1].reset_link
        first_token = parse_qs(urlparse(first_link).query)["token"][0]

        self.client.post("/forgot-password", data={"identifier": "scrum50"})
        second_link = self.notifications.sent_notifications[-1].reset_link
        second_token = parse_qs(urlparse(second_link).query)["token"][0]

        self.assertNotEqual(first_token, second_token)
        self.assertIsNone(self.store.get_by_reset_token(first_token))
        self.assertIsNotNone(self.store.get_by_reset_token(second_token))

    def test_reset_token_expires_after_sixty_seconds(self):
        self.client.post("/forgot-password", data={"identifier": "scrum50"})
        token = parse_qs(urlparse(self.notifications.sent_notifications[-1].reset_link).query)["token"][0]

        self.clock.advance(61)
        response = self.client.get(f"/reset-password?token={token}")
        self.assertIn(b"Reset token has expired.", response.data)

    def test_password_reset_requires_strong_password_and_invalidates_previous_token(self):
        self.client.post("/forgot-password", data={"identifier": "scrum50"})
        token = parse_qs(urlparse(self.notifications.sent_notifications[-1].reset_link).query)["token"][0]

        weak_response = self.client.post(
            "/reset-password",
            data={"token": token, "new_password": "weak"},
        )
        self.assertIn(b"Password must be at least 8 characters long.", weak_response.data)

        strong_response = self.client.post(
            "/reset-password",
            data={"token": token, "new_password": "NewPass1!"},
            follow_redirects=True,
        )
        self.assertIn(b"Password reset successfully. Please log in again.", strong_response.data)
        self.assertIsNone(self.store.get_by_reset_token(token))

    def test_successful_reset_allows_login_with_new_password(self):
        self.client.post("/forgot-password", data={"identifier": "scrum50"})
        token = parse_qs(urlparse(self.notifications.sent_notifications[-1].reset_link).query)["token"][0]

        self.client.post("/reset-password", data={"token": token, "new_password": "BetterPass1!"})
        login_response = self.client.post(
            "/login",
            data={"username": "scrum50", "password": "BetterPass1!"},
            follow_redirects=False,
        )
        self.assertEqual(login_response.status_code, 302)
        self.assertIn("/dashboard", login_response.headers["Location"])

    def test_automation_reset_recreates_missing_notification_log_directory(self):
        with tempfile.TemporaryDirectory() as temp_dir:
            log_path = Path(temp_dir) / "target" / "automation" / "reset-notifications.log"
            notifications = InMemoryNotificationSink(log_path=str(log_path))
            app = create_app(
                {
                    "TESTING": True,
                    "AUTH_STORE": InMemoryAuthStore.seeded(),
                    "NOTIFICATION_SINK": notifications,
                    "CLOCK": self.clock,
                }
            )
            client = app.test_client()

            shutil.rmtree(log_path.parent)

            response = client.post("/__automation__/reset")

            self.assertEqual(response.status_code, 200)
            self.assertTrue(log_path.exists())

    def test_automation_reset_ignores_notification_sink_cleanup_failures(self):
        app = create_app(
            {
                "TESTING": True,
                "AUTH_STORE": InMemoryAuthStore.seeded(),
                "NOTIFICATION_SINK": FailingNotificationSink(),
                "CLOCK": self.clock,
            }
        )
        client = app.test_client()

        response = client.post("/__automation__/reset")

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.get_json(), {"status": "reset"})


if __name__ == "__main__":
    unittest.main()