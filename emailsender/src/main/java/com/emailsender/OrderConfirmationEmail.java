package com.emailsender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.ResourceBundle;

@Service
@AllArgsConstructor
public class OrderConfirmationEmail {
    private final JavaMailSender javaMailSender;

    void sendEmail(String recipientEmail) throws MessagingException {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipientEmail);
        helper.setSubject(bundle.getString("email.subject"));
        helper.setText(bundle.getString("email.thankYouMessage"), true);

        javaMailSender.send(message);
    }


}
