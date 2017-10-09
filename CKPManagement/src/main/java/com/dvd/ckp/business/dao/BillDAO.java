package com.dvd.ckp.business.dao;

import java.util.List;

import com.dvd.ckp.domain.BillViewDetail;
import com.dvd.ckp.domain.Bills;
import com.dvd.ckp.domain.BillsDetail;
import com.dvd.ckp.domain.CalculatorRevenue;

public interface BillDAO {

	public List<Bills> getData();

	public void save(Bills bills);

	public void update(Bills bills);

	public void delete(Bills bills);

	public List<BillsDetail> getBillDetail();

	public List<BillsDetail> getBillDetail(Long billID);

	public void save(BillsDetail billsDetail);

	public void update(BillsDetail billsDetail);

	public void delete(BillsDetail billsDetail);

	public List<CalculatorRevenue> calculatorRevenue(Long constructionId, Long pumpId, Long pumpType, Long locationType,
			Long locationID, Double quantity, int shift);

	public List<BillViewDetail> getDataView(Long billID);

}
