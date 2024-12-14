package com.bujdi.carRecords.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class EmailSender {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

    public EmailSender(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(
            String email,
            String subject,
            String templateName,
            Map<String, Object> templateModel
    ) throws MessagingException, UnsupportedEncodingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            Context context = new Context();
            context.setVariables(templateModel);
            String htmlContent = templateEngine.process(templateName, context);

            helper.setFrom("noreply@autojournalapp.com", "Auto Journal App");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Sent mail to {} with subject {}", email, subject);
        } catch (Throwable t) {
            logger.error("Failed to send email to {}, subject: {}: {}", email, subject, t.getMessage(), t);
        }
    }
}