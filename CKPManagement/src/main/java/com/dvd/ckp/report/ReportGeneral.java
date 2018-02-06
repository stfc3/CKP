/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.report;

import com.dvd.ckp.birt.BirtConstant;
import com.dvd.ckp.business.service.ConstructionService;
import com.dvd.ckp.business.service.CustomerService;
import com.dvd.ckp.component.MyListModel;
import com.dvd.ckp.domain.Construction;
import com.dvd.ckp.domain.Customer;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.DateTimeUtils;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
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
 * @author dmin
 */
public class ReportGeneral extends GenericForwardComposer {

    private static final Logger logger = Logger.getLogger(ReportGeneral.class);
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
    private Datebox dteMonth;

    private List<Customer> lstCustomers;
    private String reportName = "reportGeneral.rptdesign";
    private String fileName = "BaoCaoTongHopKhoiLuongGiaTri";

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

    public void onClick$btnHtml() {

        if (!validateParam()) {
            return;
        }
        HashMap paramMap = getParam();
        StringBuilder src = buildUrl(paramMap);
        ifrReportMonth.setSrc(src.toString());
    }

    public void onClick$btnExcel() {
        if (!validateParam()) {
            return;
        }
        HashMap paramMap = getParam();
        paramMap.put(BirtConstant.PARAM_EXTENSION, BirtConstant.EXCEL_EXTENSION);
        paramMap.put(BirtConstant.PARAM_FILE_NAME, fileName);
        StringBuilder src = buildUrl(paramMap);
        ifrReportMonth.setSrc(src.toString());
    }

    private StringBuilder buildUrl(HashMap paramMap) {

        StringBuilder src = new StringBuilder(BirtConstant.BIRT_SERVLET);
        for (Object key : paramMap.keySet()) {
            src.append(key);
            src.append(BirtConstant.BIRT_EQUAL);
            src.append(paramMap.get(key));
            src.append(BirtConstant.BIRT_AND);
        }
        src.append(BirtConstant.PARAM_TIME);
        src.append(BirtConstant.BIRT_EQUAL);
        src.append(System.currentTimeMillis());
        return src;
    }

    private HashMap getParam() {
        HashMap paramMap = new HashMap();
        paramMap.put(BirtConstant.PARAM_REPORT, reportName);
        if (StringUtils.isValidString(cbxCustomer.getValue()) && !Labels.getLabel("option").equals(cbxCustomer.getValue())) {
            paramMap.put(cbxCustomer.getName(), cbxCustomer.getSelectedItem().getValue());
        }
        if (StringUtils.isValidString(cbxConstruction.getValue()) && !Labels.getLabel("option").equals(cbxConstruction.getValue())) {
            paramMap.put(cbxConstruction.getName(), cbxConstruction.getSelectedItem().getValue());
        }
        paramMap.put(dteMonth.getName(), DateTimeUtils.convertDateToString(dteMonth.getValue(), BirtConstant.PRD_ID));
        return paramMap;
    }

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
        if (dteMonth.getValue() == null) {
            Messagebox.show("Tháng không được để trống", "Lỗi", Messagebox.OK, Messagebox.ERROR);
            return false;
        }
        return true;
    }

}
