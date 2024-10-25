package com.bujdi.carRecords.service;

import com.bujdi.carRecords.emails.EmailSender;
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

    public void sendEmail(String email, String subject, String content) {
        Map<String, Object> model = new HashMap<>();
        model.put("subject", subject);
        model.put("content", content);

        try {
            emailSender.sendEmail(email, subject, "emailTemplate.html", model);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
