package com.dvd.ckp.business.dao;

import java.util.List;

import com.dvd.ckp.domain.Bills;
import com.dvd.ckp.domain.BillsDetail;

public interface BillDAO {

	public List<Bills> getData();

	public void save(Bills bills);

	public void update(Bills bills);

	public void delete(Bills bills);

	public List<BillsDetail> getBillDetail();

}
