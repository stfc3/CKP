/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.service;

import com.dvd.ckp.business.dao.UserDAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dmin
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO UserDAO;

    @Transactional(readOnly = true)
    @Override
    public <T> List<T> getAll(Class<T> klass) {
        return UserDAO.getAll(klass);
    }

}
