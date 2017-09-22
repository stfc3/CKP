package com.dvd.ckp.business.service;

import java.util.List;

import com.dvd.ckp.domain.Bills;
import com.dvd.ckp.domain.BillsDetail;

public interface BillsServices {

	public List<Bills> getAllData();

	public void save(Bills bills);

	public void update(Bills bills);

	public void delete(Bills bills);
	
	public List<BillsDetail> getBillDetail();

}
