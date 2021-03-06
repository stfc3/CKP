package com.dvd.ckp.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dvd.ckp.bean.QuantityValue;
import com.dvd.ckp.business.dao.BillDAO;
import com.dvd.ckp.domain.BillViewDetail;
import com.dvd.ckp.domain.Bills;
import com.dvd.ckp.domain.BillsDetail;
import com.dvd.ckp.domain.CalculatorRevenue;
import java.util.Date;

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
    public List<CalculatorRevenue> calculatorRevenue(Long constructionId, Long pumpType, Long locationType,
            Long locationID, Double quantity, Double shift, Double numSwitch, Integer numAuto) {
        // TODO Auto-generated method stub
        return billDAO.calculatorRevenue(constructionId, pumpType, locationType, locationID, quantity, shift, numSwitch,
                numAuto);
    }

    @Override
    public List<BillsDetail> getBillDetail(Long billID) {
        // TODO Auto-generated method stub
        return billDAO.getBillDetail(billID);
    }

    @Override
    public List<BillViewDetail> getDataView(Long billID) {
        // TODO Auto-generated method stub
        return billDAO.getDataView(billID);
    }

    @Override
    public List<QuantityValue> getQuantity(Long billDetailId) {
        // TODO Auto-generated method stub
        return billDAO.getQuantity(billDetailId);
    }

    @Override
    public void update(Integer isFar, Double quantityConvert, Integer maxStaff, Long billDetail) {
        // TODO Auto-generated method stub
        billDAO.update(isFar, quantityConvert, maxStaff, billDetail);
    }

    @Override
    public void upadte(Double quantityApprove, Double totalApprove, Long billDetailID) {
        // TODO Auto-generated method stub

        billDAO.upadte(quantityApprove, totalApprove, billDetailID);

    }

    @Override
    public List<BillViewDetail> getApproveBill(String billCode, Long contructionId, Date pumpDate, Long pumpId, Integer limitQuery) {
        // TODO Auto-generated method stub
        return billDAO.getApproveBill(billCode, contructionId, pumpDate, pumpId, limitQuery);
    }

    @Override
    public void update(String billCode, Long billID) {
        billDAO.update(billCode, billID);
    }

    @Override
    public List<Bills> onSearch(Long customer, Long construction, String date, Integer limitQuery) {
        return billDAO.onSearch(customer, construction, date, limitQuery);
    }
    @Override
    public List<Bills> getBillByCode(String billCode) {
        return billDAO.getBillByCode(billCode);
    }

}
