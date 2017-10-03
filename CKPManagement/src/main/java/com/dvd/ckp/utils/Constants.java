/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.utils;

/**
 *
 * @author dmin
 */
public interface Constants {

    //Session
    String TOKEN = "token";
    String SESSION_USER = "user";
    String SESSION_PRICE = "price";
    String SESSION_PRICE_LOCATION = "price_location";

    //Page
    String PAGE_HOME = "/index.zul";
    String PAGE_LOGIN = "/login.zul";
    String PAGE_CHANGE_PASSWORD = "/changepassword.zul";
    String PASSWORD_PATTERN = "(?=.*[0-9])([a-z])([A-Z])(?=\\S+$).{8,}";
    String RESET_RANDOM_PASSWORD = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    Integer STATUS_INACTIVE = 0;
    Integer STATUS_ACTIVE = 1;
    Long DEFAULT_ID = 0l;
    Long SPECIAL_ID = 9999l;
    Integer FIRST_INDEX = 0;

    //Key param
    String PRAM_BANK = "BANK";
    String PRAM_PUMP_TYPE = "PUMP_TYPE";
    String PRAM_LOCATION_TYPE = "LOCATION_TYPE";
    String PRAM_CONVERT_TYPE = "CONVERT_TYPE";
    String PRAM_PRICE_TYPE = "PRICE_TYPE";
    String PRAM_OBJECT = "OBJECT";
}
