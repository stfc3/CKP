/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.utils;

import com.dvd.ckp.domain.Param;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dmin
 */
public interface Constants {

    //Session
    String USER_TOKEN = "userToken";

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
    String RENT_TYPE = "RENT_PUMP";
    String PARAM_DEPARTMENT = "DEPARTMENT";
    public static String PARAM_POSITION = "POSITION";

    Integer USER_ADMIN = 1;

    String[] PARAM_TYPE = {PRAM_BANK, PRAM_PUMP_TYPE, PRAM_LOCATION_TYPE, PRAM_CONVERT_TYPE, RENT_TYPE, PARAM_DEPARTMENT, PARAM_POSITION};

    public static String getParamFromType(String type) {
        String result = "";
        switch (type) {
            case PRAM_BANK:
                result = "Ngân hàng";
                break;
            case PRAM_PUMP_TYPE:
                result = "Loại bơm";
                break;
            case PRAM_CONVERT_TYPE:
                result = "Loại chuyển đổi";
                break;
            case PRAM_LOCATION_TYPE:
                result = "Loại vị trí";
                break;
            case "RENT_PUMP":
                result = "Cần phân phối";
                break;
            case PARAM_DEPARTMENT:
                result = "Phòng ban";
                break;
            case PARAM_POSITION:
                result = "Chức vụ";
                break;
            default:
                break;
        }
        return result;
    }

    public static List<Param> getParamKey() {
        List<Param> vlstParam = new ArrayList<>();
        Param p;
        try {
            for (int i = 0; i < PARAM_TYPE.length; i++) {
                p = new Param();
                p.setParamKey(PARAM_TYPE[i]);
                p.setParamKeyName(getParamFromType(PARAM_TYPE[i]));
                vlstParam.add(p);
            }
        } catch (Exception e) {
        }
        return vlstParam;
    }
}
