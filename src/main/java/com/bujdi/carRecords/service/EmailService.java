package com.bujdi.carRecords.service;

import com.bujdi.carRecords.emails.EmailSender;
import com.bujdi.carRecords.model.User;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    private final EmailSender emailSender;

    @Autowired
    public EmailService(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(User user, String subject, String content) {
        Map<String, Object> model = new HashMap<>();
        model.put("subject", subject);
        model.put("content", content);
        model.put("username", user.getDisplayName());

        try {
            emailSender.sendEmail(user.getUsername(), subject, "designedTemplate.html", model);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
