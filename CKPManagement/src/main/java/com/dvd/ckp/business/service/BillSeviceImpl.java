package com.dvd.ckp.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dvd.ckp.business.dao.BillDAO;
import com.dvd.ckp.domain.Bills;
import com.dvd.ckp.domain.BillsDetail;

public class BillSeviceImpl implements BillsServices {
	@Autowired
	BillDAO billDAO;

	@Override
	public List<Bills> getAllData() {
		// TODO Auto-generated method stub
		return billDAO.getData();
	}

	@Override
	public void save(Bills bills) {
		billDAO.save(bills);

	}

	@Override
	public void update(Bills bills) {
		billDAO.update(bills);

	}

	@Override
	public void delete(Bills bills) {
		billDAO.delete(bills);

	}

	@Override
	public List<BillsDetail> getBillDetail() {
		// TODO Auto-generated method stub
		return billDAO.getBillDetail();
	}

}
