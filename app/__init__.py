import os
import logging

from flask import Flask, jsonify, request

from app.notifications import InMemoryNotificationSink
from app.routes import register_routes
from app.services import AuthService
from app.store import InMemoryAuthStore
from app.testing import MutableClock


LOGGER = logging.getLogger(__name__)


def create_app(test_config=None):
    app = Flask(__name__)
    app.config.from_mapping(
        SECRET_KEY="scrum-50-local-secret",
        TESTING=False,
        AUTOMATION_MODE=True,
        AUTOMATION_RUN_ID=os.environ.get("AUTOMATION_RUN_ID", ""),
    )

    if test_config:
        app.config.update(test_config)

    store = app.config.get("AUTH_STORE") or InMemoryAuthStore.seeded()
    notification_log = (
        app.config.get("NOTIFICATION_LOG")
        or os.environ.get("NOTIFICATION_LOG")
        or "target/automation/reset-notifications.log"
    )
    notifications = app.config.get("NOTIFICATION_SINK") or InMemoryNotificationSink(log_path=notification_log)
    clock = app.config.get("CLOCK") or MutableClock()
    auth_service = AuthService(store=store, notification_sink=notifications, clock=clock)

    app.auth_store = store
    app.notification_sink = notifications
    app.auth_service = auth_service
    app.mutable_clock = clock

    register_routes(app)

    @app.post("/__automation__/reset")
    def automation_reset():
        app.auth_store = InMemoryAuthStore.seeded()
        app.auth_service.store = app.auth_store
        if hasattr(app.notification_sink, "clear"):
            try:
                app.notification_sink.clear()
            except Exception:
                LOGGER.exception("Notification sink cleanup failed during automation reset")
        if hasattr(app.mutable_clock, "reset"):
            app.mutable_clock.reset()
        return jsonify({"status": "reset"})

    @app.post("/__automation__/clock/advance")
    def automation_advance_clock():
        payload = request.get_json(silent=True) or {}
        seconds = int(payload.get("seconds", 0))
        if hasattr(app.mutable_clock, "advance"):
            app.mutable_clock.advance(seconds)
        return jsonify({"status": "advanced", "seconds": seconds})

    @app.get("/__automation__/health")
    def automation_health():
        return jsonify({"status": "ok", "runId": app.config.get("AUTOMATION_RUN_ID", "")})

    return app