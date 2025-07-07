package com.webapp.webapp_api.service.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String toEmail, String registered, String token) {
        String link = String.format("http://localhost:8080/auth/%s/verify?token=%s", registered, token);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Email Verification Kafein Web App");
        message.setText("Click the link to verify your email:\n" + link);

        mailSender.send(message);
    }
}
