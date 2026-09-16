#!/bin/bash
###############################################################################
# Security Guardrails Hook for the SDLC Automation Workflow
#
# Invoked by Claude Code as a PreToolUse / PostToolUse hook (see
# .claude/settings.json). Reads the hook JSON payload from stdin.
#
# Usage: security-guardrails.sh {pre|post}
###############################################################################

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
AUDIT_LOG="$SCRIPT_DIR/audit.log"
VIOLATIONS_LOG="$SCRIPT_DIR/violations.log"
TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')

EXIT_SUCCESS=0
EXIT_BLOCKED=2

log_audit() {
    echo "[$TIMESTAMP] $1" >> "$AUDIT_LOG"
}

log_violation() {
    echo "[$TIMESTAMP] [VIOLATION] $1" >> "$VIOLATIONS_LOG"
    echo "[$TIMESTAMP] [VIOLATION] $1" >> "$AUDIT_LOG"
}

extract_field() {
    # extract_field <json> <field>
    local json="$1" field="$2"
    if command -v jq >/dev/null 2>&1; then
        printf '%s' "$json" | jq -r --arg f "$field" '.[$f] // empty' 2>/dev/null
        return
    fi
    printf '%s' "$json" | sed -n "s/.*\"$field\"[[:space:]]*:[[:space:]]*\"\\([^\"]*\\)\".*/\\1/p" | head -1
}

pre_tool_validation() {
    local payload
    payload="$(cat)"
    local tool_name
    tool_name="$(extract_field "$payload" tool_name)"

    check_sensitive_data "$payload" || {
        log_violation "Blocked $tool_name: potential sensitive data exposure in tool input"
        echo "Blocked: tool input appears to contain a credential/secret value (password, apiKey, token, etc.). Remove the sensitive value before retrying." >&2
        return $EXIT_BLOCKED
    }

    validate_input_patterns "$payload" || {
        log_violation "Blocked $tool_name: malicious input pattern detected"
        echo "Blocked: tool input matched a disallowed pattern (eval/exec/script injection/path traversal/etc.)." >&2
        return $EXIT_BLOCKED
    }

    log_audit "PreToolUse OK: $tool_name"
    return $EXIT_SUCCESS
}

check_sensitive_data() {
    local input="$1"
    local sensitive_patterns=(password apiKey api_key token secret credentials privateKey private_key bearer authorization)

    for pattern in "${sensitive_patterns[@]}"; do
        if echo "$input" | grep -iE "\"?${pattern}\"?[[:space:]]*[:=][[:space:]]*\"?[A-Za-z0-9_-]{8,}" > /dev/null 2>&1; then
            return 1
        fi
    done
    return 0
}

validate_input_patterns() {
    local input="$1"
    local malicious_patterns=(
        'eval\('
        'exec\('
        '<script>'
        'DROP TABLE'
        "'; DELETE FROM"
        '\.\./\.\./\.\./'
        'rm -rf /'
        'format C:'
    )

    for pattern in "${malicious_patterns[@]}"; do
        if echo "$input" | grep -E "$pattern" > /dev/null 2>&1; then
            return 1
        fi
    done
    return 0
}

post_tool_validation() {
    local payload
    payload="$(cat)"
    local tool_name
    tool_name="$(extract_field "$payload" tool_name)"

    check_security_violations "$payload" "$tool_name"
    cleanup_resources
    log_audit "PostToolUse: $tool_name"

    return $EXIT_SUCCESS
}

check_security_violations() {
    local output="$1" tool_name="$2"

    if echo "$output" | grep -iE "sudo|admin|root|elevated" > /dev/null 2>&1; then
        log_violation "$tool_name output contains a privilege-escalation-related keyword (alert only, not blocked)"
    fi

    if echo "$output" | grep -E "eval|exec|system|Runtime\.getRuntime" > /dev/null 2>&1; then
        log_violation "$tool_name output contains a code-injection-related keyword (alert only, not blocked)"
    fi
}

cleanup_resources() {
    local cleanup_patterns=("temp_*.tmp" "*.credentials" ".env.local" "sensitive_*.log")
    local repo_root
    repo_root="$(cd "$SCRIPT_DIR/../.." && pwd)"

    for pattern in "${cleanup_patterns[@]}"; do
        find "$repo_root" -name "$pattern" -type f -mtime +1 -delete 2>/dev/null
    done
}

main() {
    local action="$1"
    case "$action" in
        pre) pre_tool_validation ;;
        post) post_tool_validation ;;
        *)
            echo "Usage: $0 {pre|post}" >&2
            exit 1
            ;;
    esac
}

main "$@"
