package com.scrum.auth.repository;

import com.scrum.auth.entity.RecoveryToken;
import com.scrum.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RecoveryTokenRepository extends JpaRepository<RecoveryToken, Long> {

    Optional<RecoveryToken> findByTokenHash(String tokenHash);

    @Query("SELECT rt FROM RecoveryToken rt WHERE rt.tokenHash = :tokenHash AND rt.used = false AND rt.expiresAt > :now")
    Optional<RecoveryToken> findValidToken(@Param("tokenHash") String tokenHash, @Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE RecoveryToken rt SET rt.used = true WHERE rt.user = :user")
    void invalidateAllTokensForUser(@Param("user") User user);

    @Modifying
    @Query("DELETE FROM RecoveryToken rt WHERE rt.expiresAt < :cutoffTime OR rt.used = true")
    int deleteExpiredAndUsedTokens(@Param("cutoffTime") LocalDateTime cutoffTime);
}
