package com.scrum.auth.dto;

public class RateLimitResponse {
    private boolean limited;
    private long remaining;
    private long resetAt;

    public RateLimitResponse() {}

    public RateLimitResponse(boolean limited, long remaining, long resetAt) {
        this.limited = limited;
        this.remaining = remaining;
        this.resetAt = resetAt;
    }

    public boolean isLimited() {
        return limited;
    }

    public void setLimited(boolean limited) {
        this.limited = limited;
    }

    public long getRemaining() {
        return remaining;
    }

    public void setRemaining(long remaining) {
        this.remaining = remaining;
    }

    public long getResetAt() {
        return resetAt;
    }

    public void setResetAt(long resetAt) {
        this.resetAt = resetAt;
    }
}
