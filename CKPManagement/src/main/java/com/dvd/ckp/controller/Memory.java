/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.ConstructionService;
import com.dvd.ckp.business.service.CustomerService;
import com.dvd.ckp.business.service.LocationServices;
import com.dvd.ckp.domain.Construction;
import com.dvd.ckp.domain.Customer;
import com.dvd.ckp.domain.Location;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;

/**
 *
 * @author dmin
 */
public class Memory {

    private static final Logger logger = Logger.getLogger(Memory.class);

    private CustomerService customerService;

    private ConstructionService constructionService;

    private LocationServices locationServices;

    //list KH
    public static Map<Long, Customer> lstCustomerCache;
    //list Cong trinh
    public static Map<Long, Construction> lstConstructionCache;

    //list Vi tri
    public static Map<Long, Location> lstLocationCache;

    /**
     * Ham start up dashboard
     */
    public void startup() {
        try {
            logger.info("==============STARTUP MEMORY==============");
            loadCustomer();
            loadConstruction();
            loadLocation();
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
        lstCustomerCache.clear();
        lstConstructionCache.clear();
        lstLocationCache.clear();
    }

    public void loadCustomer() {
        List<Customer> vlstCustomers;
        vlstCustomers = customerService.getCustomerActive();
        if (vlstCustomers != null) {
            lstCustomerCache = vlstCustomers.stream().collect(Collectors.toMap(Customer::getCustomerId, customers -> customers));
        } else {
            lstCustomerCache = new HashMap<>();
        }
    }

    public void loadConstruction() {
        List<Construction> vlstConstructions;
        vlstConstructions = constructionService.getConstructionActive();
        if (vlstConstructions != null) {
            lstConstructionCache = vlstConstructions.stream().collect(Collectors.toMap(Construction::getConstructionId, constructions -> constructions));
        } else {
            lstConstructionCache = new HashMap<>();
        }
    }

    public void loadLocation() {
        List<Location> vlstLocation;
        vlstLocation = locationServices.getListLocation();
        if (vlstLocation != null) {
            lstLocationCache = vlstLocation.stream().collect(Collectors.toMap(Location::getLocationID, locations -> locations));
        } else {
            lstLocationCache = new HashMap<>();
        }
    }

    public Map<Long, Customer> getCustomerCache() {
        if (lstCustomerCache == null || lstCustomerCache.isEmpty()) {
            loadCustomer();
        }
        return lstCustomerCache;
    }

    public void setLstCustomerCache(Map<Long, Customer> lstCustomer) {
        lstCustomerCache = lstCustomer;
    }

    public Map<Long, Construction> getConstructionCache() {
        if (lstConstructionCache == null || lstConstructionCache.isEmpty()) {
            loadConstruction();
        }
        return lstConstructionCache;
    }

    public void setLstConstructionCache(Map<Long, Construction> constructionCache) {
        lstConstructionCache = constructionCache;
    }

    public Map<Long, Location> getLocationCache() {
        if (lstLocationCache == null || lstLocationCache.isEmpty()) {
            loadLocation();
        }
        return lstLocationCache;
    }

    public void setLstLocationCache(Map<Long, Location> lstLocationCache) {
        Memory.lstLocationCache = lstLocationCache;
    }

    /*------------------------------get set cua service ------------------------------*/
    public CustomerService getCustomerService() {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public ConstructionService getConstructionService() {
        return constructionService;
    }

    public void setConstructionService(ConstructionService constructionService) {
        this.constructionService = constructionService;
    }

    public LocationServices getLocationServices() {
        return locationServices;
    }

    public void setLocationServices(LocationServices locationServices) {
        this.locationServices = locationServices;
    }

}
