package com.scrum.auth.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("userEmail", userEmail);
        return "dashboard";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/auth/login";
    }
}
