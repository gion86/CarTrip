package com.cartrip.mail;

import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Mail sender class that uses GMail SMTP server as default.
 */
public class GmailSender extends javax.mail.Authenticator {

    private String user;
    private String password;
    private Session session;

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public GmailSender(String user, String password) {
        this.user = user;
        this.password = password;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
    }

    public synchronized boolean sendMail(String subject, String body, String sender, String recipients) {
        try {
            MimeMessage mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress(sender));
            mm.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(recipients)));
            mm.setSubject(subject);
            mm.setText(body);
            Transport.send(mm);
            return true;
        } catch (MessagingException e) {
            Log.d("GmailSender", "sendMail: " , e);
        }
        return false;
    }
}