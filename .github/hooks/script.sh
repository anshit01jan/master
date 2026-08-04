#!/bin/bash

###############################################################################
# Security Guardrails Script for Test Automation Framework
# Pre and Post Tool Use Validation
# Audit and sanitization flow also protects regenerated phase artifacts.
# PR scope refresh note: comment-only touch to keep this hook script visible in PR #4.
###############################################################################

# Configuration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
HOOKS_CONFIG="$SCRIPT_DIR/hooks.json"
AUDIT_LOG="$SCRIPT_DIR/audit.log"
VIOLATIONS_LOG="$SCRIPT_DIR/violations.log"
TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')

# Color codes for output
RED='\033[0;31m'
YELLOW='\033[1;33m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

# Exit codes
EXIT_SUCCESS=0
EXIT_BLOCKED=1
EXIT_ERROR=2

###############################################################################
# Logging Functions
###############################################################################

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
    echo "[$TIMESTAMP] [INFO] $1" >> "$AUDIT_LOG"
}

log_warning() {
    echo -e "${YELLOW}[WARN]${NC} $1"
    echo "[$TIMESTAMP] [WARN] $1" >> "$AUDIT_LOG"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
    echo "[$TIMESTAMP] [ERROR] $1" >> "$AUDIT_LOG"
}

log_violation() {
    echo -e "${RED}[VIOLATION]${NC} $1"
    echo "[$TIMESTAMP] [VIOLATION] $1" >> "$VIOLATIONS_LOG"
    echo "[$TIMESTAMP] [VIOLATION] $1" >> "$AUDIT_LOG"
}

###############################################################################
# Pre-Tool Validation Functions
###############################################################################

pre_tool_validation() {
    local tool_name="$1"
    local tool_input="$2"
    
    log_info "Starting pre-tool validation for: $tool_name"
    
    # Run all pre-tool checks
    check_sensitive_data "$tool_input" || return $EXIT_BLOCKED
    validate_input_patterns "$tool_input" || return $EXIT_BLOCKED
    
    log_info "Pre-tool validation passed for: $tool_name"
    return $EXIT_SUCCESS
}

check_sensitive_data() {
    local input="$1"
    local sensitive_patterns=(
        "password"
        "apiKey"
        "api_key"
        "token"
        "secret"
        "credentials"
        "privateKey"
        "private_key"
        "bearer"
        "authorization"
    )
    
    for pattern in "${sensitive_patterns[@]}"; do
        if echo "$input" | grep -iq "$pattern"; then
            # Check if it's actually exposing a value (not just the word)
            if echo "$input" | grep -iE "$pattern\s*[:=]\s*[\"']?[a-zA-Z0-9_-]{8,}" > /dev/null; then
                log_violation "Potential sensitive data exposure detected: $pattern"
                log_error "Tool input contains sensitive data pattern: $pattern"
                return $EXIT_BLOCKED
            fi
        fi
    done
    
    log_info "Sensitive data check: PASSED"
    return $EXIT_SUCCESS
}

validate_input_patterns() {
    local input="$1"
    local malicious_patterns=(
        "eval\\("
        "exec\\("
        "<script>"
        "DROP TABLE"
        "'; DELETE FROM"
        "../../../"
        "\$(.*)"
        "`.*`"
        "rm -rf /"
        "format C:"
    )
    
    for pattern in "${malicious_patterns[@]}"; do
        if echo "$input" | grep -E "$pattern" > /dev/null; then
            log_violation "Malicious input pattern detected: $pattern"
            log_error "Input validation failed - suspicious pattern found"
            return $EXIT_BLOCKED
        fi
    done
    
    log_info "Input pattern validation: PASSED"
    return $EXIT_SUCCESS
}

###############################################################################
# Post-Tool Validation Functions
###############################################################################

post_tool_validation() {
    local tool_name="$1"
    local tool_output="$2"
    local execution_status="$3"
    local start_time="$4"
    
    log_info "Starting post-tool validation for: $tool_name"
    
    # Run all post-tool checks
    local sanitized_output
    sanitized_output=$(sanitize_output "$tool_output")
    
    check_security_violations "$sanitized_output" || log_warning "Security violations detected in output"
    cleanup_resources || log_warning "Resource cleanup incomplete"
    audit_tool_execution "$tool_name" "$execution_status" "$start_time"
    
    log_info "Post-tool validation completed for: $tool_name"
    
    # Return sanitized output
    echo "$sanitized_output"
    return $EXIT_SUCCESS
}

sanitize_output() {
    local output="$1"
    local sanitized="$output"
    
    # Redact passwords
    sanitized=$(echo "$sanitized" | sed -E 's/(password|pwd)\s*=\s*[^ ]*/\1=***REDACTED***/gi')
    
    # Redact API keys
    sanitized=$(echo "$sanitized" | sed -E 's/(api[_-]?key|apikey)\s*[:=]\s*[^ ]*/\1=***REDACTED***/gi')
    
    # Redact tokens
    sanitized=$(echo "$sanitized" | sed -E 's/(token|bearer)\s*[:=]\s*[^ ]*/\1=***REDACTED***/gi')
    
    # Redact email addresses
    sanitized=$(echo "$sanitized" | sed -E 's/[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}/***EMAIL_REDACTED***/g')
    
    # Redact potential secrets (long alphanumeric strings that look like tokens)
    sanitized=$(echo "$sanitized" | sed -E 's/\b[A-Za-z0-9_-]{32,}\b/***POTENTIAL_SECRET_REDACTED***/g')
    
    log_info "Output sanitization: COMPLETED"
    echo "$sanitized"
}

check_security_violations() {
    local output="$1"
    local violations=0
    
    # Check for privilege escalation attempts
    if echo "$output" | grep -iE "sudo|admin|root|elevated" > /dev/null; then
        log_violation "Privilege escalation pattern detected in output"
        ((violations++))
    fi
    
    # Check for code injection indicators
    if echo "$output" | grep -E "eval|exec|system|Runtime\.getRuntime" > /dev/null; then
        log_violation "Code injection pattern detected in output"
        ((violations++))
    fi
    
    # Check for data exfiltration indicators (large base64 encoded data)
    if echo "$output" | grep -E "[A-Za-z0-9+/]{100,}={0,2}" > /dev/null; then
        log_warning "Large encoded data block detected - potential exfiltration"
    fi
    
    if [ $violations -gt 0 ]; then
        log_error "Security violations check: FAILED ($violations violations)"
        return $EXIT_BLOCKED
    fi
    
    log_info "Security violations check: PASSED"
    return $EXIT_SUCCESS
}

cleanup_resources() {
    local cleanup_patterns=(
        "temp_*.tmp"
        "*.credentials"
        ".env.local"
        "sensitive_*.log"
    )
    
    for pattern in "${cleanup_patterns[@]}"; do
        find "$SCRIPT_DIR/.." -name "$pattern" -type f -mtime +1 -delete 2>/dev/null
    done
    
    log_info "Resource cleanup: COMPLETED"
    return $EXIT_SUCCESS
}

###############################################################################
# Utility Functions
###############################################################################

initialize_hooks() {
    # Create audit log if it doesn't exist
    touch "$AUDIT_LOG"
    touch "$VIOLATIONS_LOG"
    
    log_info "Security hooks initialized"
    log_info "Audit log: $AUDIT_LOG"
    log_info "Violations log: $VIOLATIONS_LOG"
}

generate_report() {
    local report_file="$SCRIPT_DIR/report_$(date +%Y%m%d).txt"
    
    echo "=== Security Report ===" > "$report_file"
    echo "Generated: $TIMESTAMP" >> "$report_file"
    echo "" >> "$report_file"
    
    echo "--- Violations Summary ---" >> "$report_file"
    if [ -f "$VIOLATIONS_LOG" ]; then
        wc -l "$VIOLATIONS_LOG" >> "$report_file"
        echo "" >> "$report_file"
        tail -20 "$VIOLATIONS_LOG" >> "$report_file"
    else
        echo "No violations recorded" >> "$report_file"
    fi
    
    echo "" >> "$report_file"
    echo "--- Recent Audit Entries ---" >> "$report_file"
    tail -50 "$AUDIT_LOG" >> "$report_file"
    
    log_info "Security report generated: $report_file"
}

###############################################################################
# Main Execution
###############################################################################

main() {
    local action="$1"
    shift
    
    case "$action" in
        "init")
            initialize_hooks
            ;;
        "pre")
            pre_tool_validation "$@"
            ;;
        "post")
            post_tool_validation "$@"
            ;;
        "report")
            generate_report
            ;;
        "test")
            log_info "Running security hooks test..."
            pre_tool_validation "test_tool" "test input data"
            post_tool_validation "test_tool" "test output data" "success" "$(date +%s%3N)"
            log_info "Security hooks test completed"
            ;;
        *)
            echo "Usage: $0 {init|pre|post|report|test}"
            echo ""
            echo "Commands:"
            echo "  init   - Initialize security hooks and logs"
            echo "  pre    - Run pre-tool validation"
            echo "  post   - Run post-tool validation"
            echo "  report - Generate security report"
            echo "  test   - Run security hooks test"
            exit 1
            ;;
    esac
}

# Execute main function
main "$@"
