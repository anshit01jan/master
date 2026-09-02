package com.scrum.auth.service;

import com.scrum.auth.entity.RecoveryToken;
import com.scrum.auth.entity.User;
import com.scrum.auth.exception.TokenExpiredException;
import com.scrum.auth.repository.RecoveryTokenRepository;
import com.scrum.auth.repository.UserRepository;
import com.scrum.auth.security.PasswordHasher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PasswordResetService {
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);
    private final RecoveryTokenRepository tokenRepo;
    private final UserRepository userRepo;
    private final PasswordHasher hasher;
    private final EmailService mailer;
    private final AuditService auditor;
    private final AuthPolicyService policy;

    @Value("${app.security.token.expiry.minutes:2}")
    private int expMin;

    @Value("${app.base.url:https://localhost:8443}")
    private String baseUrl;

    public PasswordResetService(RecoveryTokenRepository tokenRepo,
                                UserRepository userRepo,
                                PasswordHasher hasher,
                                EmailService mailer,
                                AuditService auditor,
                                AuthPolicyService policy) {
        this.tokenRepo = tokenRepo;
        this.userRepo = userRepo;
        this.hasher = hasher;
        this.mailer = mailer;
        this.auditor = auditor;
        this.policy = policy;
    }

    @Transactional
    public void initiatePasswordReset(String email, String ip, HttpServletRequest request) {
        logger.info("Reset initiated");
        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null) {
            Map<String, Object> details = new HashMap<>();
            details.put("email", email);
            details.put("reason", "User not found");
            auditor.logEvent(null, "PASSWORD_RESET_ATTEMPT", "WARNING", details, request);
            return;
        }

        invalidateAllTokens(user);
        String token = UUID.randomUUID().toString();
        LocalDateTime exp = LocalDateTime.now().plusMinutes(expMin);

        RecoveryToken rt = new RecoveryToken(user, hasher.hash(token), exp);
        tokenRepo.save(rt);

        String link = baseUrl + "/auth/reset-password?token=" + token;
        sendMail(user.getEmail(), link);

        Map<String, Object> details = new HashMap<>();
        details.put("email", email);
        auditor.logEvent(user.getId(), "PASSWORD_RESET_INITIATED", "INFO", details, request);
    }

    @Transactional
    public void resetPassword(String token, String newVal, String ip, HttpServletRequest request) {
        logger.info("Reset complete");
        if (!policy.isPasswordStrong(newVal)) {
            throw new IllegalArgumentException("Requirement not met");
        }

        RecoveryToken rt = tokenRepo.findValidToken(hasher.hash(token), LocalDateTime.now())
                .orElseThrow(() -> new TokenExpiredException("Invalid"));

        User user = rt.getUser();
        user.setPasswordHash(hasher.hash(newVal));
        user.setUpdatedAt(LocalDateTime.now());
        userRepo.save(user);

        rt.setUsed(true);
        tokenRepo.save(rt);

        invalidateAllTokens(user);

        Map<String, Object> details = new HashMap<>();
        details.put("email", user.getEmail());
        auditor.logEvent(user.getId(), "PASSWORD_RESET_COMPLETED", "INFO", details, request);
    }

    @Transactional
    public void invalidateAllTokens(User user) {
        tokenRepo.invalidateAllTokensForUser(user);
    }

    public boolean isValid(String token) {
        return tokenRepo.findValidToken(hasher.hash(token), LocalDateTime.now()).isPresent();
    }

    private void sendMail(String email, String link) {
        try {
            String subj = "Reset";
            String body = "<html><p>Click: <a href='" + link + "'>here</a></p></html>";
            mailer.sendPasswordResetEmail(email, link);
        } catch (Exception e) {
            logger.error("Mail failed", e);
            throw new RuntimeException("Mail failed");
        }
    }
}
