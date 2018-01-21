/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.CustomerService;
import com.dvd.ckp.domain.Customer;
import com.dvd.ckp.utils.SpringConstant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;

/**
 *
 * @author dmin
 */
public class Memory {

    private static final Logger logger = Logger.getLogger(Memory.class);
   
    private CustomerService customerService;

    public CustomerService getCustomerService() {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }
    

    public static Map<Long, Customer> mapCustomer;

    /**
     * Ham start up dashboard
     */
    public void startup() {
        try {
            logger.info("==============STARTUP MEMORY==============");
//            customerService = (CustomerService) SpringUtil.getBean(SpringConstant.CUSTOMER_SERVICES);
            loadCustomer();
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * Ham reload cache
     */
    public void reloadCache() {
        try {
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * Ham shut down
     */
    public void shutdown() {
        logger.info("==============SHUTDOWN MEMORY==============");
        clearCache();
    }

    /**
     * Ham xoa cache
     */
    private synchronized void clearCache() {
        mapCustomer.clear();
    }

    private void loadCustomer() {
        List<Customer> vlstCustomers;
        vlstCustomers = customerService.getCustomerActive();
        if (vlstCustomers != null) {
            mapCustomer = vlstCustomers.stream().collect(Collectors.toMap(Customer::getCustomerId, customers -> customers));
        } else {
            mapCustomer = new HashMap<>();
        }
    }

}
