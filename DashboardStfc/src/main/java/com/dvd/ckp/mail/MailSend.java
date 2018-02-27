package com.dvd.ckp.mail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class MailSend {

    private static final Logger logger = Logger.getLogger(MailSend.class);

    public void sendMail(String contentEmail, String listEmailSent) {
        try {

            // Email data
            String Email_Id = "stfc.test@gmail.com"; // change to your email ID
            String password = "12345678a@"; // change to your password
//            String recipient_mail_id = "stfc.test@gmail.com"; // change to
            // recipient
            // email id
            String mail_subject = "This is a test mail From Java";

            // Set mail properties
            Properties props = System.getProperties();
            String host_name = "smtp.gmail.com";
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host_name);
            props.put("mail.smtp.user", Email_Id);
            props.put("mail.smtp.password", password);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props);
            MimeMessage message = new MimeMessage(session);

            try {
                // Set email data
                message.setFrom(new InternetAddress(Email_Id));

                message.setSubject(mail_subject);
                MimeMultipart multipart = new MimeMultipart();
                BodyPart messageBodyPart = new MimeBodyPart();

                // Set key values
//                Map<String, String> input = new HashMap<String, String>();
//                input.put("Author", "java2db.com");
//                input.put("Topic", "HTML Template for Email");
//                input.put("Content In", "English");
                InternetAddress[] recipient_mail_id = getInternetAddresses(listEmailSent);
                message.addRecipients(Message.RecipientType.TO, recipient_mail_id);

                // HTML mail content
                // String htmlText =
                // readEmailFromHtml("C:/mail/HTMLTemplate.html", input);
                String htmlText = contentEmail;
                messageBodyPart.setContent(htmlText, "text/html");

                multipart.addBodyPart(messageBodyPart);
                message.setContent(multipart);

                // Conect to smtp server and send Email
                Transport transport = session.getTransport("smtp");
                transport.connect(host_name, Email_Id, password);
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
                System.out.println("Mail sent successfully...");

            } catch (MessagingException ex) {
//				logger.error(ex.getMessage(), ex);
            } catch (Exception ae) {
                ae.printStackTrace();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Method to replace the values for keys
    protected String readEmailFromHtml(String filePath, Map<String, String> input) {
        String msg = readContentFromFile(filePath);
        try {
            Set<Entry<String, String>> entries = input.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                msg = msg.replace(entry.getKey().trim(), entry.getValue().trim());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return msg;
    }

    // Method to read HTML file as a String
    private String readContentFromFile(String fileName) {
        StringBuffer contents = new StringBuffer();

        try {
            // use buffering, reading one line at a time
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                reader.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return contents.toString();
    }

    private static InternetAddress[] getInternetAddresses(String recipients) throws AddressException {
        ArrayList<String> recipientsArray = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(recipients, ",");

        while (st.hasMoreTokens()) {
            recipientsArray.add(st.nextToken());
        }

        int sizeTo = recipientsArray.size();
        InternetAddress[] ainternetaddress1 = new InternetAddress[sizeTo];
        for (int i = 0; i < sizeTo; i++) {
            ainternetaddress1[i] = new InternetAddress(recipientsArray.get(i).toString());
        }
        return ainternetaddress1;
    }

    public static void main(String[] args) {
        String htmlContent = "<table style=\"width:100%;\">\n"
                + "  <tr style=\"width:100%; border: 1px solid red;\">\n"
                + "    <th style=\"width:100%; border: 1px solid red;\">Firstname</th>\n"
                + "    <th style=\"width:100%; border: 1px solid red;\">Lastname</th> \n"
                + "    <th style=\"width:100%; border: 1px solid red;\">Age</th>\n"
                + "  </tr>\n"
                + "  <tr style=\"width:100%; border: 1px solid red;\">\n"
                + "    <td style=\"width:100%; border: 1px solid red;\">Jill</td>\n"
                + "    <td style=\"width:100%; border: 1px solid red;\">Smith</td> \n"
                + "    <td style=\"width:100%; border: 1px solid red;\">50</td>\n"
                + "  </tr>\n"
                + "  <tr style=\"width:100%; border: 1px solid red;\">\n"
                + "    <td style=\"width:100%; border: 1px solid red;\">Eve</td>\n"
                + "    <td style=\"width:100%; border: 1px solid red;\">Jackson</td> \n"
                + "    <td style=\"width:100%; border: 1px solid red;\">94</td>\n"
                + "  </tr>\n"
                + "</table>";
        new MailSend().sendMail(htmlContent, "viettx.dev@gmail.com,daond226@gmail.com,dongdang.tk61@gmail.com");

    }

}
