package com.scrum.auth.repository;

import com.scrum.auth.entity.LockoutTracking;
import com.scrum.auth.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface LockoutTrackingRepository extends JpaRepository<LockoutTracking, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT lt FROM LockoutTracking lt WHERE lt.user = :user")
    Optional<LockoutTracking> findByUserForUpdate(@Param("user") User user);

    Optional<LockoutTracking> findByUser(User user);

    @Modifying
    @Query("DELETE FROM LockoutTracking lt WHERE lt.lockoutUntil < :cutoffTime AND lt.failedAttempts = 0")
    int deleteExpiredRecords(@Param("cutoffTime") LocalDateTime cutoffTime);
}
