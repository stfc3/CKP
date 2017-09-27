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

/**
 *
 * @author dmin
 */
public interface ContractDAO {

    List<Contract> getAllContract();

    List<Contract> getContractActive();

    void insertOrUpdateContract(Contract contract);

    List<Price> getAllPrice();

    List<Price> getPriceByContract(Long contractId);

    void insertOrUpdatePrice(Price price);
    
    List<PriceLocation> getPriceLocationByPrice(Long priceId);

    void insertOrUpdatePriceLocation(PriceLocation priceLocation);
}
