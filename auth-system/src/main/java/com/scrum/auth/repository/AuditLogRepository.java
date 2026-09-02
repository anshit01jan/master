package com.scrum.auth.repository;

import com.scrum.auth.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Modifying
    @Query("DELETE FROM AuditLog al WHERE al.createdAt < :cutoffTime")
    int deleteOldLogs(@Param("cutoffTime") LocalDateTime cutoffTime);
}
