package com.dvd.ckp.business.service;

import java.util.List;

import com.dvd.ckp.bean.QuantityValue;
import com.dvd.ckp.domain.BillViewDetail;
import com.dvd.ckp.domain.Bills;
import com.dvd.ckp.domain.BillsDetail;
import com.dvd.ckp.domain.CalculatorRevenue;
import java.util.Date;

public interface BillsServices {

    public List<Bills> getAllData();

    public List<Bills> onSearch(Long customer, Long construction, String date, Integer limitQuery);

    public void save(Bills bills);

    public void update(Bills bills);

    public void delete(Bills bills);

    public List<BillsDetail> getBillDetail();

    public List<BillsDetail> getBillDetail(Long billID);

    public void save(BillsDetail billsDetail);

    public void update(BillsDetail billsDetail);

    public void update(Integer isFar, Double quantityConvert, Integer maxStaff, Long billDetail);

    public void upadte(Double quantityApprove, Double totalApprove, Long billDetailID);

    public void update(String billCode, Long billID);

    public void delete(BillsDetail billsDetail);

    public List<CalculatorRevenue> calculatorRevenue(Long constructionId, Long pumpType, Long locationType,
            Long locationID, Double quantity, Integer shift, Integer numSwitch, Integer numAuto);

    public List<BillViewDetail> getDataView(Long billID);

    public List<QuantityValue> getQuantity(Long billDetailId);

    public List<BillViewDetail> getApproveBill(String billCode, Long contructionId, Date pumpDate, Long pumpId, Integer limitQuery);

    public List<Bills> getBillByCode(String billCode);
}
