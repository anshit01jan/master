# Implementation Plan: Secure Authentication System (SCRUM-31)

**Document Version:** 1.0  
**Date:** 2026-09-02  
**Status:** APPROVED FOR EXECUTION  
**Based On:** architecture.md (v1.0, fully approved with 15 clarifications resolved)

---

## Plan Scope

This implementation plan breaks the approved secure authentication system architecture into an ordered, dependency-based task list. The plan enables a development team to build a production-ready login system with account lockout, password reset, audit logging, and rate limiting.

**Scope Includes:**
- Database design and schema initialization
- Core service layer (authentication, lockout, reset, hashing, policies)
- Security layer (rate limiting, input validation, error handling)
- Support services (email, audit logging, user management)
- Data access layer (4 DAOs with proper abstractions)
- Controller layer (HTTP endpoints for login, recovery, reset, logout)
- View layer (4 Thymeleaf/JSP templates)
- Configuration and integration points
- Testing and validation setup

**Scope Excludes:**
- Admin dashboards or user management UI
- Role-based access control (beyond basic session authentication)
- Multi-factor authentication (MFA)
- OAuth/SAML integration
- Mobile app or API-first design

**Technology Stack (Confirmed):**
- Backend: Spring Boot 3.x + Spring Security
- Language: Java 17 LTS
- Database: MySQL 8.0 with HikariCP connection pooling
- View Layer: Thymeleaf templating
- Email: Spring Mail + JavaMail (async delivery)
- Hashing: BCryptPasswordEncoder (strength 12)
- Testing: JUnit 5, Mockito, Spring Test with H2 in-memory DB
- Build: Maven 3.9+
- Audit Logging: SLF4J + Logback
- Execution Environment: Localhost (MVP, single-instance)

---

## Dependency-Ordered Task List

Tasks are organized by execution order, reflecting build dependencies. Each task includes:
- Task ID and name
- Description and acceptance criteria
- Dependencies (tasks that must complete first)
- Estimated complexity (Low/Medium/High/XHigh)
- Artifacts produced

---

### **PHASE 1: FOUNDATION & CONFIGURATION**

#### Task 1.1: Project Setup & Maven Configuration

**Description:**
Initialize Maven project with Spring Boot 3.x parent POM. Define all dependencies required for authentication, security, validation, testing, email, and logging. Configure Maven plugins for compilation, testing, and packaging.

**Dependencies:** None (first task)

**Acceptance Criteria:**
- ✓ pom.xml created with Spring Boot 3.x parent (latest LTS)
- ✓ Dependencies included: spring-boot-starter-web, spring-boot-starter-security, spring-boot-starter-data-jpa, mysql-connector-java, spring-boot-starter-mail, spring-boot-starter-thymeleaf, hibernate-validator, logback, junit-jupiter, mockito
- ✓ Maven compiler plugin configured for Java 17 source/target
- ✓ Maven build runs successfully: `mvn clean install`
- ✓ Plugins included: maven-compiler-plugin, maven-surefire-plugin, maven-shade-plugin (if needed for packaging)

**Complexity:** Low

**Artifacts:**
- `pom.xml` (with all dependencies declared)
- Maven project structure (src/main/java, src/test/java, etc.)

---

#### Task 1.2: Application Properties & Environment Configuration

**Description:**
Create application.properties files with Spring Boot configuration for server, database, email, security, and logging settings. All sensitive values externalized to environment.

**Dependencies:** Task 1.1 (pom.xml exists)

**Acceptance Criteria:**
- ✓ `src/test/resources/application.properties` created with defaults
- ✓ Application properties include: server.port, server.servlet.context-path, spring.datasource.url, spring.jpa.properties.hibernate.dialect
- ✓ HikariCP connection pool configured: hikari.maximum-pool-size=20, minimum-idle=5, idle-timeout=600000, max-lifetime=1800000
- ✓ HTTPS/TLS configured with certificate references (values from environment)
- ✓ Session timeout configured: server.servlet.session.timeout=30m
- ✓ Email service properties configured (host, port, username from environment)
- ✓ Logging configuration: logging.level.root=INFO, logging.level.com.example.auth=DEBUG, logging.file.name
- ✓ All sensitive values use environment variables for runtime configuration
- ✓ `.env.example` provided as template for developers

**Complexity:** Low

**Artifacts:**
- `src/test/resources/application.properties`
- `src/test/resources/env.properties`
- `.env.example` (template for developers)
- Configuration documentation (comments in properties files)

---

#### Task 1.3: Database Schema Creation & Initialization

**Description:**
Write SQL DDL scripts to create all tables (users, lockout_tracking, recovery_tokens, audit_logs, rate_limit_tracking) with proper constraints, indexes, and relationships. Test schema creation against local MySQL instance.

**Dependencies:** Task 1.2 (database connection properties defined)

**Acceptance Criteria:**
- ✓ SQL migration script created: `src/main/resources/db/migration/V1__init_schema.sql`
- ✓ Users table created with columns: id (PK), username (UNIQUE), email (UNIQUE), auth_hash, created_at, updated_at
- ✓ Lockout_tracking table created with columns: id (PK), user_id (FK), failed_attempts, locked, lock_timestamp, last_attempt, version (for optimistic locking)
- ✓ Recovery_tokens table created with columns: id (PK), user_id (FK), token (UNIQUE), expiration_time, used, created_at
- ✓ Audit_logs table created with columns: id (PK), event_type, username, ip_address, timestamp, details, status, user_id (FK)
- ✓ Rate_limit_tracking table created with columns: id (PK), ip_address, username, timestamp, success
- ✓ Foreign key constraints added with ON DELETE CASCADE
- ✓ Indexes created on: users(username), users(email), lockout_tracking(user_id), lockout_tracking(locked, lock_timestamp), recovery_tokens(token), recovery_tokens(user_id, expiration_time), audit_logs(username), audit_logs(timestamp)
- ✓ Schema tested: migration script runs without errors
- ✓ Tables verified: SHOW TABLES returns 5 tables

**Complexity:** Low

**Artifacts:**
- `src/main/resources/db/migration/V1__init_schema.sql`
- Schema documentation (ERD diagram as comment)

---

#### Task 1.4: Spring Boot Application Bootstrap & Configuration Class

**Description:**
Create main Spring Boot application class and SecurityConfiguration bean for HTTPS redirect, session management, and basic security setup.

**Dependencies:** Task 1.1, 1.2, 1.3 (all configuration in place)

**Acceptance Criteria:**
- ✓ Main application class created: `AuthApplicationMain.java` with `@SpringBootApplication`
- ✓ SecurityConfiguration bean created: `SecurityConfiguration.java` with @Configuration
- ✓ PasswordEncoder bean defined: `BCryptPasswordEncoder(12)`
- ✓ HTTPS redirect configured: HTTP redirects to HTTPS
- ✓ Session management configured: HttpSession with 30-minute timeout, HttpOnly flag, Secure flag, SameSite policy
- ✓ Session creation policy: IF_REQUIRED (creates session only on successful login)
- ✓ Application starts successfully: `mvn spring-boot:run` runs without errors
- ✓ Health endpoint accessible: GET /actuator/health returns UP status

**Complexity:** Low

**Artifacts:**
- `src/main/java/com/example/auth/AuthApplicationMain.java`
- `src/main/java/com/example/auth/config/SecurityConfiguration.java`

---

### **PHASE 2: DATA ACCESS LAYER & ENTITY MODELS**

#### Task 2.1: Domain Entity Models & JPA Mappings

**Description:**
Create entity classes for User, LockoutTracking, RecoveryToken, AuditLog with JPA annotations. Define relationships (One-to-Many from User to others).

**Dependencies:** Task 1.3 (database schema defined)

**Acceptance Criteria:**
- ✓ User.java entity created with @Entity, @Table("users"), fields: id, username, email, authHash, createdAt, updatedAt
- ✓ LockoutTracking.java entity created with fields: id, userId, failedAttempts, locked, lockTimestamp, lastAttempt, version (for optimistic locking)
- ✓ RecoveryToken.java entity created with fields: id, userId, token, expirationTime, used, createdAt
- ✓ AuditLog.java entity created with fields: id, eventType, username, ipAddress, timestamp, details, status, userId
- ✓ RateLimitTracking.java entity created with fields: id, ipAddress, username, timestamp, success
- ✓ Foreign key relationships defined with @ManyToOne and optional @OneToMany
- ✓ Cascade behavior defined: CascadeType.REMOVE for lockout/token/audit on user delete
- ✓ Entities auto-mapped by Hibernate with auto-generated primary keys
- ✓ Unit test verifies entity creation without errors

**Complexity:** Low

**Artifacts:**
- `src/main/java/com/example/auth/entity/User.java`
- `src/main/java/com/example/auth/entity/LockoutTracking.java`
- `src/main/java/com/example/auth/entity/RecoveryToken.java`
- `src/main/java/com/example/auth/entity/AuditLog.java`
- `src/main/java/com/example/auth/entity/RateLimitTracking.java`

---

#### Task 2.2: Repository Interfaces (Spring Data JPA)

**Description:**
Create Spring Data Repository interfaces for each entity. Define custom query methods needed for authentication and security checks.

**Dependencies:** Task 2.1 (entities defined)

**Acceptance Criteria:**
- ✓ UserRepository extends JpaRepository<User, Long>
  - Methods: findByUsername(String), findByEmail(String), existsByUsername(String)
- ✓ LockoutRepository extends JpaRepository<LockoutTracking, Long>
  - Methods: findByUserId(Long), updateFailedAttempts(Long, int), updateLockStatus(Long, boolean), findLockedAccounts()
- ✓ RecoveryTokenRepository extends JpaRepository<RecoveryToken, Long>
  - Methods: findByToken(String), findByUserId(Long), deleteExpired(), markAsUsed(String)
- ✓ AuditLogRepository extends JpaRepository<AuditLog, Long>
  - Methods: findByUsername(String), findByIpAddress(String), findByEventType(String), deleteOlderThan(Date)
- ✓ RateLimitRepository extends JpaRepository<RateLimitTracking, Long>
  - Methods: findByIpAddress(String), findByUsername(String), countRecentAttempts(String, Date), deleteOlderThan(Date)
- ✓ All queries use @Query for complex operations
- ✓ Unit tests verify repository existence and basic CRUD operations

**Complexity:** Low

**Artifacts:**
- `src/main/java/com/example/auth/repository/UserRepository.java`
- `src/main/java/com/example/auth/repository/LockoutRepository.java`
- `src/main/java/com/example/auth/repository/RecoveryTokenRepository.java`
- `src/main/java/com/example/auth/repository/AuditLogRepository.java`
- `src/main/java/com/example/auth/repository/RateLimitRepository.java`

---

#### Task 2.3: Custom Exception Classes

**Description:**
Create custom exception hierarchy for authentication, validation, and system errors. Will be caught by ErrorHandler in Phase 4.

**Dependencies:** None (standalone exceptions)

**Acceptance Criteria:**
- ✓ Base exception: AuthException extends RuntimeException
- ✓ InvalidCredentialsException, AccountLockedException, WeakPasswordException, TokenExpiredException, TokenNotFoundException, UserNotFoundException, RateLimitException, ValidationException created
- ✓ Each exception includes message and optional cause
- ✓ Exceptions used in services (Task 3.x) for clear error signaling

**Complexity:** Low

**Artifacts:**
- `src/main/java/com/example/auth/exception/AuthException.java`
- `src/main/java/com/example/auth/exception/InvalidCredentialsException.java`
- `src/main/java/com/example/auth/exception/AccountLockedException.java`
- `src/main/java/com/example/auth/exception/WeakPasswordException.java`
- `src/main/java/com/example/auth/exception/TokenExpiredException.java`
- `src/main/java/com/example/auth/exception/TokenNotFoundException.java`
- `src/main/java/com/example/auth/exception/UserNotFoundException.java`
- `src/main/java/com/example/auth/exception/RateLimitException.java`
- `src/main/java/com/example/auth/exception/ValidationException.java`

---

### **PHASE 3: CORE SERVICE LAYER**

#### Task 3.1: AuthHasher Service (Bcrypt Integration)

**Description:**
Create AuthHasher service to encapsulate bcrypt hashing and verification logic. Single responsibility: hash and verify.

**Dependencies:** Task 1.4 (PasswordEncoder bean available via Spring)

**Acceptance Criteria:**
- ✓ AuthHasher.java service created with @Service
- ✓ Methods: hash(String input) → String bcryptHash, matches(String raw, String hash) → Boolean
- ✓ Uses BCryptPasswordEncoder bean injected via constructor
- ✓ Hash verification always compares full hashes (prevent timing attacks)
- ✓ Unit tests verify: hash is reproducible, matches() returns true for correct value, matches() returns false for incorrect value
- ✓ No sensitive values logged to console

**Complexity:** Low

**Artifacts:**
- `src/main/java/com/example/auth/service/AuthHasher.java`
- `src/test/java/com/example/auth/service/AuthHasherTest.java` (unit tests)

---

#### Task 3.2: AuthPolicyService (Validation)

**Description:**
Create AuthPolicyService to validate credential strength during reset flow. Enforce 8+ chars, 1 uppercase, 1 lowercase, 1 special character.

**Dependencies:** Task 2.3 (WeakPasswordException defined)

**Acceptance Criteria:**
- ✓ AuthPolicyService.java service created with @Service
- ✓ Method: validateStrength(String credential) → throws WeakPasswordException if weak
- ✓ Validation rules hardcoded: length 8-128, requires uppercase, lowercase, special character
- ✓ Regex pattern enforces all requirements
- ✓ Clear error message returned: strength requirements listed
- ✓ Unit tests verify: strong inputs pass validation, weak inputs throw exception (test 5+ weak cases)

**Complexity:** Low

**Artifacts:**
- `src/main/java/com/example/auth/service/AuthPolicyService.java`
- `src/test/java/com/example/auth/service/AuthPolicyServiceTest.java` (unit tests)

---

#### Task 3.3: AuthResetService (Token Generation & Validation)

**Description:**
Create AuthResetService to generate reset tokens (256-bit SecureRandom), validate tokens, and execute reset flow.

**Dependencies:** Task 2.2 (RecoveryTokenRepository), Task 3.1 (AuthHasher), Task 3.2 (AuthPolicyService), Task 2.3 (exceptions)

**Acceptance Criteria:**
- ✓ AuthResetService.java service created with @Service, @Transactional
- ✓ Method: generateResetToken(Long userId) → String token
  - Invalidates all previous tokens for user
  - Generates 32 bytes of SecureRandom
  - Base64 encodes (URL-safe)
  - Result: 43-character alphanumeric token
  - Stores in DB with expiration_time = NOW + 120 seconds
  - Returns token string
- ✓ Method: validateToken(String token) → RecoveryToken
  - Finds token by token string
  - Checks: token exists, used=false, expiration_time > NOW
  - Returns token object if valid, throws TokenExpiredException or TokenNotFoundException
- ✓ Method: resetCredential(String token, String newCredential) → void
  - Calls validateToken() to verify token
  - Calls authPolicyService.validateStrength() to verify credential
  - Hashes credential and updates user
  - Marks token used and invalidates all user tokens
  - Calls auditService.logResetSuccess()
- ✓ Unit tests verify: token generation is unique, token validation rejects expired tokens, reset updates hash correctly

**Complexity:** Medium

**Artifacts:**
- `src/main/java/com/example/auth/service/AuthResetService.java`
- `src/test/java/com/example/auth/service/AuthResetServiceTest.java` (unit tests with mocked repositories)

---

#### Task 3.4: UserService (User Lookup & Management)

**Description:**
Create UserService to handle user lookups by username/email. Single responsibility: user data retrieval.

**Dependencies:** Task 2.2 (UserRepository)

**Acceptance Criteria:**
- ✓ UserService.java service created with @Service
- ✓ Methods: findByUsernameOrEmail(String identifier) → User, findByUsername(String) → User, findByEmail(String) → User, findById(Long) → User, findByIdOrThrow(Long) → User (throws UserNotFoundException)
- ✓ findByUsernameOrEmail() handles both cases: tries username first, then email
- ✓ All methods delegate to UserRepository
- ✓ UserNotFoundException thrown if user not found
- ✓ Unit tests verify: correct user returned for valid input, exception thrown for invalid

**Complexity:** Low

**Artifacts:**
- `src/main/java/com/example/auth/service/UserService.java`
- `src/test/java/com/example/auth/service/UserServiceTest.java` (unit tests)

---

#### Task 3.5: LockoutService (Account Lockout Management)

**Description:**
Create LockoutService to manage failed login attempts, lockout enforcement, auto-unlock logic. Single responsibility: lockout state management.

**Dependencies:** Task 2.2 (LockoutRepository), Task 2.3 (AccountLockedException), Task 3.1 (AuditService placeholder)

**Acceptance Criteria:**
- ✓ LockoutService.java service created with @Service, @Transactional
- ✓ Method: isAccountLocked(String username) → Boolean
  - Finds lockout record for user
  - Checks if locked AND lock_timestamp > NOW - 60 seconds
  - If locked but >= 60s elapsed, calls auto-unlock() and returns false
  - Returns locked status
- ✓ Method: recordFailedAttempt(String username) → void
  - Increments failed_attempts counter
  - If failed_attempts >= 3, locks account
  - Uses optimistic locking: version field incremented on each update, retry if conflict
- ✓ Method: resetFailedAttempts(String username) → void
  - Sets failed_attempts=0, locked=false, lock_timestamp=NULL
- ✓ Method: unlockAccount(String username) → void (called after 60s auto-unlock)
  - Sets locked=false, failed_attempts=0, lock_timestamp=NULL
- ✓ Unit tests verify: 3 attempts trigger lock, 4th attempt throws AccountLockedException, auto-unlock after 61 seconds works

**Complexity:** Medium

**Artifacts:**
- `src/main/java/com/example/auth/service/LockoutService.java`
- `src/test/java/com/example/auth/service/LockoutServiceTest.java` (unit tests with mocked repositories)

---

#### Task 3.6: RateLimitService (Brute-Force Prevention)

**Description:**
Create RateLimitService to enforce rate limiting by IP and username. Database-backed (not in-memory) for multi-instance consistency.

**Dependencies:** Task 2.2 (RateLimitRepository), Task 2.3 (RateLimitException)

**Acceptance Criteria:**
- ✓ RateLimitService.java service created with @Service, @Transactional
- ✓ Method: isRateLimited(String ipAddress, String username) → Boolean
  - Counts failures for IP in last 5 minutes
  - Counts failures for username in last 1 hour
  - Returns true if either limit exceeded (>5 per IP, >10 per username)
- ✓ Method: recordAttempt(String ipAddress, String username, Boolean success) → void
  - Inserts into rate_limit_tracking
  - Triggers database INSERT; no in-memory state
- ✓ Scheduled task (background): deleteOldRecords() runs daily
  - Deletes rate_limit_tracking entries older than 2 hours
- ✓ Unit tests verify: 5th IP-based failure triggers limit, 11th username-based failure triggers limit

**Complexity:** Medium

**Artifacts:**
- `src/main/java/com/example/auth/service/RateLimitService.java`
- `src/test/java/com/example/auth/service/RateLimitServiceTest.java` (unit tests)

---

#### Task 3.7: AuthService (Core Authentication Logic)

**Description:**
Create AuthService to validate credentials. Single responsibility: authenticate username/credential. Delegates lockout to LockoutService.

**Dependencies:** Task 2.2 (UserRepository), Task 3.1 (AuthHasher), Task 3.4 (UserService), Task 3.5 (LockoutService), Task 3.8 (AuditService), Task 2.3 (exceptions)

**Acceptance Criteria:**
- ✓ AuthService.java service created with @Service, @Transactional
- ✓ Method: authenticate(String username, String input) → UserDetails
  1. Check if locked: lockoutService.isAccountLocked(username)
  2. Find user: userService.findByUsernameOrEmail(username)
  3. Verify input: authHasher.matches(input, user.authHash)
  4. Success: reset counter, log successful auth, return user details
- ✓ Generic error message always returned (do not reveal which check failed)
- ✓ Unit tests verify: valid input returns user, invalid input throws exception, locked account throws exception

**Complexity:** Medium

**Artifacts:**
- `src/main/java/com/example/auth/service/AuthService.java`
- `src/test/java/com/example/auth/service/AuthServiceTest.java` (unit tests with mocked dependencies)

---

#### Task 3.8: AuditService (Security Event Logging)

**Description:**
Create AuditService to log all security events to both SLF4J console and audit_logs table.

**Dependencies:** Task 2.2 (AuditLogRepository)

**Acceptance Criteria:**
- ✓ AuditService.java service created with @Service, @Transactional
- ✓ Methods for all audit events:
  - logSuccessfulAuth, logFailedAttempt, logAccountLocked, logAccountUnlocked
  - logResetRequested, logEmailSent, logEmailSendFailed
  - logResetTokenValidated, logResetTokenExpired, logResetSuccess, logLogout
- ✓ Each method logs to SLF4J at INFO level (success events) or WARN level (failures)
- ✓ Each method inserts into audit_logs table with event_type, username, ip_address, timestamp, status, details
- ✓ No sensitive values or tokens logged
- ✓ Unit tests verify: events logged to repository, SLF4J call made

**Complexity:** Low

**Artifacts:**
- `src/main/java/com/example/auth/service/AuditService.java`
- `src/test/java/com/example/auth/service/AuditServiceTest.java` (unit tests)

---

#### Task 3.9: EmailService (Async Email Delivery)

**Description:**
Create EmailService to send reset emails asynchronously via @Async. Includes failure logging.

**Dependencies:** Task 3.8 (AuditService)

**Acceptance Criteria:**
- ✓ EmailService.java service created with @Service
- ✓ Method: sendResetEmail(User user, String resetToken) → void (marked @Async)
  - Builds reset URL with token
  - Creates MimeMessage with subject, HTML body
  - Sends via mailSender (Spring Mail bean)
  - On success: auditService.logEmailSent()
  - On failure (MailException): auditService.logEmailSendFailed()
- ✓ EmailService bean returns immediately (non-blocking) to caller
- ✓ Async executor configured: ThreadPoolExecutor(core=5, max=10, queue=100)
- ✓ SMTP configured via environment properties
- ✓ Unit tests verify: email message created correctly, async method executes

**Complexity:** Medium

**Artifacts:**
- `src/main/java/com/example/auth/service/EmailService.java`
- `src/main/java/com/example/auth/config/AsyncConfiguration.java` (executor bean)
- `src/test/java/com/example/auth/service/EmailServiceTest.java` (unit tests)

---

### **PHASE 4: SECURITY & CROSS-CUTTING CONCERNS**

#### Task 4.1: Input Validation Filter & Validator

**Description:**
Create InputValidator component and InputValidationFilter to validate all user input before business logic.

**Dependencies:** Task 2.3 (ValidationException)

**Acceptance Criteria:**
- ✓ InputValidator.java utility class created with validation methods
- ✓ Methods for each field type: validateUsername(), validateEmail(), validateInput()
- ✓ Regex patterns defined in architecture approved
- ✓ InputValidationFilter servlet filter created
  - Intercepts requests to /login, /recovery, /reset endpoints
  - Validates request parameters before delegating to controller
  - Returns 400 Bad Request with generic error message on validation failure
- ✓ Filter order: SecurityFilter → RateLimitFilter → InputValidationFilter → Controller
- ✓ Unit tests verify: valid inputs pass validation, invalid inputs throw exceptions

**Complexity:** Low

**Artifacts:**
- `src/main/java/com/example/auth/validation/InputValidator.java`
- `src/main/java/com/example/auth/filter/InputValidationFilter.java`
- `src/test/java/com/example/auth/validation/InputValidatorTest.java` (unit tests)

---

#### Task 4.2: Rate Limit Filter (Servlet Filter)

**Description:**
Create RateLimitFilter servlet filter to enforce rate limiting at filter level BEFORE business logic.

**Dependencies:** Task 3.6 (RateLimitService), Task 2.3 (RateLimitException)

**Acceptance Criteria:**
- ✓ RateLimitFilter.java servlet filter created
  - Extracts client IP address from request (handle X-Forwarded-For header)
  - Extracts username from request parameter
  - Checks rate limit: rateLimit.isRateLimited(ipAddress, username)
  - If limited: return HTTP 429 with Retry-After header
  - If OK: record attempt and continue filter chain
- ✓ Filter order configured: apply BEFORE InputValidationFilter
- ✓ Logs rate limit breaches
- ✓ Integration tests verify: 6th request from same IP rejected, 11th request for same username rejected

**Complexity:** Medium

**Artifacts:**
- `src/main/java/com/example/auth/filter/RateLimitFilter.java`
- `src/test/java/com/example/auth/filter/RateLimitFilterTest.java` (integration tests)

---

#### Task 4.3: Error Handler & Exception Mapping

**Description:**
Create GlobalErrorHandler (@ControllerAdvice) to catch all exceptions and return generic error messages.

**Dependencies:** Task 2.3 (all exception classes)

**Acceptance Criteria:**
- ✓ GlobalErrorHandler.java created with @ControllerAdvice
- ✓ @ExceptionHandler methods map each exception to HTTP status and message (per architecture)
- ✓ Each handler logs full exception details at ERROR level (internal only)
- ✓ Each handler returns ErrorResponse DTO with generic message (no stack trace)
- ✓ Integration tests verify: exceptions caught, generic messages returned, HTTP status codes correct

**Complexity:** Low

**Artifacts:**
- `src/main/java/com/example/auth/controller/GlobalErrorHandler.java`
- `src/main/java/com/example/auth/dto/ErrorResponse.java` (DTO)
- `src/test/java/com/example/auth/controller/GlobalErrorHandlerTest.java` (integration tests)

---

#### Task 4.4: Security Filter (HTTPS, CSRF, Session Security)

**Description:**
Create or configure SecurityFilter for HTTPS redirect, CSRF protection, secure session cookies, and security headers.

**Dependencies:** Task 1.4 (SecurityConfiguration)

**Acceptance Criteria:**
- ✓ SecurityConfiguration extended with filter chain:
  - HTTPS redirect: HTTP redirects to HTTPS
  - CSRF protection enabled (Spring Security default)
  - Session cookie secure flags: HttpOnly flag, Secure flag, SameSite policy
  - Security headers: X-Content-Type-Options, X-Frame-Options, X-XSS-Protection
- ✓ Login endpoint configured: POST /login
- ✓ Logout endpoint configured: GET /logout
- ✓ Integration tests verify: HTTP request redirects to HTTPS, secure cookies set, CSRF token validated

**Complexity:** Low

**Artifacts:**
- `src/main/java/com/example/auth/config/SecurityConfiguration.java` (updated)

---

#### Task 4.5: Session Management Controller & Configuration

**Description:**
Create SessionController as thin wrapper around Spring Security's HttpSession. Provides utility methods for session lifecycle.

**Dependencies:** Task 1.4 (SecurityConfiguration)

**Acceptance Criteria:**
- ✓ SessionController.java created with @Controller
- ✓ Methods: invalidateSession(), createSession(), isSessionValid(), getSessionAttribute()
- ✓ Session ID regeneration on login
- ✓ Session timeout: 30 minutes (configured in SecurityConfiguration)
- ✓ Logging hooks for session lifecycle events
- ✓ Integration tests verify: session created on login, invalidated on logout

**Complexity:** Low

**Artifacts:**
- `src/main/java/com/example/auth/controller/SessionController.java`
- `src/test/java/com/example/auth/controller/SessionControllerTest.java` (integration tests)

---

### **PHASE 5: CONTROLLER LAYER**

#### Task 5.1: Authentication Controller (Login/Logout/Recovery/Reset)

**Description:**
Create AuthController to handle all authentication endpoints.

**Dependencies:** Task 3.7 (AuthService), Task 3.8 (AuditService), Task 3.9 (EmailService), Task 3.3 (AuthResetService), Task 4.5 (SessionController), Task 4.1 (InputValidator), Task 4.3 (ErrorHandler)

**Acceptance Criteria:**
- ✓ AuthController.java created with @Controller
- ✓ Endpoints implemented:
  - GET /login → Display login form
  - POST /login → Authenticate and create session
  - GET /logout → Invalidate session
  - GET /recovery → Display recovery form
  - POST /recovery → Generate reset token and send email
  - GET /reset?token={token} → Display reset form
  - POST /reset → Update user record
- ✓ All forms use CSRF tokens
- ✓ Client IP tracked for audit logs
- ✓ Integration tests verify: login creates session, logout invalidates, reset flow works end-to-end

**Complexity:** High

**Artifacts:**
- `src/main/java/com/example/auth/controller/AuthController.java`
- `src/test/java/com/example/auth/controller/AuthControllerTest.java` (integration tests)

---

### **PHASE 6: VIEW LAYER**

#### Task 6.1: Login View (Thymeleaf Template)

**Description:**
Create LoginView Thymeleaf template for login form.

**Dependencies:** Task 1.4 (Spring Boot + Thymeleaf configured)

**Acceptance Criteria:**
- ✓ Template: `src/main/resources/templates/login.html`
- ✓ Form submits POST to /login with CSRF token
- ✓ Fields: username, input
- ✓ Links: "Forgot input?" → /recovery
- ✓ Error messages displayed if login fails
- ✓ Bootstrap styling for responsive design

**Complexity:** Low

**Artifacts:**
- `src/main/resources/templates/login.html`

---

#### Task 6.2: Recovery View (Thymeleaf Template)

**Description:**
Create RecoveryView Thymeleaf template for recovery form.

**Dependencies:** Task 1.4 (Spring Boot + Thymeleaf configured)

**Acceptance Criteria:**
- ✓ Template: `src/main/resources/templates/recovery.html`
- ✓ Form submits POST to /recovery with CSRF token
- ✓ Field: email
- ✓ Button: "Send Reset Link"
- ✓ Success message: "Check your email for reset link"
- ✓ Bootstrap styling

**Complexity:** Low

**Artifacts:**
- `src/main/resources/templates/recovery.html`

---

#### Task 6.3: Reset View (Thymeleaf Template)

**Description:**
Create ResetView Thymeleaf template for reset form.

**Dependencies:** Task 1.4 (Spring Boot + Thymeleaf configured)

**Acceptance Criteria:**
- ✓ Template: `src/main/resources/templates/reset.html`
- ✓ Form submits POST to /reset with CSRF token and token parameter
- ✓ Fields: new input, confirm input
- ✓ Display token status: "Token valid" or error
- ✓ Button: "Reset Input"
- ✓ Success message: "Reset successful, please login"

**Complexity:** Low

**Artifacts:**
- `src/main/resources/templates/reset.html`

---

#### Task 6.4: Dashboard View (Placeholder)

**Description:**
Create Dashboard view as post-login success destination.

**Dependencies:** Task 1.4 (Spring Boot + Thymeleaf configured)

**Acceptance Criteria:**
- ✓ Template: `src/main/resources/templates/dashboard.html`
- ✓ Display: "Welcome, [username]"
- ✓ Button: "Logout" → /logout
- ✓ Protected route: Requires active session

**Complexity:** Low

**Artifacts:**
- `src/main/resources/templates/dashboard.html`

---

### **PHASE 7: INTEGRATION & CONFIGURATION**

#### Task 7.1: Spring Security Configuration Finalization

**Description:**
Complete and test SecurityConfiguration with all filters wired together.

**Dependencies:** Task 4.1-4.5 (all filters/configs), Task 5.1 (AuthController)

**Acceptance Criteria:**
- ✓ Filter chain order correct
- ✓ Endpoint protection configured
- ✓ HTTPS redirect, session security, CSRF protection enabled
- ✓ Error page configured
- ✓ Application starts without errors
- ✓ Health check passes

**Complexity:** Medium

**Artifacts:**
- `src/main/java/com/example/auth/config/SecurityConfiguration.java` (finalized)

---

#### Task 7.2: Database Migration Script (Flyway Integration)

**Description:**
Integrate Flyway for automated database schema versioning.

**Dependencies:** Task 1.3 (SQL schema), Task 1.2 (application.properties)

**Acceptance Criteria:**
- ✓ Flyway dependency added to pom.xml
- ✓ Migration file in place: `src/main/resources/db/migration/V1__init_schema.sql`
- ✓ Flyway configured in application.properties
- ✓ On startup, Flyway automatically runs migrations
- ✓ Database schema verified after startup

**Complexity:** Low

**Artifacts:**
- `pom.xml` (Flyway dependency added)
- `src/main/resources/db/migration/V1__init_schema.sql` (confirmed)

---

#### Task 7.3: Logging Configuration (Logback)

**Description:**
Create logback-spring.xml configuration for structured logging with audit trail and log rotation.

**Dependencies:** Task 3.8 (AuditService logs to SLF4J)

**Acceptance Criteria:**
- ✓ Configuration file: `src/main/resources/logback-spring.xml`
- ✓ Appenders: console, audit file, application log file
- ✓ Log levels configured: root=INFO, com.example.auth=DEBUG
- ✓ Log format specified
- ✓ File rotation: daily, max size 100MB, retention 30 days

**Complexity:** Low

**Artifacts:**
- `src/main/resources/logback-spring.xml`

---

#### Task 7.4: Application Environment Properties Configuration

**Description:**
Create environment template files for configuration management.

**Dependencies:** Task 1.2 (application.properties structure)

**Acceptance Criteria:**
- ✓ `.env.example` template provided for developers
- ✓ `src/test/resources/env.properties` with test environment values
- ✓ Application.properties references environment variables correctly
- ✓ Template documents all required environment variables
- ✓ Template does not include sensitive values

**Complexity:** Low

**Artifacts:**
- `.env.example` (template for developers)
- `src/test/resources/env.properties`

---

### **PHASE 8: TESTING & VALIDATION**

#### Task 8.1: Unit Test Suite (Services & Utils)

**Description:**
Create comprehensive unit tests for all service classes.

**Dependencies:** Task 3.1-3.9 (all services defined)

**Acceptance Criteria:**
- ✓ Unit tests for each service (9 test classes)
- ✓ Test coverage: 80%+ for service layer
- ✓ All tests pass: `mvn test`
- ✓ Mocks used for external dependencies

**Complexity:** High

**Artifacts:**
- `src/test/java/com/example/auth/service/*Test.java` (9 test classes)

---

#### Task 8.2: Integration Test Suite (Controllers & Filters)

**Description:**
Create integration tests that verify HTTP request/response cycles.

**Dependencies:** Task 5.1 (AuthController), Task 4.1-4.5 (all filters), Task 8.1 (unit tests passing)

**Acceptance Criteria:**
- ✓ Integration tests using @SpringBootTest with MockMvc
- ✓ Test coverage: 70%+ for controller layer
- ✓ All tests pass: `mvn verify`
- ✓ Uses H2 in-memory database for isolation

**Complexity:** High

**Artifacts:**
- `src/test/java/com/example/auth/controller/*Test.java` (integration test classes)
- `src/test/resources/application-test.properties`

---

#### Task 8.3: End-to-End Test Scenarios

**Description:**
Document and execute end-to-end test scenarios covering complete user journeys.

**Dependencies:** Task 8.1-8.2 (unit and integration tests passing), Task 5.1 (AuthController ready)

**Acceptance Criteria:**
- ✓ 7 test scenarios documented and executed
- ✓ Each scenario verified
- ✓ Audit logs checked for each scenario
- ✓ All scenarios pass

**Complexity:** Medium

**Artifacts:**
- `docs/E2E_TEST_SCENARIOS.md`

---

#### Task 8.4: Performance & Load Testing

**Description:**
Execute performance tests to verify response time targets under load.

**Dependencies:** Task 8.2 (integration tests passing), Task 7.2 (database schema), Task 7.3 (logging configured)

**Acceptance Criteria:**
- ✓ Performance test tool configured (Apache JMeter or Gatling)
- ✓ Test scenarios: single user, 10 concurrent, 50 concurrent, 100 concurrent users
- ✓ Results document: response time distribution, throughput, error rate
- ✓ Single user target met

**Complexity:** Medium

**Artifacts:**
- `docs/PERFORMANCE_TEST_RESULTS.md`

---

#### Task 8.5: Security Testing

**Description:**
Perform security tests to verify defenses against common attacks.

**Dependencies:** Task 5.1 (AuthController), Task 4.1-4.5 (security filters)

**Acceptance Criteria:**
- ✓ SQL injection test: validation error (not database error)
- ✓ XSS test: validation error (not executed)
- ✓ CSRF test: 403 error without CSRF token
- ✓ Brute-force test: 429 rate limit error
- ✓ Token guessing test: 400 error (no enumeration hint)
- ✓ Session fixation test: Session ID changes after login
- ✓ HTTPS enforcement test: HTTP redirects to HTTPS
- ✓ All tests pass

**Complexity:** High

**Artifacts:**
- `docs/SECURITY_TEST_RESULTS.md`

---

### **PHASE 9: DEPLOYMENT & DELIVERY**

#### Task 9.1: Docker Configuration (Optional MVP)

**Description:**
Create Dockerfile and docker-compose.yml to containerize application.

**Dependencies:** Task 7.2-7.4 (all configuration files), Task 1.4 (application ready to run)

**Acceptance Criteria:**
- ✓ Dockerfile created for application
- ✓ docker-compose.yml created with services: app, mysql, mailhog
- ✓ Application builds and runs in Docker
- ✓ Can login via HTTPS endpoint
- ✓ Emails captured by Mailhog

**Complexity:** Medium

**Artifacts:**
- `Dockerfile`
- `docker-compose.yml`
- `.dockerignore`

---

#### Task 9.2: Build & Package

**Description:**
Build application as executable JAR with Maven.

**Dependencies:** Task 8.1-8.5 (all tests), Task 1.1 (pom.xml)

**Acceptance Criteria:**
- ✓ Maven build: `mvn clean package`
- ✓ All tests pass: 0 failures
- ✓ JAR created: `target/auth-system-1.0.0.jar`
- ✓ JAR executable: `java -jar` starts application
- ✓ Application health check passes

**Complexity:** Low

**Artifacts:**
- `target/auth-system-1.0.0.jar` (executable)

---

#### Task 9.3: Documentation

**Description:**
Create comprehensive documentation for deployment and operations.

**Dependencies:** All tasks completed

**Acceptance Criteria:**
- ✓ README.md: Project overview, quick start
- ✓ DEPLOYMENT.md: How to deploy
- ✓ CONFIGURATION.md: Environment variables, configuration options
- ✓ OPERATIONS.md: Monitoring, logging, troubleshooting
- ✓ API.md: HTTP endpoints, request/response examples
- ✓ TEST_STRATEGY.md: Test coverage, running tests

**Complexity:** Low

**Artifacts:**
- `README.md`
- `DEPLOYMENT.md`
- `CONFIGURATION.md`
- `OPERATIONS.md`
- `API.md`
- `TEST_STRATEGY.md`

---

## Recommended Execution Order

### **Critical Path (Blocking Dependencies)**

1. **Phase 1** (Foundation): Tasks 1.1 → 1.2 → 1.3 → 1.4 (sequential, 2-3 days)
2. **Phase 2** (Data Access): Tasks 2.1 → 2.2 → 2.3 (can parallelize 2.3) (1-2 days)
3. **Phase 3** (Services): Tasks 3.1-3.9 with strategic parallelization (4-5 days)
4. **Phase 4** (Security): Tasks 4.1 → 4.2 → 4.3 → 4.4 → 4.5 (2-3 days)
5. **Phase 5** (Controllers): Task 5.1 (1-2 days)
6. **Phase 6** (Views): Tasks 6.1-6.4 (can parallelize) (1 day)
7. **Phase 7** (Integration): Tasks 7.1 → 7.2 → 7.3 → 7.4 (1-2 days)
8. **Phase 8** (Testing): Tasks 8.1 → 8.2 → 8.3 → 8.4 → 8.5 (2-3 days)
9. **Phase 9** (Deployment): Tasks 9.1 → 9.2 → 9.3 (1 day)

**Total Duration (Critical Path): ~18-25 days** for a team of 1-2 developers

---

## Blocked Tasks

**No permanently blocked tasks identified.** All task dependencies are linear and resolvable through sequential execution.

---

## Success Criteria

Implementation is complete when:

1. ✓ All 9 phases completed with 0 open blockers
2. ✓ All unit tests pass (80%+ code coverage)
3. ✓ All integration tests pass (70%+ controller coverage)
4. ✓ E2E test scenarios verified (7/7 scenarios pass)
5. ✓ Performance target met
6. ✓ Security tests pass: SQL injection, XSS, CSRF, brute-force blocked
7. ✓ Application builds without warnings
8. ✓ Docker image builds and runs locally
9. ✓ Audit logs capture all security events
10. ✓ Documentation complete and accurate

---

**Document End**
