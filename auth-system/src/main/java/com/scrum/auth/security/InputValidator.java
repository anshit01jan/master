package com.scrum.auth.security;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class InputValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern SAFE_STRING = Pattern.compile("^[a-zA-Z0-9@._%-]{1,255}$");
    private static final int MAX_LENGTH = 255;

    public boolean isValidEmail(String input) {
        if (input == null || input.isEmpty() || input.length() > MAX_LENGTH) {
            return false;
        }
        return EMAIL_PATTERN.matcher(input).matches();
    }

    public boolean isSafeInput(String input) {
        if (input == null || input.isEmpty() || input.length() > MAX_LENGTH) {
            return false;
        }
        return SAFE_STRING.matcher(input).matches();
    }

    public String sanitize(String input) {
        if (input == null) {
            return "";
        }
        return input.replaceAll("[<>\"'%;()&+]", "").trim();
    }

    public boolean isValidToken(String token) {
        return token != null && !token.isEmpty() && token.length() < 1000 &&
               token.matches("^[a-fA-F0-9-]+$");
    }
}
