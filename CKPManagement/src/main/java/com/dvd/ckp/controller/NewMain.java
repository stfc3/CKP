/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.utils.Constants;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author daond
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String RESET_RANDOM_PASSWORD = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String pwd = RandomStringUtils.random(8, RESET_RANDOM_PASSWORD);
//        System.out.println(pwd);
        
        if (!"654321aA".matches(Constants.PASSWORD_PATTERN)) {
            System.out.println("A");   
        }
    }

}
