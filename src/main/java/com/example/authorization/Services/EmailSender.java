package com.example.authorization.Services;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class EmailSender {

    Session mailSession;

    private int port = 465;
    private String from = "hpmoreacc@gmail.com";
    private String fromUserEmailPassword = "Gfhjkm1234";
    private String name = "Ваша любимая кафедра";
    @Getter @Setter private String subject;
    @Getter @Setter private String msg;
    @Getter @Setter private String alternativeMsg;

    HtmlEmail htmlEmail;

    private void setMailServerProperties()
    {
        Properties emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", port);
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailSession = Session.getDefaultInstance(emailProperties, null);
    }

    public void sendEmail(String recipient, String subject, String emailBody) throws MessagingException{

        setMailServerProperties();
        String emailHost = "smtp.gmail.com";
        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, from, fromUserEmailPassword);
        /**
         * Draft the message
         * */
        MimeMessage emailMessage = new MimeMessage(mailSession);//draftEmailMessage(recipient, subject, emailBody);
        emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        emailMessage.setSubject(subject);
        emailMessage.setText(emailBody, "UTF-8", "html");
        /**
         * Send the mail
         * */
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        System.out.println("Email sent successfully.");
    }
}
