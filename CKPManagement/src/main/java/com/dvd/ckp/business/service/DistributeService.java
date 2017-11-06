/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.service;

import com.dvd.ckp.domain.Construction;
import com.dvd.ckp.domain.Distribute;
import java.util.List;

/**
 *
 * @author dmin
 */
public interface DistributeService {

    List<Distribute> getAllDistribute();

    List<Distribute> getDistributeActive();

    void insertOrUpdateDistribute(Distribute distribute);
    
}
