package com.dvd.ckp.mailsend;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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

public class Test {

    public static void main(String[] args) {
        //Email data
        String Email_Id = "stfc.test@gmail.com";        //change it to your email ID
        String password = "12345678a@";                   //chaneg it to your password
        String mail_subject = "This is a HTML test mail";

        //Set mail properties
        Properties props = System.getProperties();
        String host_name = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host_name);
        props.put("mail.smtp.user", Email_Id);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(props);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Email_Id));
            message.setSubject(mail_subject);
            MimeMultipart multipart = new MimeMultipart();
            BodyPart messageBodyPart = new MimeBodyPart();

            //HTML mail content 
            String htmlText = mailContent("java2db.com", "abcdefgh");
            messageBodyPart.setContent(htmlText, "text/html");

            //Add recipients
            InternetAddress[] recipient_mail_id = getInternetAddresses("viettx.dev@gmail.com");
            message.addRecipients(Message.RecipientType.TO, recipient_mail_id);

//            // Add CC  
//            InternetAddress[] CcAddress = getInternetAddresses("exampleCC@abc.com");
//            message.setRecipients(javax.mail.Message.RecipientType.CC, CcAddress);
//
//            // Add BBC
//            InternetAddress[] BccAddress = getInternetAddresses("exampleBBC@abc.com");
//            message.setRecipients(javax.mail.Message.RecipientType.BCC, BccAddress);
            multipart.addBodyPart(messageBodyPart);

            //Add image from the image path
//            messageBodyPart = new MimeBodyPart();
//            DataSource fds = new FileDataSource("C:\\Users\\admin\\Desktop\\20170728_161038.jpg");
//            messageBodyPart.setDataHandler(new DataHandler(fds));
//            messageBodyPart.setHeader("Content-ID", "<image>");
//            multipart.addBodyPart(messageBodyPart);

            // Add attachments
            List<String> MailAttachment = new ArrayList<String>();
            MailAttachment.add("C:\\Users\\admin\\Desktop\\20170728_161038.jpg");
            if (!MailAttachment.isEmpty()) {
                for (String fileAttachment : MailAttachment) {
                    messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(fileAttachment);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(source.getName());
                    multipart.addBodyPart(messageBodyPart);
                }

            }
            message.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(host_name, Email_Id, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            System.out.println("Sent Email successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
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

    public static String mailContent(String mailFrom, String mailTo) {
        StringBuffer mailcontent = new StringBuffer();
        String MAIL_CONTENT_1 = "";
        String MAIL_CONTENT_2 = "";
        String MAIL_CONTENT_3 = "";
        String MAIL_CONTENT_4 = "";
        String MAIL_CONTENT_5 = "";
        String MAIL_CONTENT_6 = "";
        String MAIL_CONTENT_7 = "";
        String MAIL_CONTENT_8 = "";

        MAIL_CONTENT_1 = "<html lang='en'><head><meta content='text/html; charset=utf-8' http-equiv='Content-Type'> <style type='text/css'> a:hover { text-decoration: none !important; } .header h1 {color: #47c8db !important; font: bold 32px Helvetica, Arial, sans-serif; margin: 0; padding: 0; line-height: 40px;} .header p {color: #c6c6c6; font: normal 12px Helvetica, Arial, sans-serif; margin: 0; padding: 0; line-height: 18px;} .content h2 {color:#646464 !important; font-weight: bold; margin: 0; padding: 0; line-height: 26px; font-size: 18px; font-family: Helvetica, Arial, sans-serif;  } .content p {color:#767676; font-weight: normal; margin: 0; padding: 0; line-height: 20px; font-size: 12px;font-family: Helvetica, Arial, sans-serif;} .content a {color: #0eb6ce; text-decoration: none;} .footer p {font-size: 11px; color:#7d7a7a; margin: 0; padding: 0; font-family: Helvetica, Arial, sans-serif;}     .footer a {color: #0eb6ce; text-decoration: none;} </style> </head>";
        MAIL_CONTENT_2 = "<body style='margin: 0; padding: 0; bgcolor='#4b4b4b'>";
        MAIL_CONTENT_3 = "<table cellpadding='0' cellspacing='0' border='0' align='center' width='100%' style='font-family: Helvetica, Arial, sans-serif; background:#2a2a2a;' class='header'><tr><td width='100%' align='left' style='padding: font-size: 0; line-height: 0; height: 7px;' height='7' colspan='2'></td> </tr> <tr><td width='5%'style='font-size: 0px;'>&nbsp;</td> <td width='95%' align='left' style='padding: 18px 0 10px;'> <h1 style='color: #47c8db; font: bold 22px Helvetica, Arial, sans-serif; margin: 5px 0 5px 0; padding: 0;'>This is a HTML test mail in Java</h1> <p style='color: #c6c6c6; font: normal 12px Helvetica, Arial, sans-serif; margin: 10px 0 10px 0; padding: 0;'>By Java2db.com</p></td></tr></table>";
        MAIL_CONTENT_4 = "<table cellpadding='0' cellspacing='0' border='0' align='center' width='600' style='color: #717171; font: normal 11px Helvetica, Arial, sans-serif; margin: 0; padding: 0;'><tr><td width='21' style='font-size: 1px; line-height: 1px;'></td> <td width='558' align='left' style='padding: 20px 0 0;'> <h2 style='color:#646464; font-weight: bold; margin: 0; padding: 0; line-height: 20px; font-size: 18px; font-family: Helvetica, Arial, sans-serif; '>Good Day</h2> </td><td width='21' style='font-size: 1px; line-height: 1px;'></td></tr><tr><td width='21' style='font-size: 1px; line-height: 1px;'></td><td style='padding: 15px 0 15px;'  valign='top'> <p style='color:#767676; font-weight: normal; text-align:justify; margin: 0; padding: 0; line-height: 20px; font-size: 12px;font-family: Helvetica, Arial, sans-serif; '>This mail having CC,BCC,Attachments and images.</p></td></tr></table>";
        MAIL_CONTENT_5 = "<table cellpadding='0' cellspacing='0' border='0' width='550' align='center' style= 'background:#c0c0c0;'> <tr> <td width='200' valign='top'><span style='color:#ffffff; font-weight: bold; margin: 0; padding: 0; line-height: 20px; font-size: 12px;font-family: Helvetica, Arial, sans-serif; '>Mail From:</span></td><td width='300'><span style='color:#000000; font-weight: normal; margin: 0; padding: 0; line-height: 20px; font-size: 12px;font-family: Helvetica, Arial, sans-serif; '>" + mailFrom + "</span></td></tr><tr> <td width='200' valign='top'><span style='color:#ffffff; font-weight: bold; margin: 0; padding: 0; line-height: 20px; font-size: 12px;font-family: Helvetica, Arial, sans-serif; '>Mail To:</span></td><td width='300'><span style='color:#000000; font-weight: normal; margin: 0; padding: 0; line-height: 20px; font-size: 12px;font-family: Helvetica, Arial, sans-serif; '>" + mailTo + "</span></td></tr></table></table>";
        MAIL_CONTENT_6 = "<table align='center'><tr><td><img src=\"cid:image\"></td></tr></table>";
        MAIL_CONTENT_7 = "<table cellpadding='0' cellspacing='0' border='0' align='center' width='600' style='color: #717171; font: normal 11px Helvetica, Arial, sans-serif; margin: 0; padding: 0;'><tr><td width='21' style='font-size: 1px; line-height: 1px;'></td> <td width='558' align='left' style='padding: 20px 0 0;'> </td><td width='21' style='font-size: 1px; line-height: 1px;'></td></tr><tr><td width='21' style='font-size: 1px; line-height: 1px;'></td><td style='padding: 15px 0 15px;'  valign='top'><ul><li><span style='color:#767676; font-weight: normal; margin: 0; padding: 0; line-height: 20px; font-size: 12px;font-family: Helvetica, Arial, sans-serif;'>Mobile : 1232XXX</span></li><li><span style='color:#767676; font-weight: normal; margin: 0; padding: 0; line-height: 20px; font-size: 12px;font-family: Helvetica, Arial, sans-serif;'>Email : xxxx@abc.xom</span></li> </ul></td></tr></table>";
        MAIL_CONTENT_8 = "<table cellpadding='0' cellspacing='0' border='0' align='center' width='600'><td style='padding: 15px 0 15px;'  valign='top'> <p style='color:green; font-weight: normal; text-align:justify; margin: 0; padding: 0; line-height: 20px; font-size: 14px;font-family: verdana, Arial, sans-serif; '>Thank you</FONT><BR/></table>";

        mailcontent.append(MAIL_CONTENT_1);
        mailcontent.append(MAIL_CONTENT_2);
        mailcontent.append(MAIL_CONTENT_3);
        mailcontent.append(MAIL_CONTENT_4);
        mailcontent.append(MAIL_CONTENT_5);
        mailcontent.append(MAIL_CONTENT_6);
        mailcontent.append(MAIL_CONTENT_7);
        mailcontent.append(MAIL_CONTENT_8);

        return mailcontent.toString();

    }
}
