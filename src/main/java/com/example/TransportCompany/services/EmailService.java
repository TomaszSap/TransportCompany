package com.example.TransportCompany.services;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

public class EmailService {
    private Mailer mailer;

    public EmailService() {
        mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587)
                .withSMTPServerUsername("your_email@gmail.com")
                .withSMTPServerPassword("your_password")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        Email email = EmailBuilder.startingBlank()
                .from("Your Name", "your_email@gmail.com")
                .to("Recipient Name", to)
                .withSubject(subject)
                .withPlainText(text)
                .buildEmail();

        mailer.sendMail(email);
    }
}
