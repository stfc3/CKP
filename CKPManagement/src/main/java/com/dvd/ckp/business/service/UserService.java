/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.service;

import com.dvd.ckp.domain.Users;
import java.util.List;

/**
 *
 * @author dmin
 */
public interface UserService {

    <T> List<T> getAll(Class<T> klass);
     Users getUserByName(String pstrUserName);
}
