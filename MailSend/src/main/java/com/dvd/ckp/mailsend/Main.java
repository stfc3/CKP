/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.mailsend;

import com.dvd.ckp.mailsend.entity.ConfigEntity;

/**
 *
 * @author admin
 */
public class Main {
    public static void main(String[] arg) throws InterruptedException{
        LoadProperties properties = new LoadProperties();
        ConfigEntity entity = properties.loadConfig();
        MailSend mailSend = new MailSend();
        while (true) {
            System.out.println("Test");
            mailSend.sendMail();
            Thread.sleep(entity.getTimeOut());
            
        }
    }
}
