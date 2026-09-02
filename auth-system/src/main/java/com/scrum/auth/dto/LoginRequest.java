package com.scrum.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "Email required")
    @Email(message = "Invalid format")
    private String email;

    @NotBlank(message = "Credential required")
    private String value;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return value;
    }

    public void setPassword(String value) {
        this.value = value;
    }
}
