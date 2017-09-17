/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.dao;

import com.dvd.ckp.domain.Contract;
import java.util.List;

/**
 *
 * @author dmin
 */
public interface ContractDAO {

    List<Contract> getAllContract();

    void insertOrUpdateContract(Contract contract);

}
