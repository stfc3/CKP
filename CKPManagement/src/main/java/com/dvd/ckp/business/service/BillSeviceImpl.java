package com.dvd.ckp.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dvd.ckp.business.dao.BillDAO;
import com.dvd.ckp.domain.Bills;
import com.dvd.ckp.domain.BillsDetail;
import com.dvd.ckp.domain.CalculatorRevenue;

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

	@Override
	public void save(BillsDetail billsDetail) {
		billDAO.save(billsDetail);

	}

	@Override
	public void update(BillsDetail billsDetail) {
		billDAO.update(billsDetail);

	}

	@Override
	public void delete(BillsDetail billsDetail) {
		// TODO Auto-generated method stub
		billDAO.delete(billsDetail);

	}

	@Override
	public List<CalculatorRevenue> calculatorRevenue(Long constructionId, Long pumpId, Long pumpType, Long locationType,
			Long locationID, Double quantity, int shift) {
		// TODO Auto-generated method stub
		return billDAO.calculatorRevenue(constructionId, pumpId, pumpType, locationType, locationID, quantity, shift);
	}

}
