package com.emailsender;

import com.emailsender.dto.OrderDto;
import com.emailsender.feginClient.UserClient;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.ResourceBundle;
@Service
@AllArgsConstructor
public class OrderConfirmationEmail {
    private final JavaMailSender javaMailSender;
    private final EmailContentBuilder emailContentBuilder;
    private final UserClient userClient;

    void sendEmail(OrderDto order) throws MessagingException {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(userClient.getUserEmail(order.userId()));
        helper.setSubject(bundle.getString("email.subject"));

        String emailContent = emailContentBuilder.buildOrderConfirmationEmail(order);
        helper.setText(emailContent, true);

        helper.addInline("logoImage", new ClassPathResource("/static/logo.png"));

        javaMailSender.send(message);
    }
}
