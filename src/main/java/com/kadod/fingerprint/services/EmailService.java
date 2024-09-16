package com.kadod.fingerprint.services;

import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.emails.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import com.mailersend.sdk.exceptions.MailerSendException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Component
public class EmailService {

    private final MailerSend mailerSend;

    @Autowired
    private JavaMailSender mailSender;

    public EmailService(@Value("${mailersend.api.key}") String apiKey) {
        this.mailerSend = new MailerSend();
        this.mailerSend.setToken(apiKey);
    }

    public void sendEmailWithAttachment(String recipient, String subject, String htmlBody, MultipartFile file) {
        try {

            // Construire l'e-mail
            Email email = new Email();
            email.setSubject(subject);
            email.setHtml(htmlBody);
            email.addRecipient("Recipient name", recipient);
            if(!file.isEmpty()){
                email.attachFile(file.getResource().getFile());
            }
            email.setFrom("votre_email@votredomaine.com", "Votre Nom");

            // Envoyer l'e-mail
            mailerSend.emails().send(email);

        } catch (MailerSendException | IOException e) {
            e.printStackTrace();  // Gérer les exceptions
        }
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

}

