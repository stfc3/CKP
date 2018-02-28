/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.daond.demo.chart;

import com.dvd.ckp.business.service.ConstructionService;
import com.dvd.ckp.business.service.CustomerService;
import com.dvd.ckp.component.MyListModel;
import com.dvd.ckp.domain.Construction;
import com.dvd.ckp.domain.Customer;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Iframe;

/**
 *
 * @author daond
 */
public class ChartLine extends GenericForwardComposer {

    private static final Logger logger = Logger.getLogger(ChartLine.class);
    @WireVariable
    protected CustomerService customerService;
    @WireVariable
    protected ConstructionService constructionService;
    @Wire
    private Iframe ifrReportMonth;
    @Wire
    private Combobox cbxCustomer;
    @Wire
    private Combobox cbxConstruction;
    @Wire
    private Datebox dteFromDate;
    @Wire
    private Datebox dteToDate;

    private List<Customer> lstCustomers;
//    private String reportName = "reportMass.rptdesign";
//    private String fileName = "BaoCaoKhoiLuongHopDong";

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        customerService = (CustomerService) SpringUtil.getBean(SpringConstant.CUSTOMER_SERVICES);
        constructionService = (ConstructionService) SpringUtil.getBean(SpringConstant.CONSTRUCTION_SERVICES);

        lstCustomers = customerService.getCustomerActive();

        Customer customerOption = new Customer();
        customerOption.setCustomerId(null);
        customerOption.setCustomerName(Labels.getLabel("option"));
        if (lstCustomers == null) {
            lstCustomers = new ArrayList<>();
        }
        lstCustomers.add(Constants.FIRST_INDEX, customerOption);
        MyListModel listCustomerModel = new MyListModel<>(lstCustomers);
        listCustomerModel.addToSelection(customerOption);
        cbxCustomer.setModel(listCustomerModel);

    }

    
//    private HashMap getParam() {
//        HashMap paramMap = new HashMap();
//        paramMap.put(BirtConstant.PARAM_REPORT, reportName);
//        if (StringUtils.isValidString(cbxCustomer.getValue()) && !Labels.getLabel("option").equals(cbxCustomer.getValue())) {
//            paramMap.put(cbxCustomer.getName(), cbxCustomer.getSelectedItem().getValue());
//        }
//        if (StringUtils.isValidString(cbxConstruction.getValue()) && !Labels.getLabel("option").equals(cbxConstruction.getValue())) {
//            paramMap.put(cbxConstruction.getName(), cbxConstruction.getSelectedItem().getValue());
//        }
//        paramMap.put(dteFromDate.getName(), DateTimeUtils.convertDateToString(dteFromDate.getValue(), BirtConstant.PRD_ID));
//        paramMap.put(dteToDate.getName(), DateTimeUtils.convertDateToString(dteToDate.getValue(), BirtConstant.PRD_ID));
//        return paramMap;
//    }

    public void onSelect$cbxCustomer() {

        Long customerId = null;
        List<Construction> lstConstructions;
//        List<Construction> constructionSelectDefault= new ArrayList<>();
        if (StringUtils.isValidString(cbxCustomer.getValue()) && !Labels.getLabel("option").equals(cbxCustomer.getValue())) {
            customerId = cbxCustomer.getSelectedItem().getValue();
        }
        lstConstructions = constructionService.getConstructionByCustomer(customerId);

        Construction constructionOption = new Construction();
        constructionOption.setConstructionId(null);
        constructionOption.setConstructionName(Labels.getLabel("option"));
        if (lstConstructions == null) {
            lstConstructions = new ArrayList<>();
        }
        lstConstructions.add(Constants.FIRST_INDEX, constructionOption);

        MyListModel listContructionModel = new MyListModel<>(lstConstructions);
        cbxConstruction.setValue("");
        cbxConstruction.setModel(listContructionModel);

    }

    private boolean validateParam() {
        if (dteFromDate.getValue() == null) {
            Messagebox.show("Từ ngày không được để trống", "Lỗi", Messagebox.OK, Messagebox.ERROR);
            return false;
        }
        if (dteToDate.getValue() == null) {
            Messagebox.show("Đến ngày không được để trống", "Lỗi", Messagebox.OK, Messagebox.ERROR);
            return false;
        }
        if (dteFromDate.getValue().after(dteToDate.getValue())) {
            Messagebox.show("Từ ngày phải nhỏ hơn hoặc bằng đến ngày", "Lỗi", Messagebox.OK, Messagebox.ERROR);
            return false;
        }
        return true;
    }

}
