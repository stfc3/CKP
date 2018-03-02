/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.mailsend;

/**
 *
 * @author admin
 */
public class Main {

    public static void main(String[] arg) throws InterruptedException {
        MailSend mailSend = new MailSend();
        mailSend.sendMail();
        System.out.println("Sent successfull");

    }
}
