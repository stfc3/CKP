/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.dao;

import com.dvd.ckp.domain.Distribute;
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
public class DistributeDAOImpl implements DistributeDAO {

    @Autowired
    SessionFactory sessionFactory;

    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Distribute> getAllDistribute() {

        Query query = getCurrentSession().getNamedQuery("Distribute.fillAllDistribute");
        return (List<Distribute>) query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Distribute> getDistributeActive() {

        Query query = getCurrentSession().getNamedQuery("Distribute.fillDistributeActive");
        return (List<Distribute>) query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void insertOrUpdateDistribute(Distribute distribute) {
        getCurrentSession().saveOrUpdate(distribute);
    }


}
