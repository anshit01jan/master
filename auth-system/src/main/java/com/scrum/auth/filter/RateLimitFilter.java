package com.scrum.auth.filter;

import com.scrum.auth.exception.RateLimitExceededException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitFilter.class);
    private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private static final long WINDOW_SIZE_MS = 60000;

    private final Map<String, RequestCounter> requestCounters = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String ipAddress = getClientIpAddress(httpRequest);
        String key = ipAddress + ":" + httpRequest.getRequestURI();

        if (isRateLimited(key)) {
            logger.warn("Rate limit exceeded for key: {}", key);
            httpResponse.setStatus(429); // Too Many Requests
            httpResponse.getWriter().write("Too many requests. Please try again later.");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isRateLimited(String key) {
        long currentTime = System.currentTimeMillis();

        requestCounters.entrySet().removeIf(entry ->
            currentTime - entry.getValue().windowStart > WINDOW_SIZE_MS
        );

        RequestCounter counter = requestCounters.computeIfAbsent(key,
            k -> new RequestCounter(currentTime));

        if (currentTime - counter.windowStart > WINDOW_SIZE_MS) {
            counter.reset(currentTime);
        }

        return counter.count.incrementAndGet() > MAX_REQUESTS_PER_MINUTE;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private static class RequestCounter {
        final AtomicInteger count = new AtomicInteger(0);
        long windowStart;

        RequestCounter(long windowStart) {
            this.windowStart = windowStart;
        }

        void reset(long newWindowStart) {
            this.count.set(0);
            this.windowStart = newWindowStart;
        }
    }
}
