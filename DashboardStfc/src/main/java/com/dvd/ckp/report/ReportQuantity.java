/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.report;

import com.dvd.ckp.birt.BirtConstant;
import com.dvd.ckp.business.service.PumpServices;
import com.dvd.ckp.business.service.StaffServices;
import com.dvd.ckp.component.MyListModel;
import com.dvd.ckp.domain.Pumps;
import com.dvd.ckp.domain.Staff;
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
 * @author daond
 */
public class ReportQuantity extends GenericForwardComposer {

    private static final Logger logger = Logger.getLogger(ReportQuantity.class);
    @WireVariable
    protected PumpServices pumpsService;
    @WireVariable
    protected StaffServices staffService;
    @Wire
    private Iframe ifrReportMonth;
    @Wire
    private Combobox cbxPump;
    @Wire
    private Combobox cbxStaff;
    @Wire
    private Datebox dteFromDate;
    @Wire
    private Datebox dteToDate;

    private List<Pumps> lstPumps;
    private List<Staff> lstStaff;
    private String reportName = "reportQuantity.rptdesign";
    private String fileName = "BaoCaoKhoiLuongCongNhan";

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        pumpsService = (PumpServices) SpringUtil.getBean(SpringConstant.PUMPS_SERVICES);
        staffService = (StaffServices) SpringUtil.getBean(SpringConstant.STAFF_SERVICES);

        lstPumps = pumpsService.getAllListData();

        Pumps customerOption = new Pumps();
        customerOption.setPumpsID(null);
        customerOption.setPumpsName(Labels.getLabel("option"));
        if (lstPumps == null) {
            lstPumps = new ArrayList<>();
        }
        lstPumps.add(Constants.FIRST_INDEX, customerOption);
        MyListModel listCustomerModel = new MyListModel<>(lstPumps);
        listCustomerModel.addToSelection(customerOption);
        cbxPump.setModel(listCustomerModel);

        lstStaff = staffService.getAllData();
        Staff staffOption = new Staff();
        staffOption.setStaffId(null);
        staffOption.setStaffName(Labels.getLabel("option"));
        if (lstStaff == null) {
            lstStaff = new ArrayList<>();
        }
        lstStaff.add(Constants.FIRST_INDEX, staffOption);

        MyListModel listStaffModel = new MyListModel<>(lstStaff);
        listStaffModel.addToSelection(staffOption);

        cbxStaff.setModel(listStaffModel);

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
        if (StringUtils.isValidString(cbxPump.getValue()) && !Labels.getLabel("option").equals(cbxPump.getValue())) {
            paramMap.put(cbxPump.getName(), cbxPump.getSelectedItem().getValue());
        }
        if (StringUtils.isValidString(cbxStaff.getValue()) && !Labels.getLabel("option").equals(cbxStaff.getValue())) {
            paramMap.put(cbxStaff.getName(), cbxStaff.getSelectedItem().getValue());
        }
        paramMap.put(dteFromDate.getName(), DateTimeUtils.convertDateToString(dteFromDate.getValue(), BirtConstant.PRD_ID));
        paramMap.put(dteToDate.getName(), DateTimeUtils.convertDateToString(dteToDate.getValue(), BirtConstant.PRD_ID));
        return paramMap;
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
