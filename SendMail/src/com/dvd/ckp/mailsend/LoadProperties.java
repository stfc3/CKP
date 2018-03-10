/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.mailsend;

import com.dvd.ckp.mailsend.common.Constant;
import com.dvd.ckp.mailsend.entity.ConfigEntity;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author admin
 */
public class LoadProperties {

    private static final Logger LOGGER = LogManager.getLogger();

    public ConfigEntity loadConfig() {
        ConfigEntity entity = new ConfigEntity();
        try {
            Properties properties = new Properties();
            String fileName = "config/config.properties";
            InputStream input = new FileInputStream(fileName);
            properties.load(new InputStreamReader(input, Charset.forName(Constant.UTF8)));

            entity.setTimeOut(Integer.valueOf(properties.getProperty(Constant.PROPERTIES_TIME)));
            entity.setRecipient(properties.getProperty(Constant.PROPERTIES_RECIPIENT));
            entity.setMailSend(properties.getProperty(Constant.PROPERTIES_MAIL));
            entity.setPassword(properties.getProperty(Constant.PROPERTIES_PASSWORD));
            entity.setTitle(properties.getProperty(Constant.PROPERTIES_TITLE));
            entity.setContent(properties.getProperty(Constant.PROPERTIES_CONTENT));
            entity.setAttachment(properties.getProperty(Constant.PROPERTIES_ATTACHMENT));

            entity.setHost(properties.getProperty(Constant.SMTP_HOST));
            entity.setPort(properties.getProperty(Constant.SMTP_PORT));
            entity.setStarttls(properties.getProperty(Constant.SMTP_STARTTLS));

            entity.setCc(properties.getProperty(Constant.MAIL_CC));
            entity.setBcc(properties.getProperty(Constant.MAIL_BCC));

        } catch (IOException | NumberFormatException e) {
            LOGGER.error(e.getMessage(), e);

        }
        return entity;
    }

}
