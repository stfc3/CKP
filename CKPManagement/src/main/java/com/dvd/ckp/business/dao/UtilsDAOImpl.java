/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.dao;

import com.dvd.ckp.domain.Param;
import java.math.BigInteger;
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
public class UtilsDAOImpl implements UtilsDAO {

    @Autowired
    SessionFactory sessionFactory;

    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Param> getParamByKey(String key) {
        Query query = getCurrentSession().getNamedQuery("Param.fillParamByKey");
        query.setParameter("paramKey", key);
        return (List<Param>) query.list();
    }
    @Override
    public BigInteger getId() {
        Query query = getCurrentSession().createSQLQuery("SELECT LAST_INSERT_ID()");
        return (BigInteger) query.list().get(0);
    }

}
