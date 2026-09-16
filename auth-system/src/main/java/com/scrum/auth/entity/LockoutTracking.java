package com.scrum.auth.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lockout_tracking", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_lockout_until", columnList = "lockout_until")
})
public class LockoutTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_lockout_user", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"))
    private User user;

    @Column(nullable = false)
    private Integer failedAttempts = 0;

    @Column
    private LocalDateTime lockoutUntil;

    @Column(nullable = false)
    private LocalDateTime lastAttemptAt;

    @Column
    private String ipAddress;

    @Version
    @Column(nullable = false)
    private Long version = 0L;

    @PrePersist
    @PreUpdate
    protected void onPersistOrUpdate() {
        if (lastAttemptAt == null) {
            lastAttemptAt = LocalDateTime.now();
        }
    }

    public LockoutTracking() {}

    public LockoutTracking(User user) {
        this.user = user;
        this.failedAttempts = 0;
        this.lastAttemptAt = LocalDateTime.now();
    }

    public LockoutTracking(User user, String ipAddress) {
        this.user = user;
        this.failedAttempts = 0;
        this.lastAttemptAt = LocalDateTime.now();
        this.ipAddress = ipAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(Integer failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public LocalDateTime getLockoutUntil() {
        return lockoutUntil;
    }

    public void setLockoutUntil(LocalDateTime lockoutUntil) {
        this.lockoutUntil = lockoutUntil;
    }

    public LocalDateTime getLastAttemptAt() {
        return lastAttemptAt;
    }

    public void setLastAttemptAt(LocalDateTime lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public boolean isLocked() {
        return lockoutUntil != null && lockoutUntil.isAfter(LocalDateTime.now());
    }

    public LocalDateTime getLockoutExpiresAt() {
        return lockoutUntil;
    }

    public void incrementFailedAttempts() {
        this.failedAttempts = (this.failedAttempts == null ? 0 : this.failedAttempts) + 1;
    }

    public void reset() {
        this.failedAttempts = 0;
        this.lockoutUntil = null;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
