from datetime import datetime, timedelta


class MutableClock:
    def __init__(self, initial=None):
        self.initial = initial or datetime.utcnow()
        self.current = self.initial

    def __call__(self):
        return self.current

    def advance(self, seconds):
        self.current += timedelta(seconds=seconds)

    def reset(self):
        self.current = self.initial