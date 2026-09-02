package com.scrum.auth.controller;

import com.scrum.auth.dto.LoginRequest;
import com.scrum.auth.dto.PasswordResetRequest;
import com.scrum.auth.dto.PasswordResetInitRequest;
import com.scrum.auth.entity.User;
import com.scrum.auth.service.AuthenticationService;
import com.scrum.auth.service.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthenticationService authenticationService,
                          PasswordResetService passwordResetService) {
        this.authenticationService = authenticationService;
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequest loginRequest,
                        BindingResult bindingResult,
                        HttpServletRequest request,
                        HttpSession session,
                        Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            String ipAddress = getClientIpAddress(request);
            User user = authenticationService.authenticateUser(
                    loginRequest.getEmail(),
                    loginRequest.getPassword(),
                    ipAddress,
                    request
            );

            session.setAttribute("userId", user.getId());
            session.setAttribute("userEmail", user.getEmail());
            return "redirect:/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage(Model model) {
        model.addAttribute("resetInitRequest", new PasswordResetInitRequest());
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String initiateForgotPassword(@Valid @ModelAttribute PasswordResetInitRequest resetInitRequest,
                                          BindingResult bindingResult,
                                          HttpServletRequest request,
                                          Model model) {
        if (bindingResult.hasErrors()) {
            return "forgot-password";
        }

        String ipAddress = getClientIpAddress(request);
        passwordResetService.initiatePasswordReset(resetInitRequest.getEmail(), ipAddress, request);

        model.addAttribute("message", "If your email is registered, you will receive a password reset link shortly.");
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        model.addAttribute("resetRequest", new PasswordResetRequest());
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token,
                                @Valid @ModelAttribute PasswordResetRequest resetRequest,
                                BindingResult bindingResult,
                                HttpServletRequest request,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("token", token);
            return "reset-password";
        }

        if (!resetRequest.getNewPassword().equals(resetRequest.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("token", token);
            return "reset-password";
        }

        try {
            String ipAddress = getClientIpAddress(request);
            passwordResetService.resetPassword(token, resetRequest.getNewPassword(), ipAddress, request);
            model.addAttribute("success", "Password has been reset successfully. You can now login.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("token", token);
            return "reset-password";
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
