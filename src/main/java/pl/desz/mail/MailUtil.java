package pl.desz.mail;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MailUtil {

    private Properties mailProps = new Properties();
    private Properties loginProps = new Properties();
    private Session session;

    public MailUtil() {
        setup();
    }

    public Message prepareBasicMail(String to, String title, String body) {

        Message msg = new MimeMessage(session);

        try {
            msg.setFrom(new InternetAddress("anybody"));
            msg.setSubject(title);
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setText(body);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return msg;
    }

    public Message addAttachments(Message msg, String newBody, String... filePath) throws MessagingException {

        BodyPart msgBodyPart = new MimeBodyPart();
        msgBodyPart.setText(newBody);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(msgBodyPart);

        for (String file : filePath) {

            msgBodyPart = new MimeBodyPart();

            DataSource fileDataSource = new FileDataSource(file);
            msgBodyPart.setDataHandler(new DataHandler(fileDataSource));
            msgBodyPart.setFileName(file.split("/")[3]); // TODO: fix extracting filename
            multipart.addBodyPart(msgBodyPart);
        }

        msg.setContent(multipart);

        return msg;
    }

    public void send(Message msg) {
        try {
            Transport.send(msg);
            System.out.println("Mail has been sent.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void setup() {

        try {
            mailProps.load(new FileReader("src/main/resources/mail.properties"));
            loginProps.load(new FileReader("src/main/resources/login.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        session = Session.getInstance(mailProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(loginProps.getProperty("username"), loginProps.getProperty("password"));
            }
        });
    }
}
