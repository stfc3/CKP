/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.dvd.ckp.business.service.BillsServices;
import com.dvd.ckp.business.service.ContractService;
import com.dvd.ckp.business.service.PumpServices;
import com.dvd.ckp.business.service.UtilsService;
import com.dvd.ckp.common.Constants;
import com.dvd.ckp.component.MyListModel;
import com.dvd.ckp.domain.Bills;
import com.dvd.ckp.domain.BillsDetail;
import com.dvd.ckp.domain.Construction;
import com.dvd.ckp.domain.Customer;
import com.dvd.ckp.domain.Location;
import com.dvd.ckp.domain.Pumps;
import com.dvd.ckp.excel.ExcelWriter;
import com.dvd.ckp.utils.DateTimeUtils;
import com.dvd.ckp.utils.FileUtils;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;
import com.dvd.ckp.utils.StyleUtils;
import java.util.stream.Collectors;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Longbox;

/**
 *
 * @author viettx
 */
public class BillsController extends GenericForwardComposer<Component> {

    /**
     *
     */
    private static final long serialVersionUID = 457994854668836097L;
    private static final Logger logger = Logger.getLogger(BillsController.class);
    ServletContext context;
//    @WireVariable
//    protected ConstructionService constructionService;
//    @WireVariable
//    protected CustomerService customerService;
    @WireVariable
    protected BillsServices billsServices;
    @WireVariable
    protected ContractService contractService;
    @WireVariable
    protected PumpServices pumpServices;
//    @WireVariable
//    protected LocationServices locationServices;
    @WireVariable
    protected UtilsService utilsService;

    @Wire
    private Grid gridBills;

    @Wire
    private Datebox dtFilterDate;

    // Model grid in window bill
    ListModelList<Bills> listDataModel;

    private List<Bills> lstBills;
    private List<Bills> lstBillsFilter;

    private List<Construction> lstConstructions;
    private List<Customer> lstCustomer;

    private Construction defaultConstruction;
    private Customer defaultCustomer;

    private List<BillsDetail> lstBillDetail;

    // Danh sach bom
    private List<Pumps> lstPumps;

    // Danh sach vi tri bom
    private List<Location> lstLocation;

    // private List<Contract> listContact;
    // Vi tri cac column trong grid
    private final int billsCode = 1;
    private final int customerID = 2;
    private final int constructionID = 3;
    private final int prd = 4;
    private final int intfilePath = 5;
    private final int intFromDate = 7;
    private final int intStartDate = 8;
    private final int intEndDate = 9;
    private final int intTodate = 10;
    private final int statusIndex = 11;

    private static final String SAVE_PATH = "/Bills/";

    private static String filePathBill = "";
    private static String fileName = "";
    private static int insertOrUpdate = 0;

    private Window bills;
    @Wire
    private Combobox cbFilterCustomer;
    private MyListModel<Customer> modelListCustomer;
    @Wire
    private Combobox cbFilterConstruction;
    private MyListModel<Construction> modelListConstruction;

    private Map<Long, Customer> mapCustomer;
    private Map<Long, Construction> mapConstruction;

    private Memory memory = new Memory();

    private boolean billCodeIsChange = false;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Long startTime = System.currentTimeMillis();
        context = Sessions.getCurrent().getWebApp().getServletContext();
        // khai bao services
//        constructionService = (ConstructionService) SpringUtil.getBean(SpringConstant.CONSTRUCTION_SERVICES);
//        customerService = (CustomerService) SpringUtil.getBean(SpringConstant.CUSTOMER_SERVICES);
        billsServices = (BillsServices) SpringUtil.getBean(SpringConstant.BILL_SERVICES);
        contractService = (ContractService) SpringUtil.getBean(SpringConstant.CONTRACT_SERVICES);
//        locationServices = (LocationServices) SpringUtil.getBean(SpringConstant.LOCATION_SERVICES);
        pumpServices = (PumpServices) SpringUtil.getBean(SpringConstant.PUMPS_SERVICES);
        utilsService = (UtilsService) SpringUtil.getBean(SpringConstant.UTILS_SERVICES);

        // list danh sach cong trinh
        lstConstructions = new ArrayList<>(memory.getConstructionCache().values());
//        List<Construction> lstCon = new ArrayList<>(memory.getConstructionCache().values());

//        if (lstCon != null && !lstCon.isEmpty()) {
//            lstConstructions.addAll(lstCon);
//        }
        // list danh sach khach hang
        lstCustomer = new ArrayList<>(memory.getCustomerCache().values());

//        List<Customer> lstCus = customerService.getCustomerActive();
//        if (lstCus != null && !lstCus.isEmpty()) {
//            lstCustomer.addAll(lstCus);
//        }
        // list danh sach chi tiet hoa don
        lstBillDetail = new ArrayList<>();
        List<BillsDetail> lstBillDe = billsServices.getBillDetail();
        if (lstBillDe != null && !lstBillDe.isEmpty()) {
            lstBillDetail.addAll(lstBillDe);
        }

        // danh sach vi tri bom
        lstLocation = new ArrayList<>();
        List<Location> lstLoca = new ArrayList<>(memory.getLocationCache().values());
        if (lstLoca != null && !lstLoca.isEmpty()) {
            lstLocation.addAll(lstLoca);
        }

        // danh sach bom
        lstPumps = new ArrayList<>();
        List<Pumps> lstPump = pumpServices.getAllListData();
        if (lstPump != null && !lstPump.isEmpty()) {
            lstPumps.addAll(lstPump);
        }

        modelListCustomer = new MyListModel(lstCustomer);
        cbFilterCustomer.setModel(modelListCustomer);

        mapCustomer = lstCustomer.stream().collect(Collectors.toMap(Customer::getCustomerId, vlstCustomer -> vlstCustomer));

        modelListConstruction = new MyListModel(lstConstructions);
        cbFilterConstruction.setModel(modelListConstruction);

        mapConstruction = lstConstructions.stream().collect(Collectors.toMap(Construction::getConstructionId, vlstConstructions -> vlstConstructions));
        // list danh sach hoa don
        lstBills = new ArrayList<>();
        // list de export data theo du lieu filter
        lstBillsFilter = new ArrayList<>();
        List<Bills> lstData = billsServices.getAllData();
        if (lstData != null && !lstData.isEmpty()) {
            lstBills.addAll(lstData);
            lstBillsFilter.addAll(lstData);
        }

        // cong trinh default
        defaultConstruction = new Construction();
        defaultConstruction.setConstructionId(-1l);
        defaultConstruction.setConstructionName(Labels.getLabel("option"));
        lstConstructions.add(0, defaultConstruction);

        // khach hang default
        defaultCustomer = new Customer();
        defaultCustomer.setCustomerId(-1l);
        defaultCustomer.setCustomerName(Labels.getLabel("option"));
        lstCustomer.add(0, defaultCustomer);

        // set model
        listDataModel = new ListModelList(lstBills);
        gridBills.setModel(listDataModel);

        logger.info("TIME QUERY DB: " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();

        setDataDefaultInGrid();

        logger.info("TIME QUERY FOR: " + (System.currentTimeMillis() - startTime));

    }

    /**
     * Edit row
     *
     * @param event
     */
    public void onEdit(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        billCodeChange(lstCell);
        Bills c = rowSelected.getValue();
        setDataConstruction(lstCell, getConstructionDefault(c.getConstructionID()), constructionID);
        setDataCustomer(lstCell, getCustomerDefault(c.getCustomerID()), customerID);
        StyleUtils.setEnableComponent(lstCell, 6);
    }

    /**
     * Cancel
     *
     * @param event
     */
    public void onCancel(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        setDisableComponent(lstCell, 6);
//        if (insertOrUpdate == 1) {
        reloadGrid();
//        }

    }

    /**
     * Save
     *
     * @param event
     */
    public void onSave(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();

        Bills bill = rowSelected.getValue();
        getDataInRow(lstCell, bill);

        bill.setStatus(1);
        save(bill, lstCell);

    }

    private void save(Bills bills, List<Component> lstCell) {
        if (!valiDate(lstCell)) {
            if (insertOrUpdate == 1) {
                bills.setStatus(1);
                billsServices.save(bills);
                lstBills.add(bills);
                lstBillsFilter.add(bills);
            } else {
                bills.setCreateDate(new Date());
                billsServices.update(bills);
            }

            insertOrUpdate = 0;
            setDisableComponent(lstCell, 6);
            reloadGrid();
        }
    }

    public void onDelete(ForwardEvent event) {
        Messagebox.show(Labels.getLabel("message.confirm.delete.content"),
                Labels.getLabel("message.confirm.delete.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
            @Override
            public void onEvent(Event e) {
                if (Messagebox.ON_YES.equals(e.getName())) {

                    Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
                    List<Component> lstCell = rowSelected.getChildren();
                    Bills bill = rowSelected.getValue();
                    getDataInRow(lstCell, bill);
                    bill.setStatus(0);
                    billsServices.delete(bill);
                    lstBills.remove(getIndexBill(bill.getBillID()));
                    lstBillsFilter.remove(getIndexBillFilter(bill.getBillID()));
                    reloadGrid();
                }
            }
        });

    }

    /**
     * Add row
     */
    public void onAdd(ForwardEvent event) {
        Bills bill = new Bills();
        bill.setStatus(1);
        listDataModel.add(0, bill);
        gridBills.setActivePage(0);
//        gridBills.setPagingPosition("1");
        gridBills.setModel(listDataModel);
        gridBills.renderAll();
        List<Component> lstCell = gridBills.getRows().getChildren().get(0).getChildren();
        setDataDefaultInGrid();
        StyleUtils.setEnableComponent(lstCell, 6);
        insertOrUpdate = 1;
    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private void getDataInRow(List<Component> lstCell, Bills bills) {

        Component component;

        Textbox txtBillsCode = null;
        Combobox cbCustomer = null;
        Combobox cbxConstruction = null;
        Datebox dtPrdID = null;
        Datebox fromdate = null;
        Datebox startDate = null;
        Datebox endDate = null;
        Datebox toDate = null;
        Combobox cbxStatus = null;
        A aFileName = null;

        // ma phieu bom
        component = lstCell.get(billsCode).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtBillsCode = (Textbox) component;
            bills.setBillCode(txtBillsCode.getValue());
        }
        // Khach hang
        component = lstCell.get(customerID).getFirstChild();
        if (component != null && component instanceof Textbox) {
            cbCustomer = (Combobox) component;
            bills.setCustomerID(cbCustomer.getSelectedItem().getValue());
        }
        // Cong trinh
        component = lstCell.get(constructionID).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxConstruction = (Combobox) component;
            bills.setConstructionID(cbxConstruction.getSelectedItem().getValue());
        }
        component = lstCell.get(intfilePath).getFirstChild();
        if (component != null && component instanceof A) {
            aFileName = (A) component;
            bills.setFileName(aFileName.getLabel());
        }

        // Ngay bom
        component = lstCell.get(prd).getFirstChild();
        if (component != null && component instanceof Datebox) {
            try {
                dtPrdID = (Datebox) component;
                bills.setPrdID(
                        DateTimeUtils.convertDateToString(dtPrdID.getValue(), Constants.FORMAT_DATE));
                bills.setStrDateInput(DateTimeUtils.convertDateToString(dtPrdID.getValue(), Constants.FORMAT_DATE_DD_MM_YYY));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        }
        // Ngay den cong truong
        component = lstCell.get(intFromDate).getFirstChild();
        if (component != null && component instanceof Datebox) {
            fromdate = (Datebox) component;
            bills.setFromDate(fromdate.getValue());
        }
        // Thoi gian bat dau
        component = lstCell.get(intStartDate).getFirstChild();
        if (component != null && component instanceof Datebox) {
            startDate = (Datebox) component;
            bills.setStartTime(startDate.getValue());
        }
        // Thoi gian bom xong
        component = lstCell.get(intEndDate).getFirstChild();
        if (component != null && component instanceof Datebox) {
            endDate = (Datebox) component;
            bills.setEndTime(endDate.getValue());
        }

        // Thoi gian bom xong
        component = lstCell.get(intTodate).getFirstChild();
        if (component != null && component instanceof Datebox) {
            toDate = (Datebox) component;
            bills.setToDate(toDate.getValue());
        }

        // Trang thai
        component = lstCell.get(statusIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxStatus = (Combobox) component;
            bills.setStatus(Integer.valueOf(cbxStatus.getSelectedItem().getValue()));
        }

    }

    /**
     * viettx bo sung 21/01/2018 validate compoment
     */
    private boolean valiDate(List<Component> lstCell) {

        Component component;
        Component componentLast;

        Combobox cbCustomer = null;
        Combobox cbxConstruction = null;
        Textbox txtBillsCode;
        Datebox dtPrdID = null;

        boolean isFalse = false;

        // ma phieu bom
        component = lstCell.get(billsCode).getFirstChild();
        componentLast = lstCell.get(billsCode).getLastChild();
        if (component != null && component instanceof Textbox) {
            txtBillsCode = (Textbox) component;
            Label mesage = (Label) componentLast;

            if (billCodeIsChange) {
                if (checkExits(txtBillsCode.getValue(), lstBills)) {

                    mesage.setValue(Labels.getLabel("validate.code.duplicate"));
                    mesage.setHflex("1");
                    txtBillsCode.focus();
                    isFalse = true;
                } else {
                    mesage.setVisible(false);
                    mesage.setHflex("0");
                    mesage.setValue("");

                }
                billCodeIsChange = false;
            }
        }
        // Khach hang
        component = lstCell.get(customerID).getFirstChild();
        componentLast = lstCell.get(customerID).getLastChild();
        if (component != null && component instanceof Textbox) {
            cbCustomer = (Combobox) component;
            Long value = null;
            if (cbCustomer.getSelectedItem() != null) {
                value = cbCustomer.getSelectedItem().getValue();
            }
            Label mesage = (Label) componentLast;
            if (value == null || value.equals(-1l)) {

                cbCustomer.setHflex("1");
                mesage.setValue(Labels.getLabel("validate.customer.empty"));
                mesage.setVisible(true);
                mesage.setHflex("1");
                cbCustomer.focus();
                isFalse = true;
            } else {
                mesage.setVisible(false);
                mesage.setHflex("0");
                mesage.setValue("");

            }

        }
        // Cong trinh
        component = lstCell.get(constructionID).getFirstChild();
        componentLast = lstCell.get(constructionID).getLastChild();
        if (component != null && component instanceof Combobox) {
            cbxConstruction = (Combobox) component;

            Long value = null;
            if (cbxConstruction.getSelectedItem() != null) {
                value = cbxConstruction.getSelectedItem().getValue();
            }
            Label mesage = (Label) componentLast;
            if (value == null || value.equals(-1l)) {

                mesage.setValue(Labels.getLabel("validate.construction.empty"));
                mesage.setVisible(true);
                mesage.setHflex("1");
                cbxConstruction.focus();
                isFalse = true;
            } else {
                mesage.setVisible(false);
                mesage.setHflex("0");
                mesage.setValue("");

            }

        }

        // Ngay bom
        component = lstCell.get(prd).getFirstChild();
        componentLast = lstCell.get(prd).getLastChild();
        if (component != null && component instanceof Datebox) {
            dtPrdID = (Datebox) component;
            Label mesage = (Label) componentLast;
            if (dtPrdID.getValue() == null) {

                mesage.setValue(Labels.getLabel("validate.pump.date.empty"));
                mesage.setVisible(true);
                mesage.setHflex("1");
                dtPrdID.focus();
                isFalse = true;
            } else {
                mesage.setVisible(false);
                mesage.setHflex("0");
                mesage.setValue("");

            }

        }

        return isFalse;
    }

    private void billCodeChange(List<Component> lstCell) {

        Component component;
        Textbox txtBillsCode;

        // ma phieu bom
        component = lstCell.get(billsCode).getFirstChild();

        if (component != null && component instanceof Textbox) {
            txtBillsCode = (Textbox) component;
            txtBillsCode.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                    // TODO Auto-generated method stub
                    billCodeIsChange = true;
                }
            });
        }

    }

    private boolean checkExits(String billCode, List<Bills> listData) {
        if (listData != null && !listData.isEmpty()) {
            for (Bills bills : listData) {
                if (billCode.equals(bills.getBillCode())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Reload grid
     */
    private void reloadGrid() {
        lstBills.clear();
        List<Bills> lstData = billsServices.getAllData();
        if (lstData != null && !lstData.isEmpty()) {
            lstBills.addAll(lstData);
            lstBillsFilter.addAll(lstData);
        }
        listDataModel = new ListModelList(lstBills);
        gridBills.setModel(listDataModel);
        setDataDefaultInGrid();
        onChange$cbFilterCustomer();
    }

    public void onChange$cbFilterCustomer() {
        Bills vbills = new Bills();
        Long vcustomerID = null;
        if (cbFilterCustomer.getSelectedItem() != null) {
            vcustomerID = cbFilterCustomer.getSelectedItem().getValue();
        }
        vbills.setCustomerID(vcustomerID);

        Long vconstructionID = null;
        if (cbFilterConstruction.getSelectedItem() != null) {
            vconstructionID = cbFilterConstruction.getSelectedItem().getValue();
        }
        vbills.setConstructionID(vconstructionID);
        try {
            if (dtFilterDate.getValue() != null) {
                String dateFilter = DateTimeUtils.convertDateToString(dtFilterDate.getValue(), Constants.FORMAT_DATE);
                vbills.setPrdID(dateFilter);
            } else {
                vbills.setPrdID(null);
            }
        } catch (WrongValueException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }

        filter(vbills);
    }

    public void onChange$cbFilterConstruction() {

        Bills bill = new Bills();
        Long customer = null;
        if (cbFilterCustomer.getSelectedItem() != null) {
            customer = cbFilterCustomer.getSelectedItem().getValue();
        }
        bill.setCustomerID(customer);

        Long construction = null;
        if (cbFilterConstruction.getSelectedItem() != null) {
            construction = cbFilterConstruction.getSelectedItem().getValue();
        }
        bill.setConstructionID(construction);
        try {
            if (dtFilterDate.getValue() != null) {
                String dateFilter = DateTimeUtils.convertDateToString(dtFilterDate.getValue(), Constants.FORMAT_DATE);
                bill.setPrdID(dateFilter);
            } else {
                bill.setPrdID(null);
            }
        } catch (WrongValueException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }

        filter(bill);
    }

    public void onChange$dtFilterDate() {
        Bills bill = new Bills();
        Long customer = null;
        if (cbFilterCustomer.getSelectedItem() != null) {
            customer = cbFilterCustomer.getSelectedItem().getValue();
        }
        bill.setCustomerID(customer);

        Long construction = null;
        if (cbFilterConstruction.getSelectedItem() != null) {
            construction = cbFilterConstruction.getSelectedItem().getValue();
        }
        bill.setConstructionID(construction);
        try {
            if (dtFilterDate.getValue() != null) {
                String dateFilter = DateTimeUtils.convertDateToString(dtFilterDate.getValue(), Constants.FORMAT_DATE);
                bill.setPrdID(dateFilter);
            } else {
                bill.setPrdID(null);
            }
        } catch (WrongValueException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }

        filter(bill);
    }

    private void filter(Bills bills) {
        List<Bills> vlstBills = new ArrayList<>();
        lstBillsFilter.clear();
        if (lstBills != null && !lstBills.isEmpty() && bills != null) {
            if (bills.getCustomerID() == null && bills.getConstructionID() == null && bills.getPrdID() == null) {
                vlstBills.addAll(lstBills);

            } else {
                for (Bills c : lstBills) {
                    // tim theo khach hang va cong trinh
                    if (bills.getCustomerID() != null && bills.getConstructionID() != null && bills.getPrdID() == null) {
                        if (bills.getCustomerID().equals(c.getCustomerID())
                                && bills.getConstructionID().equals(c.getConstructionID())) {
                            vlstBills.add(c);
                        }
                    } // tim theo khachs hang
                    else if (bills.getCustomerID() != null && bills.getConstructionID() == null
                            && bills.getPrdID() == null) {
                        if (bills.getCustomerID().equals(c.getCustomerID())) {
                            vlstBills.add(c);
                        }
                    } // tim theo cong trinh
                    else if (bills.getCustomerID() == null && bills.getConstructionID() != null
                            && bills.getPrdID() == null) {
                        if (bills.getConstructionID().equals(c.getConstructionID())) {
                            vlstBills.add(c);
                        }
                    } // Tim theo ngay
                    else if (bills.getCustomerID() == null && bills.getConstructionID() == null
                            && StringUtils.isValidString(bills.getPrdID())) {
                        if (bills.getPrdID().equals(c.getPrdID())) {
                            vlstBills.add(c);
                        }
                    } // Tim theo khach hang va ngay in hoa don
                    else if (bills.getCustomerID() != null && bills.getConstructionID() == null
                            && StringUtils.isValidString(bills.getPrdID())) {
                        if (bills.getCustomerID().equals(c.getCustomerID()) && bills.getPrdID().equals(c.getPrdID())) {
                            vlstBills.add(c);
                        }
                    } else if (bills.getCustomerID() == null && bills.getConstructionID() != null
                            && StringUtils.isValidString(bills.getPrdID())) {
                        if (bills.getConstructionID().equals(c.getConstructionID())
                                && bills.getPrdID().equals(c.getPrdID())) {
                            vlstBills.add(c);
                        }
                    } else if (bills.getCustomerID() != null && bills.getConstructionID() != null
                            && StringUtils.isValidString(bills.getPrdID())) {
                        if (bills.getCustomerID().equals(c.getCustomerID())
                                && bills.getConstructionID().equals(c.getConstructionID())
                                && bills.getPrdID().equals(c.getPrdID())) {
                            vlstBills.add(c);
                        }
                    }
                }
            }
        }
        lstBillsFilter.addAll(vlstBills);
        listDataModel = new ListModelList(vlstBills);
        gridBills.setModel(listDataModel);
        setDataDefaultInGrid();

    }

    // Set data for combobox construction
    private void setDataConstruction(List<Component> lstCell, List<Construction> selectedIndex, int columnIndex) {
        Combobox combobox = null;
        Component component = lstCell.get(columnIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            combobox = (Combobox) component;
            MyListModel listDataModelConstruction = new MyListModel(lstConstructions);
            listDataModelConstruction.setSelection(selectedIndex);
            combobox.setModel(listDataModelConstruction);
        }

    }

    // Set data for combobox customer
    private void setDataCustomer(List<Component> lstCell, List<Customer> selectedIndex, int columnIndex) {
        Combobox combobox = null;
        Component component = lstCell.get(columnIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            combobox = (Combobox) component;
            MyListModel listDataModelCustomer = new MyListModel(lstCustomer);
            listDataModelCustomer.setSelection(selectedIndex);
            combobox.setModel(listDataModelCustomer);
        }

    }

    /**
     * Save
     *
     * @param event
     */
    public void onUploadFile(ForwardEvent event) {
        if (event.getOrigin() instanceof UploadEvent) {
            UploadEvent uploadEvent = (UploadEvent) event.getOrigin();
            Media media = uploadEvent.getMedia();
            Cell cell = (Cell) uploadEvent.getTarget().getParent();
            A aFileName = (A) cell.getFirstChild();
            aFileName.setLabel(media.getName());
            FileUtils fileUtils = new FileUtils();
            fileUtils.saveFile(media, context.getRealPath("file/bills"));
        }
    }

    public void onDownloadFile(ForwardEvent event) {
        try {
            A aFileName = (A) event.getOrigin().getTarget();
            File file = new File(context.getRealPath("file/bills/" + aFileName.getLabel()));
            Filedownload.save(file, null);
        } catch (FileNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    // get Construction default
    private List<Construction> getConstructionDefault(Long constructionId) {
        List<Construction> constructionSelected = new ArrayList<>();
        constructionSelected.add(mapConstruction.getOrDefault(constructionId, defaultConstruction));
        return constructionSelected;
    }

    // get Customer default
    private List<Customer> getCustomerDefault(Long customerId) {
        List<Customer> customerSelected = new ArrayList<>();
        customerSelected.add(mapCustomer.getOrDefault(customerId, defaultCustomer));
        return customerSelected;
    }

    private void setDataDefaultInGrid() {
        gridBills.renderAll();
        List<Component> lstRows = gridBills.getRows().getChildren();
        if (lstRows != null && !lstRows.isEmpty()) {
            for (int i = 0; i < lstRows.size(); i++) {
                Bills bills = listDataModel.get(i);
                Component row = lstRows.get(i);
                List<Component> lstCell = row.getChildren();
//                logger.info("Customer id: " + bills.getCustomerID());
                setDataCustomer(lstCell, getCustomerDefault(bills.getCustomerID()), customerID);
                setDataConstruction(lstCell, getConstructionDefault(bills.getConstructionID()), constructionID);
            }
        }
    }

    /**
     * Action export excel data
     *
     * @param event
     */
    public void onExport(ForwardEvent event) {
        ExcelWriter<Bills> excelWriter = new ExcelWriter<Bills>();
        try {
            int index = 0;
            for (Bills staff : lstBillsFilter) {
                index++;
                staff.setIndex(index);
            }
            String pathFileInput = Constants.PATH_FILE + "file/template/export/bills_data_export.xlsx";
            String pathFileOut = Constants.PATH_FILE + "file/export/bills_data_export.xlsx";

            excelWriter.write(lstBillsFilter, pathFileInput, pathFileOut);
            File file = new File(pathFileOut);
            Filedownload.save(file, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * Action import excel for bills
     *
     * @param event
     */
    public void onImport(ForwardEvent event) {
        Messagebox.show(Labels.getLabel("not.support"), Labels.getLabel("comfirm"), Messagebox.OK,
                Messagebox.INFORMATION);
    }

    /**
     * Lay thon tin cong trinh
     *
     * @param constructionId
     * @return
     */
    private Construction getConstruction(Long constructionId) {

        if (lstConstructions != null && !lstConstructions.isEmpty()) {
            for (Construction construction : lstConstructions) {
                if (constructionId != null && constructionId.equals(construction.getConstructionId())) {
                    return construction;

                }
            }
        }
        return null;
    }

    private Customer getCustomer(Long customerId) {

        if (lstCustomer != null && !lstCustomer.isEmpty()) {
            for (Customer item : lstCustomer) {
                if (customerId != null && customerId.equals(item.getCustomerId())) {
                    return item;

                }
            }
        }
        return null;
    }



    /**
     * Open windown add bill detail
     *
     * @param event
     */
    @SuppressWarnings({"unchecked", "unchecked"})
    public void onAddDetail(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        if (!valiDate(lstCell)) {
            Bills billsValue = rowSelected.getValue();
            getDataInRow(lstCell, billsValue);
            billsValue.setStatus(1);

            save(billsValue, lstCell);
            reloadGrid();

            Map<String, Object> arguments = new HashMap();
            BillsDetail billsDetail = getBillsDetail(billsValue.getBillID());
            if (billsDetail != null) {
                arguments.put("detail", billsDetail);
            } else {
                arguments.put("detail", new BillsDetail());
            }
            
            arguments.put("bill", billsValue);

            if (billsDetail != null) {
                Pumps pumps = getPumps(billsDetail.getPumpID());
                arguments.put("pumps", pumps);
                Location location = getLocation(billsDetail.getLocationId());
                arguments.put("location", location);
            }
            Construction construction = getConstruction(billsValue.getConstructionID());
            arguments.put("construction", construction);

            Customer customer = getCustomer(billsValue.getCustomerID());
            arguments.put("customer", customer);
            
            
            final Window windownUpload = (Window) Executions.createComponents("/manager/include/billDetail.zul", bills,
                    arguments);
            windownUpload.doModal();
            windownUpload.setBorder(true);
            windownUpload.setBorder("normal");
            windownUpload.setClosable(true);
            windownUpload.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

                @Override
                public void onEvent(Event event) throws Exception {
                    lstBillDetail = billsServices.getBillDetail();
                    reloadGrid();
                }
            });
        }
    }

    /**
     * Lay thong tin may bom len man hinh view bill detail
     *
     * @param pumps
     * @return
     */
    private Pumps getPumps(Long pumps) {
        if (lstPumps != null && !lstPumps.isEmpty()) {
            for (Pumps item : lstPumps) {
                if (pumps != null && pumps.equals(item.getPumpsID())) {
                    return item;
                }
            }
        }
        return null;
    }

    public BillsDetail getBillsDetail(Long billID) {
        if (lstBillDetail != null && !lstBillDetail.isEmpty()) {
            for (BillsDetail detail : lstBillDetail) {
                if (billID != null && billID.equals(detail.getBillId())) {
                    return detail;
                }
            }
        }
        return null;

    }

    /**
     * lay vi tri bom len view bill detail
     *
     * @param locationId
     * @return
     */
    private Location getLocation(Long locationId) {
        if (lstLocation != null && !lstLocation.isEmpty()) {
            for (Location item : lstLocation) {
                if (locationId != null && locationId.equals(item.getLocationID())) {
                    return item;
                }
            }
        }
        return null;
    }

    private int getIndexBill(Long bill) {
        if (lstBills != null && !lstBills.isEmpty()) {
            for (Bills bills : lstBills) {
                if (bill.equals(bills.getBillID())) {
                    return lstBills.indexOf(bills);
                }
            }
        }
        return -1;
    }

    private int getIndexBillFilter(Long bill) {
        if (lstBillsFilter != null && !lstBillsFilter.isEmpty()) {
            for (Bills bills : lstBillsFilter) {
                if (bill.equals(bills.getBillID())) {
                    return lstBillsFilter.indexOf(bills);
                }
            }
        }
        return -1;
    }

    private static void setDisableComponent(List<Component> lstCell, int numberAction) {
        if (lstCell != null && !lstCell.isEmpty()) {
            for (Component c : lstCell) {
                if (c instanceof Cell) {
                    Component child = c.getFirstChild();
                    if (child instanceof Combobox) {
                        ((Combobox) child).setButtonVisible(false);
                        ((Combobox) child).setInplace(true);
                        ((Combobox) child).setReadonly(true);
                    } else if (child instanceof Datebox) {
                        ((Datebox) child).setButtonVisible(false);
                        ((Datebox) child).setInplace(true);
                        ((Datebox) child).setReadonly(true);
                    } else if (child instanceof Doublebox) {
                        ((Doublebox) child).setReadonly(true);
                        ((Doublebox) child).setInplace(true);
                    } else if (child instanceof Intbox) {
                        ((Intbox) child).setReadonly(true);
                        ((Intbox) child).setInplace(true);
                    } else if (child instanceof Longbox) {
                        ((Longbox) child).setReadonly(true);
                        ((Longbox) child).setInplace(true);
                    } else if (child instanceof Textbox) {
                        ((Textbox) child).setReadonly(true);
                        ((Textbox) child).setInplace(true);
                    } else if (child instanceof Checkbox) {
                        ((Checkbox) child).setDisabled(true);
                    } else if (child instanceof A && c.getChildren().size() == 1) {
                        ((A) child).setDisabled(true);
                    } else if (child instanceof A && c.getChildren().size() == 2) {
                        Button btn = (Button) c.getChildren().get(1);
//                        btn.setDisabled(true);
                        btn.setVisible(false);
                    } else if (child instanceof A && c.getChildren().size() >= 4) {
                        A edit;
                        A delete;
                        A save;
                        A cancel;
                        A detail;
                        A view;
                        A reset;
                        // edit, delete, save, cancel
                        switch (numberAction) {
                            case 4:
                                edit = (A) child;
                                delete = (A) c.getChildren().get(1);
                                save = (A) c.getChildren().get(2);
                                cancel = (A) c.getChildren().get(3);

                                edit.setVisible(true);
                                delete.setVisible(true);

                                save.setVisible(false);
                                cancel.setVisible(false);
                                break;
                            case 5:
                                edit = (A) child;
                                delete = (A) c.getChildren().get(1);
                                save = (A) c.getChildren().get(2);
                                cancel = (A) c.getChildren().get(3);
                                reset = (A) c.getChildren().get(4);

                                edit.setVisible(true);
                                delete.setVisible(true);
                                reset.setVisible(true);

                                save.setVisible(false);
                                cancel.setVisible(false);
                                break;
                            case 6:
                                edit = (A) child;
                                delete = (A) c.getChildren().get(1);
                                save = (A) c.getChildren().get(2);
                                cancel = (A) c.getChildren().get(3);
                                detail = (A) c.getChildren().get(4);
                                view = (A) c.getChildren().get(5);

                                edit.setVisible(true);
                                delete.setVisible(true);
                                view.setVisible(false);

                                save.setVisible(false);
                                cancel.setVisible(false);
                                detail.setVisible(false);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

}
