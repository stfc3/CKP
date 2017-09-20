/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.dao;

import com.dvd.ckp.domain.Customer;
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
public class CustomerDAOImpl implements CustomerDAO {

	@Autowired
	SessionFactory sessionFactory;

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> getAllCustomer() {

		Query query = getCurrentSession().getNamedQuery("Customers.fillAllCustomer");
		return (List<Customer>) query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> getCustomerActive() {

		Query query = getCurrentSession().getNamedQuery("Customers.fillCustomerActive");
		return (List<Customer>) query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertOrUpdateCustomer(Customer customer) {
		getCurrentSession().saveOrUpdate(customer);
	}

}
