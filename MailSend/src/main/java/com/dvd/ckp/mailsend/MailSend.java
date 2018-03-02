package com.dvd.ckp.mailsend;

import com.dvd.ckp.mailsend.common.Constant;
import com.dvd.ckp.mailsend.entity.ConfigEntity;
import com.dvd.ckp.mailsend.utils.DatetimeUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Date;
import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

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
    private LoadProperties properties = new LoadProperties();

    public void sendMail() {
        try {
            ConfigEntity entity = properties.loadConfig();
            // Set mail properties
            Properties props = System.getProperties();
            props.put(Constant.SMTP_STARTTLS, Constant.TRUE);
            props.put(Constant.SMTP_HOST, Constant.HOST_NAME);
            props.put(Constant.SMTP_USER, entity.getMailSend());
            props.put(Constant.SMTP_PASSWORD, entity.getPassword());
            props.put(Constant.SMTP_PORT, Constant.PORT);
            props.put(Constant.SMTP_AUTH, Constant.TRUE);

            Session session = Session.getDefaultInstance(props);
            MimeMessage message = new MimeMessage(session);

            try {
                // Set email data
                message.setFrom(new InternetAddress(entity.getMailSend()));

                message.setSubject(entity.getTitle() + " " + DatetimeUtils.convertDateToString(new Date(), Constant.FORMAT_DATE), Constant.UTF8);
                MimeMultipart multipart = new MimeMultipart();
                BodyPart messageBodyPart = new MimeBodyPart();

                // Set key values
//                Map<String, String> input = new HashMap<String, String>();
//                input.put("Author", "java2db.com");
//                input.put("Topic", "HTML Template for Email");
//                input.put("Content In", "English");
                InternetAddress[] recipient_mail_id = getInternetAddresses(entity.getRecipient());
                message.addRecipients(Message.RecipientType.TO, recipient_mail_id);

                // HTML mail content
                // String htmlText =
                // readEmailFromHtml("C:/mail/HTMLTemplate.html", input);
                String htmlText = entity.getContent();
                messageBodyPart.setContent(htmlText, Constant.HTML_UTF_8);

                multipart.addBodyPart(messageBodyPart);

                // Add attachments
                List<String> attachment = getListAttachment(entity.getAttachment());

                if (!attachment.isEmpty()) {
                    for (String fileAttachment : attachment) {
                        messageBodyPart = new MimeBodyPart();
                        DataSource source = new FileDataSource(fileAttachment);
                        messageBodyPart.setDataHandler(new DataHandler(source));
                        messageBodyPart.setFileName(source.getName());
                        multipart.addBodyPart(messageBodyPart);
                    }

                }
                message.setContent(multipart);

                // Conect to smtp server and send Email
                Transport transport = session.getTransport(Constant.SMTP);
                transport.connect(Constant.HOST_NAME, entity.getMailSend(), entity.getPassword());
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();

            } catch (MessagingException ex) {
                logger.error(ex.getMessage(), ex);
            } catch (Exception ae) {
                logger.error(ae.getMessage(), ae);
            }
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
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
        StringTokenizer st = new StringTokenizer(recipients, ";");

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

    private List<String> getListAttachment(String value) {
        String[] arrayValue = value.split(";");
        List<String> lstAttachment = Arrays.asList(arrayValue);
        return lstAttachment;
    }

}
