/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.dao;

import com.dvd.ckp.domain.User;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dmin
 */
@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    SessionFactory sessionFactory;

    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public User getUserByName(String pstrUserName) {

        Query query = getCurrentSession().getNamedQuery("User.getUserByName");
        query.setParameter("userName", pstrUserName);
        List lstUsers = query.list();
        if (lstUsers != null && !lstUsers.isEmpty()) {
            return (User) lstUsers.get(0);
        }
        return null;
    }

    @Override
    public List<User> getAllUser() {
        Query query = getCurrentSession().getNamedQuery("User.getAllUser");
        List<User> lstUsers = query.list();
        if (lstUsers != null && !lstUsers.isEmpty()) {
            return lstUsers;
        }
        return null;
    }

    @Override
    public void insertOrUpdateUser(User user) {
        getCurrentSession().saveOrUpdate(user);
    }

}
