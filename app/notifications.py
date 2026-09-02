from dataclasses import dataclass, field
from pathlib import Path


@dataclass
class ResetNotification:
    email: str
    reset_link: str


@dataclass
class InMemoryNotificationSink:
    sent_notifications: list[ResetNotification] = field(default_factory=list)
    log_path: str | None = None

    def __post_init__(self):
        if self.log_path:
            self._write_log("")

    def send_reset_link(self, email, reset_link):
        notification = ResetNotification(email=email, reset_link=reset_link)
        self.sent_notifications.append(notification)
        if self.log_path:
            try:
                Path(self.log_path).parent.mkdir(parents=True, exist_ok=True)
                with Path(self.log_path).open("a", encoding="utf-8") as handle:
                    handle.write(f"{notification.email}\t{notification.reset_link}\n")
            except OSError:
                pass

    def clear(self):
        self.sent_notifications.clear()
        if self.log_path:
            self._write_log("")

    def _write_log(self, content):
        log_file = Path(self.log_path)
        try:
            log_file.parent.mkdir(parents=True, exist_ok=True)
            log_file.write_text(content, encoding="utf-8")
        except OSError:
            pass