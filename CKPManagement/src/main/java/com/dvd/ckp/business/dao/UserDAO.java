/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.dao;

import com.dvd.ckp.domain.Users;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface UserDAO {

    Users getUserByName(String pstrUserName);
    
    List<Users> getAllUser();
    
    void insertOrUpdateUser(Users user);
}
