/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.dao;

import com.dvd.ckp.domain.Construction;
import com.dvd.ckp.domain.Customers;
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
public class ConstructionDAOImpl implements ConstructionDAO {

    @Autowired
    SessionFactory sessionFactory;

    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Construction> getAllConstruction() {

        Query query = getCurrentSession().getNamedQuery("Construction.fillAllConstruction");
        return (List<Construction>) query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void insertOrUpdateConstruction(Construction construction) {
        getCurrentSession().saveOrUpdate(construction);
    }

}
