/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.mailsend;

import com.dvd.ckp.mailsend.entity.ConfigEntity;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author admin
 */
public class LoadProperties {

    private static final Logger LOGGER = Logger.getLogger(LoadProperties.class);

    public ConfigEntity loadConfig() {
        ConfigEntity entity = new ConfigEntity();
        try {
            Properties properties = new Properties();
            InputStream input = new FileInputStream("config/config.properties");
            properties.load(new InputStreamReader(input, Charset.forName("UTF-8")));

            entity.setTimeOut(Integer.valueOf(properties.getProperty("time.out")));
            entity.setRecipient(properties.getProperty("recipient"));
            entity.setMailSend(properties.getProperty("mail.send"));
            entity.setPassword(properties.getProperty("password"));
            entity.setTitle(properties.getProperty("title"));
            entity.setContent(properties.getProperty("content"));
            entity.setAttachment(properties.getProperty("attachment"));

        } catch (IOException | NumberFormatException e) {
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return entity;
    }
    
}
