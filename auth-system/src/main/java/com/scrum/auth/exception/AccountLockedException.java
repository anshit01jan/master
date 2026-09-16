package com.scrum.auth.exception;

import java.time.LocalDateTime;

public class AccountLockedException extends AuthException {

    private final LocalDateTime lockoutUntil;

    public AccountLockedException(String message, LocalDateTime lockoutUntil) {
        super(message);
        this.lockoutUntil = lockoutUntil;
    }

    public LocalDateTime getLockoutUntil() {
        return lockoutUntil;
    }
}
