/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.service;

import com.dvd.ckp.business.dao.ContractDAO;
import com.dvd.ckp.domain.Contract;
import com.dvd.ckp.domain.Price;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dmin
 */
@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractDAO ContractDAO;


    @Transactional(readOnly = true)
    @Override
    public List<Contract> getAllContract() {
        return ContractDAO.getAllContract();
    }
    @Transactional(readOnly = true)
    @Override
    public List<Contract> getContractActive() {
        return ContractDAO.getContractActive();
    }
    
    @Transactional
    @Override
    public void insertOrUpdateContract(Contract contract) {
        ContractDAO.insertOrUpdateContract(contract);
    }
    @Transactional(readOnly = true)
    @Override
    public List<Price> getAllPrice() {
        return ContractDAO.getAllPrice();
    }
    @Transactional(readOnly = true)
    @Override
    public List<Price> getPriceByContract(Long contractId) {
        return ContractDAO.getPriceByContract(contractId);
    }
    
    @Transactional
    @Override
    public void insertOrUpdatePrice(Price price) {
        ContractDAO.insertOrUpdatePrice(price);
    }

}
