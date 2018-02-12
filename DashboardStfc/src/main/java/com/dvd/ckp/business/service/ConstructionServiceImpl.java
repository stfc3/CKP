/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.service;

import com.dvd.ckp.business.dao.ConstructionDAO;
import com.dvd.ckp.domain.Construction;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dmin
 */
@Service
public class ConstructionServiceImpl implements ConstructionService {

    @Autowired
    private ConstructionDAO ConstructionDAO;


    @Transactional(readOnly = true)
    @Override
    public List<Construction> getAllConstruction() {
        return ConstructionDAO.getAllConstruction();
    }
    @Transactional(readOnly = true)
    @Override
    public List<Construction> getConstructionActive() {
        return ConstructionDAO.getConstructionActive();
    }
    
    @Transactional
    @Override
    public void insertOrUpdateConstruction(Construction construction) {
        ConstructionDAO.insertOrUpdateConstruction(construction);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<Construction> getConstructionByCustomer(Long customerId) {
        return ConstructionDAO.getConstructionByCustomer(customerId);
    }

}
