package com.scrum.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class PasswordResetRequest {
    @NotBlank(message = "Required")
    private String newValue;

    @NotBlank(message = "Required")
    private String confirmValue;

    public String getNewPassword() {
        return newValue;
    }

    public void setNewPassword(String newValue) {
        this.newValue = newValue;
    }

    public String getConfirmPassword() {
        return confirmValue;
    }

    public void setConfirmPassword(String confirmValue) {
        this.confirmValue = confirmValue;
    }
}
