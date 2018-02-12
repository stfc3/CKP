package com.dvd.ckp.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.dvd.ckp.business.service.BillsServices;
import com.dvd.ckp.business.service.StaffServices;
import com.dvd.ckp.component.MyListModel;
import com.dvd.ckp.domain.Staff;
import com.dvd.ckp.domain.StaffQuantity;
import com.dvd.ckp.utils.SpringConstant;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public class AddStaffController extends GenericForwardComposer {

    /**
     *
     */
    private static final long serialVersionUID = -278079129703470694L;
    private static final Logger logger = Logger.getLogger(AddStaffController.class);
    @WireVariable
    protected StaffServices staffService;
    @WireVariable
    protected BillsServices billsServices;
    MyListModel<Staff> listDataStaff;

    private ListModelList<Staff> listDataModel;
    private List<Staff> lstStaff;

    private List<Staff> lstStaffAll;

    private ListModelList<Staff> listDataModelSelected;
    private List<Staff> lstStaffSelected;
    @Wire
    private Grid gridFullStaff;
    @Wire
    private Window addStaff;

    @Wire
    private Grid gridSelectStaff;
    @Wire
    private Label titleStaffSelected;
//	@Wire
//	private Radio yes;
//	@Wire
//	private Radio no;
    @Wire
    private Intbox intMaxStaff;
    @Wire
    private Doublebox dbQuantityConvert;
    @Wire
    private Longbox txtBillID;
    @Wire
    private Longbox txtPumpType;
    @Wire
    private Combobox cbFilterName;

    @Wire
    private Combobox cbFilterNameSelected;

    private Long billID;
    List<StaffQuantity> listQuantity;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        staffService = (StaffServices) SpringUtil.getBean(SpringConstant.STAFF_SERVICES);
        billsServices = (BillsServices) SpringUtil.getBean(SpringConstant.BILL_SERVICES);
        lstStaff = new ArrayList<Staff>();
        lstStaffAll = new ArrayList<Staff>();
        billID = txtBillID.getValue();
        Long pumpType = txtPumpType.getValue();

        List<Staff> vlstStaff = staffService.getAllData();
        if (vlstStaff != null) {
            lstStaff.addAll(vlstStaff);
            lstStaffAll.addAll(vlstStaff);

        }

        /*
		 * lay danh sach nhung cong nhan da duoc trong bang quantity_staff
         */
        listQuantity = new ArrayList<>();
        List<StaffQuantity> lstStaffQuantity = staffService.getQuantity(billID);
        if (lstStaffQuantity != null && !lstStaffQuantity.isEmpty()) {
            listQuantity.addAll(lstStaffQuantity);
        }
        int staff = listQuantity.size();
        if (pumpType == 1l) {
            if (staff == 0) {
                intMaxStaff.setValue(5);
            } else {
                intMaxStaff.setValue(staff);
            }
        } else if (pumpType == 3l) {

            if (staff == 0) {
                intMaxStaff.setValue(3);
            } else {
                intMaxStaff.setValue(staff);
            }
        }
        /*
		 * loai bo nhung cong nhan da duoc chon khoi list full
         */
        removeStaffSelected(lstStaffQuantity);
        listDataModel = new ListModelList<Staff>(lstStaff);
        gridFullStaff.setModel(listDataModel);

        getDataSelected(lstStaffQuantity);
        listDataModelSelected = new ListModelList<Staff>(lstStaffSelected);
        gridSelectStaff.setModel(listDataModelSelected);

        listDataStaff = new MyListModel<>(lstStaff);
        cbFilterName.setModel(listDataStaff);

        cbFilterNameSelected.setModel(listDataModelSelected);

    }

    private void getDataSelected(List<StaffQuantity> staffQuantities) {
        lstStaffSelected = new ArrayList<>();

        if (lstStaffAll != null && !lstStaffAll.isEmpty()) {
            for (Staff staff : lstStaffAll) {
                if (staffQuantities != null && !staffQuantities.isEmpty()) {
                    for (StaffQuantity quantity : staffQuantities) {
                        if (quantity.getStaffId().equals(staff.getStaffId())) {
                            // lstStaff.remove(getIndexStaff(staff.getStaffId()));
                            lstStaffSelected.add(staff);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void removeStaffSelected(List<StaffQuantity> staffQuantities) {

        if (lstStaffAll != null && !lstStaffAll.isEmpty()) {
            for (Staff staff : lstStaffAll) {
                if (staffQuantities != null && !staffQuantities.isEmpty()) {
                    for (StaffQuantity quantity : staffQuantities) {
                        if (quantity.getStaffId().equals(staff.getStaffId())) {
                            lstStaff.remove(staff);
                            break;

                        }
                    }
                }
            }
        }
    }

    public void onAdd(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        Staff c = rowSelected.getValue();
        getDataInRow(lstCell, c);
        lstStaffSelected.add(c);
        if (lstStaffSelected != null && !lstStaffSelected.isEmpty()) {
            gridSelectStaff.setVisible(true);
            titleStaffSelected.setVisible(true);
        }
        lstStaff.remove(getIndexStaff(c.getStaffId()));
        onReloadSelectGrid();
    }

    public void onAddRow(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget();
        List<Component> lstCell = rowSelected.getChildren();
        Staff c = rowSelected.getValue();
        getDataInRow(lstCell, c);
        lstStaffSelected.add(c);
        if (lstStaffSelected != null && !lstStaffSelected.isEmpty()) {
            gridSelectStaff.setVisible(true);
            titleStaffSelected.setVisible(true);
        }
        lstStaff.remove(getIndexStaff(c.getStaffId()));
        onReloadSelectGrid();
    }

    private void getDataInRow(List<Component> lstCell, Staff staff) {

        Textbox txtStaffCode = (Textbox) lstCell.get(1).getFirstChild();
        Textbox txtStaffName = (Textbox) lstCell.get(2).getFirstChild();
        Textbox txtPhone = (Textbox) lstCell.get(3).getFirstChild();
        Textbox txtEmail = (Textbox) lstCell.get(4).getFirstChild();
        Textbox txtAddress = (Textbox) lstCell.get(5).getFirstChild();
        staff.setStaffCode(txtStaffCode.getValue());
        staff.setStaffName(txtStaffName.getValue());
        staff.setPhone(txtPhone.getValue());
        staff.setAddress(txtAddress.getValue());
        staff.setEmail(txtEmail.getValue());

    }

    private void onReloadSelectGrid() {
        listDataModel = new ListModelList<Staff>(lstStaff);
        gridFullStaff.setModel(listDataModel);
        listDataModelSelected = new ListModelList<Staff>(lstStaffSelected);
        gridSelectStaff.setModel(listDataModelSelected);
    }

    private int getIndexStaff(Long staffID) {
        if (lstStaff != null && !lstStaff.isEmpty()) {
            for (Staff staff : lstStaff) {
                if (staffID.equals(staff.getStaffId())) {
                    return lstStaff.indexOf(staff);
                }
            }
        }
        return -1;
    }

    private int getIndexStaffSelected(Long staffID) {
        if (lstStaffSelected != null && !lstStaffSelected.isEmpty()) {
            for (Staff staff : lstStaffSelected) {
                if (staffID.equals(staff.getStaffId())) {
                    return lstStaffSelected.indexOf(staff);
                }
            }
        }
        return -1;
    }

    public void onDelete(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        Staff c = rowSelected.getValue();
        getDataInRow(lstCell, c);
        lstStaff.add(c);
        lstStaffSelected.remove(getIndexStaffSelected(c.getStaffId()));

        onReloadSelectGrid();
    }

    public void onAction(ForwardEvent event) {
        Messagebox.show(Labels.getLabel("staff.quantity.comfirm"), Labels.getLabel("comfirm"),
                Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event e) {
                if (Messagebox.ON_YES.equals(e.getName())) {
                    int isFar = 0;
//							if (yes.isChecked()) {
//								isFar = 1;
//							} else {
//								isFar = 0;
//							}
                    Long pumpType = txtPumpType.getValue();
                    Integer maxStaff = intMaxStaff.getValue();
                    if (maxStaff == null) {
                        if (pumpType == 1l) {
                            maxStaff = 5;
                        } else if (pumpType == 3l) {
                            maxStaff = 3;
                        }

                    }

                    Double quantityConvert = dbQuantityConvert.getValue();
                    if (quantityConvert == null) {
                        quantityConvert = 60d;
                    }
                    billsServices.update(isFar, quantityConvert, maxStaff, billID);

                    staffService.delete(billID);
                    List<StaffQuantity> lstQuantity = new ArrayList<>();

                    if (lstStaffSelected != null && !lstStaffSelected.isEmpty()) {
                        for (Staff staff : lstStaffSelected) {
                            StaffQuantity quantity = new StaffQuantity();
                            quantity.setStaffId(staff.getStaffId());
                            quantity.setBillId(billID);

                            lstQuantity.add(quantity);

                        }
                    }
                    staffService.save(lstQuantity);
                    logger.info("Bill detail id: " + billID);
                    billsServices.getQuantity(billID).get(0).getV_quantity();
                    onReloadSelectGrid();

                    //reload data
                    Events.sendEvent("onClick", (Button) ((Window) addStaff.getParent()).getFellow("reloadData"), null);

                }
                addStaff.detach();
            }
        });
    }

    public void onChange$cbFilterName() {
        Staff staff = new Staff();
        Long staffID = null;
        if (cbFilterName.getSelectedItem() != null) {
            staffID = cbFilterName.getSelectedItem().getValue();
        }

        staff.setStaffId(staffID);

        filter(staff);
    }

    public void onChange$cbFilterNameSelected() {
        Staff staff = new Staff();
        Long staffID = null;
        if (cbFilterNameSelected.getSelectedItem() != null) {
            staffID = cbFilterNameSelected.getSelectedItem().getValue();
        }

        staff.setStaffId(staffID);

        filterSelect(staff);
    }

    private void filter(Staff staff) {
        List<Staff> vlstData = new ArrayList<>();
        if (staff.getStaffId() == null) {
            vlstData.addAll(lstStaff);

        } else {
            if (lstStaff != null && !lstStaff.isEmpty()) {
                for (Staff item : lstStaff) {
                    if (staff.getStaffId() != null && staff.getStaffId().equals(item.getStaffId())) {
                        vlstData.add(item);
                    }
                }
            }
        }
        listDataModel = new ListModelList<Staff>(vlstData);
        gridFullStaff.setModel(listDataModel);
    }

    private void filterSelect(Staff staff) {
        List<Staff> vlstData = new ArrayList<>();
        if (staff.getStaffId() == null) {
            vlstData.addAll(lstStaffSelected);

        } else {
            if (lstStaffSelected != null && !lstStaffSelected.isEmpty()) {
                for (Staff item : lstStaffSelected) {
                    if (staff.getStaffId() != null && staff.getStaffId().equals(item.getStaffId())) {
                        vlstData.add(item);
                    }
                }
            }
        }
        listDataModelSelected = new ListModelList<Staff>(vlstData);
        gridSelectStaff.setModel(listDataModelSelected);
    }
}
