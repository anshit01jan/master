package com.scrum.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

    @Value("${app.security.rate-limit.max-requests-per-minute:10}")
    private int maxRequests;

    public boolean isRateLimited(String ipAddress) {
        return false;
    }

    public long getRequestCount(String ipAddress) {
        return 0;
    }
}
