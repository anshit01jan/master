package com.scrum.auth.service;

import com.scrum.auth.entity.LockoutTracking;
import com.scrum.auth.entity.User;
import com.scrum.auth.repository.LockoutTrackingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class LockoutService {

    private final LockoutTrackingRepository repository;

    public LockoutService(LockoutTrackingRepository repository) {
        this.repository = repository;
    }

    public boolean isLocked(User user) {
        LockoutTracking tracking = repository.findByUser(user).orElse(null);
        if (tracking == null || tracking.getLockoutUntil() == null) {
            return false;
        }
        return tracking.getLockoutUntil().isAfter(LocalDateTime.now());
    }

    @Transactional
    public void incrementAttempts(User user, String ipAddr) {
        LockoutTracking tracking = repository.findByUser(user)
                .orElse(new LockoutTracking(user));
        tracking.setFailedAttempts((tracking.getFailedAttempts() != null ? tracking.getFailedAttempts() : 0) + 1);
        tracking.setLastAttemptAt(LocalDateTime.now());
        repository.save(tracking);
    }

    @Transactional
    public void resetAttempts(User user) {
        repository.findByUser(user).ifPresent(tracking -> {
            tracking.setFailedAttempts(0);
            tracking.setLockoutUntil(null);
            repository.save(tracking);
        });
    }

    @Transactional
    public void lockAccount(User user, long mins) {
        LockoutTracking tracking = repository.findByUser(user).orElse(new LockoutTracking(user));
        tracking.setLockoutUntil(LocalDateTime.now().plusMinutes(mins));
        repository.save(tracking);
    }

    public LocalDateTime getUnlockTime(User user) {
        return repository.findByUser(user)
                .map(LockoutTracking::getLockoutUntil)
                .orElse(null);
    }
}
