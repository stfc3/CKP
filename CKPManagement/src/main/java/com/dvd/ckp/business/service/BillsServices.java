package com.dvd.ckp.business.service;

import java.util.List;

import com.dvd.ckp.bean.QuantityValue;
import com.dvd.ckp.domain.BillViewDetail;
import com.dvd.ckp.domain.Bills;
import com.dvd.ckp.domain.BillsDetail;
import com.dvd.ckp.domain.CalculatorRevenue;

public interface BillsServices {

	public List<Bills> getAllData();

	public void save(Bills bills);

	public void update(Bills bills);

	public void delete(Bills bills);

	public List<BillsDetail> getBillDetail();

	public List<BillsDetail> getBillDetail(Long billID);

	public void save(BillsDetail billsDetail);

	public void update(BillsDetail billsDetail);

	public void update(Integer isFar, Double quantityConvert, Integer maxStaff, Long billDetail);

	public void upadte(Double quantityApprove, Double totalApprove, Long billDetailID);

	public void delete(BillsDetail billsDetail);

	public List<CalculatorRevenue> calculatorRevenue(Long constructionId, Long pumpType, Long locationType,
			Long locationID, Double quantity, Integer shift, int numSwitch, int numAuto);

	public List<BillViewDetail> getDataView(Long billID);

	public List<QuantityValue> getQuantity(Long billDetailId);

}
