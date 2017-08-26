/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.dao;

import com.dvd.ckp.domain.Users;
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
    public <T> List<T> getAll(Class<T> klass) {
        return getCurrentSession().createQuery("from " + klass.getName()).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Users getUserByName(String pstrUserName) {
        
        Query query = getCurrentSession().getNamedQuery("Users.getUserByName");
        query.setParameter("userName", pstrUserName);
        List lstUsers=query.list();
        if(lstUsers!=null && !lstUsers.isEmpty()){
            return (Users) lstUsers.get(0);
        }
        return null;
    }

}
