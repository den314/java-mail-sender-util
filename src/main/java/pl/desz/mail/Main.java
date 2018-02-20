package pl.desz.mail;

import javax.mail.Message;
import javax.mail.MessagingException;

public class Main {

    public static void main(String[] args) throws MessagingException {

        MailUtil mailUtil = new MailUtil();
        String msgBody = "Body of the massage is...";

        Message msg = mailUtil.prepareBasicMail("szczukocki.denis@gmail.com", "intellij is here", msgBody);
        msg = mailUtil.addAttachments(msg, msgBody, "src/main/resources/employeeReport.jrxml", "src/main/resources/employee-schema.sql");

        mailUtil.send(msg);
    }
}
