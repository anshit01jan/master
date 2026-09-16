package com.scrum.auth.service;

import org.springframework.stereotype.Service;

@Service
public class AuthPolicyService {
    private static final int MIN_LENGTH = 8;

    public boolean isPasswordStrong(String value) {
        if (value == null || value.length() < MIN_LENGTH) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasSpecial = false;

        for (char c : value.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        return hasUpper && hasLower && hasSpecial;
    }

    public long getTokenExpirySeconds(long mins) {
        return mins * 60;
    }
}
