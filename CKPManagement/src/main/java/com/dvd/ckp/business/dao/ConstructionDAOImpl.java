/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.dao;

import com.dvd.ckp.domain.Construction;
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
    public List<Construction> getConstructionActive() {

        Query query = getCurrentSession().getNamedQuery("Construction.fillConstructionActive");
        return (List<Construction>) query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void insertOrUpdateConstruction(Construction construction) {
        getCurrentSession().saveOrUpdate(construction);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Construction> getConstructionByCustomer(Long customerId) {

        if (customerId != null) {
            StringBuilder vsbdSQL = new StringBuilder("SELECT ct FROM Construction ct, Contract ctr, Customer c ");
            vsbdSQL.append("WHERE ct.contractId=ctr.contractId AND ctr.customerId=c.customerId ");
            vsbdSQL.append("AND c.customerId= :customerId");
            Query query = getCurrentSession().createQuery(vsbdSQL.toString());
            query.setParameter("customerId", customerId);
            return (List<Construction>) query.list();
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Construction getConstructionById(Long constructionId) {

        Query query = getCurrentSession().getNamedQuery("Construction.fillConstructionById");
        query.setParameter("constructionId", constructionId);
        List lstConstruction = query.list();
        if (lstConstruction != null && !lstConstruction.isEmpty()) {
            return (Construction) lstConstruction.get(0);
        }
        return null;
    }

}
