/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.service;

import com.dvd.ckp.business.dao.DistributeDAO;
import com.dvd.ckp.domain.Distribute;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dmin
 */
@Service
public class DistributeServiceImpl implements DistributeService {

    @Autowired
    private DistributeDAO DistributeDAO;


    @Transactional(readOnly = true)
    @Override
    public List<Distribute> getAllDistribute() {
        return DistributeDAO.getAllDistribute();
    }
    @Transactional(readOnly = true)
    @Override
    public List<Distribute> getDistributeActive() {
        return DistributeDAO.getDistributeActive();
    }
    
    @Transactional
    @Override
    public void insertOrUpdateDistribute(Distribute distribute) {
        DistributeDAO.insertOrUpdateDistribute(distribute);
    }
    

}
