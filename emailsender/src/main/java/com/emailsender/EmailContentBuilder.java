package com.emailsender;

import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class EmailContentBuilder {
    private final ResourceBundle bundle;

    public EmailContentBuilder() {
        this.bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
    }

    public String buildOrderConfirmationEmail(OrderDto order) {
        return String.format(
                "<html>" +
                        "<head>" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; max-width: 600px; margin: auto; color: #333; }" +
                        ".container { padding: 20px; border: 1px solid #ddd; border-radius: 5px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }" +
                        "h2 { color: #333; }" +
                        "p { line-height: 1.6; }" +
                        "a { color: #1a73e8; text-decoration: none; }" +
                        "a:hover { text-decoration: underline; }" +
                        ".footer { margin-top: 20px; text-align: center; }" +
                        ".footer img { width: 150px; margin-top: 10px; }" +
                        "@media only screen and (max-width: 600px) { .container { padding: 10px; } }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='container'>" +
                        "<h2>Witaj!</h2>" +
                        "<p>Dziękujemy za zakupy w naszym sklepie. Poniżej znajdują się szczegóły Twojego zamówienia:</p>" +
                        "<p><strong>Numer zamówienia:</strong> %s</p>" +
                        "<p><strong>Wartość zamówienia:</strong> %.2f zł</p>" +
                        "<p><strong>Status zamówienia:</strong> Pakowane</p>" +
                        "<p>Twoje zamówienie jest obecnie przygotowywane do wysyłki. Otrzymasz kolejną wiadomość e-mail, gdy zamówienie zostanie wysłane.</p>" +
                        "<br>" +
                        "<p>Z poważaniem,<br>Twój zespół Sklepu</p>" +
                        "<br>" +
                        "<div class='footer'>" +
                        "<p>Skontaktuj się z nami: <a href='mailto:wojtekapachekafka@gmail.com'>support@example.com</a> | +48 123 456 789</p>" +
                        "<p><img src='cid:logoImage' alt='Logo'></p>" +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>",
                order.orderName(),
                order.totalPrice(),
                order.orderName()
        );
    }
    }

