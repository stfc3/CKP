/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.dao;

import com.dvd.ckp.domain.Contract;
import com.dvd.ckp.domain.Price;
import com.dvd.ckp.domain.PriceLocation;
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
public class ContractDAOImpl implements ContractDAO {

    @Autowired
    SessionFactory sessionFactory;

    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Contract> getAllContract() {

        Query query = getCurrentSession().getNamedQuery("Contract.fillAllContract");
        return (List<Contract>) query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Contract> getContractActive() {

        Query query = getCurrentSession().getNamedQuery("Contract.fillContractActive");
        return (List<Contract>) query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void insertOrUpdateContract(Contract contract) {
        getCurrentSession().saveOrUpdate(contract);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Price> getAllPrice() {
        Query query = getCurrentSession().getNamedQuery("Price.fillPriceActive");
        return (List<Price>) query.list();
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<Price> getPriceByContract(Long contractId, Integer priceType) {
        Query query = getCurrentSession().getNamedQuery("Price.fillPriceByContract");
        query.setParameter("contractId", contractId);
        query.setParameter("priceType", priceType);
        return (List<Price>) query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void insertOrUpdatePrice(Price price) {
        getCurrentSession().saveOrUpdate(price);
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<PriceLocation> getPriceLocationByPrice(Long priceId) {
        Query query = getCurrentSession().getNamedQuery("PriceLocation.fillPriceLocationByPrice");
        query.setParameter("priceId", priceId);
        return (List<PriceLocation>) query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void insertOrUpdatePriceLocation(PriceLocation priceLocation) {
        getCurrentSession().saveOrUpdate(priceLocation);
    }

}
