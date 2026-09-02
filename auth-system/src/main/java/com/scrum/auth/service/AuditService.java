package com.scrum.auth.service;

import com.scrum.auth.entity.AuditLog;
import com.scrum.auth.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuditService {

    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Async
    @Transactional
    public void logEvent(Long userId, String eventType, String severity, Map<String, Object> details, HttpServletRequest request) {
        try {
            String ipAddress = maskIpAddress(getClientIpAddress(request));
            String userAgent = request.getHeader("User-Agent");
            String detailsJson = buildDetailsJson(details);

            AuditLog auditLog = new AuditLog(userId, eventType, severity, detailsJson, ipAddress, userAgent);
            auditLogRepository.save(auditLog);

            logger.info("Audit log created - EventType: {}, Severity: {}, UserId: {}", eventType, severity, userId);
        } catch (Exception e) {
            logger.error("Failed to create audit log", e);
        }
    }

    @Transactional
    public void cleanupOldLogs(int retentionDays) {
        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(retentionDays);
        int deletedCount = auditLogRepository.deleteOldLogs(cutoffTime);
        logger.info("Deleted {} old audit logs older than {}", deletedCount, cutoffTime);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String maskIpAddress(String ipAddress) {
        if (ipAddress == null) return null;
        String[] parts = ipAddress.split("\\.");
        if (parts.length == 4) {
            return parts[0] + "." + parts[1] + "." + parts[2] + ".XXX";
        }
        return ipAddress;
    }

    private String buildDetailsJson(Map<String, Object> details) {
        if (details == null) {
            details = new HashMap<>();
        }
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : details.entrySet()) {
            if (!first) json.append(",");
            json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
            first = false;
        }
        json.append("}");
        return json.toString();
    }
}
