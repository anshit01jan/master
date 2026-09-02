package com.scrum.auth.service;

import com.scrum.auth.entity.LockoutTracking;
import com.scrum.auth.entity.User;
import com.scrum.auth.exception.AccountLockedException;
import com.scrum.auth.exception.InvalidCredentialsException;
import com.scrum.auth.exception.RateLimitExceededException;
import com.scrum.auth.repository.LockoutTrackingRepository;
import com.scrum.auth.repository.UserRepository;
import com.scrum.auth.security.PasswordHasher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final int LOCKOUT_DURATION_MINUTES = 1;

    private final UserRepository userRepository;
    private final LockoutTrackingRepository lockoutTrackingRepository;
    private final PasswordHasher passwordHasher;
    private final AuditService auditService;

    public AuthenticationService(UserRepository userRepository,
                                  LockoutTrackingRepository lockoutTrackingRepository,
                                  PasswordHasher passwordHasher,
                                  AuditService auditService) {
        this.userRepository = userRepository;
        this.lockoutTrackingRepository = lockoutTrackingRepository;
        this.passwordHasher = passwordHasher;
        this.auditService = auditService;
    }

    @Transactional
    public User authenticateUser(String email, String password, String ipAddress, HttpServletRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    Map<String, Object> details = new HashMap<>();
                    details.put("email", email);
                    details.put("reason", "User not found");
                    auditService.logEvent(null, "LOGIN_FAILED", "WARNING", details, request);
                    return new InvalidCredentialsException("Invalid email or password");
                });

        checkAccountLockout(user, ipAddress, request);

        if (!passwordHasher.verify(password, user.getPasswordHash())) {
            handleFailedLogin(user, ipAddress, request);
            throw new InvalidCredentialsException("Invalid email or password");
        }

        handleSuccessfulLogin(user, ipAddress, request);
        return user;
    }

    private void checkAccountLockout(User user, String ipAddress, HttpServletRequest request) {
        LockoutTracking tracking = lockoutTrackingRepository.findByUser(user)
                .orElse(null);

        if (tracking != null && tracking.isLocked()) {
            if (tracking.getLockoutUntil().isAfter(LocalDateTime.now())) {
                Map<String, Object> details = new HashMap<>();
                details.put("email", user.getEmail());
                details.put("lockedUntil", tracking.getLockoutUntil().toString());
                auditService.logEvent(user.getId(), "ACCOUNT_LOCKED", "CRITICAL", details, request);
                throw new AccountLockedException("Account is locked due to multiple failed login attempts. Try again after "
                        + tracking.getLockoutUntil(), tracking.getLockoutUntil());
            } else {
                unlockAccount(tracking);
            }
        }
    }

    private void handleFailedLogin(User user, String ipAddress, HttpServletRequest request) {
        LockoutTracking tracking = lockoutTrackingRepository.findByUser(user)
                .orElse(new LockoutTracking(user, ipAddress));

        tracking.incrementFailedAttempts();
        tracking.setLastAttemptAt(LocalDateTime.now());
        tracking.setIpAddress(ipAddress);

        if (tracking.getFailedAttempts() >= MAX_LOGIN_ATTEMPTS) {
            tracking.setLockoutUntil(LocalDateTime.now().plusMinutes(LOCKOUT_DURATION_MINUTES));
            Map<String, Object> details = new HashMap<>();
            details.put("email", user.getEmail());
            details.put("failedAttempts", tracking.getFailedAttempts());
            auditService.logEvent(user.getId(), "ACCOUNT_LOCKED", "CRITICAL", details, request);
        }

        lockoutTrackingRepository.save(tracking);

        Map<String, Object> details = new HashMap<>();
        details.put("email", user.getEmail());
        details.put("reason", "Invalid password");
        auditService.logEvent(user.getId(), "LOGIN_FAILED", "WARNING", details, request);
    }

    private void handleSuccessfulLogin(User user, String ipAddress, HttpServletRequest request) {
        lockoutTrackingRepository.findByUser(user).ifPresent(this::unlockAccount);
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        Map<String, Object> details = new HashMap<>();
        details.put("email", user.getEmail());
        auditService.logEvent(user.getId(), "LOGIN_SUCCESS", "INFO", details, request);
    }

    private void unlockAccount(LockoutTracking tracking) {
        tracking.reset();
        lockoutTrackingRepository.save(tracking);
    }
}
