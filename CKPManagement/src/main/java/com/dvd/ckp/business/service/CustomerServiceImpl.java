/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.service;

import com.dvd.ckp.business.dao.CustomerDAO;
import com.dvd.ckp.domain.Customer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dmin
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDAO CustomerDAO;


    @Transactional(readOnly = true)
    @Override
    public List<Customer> getAllCustomer() {
        return CustomerDAO.getAllCustomer();
    }
    @Transactional(readOnly = true)
    @Override
    public List<Customer> getCustomerActive() {
        return CustomerDAO.getCustomerActive();
    }
    
    @Transactional
    @Override
    public void insertOrUpdateCustomer(Customer customer) {
        CustomerDAO.insertOrUpdateCustomer(customer);
    }

}
