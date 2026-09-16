package com.scrum.auth.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHasher {

    private final BCryptPasswordEncoder encoder;

    public PasswordHasher(@Value("${app.security.bcrypt.strength:12}") int strength) {
        this.encoder = new BCryptPasswordEncoder(strength);
    }

    public String hash(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    public boolean verify(String plainPassword, String passwordHash) {
        return encoder.matches(plainPassword, passwordHash);
    }
}
