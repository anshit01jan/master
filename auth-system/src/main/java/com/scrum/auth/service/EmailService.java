package com.scrum.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String fromAddress;
    private final String resetUrlBase;

    public EmailService(JavaMailSender mailSender,
                        @Value("${app.mail.from}") String fromAddress,
                        @Value("${app.reset.url}") String resetUrlBase) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
        this.resetUrlBase = resetUrlBase;
    }

    @Async
    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(toEmail);
            message.setSubject("Password Reset Request");
            message.setText(buildResetEmailBody(token));

            mailSender.send(message);
            logger.info("Password reset email sent to: {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send password reset email to: {}", toEmail, e);
        }
    }

    private String buildResetEmailBody(String token) {
        return String.format(
                "You have requested to reset your password.\n\n" +
                        "Click the link below to reset your password:\n" +
                        "%s?token=%s\n\n" +
                        "This link will expire in 2 minutes.\n\n" +
                        "If you did not request this, please ignore this email.",
                resetUrlBase, token
        );
    }
}
