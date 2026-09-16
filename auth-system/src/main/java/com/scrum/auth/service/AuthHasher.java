package com.scrum.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthHasher {
    private final BCryptPasswordEncoder encoder;

    public AuthHasher() {
        this.encoder = new BCryptPasswordEncoder(12);
    }

    public String hashValue(String input) {
        return encoder.encode(input);
    }

    public boolean matches(String input, String hash) {
        return encoder.matches(input, hash);
    }
}
