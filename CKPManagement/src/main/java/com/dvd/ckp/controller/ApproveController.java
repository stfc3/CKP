/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import com.dvd.ckp.business.service.BillsServices;
import com.dvd.ckp.business.service.ConstructionService;
import com.dvd.ckp.business.service.ParamService;
import com.dvd.ckp.business.service.PumpServices;
import com.dvd.ckp.component.MyListModel;
import com.dvd.ckp.domain.BillViewDetail;
import com.dvd.ckp.domain.Bills;
import com.dvd.ckp.domain.BillsDetail;
import com.dvd.ckp.domain.Construction;
import com.dvd.ckp.domain.Param;
import com.dvd.ckp.domain.Pumps;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StyleUtils;
import com.dvd.ckp.utils.ValidateUtils;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;

/**
 *
 * @author viettx
 */
public class ApproveController extends GenericForwardComposer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ApproveController.class);
    @Autowired
    protected BillsServices billServices;

    @Autowired
    protected ConstructionService contructionServices;

    @Autowired
    protected PumpServices pumpServices;
    @Autowired
    protected ParamService paramService;

    @Wire
    private Grid gridApprove;
    @Wire
    private Combobox cbBillCode;
    private ListModelList<BillViewDetail> listApprove;

    private List<BillViewDetail> listData;

    private List<Bills> lstBill;

    private List<BillsDetail> lstDataBill;

    private ListModelList<String> modelListBills;

    private Combobox cbContruction;
    private MyListModel<Construction> modelListContruction;

    private Datebox dtFilterDate;

    private Combobox cbPump;
    private MyListModel<Pumps> modelListPump;

    private List<Construction> listContruction;
    private List<Pumps> listPumps;

    @Wire
    private Window approve;
    private Memory memory = new Memory();

    private Integer limitQuery = 200;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        billServices = (BillsServices) SpringUtil.getBean(SpringConstant.BILL_SERVICES);
        contructionServices = (ConstructionService) SpringUtil.getBean(SpringConstant.CONSTRUCTION_SERVICES);
        pumpServices = (PumpServices) SpringUtil.getBean(SpringConstant.PUMPS_SERVICES);
        paramService = (ParamService) SpringUtil.getBean(SpringConstant.PARAM_SERVICES);

        List<Param> listParam = paramService.getParamByKey(Constants.PARAM_LIMIT_QUERY);
        if (listParam != null && !listParam.isEmpty()) {
            limitQuery = listParam.get(0).getParamValue().intValue();
        }
        lstBill = billServices.getAllData();

        listData = billServices.getApproveBill(null, null, null, null, limitQuery);
        List<BillViewDetail> vlstData = new ArrayList<>();
        if (listData != null && !listData.isEmpty()) {
            vlstData.addAll(listData);
        }
        listApprove = new ListModelList<>(vlstData);

        gridApprove.setModel(listApprove);

        listContruction = new ArrayList<>(memory.getConstructionCache().values());

        modelListContruction = new MyListModel<>(listContruction);
        cbContruction.setModel(modelListContruction);

        modelListBills = new ListModelList<>(getListBillCode(lstBill));
        cbBillCode.setModel(modelListBills);

        listPumps = pumpServices.getAllListData();
        modelListPump = new MyListModel<>(listPumps);
        cbPump.setModel(modelListPump);
    }

    private List<String> getListBillCode(List<Bills> listData) {
        List<String> listCode = new ArrayList<>();
        if (listData != null && !listData.isEmpty()) {
            for (Bills item : listData) {
                listCode.add(item.getBillCode());
            }
        }
        return listCode;
    }

    private void reload() {
        listData.clear();
        listData = billServices.getApproveBill(null, null, null, null, limitQuery);

        List<BillViewDetail> vlstData = new ArrayList<>();
        if (listData != null && !listData.isEmpty()) {
            vlstData.addAll(listData);
        }
        listApprove = new ListModelList<>(vlstData);
        gridApprove.setModel(listApprove);
    }

    public void onAddStaff(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        BillViewDetail view = rowSelected.getValue();
        getDataInRow(view, 1);
    }

    public void onApprove(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        BillViewDetail view = rowSelected.getValue();
        getDataInRow(view, 2);
    }

    private void getDataInRow(BillViewDetail data, int isApprove) {

        List<BillsDetail> lstDetail = billServices.getBillDetail(data.getBillID());
        lstDataBill = new ArrayList<>();
        if (lstDetail != null && !lstDetail.isEmpty()) {
            lstDataBill.addAll(lstDetail);
        }
        // TODO Auto-generated method stub
        Map<String, Object> arguments = new HashMap();
        arguments.put("billDetailID", data.getBillDetailID());
        arguments.put("bill", getBill(data.getBillID()));
        arguments.put("billDetai", getBillDetail(data.getBillDetailID()));
        final Window windownUpload = (Window) Executions.createComponents("/manager/include/addStaff.zul", approve,
                arguments);
        windownUpload.doModal();
        windownUpload.setBorder(true);
        windownUpload.setBorder("normal");
        windownUpload.setClosable(true);

    }

    private BillsDetail getBillDetail(Long billDetailID) {
        if (lstDataBill != null && !lstDataBill.isEmpty()) {
            for (BillsDetail billsDetail : lstDataBill) {
                if (billDetailID.equals(billsDetail.getBillDetailId())) {
                    return billsDetail;
                }
            }
        }
        return null;
    }

    private Bills getBill(Long bill) {
        if (lstBill != null && !lstBill.isEmpty()) {
            for (Bills bills : lstBill) {
                if (bill != null && bill.equals(bills.getBillID())) {
                    return bills;
                }
            }
        }
        return null;
    }

    public void onChange$cbBillCode() {
        filter();
    }

    public void onChange$cbContruction() {
        filter();
    }

    public void onChange$dtFilterDate() {
        filter();
    }

    public void onChange$cbPump() {
        filter();
    }

    private void filter() {
        String billCode = null;
        if (cbBillCode.getSelectedItem() != null) {
            billCode = cbBillCode.getSelectedItem().getValue();
        }
        Long constructionId = null;
        if (cbContruction.getSelectedItem() != null) {
            constructionId = cbContruction.getSelectedItem().getValue();
        }
        Date pumpDate = null;
        if (dtFilterDate != null) {
            pumpDate = dtFilterDate.getValue();
        }
        Long pumpId = null;
        if (cbPump.getSelectedItem() != null) {
            pumpId = cbPump.getSelectedItem().getValue();
        }
        listData = billServices.getApproveBill(billCode, constructionId, pumpDate, pumpId, limitQuery);
        listApprove = new ListModelList<>(listData);
        gridApprove.setModel(listApprove);

    }

    public void onClick$reloadData() {
        reload();
        onChange$cbBillCode();
    }

    /**
     * Edit row
     *
     * @param event
     */
    public void onEdit(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        StyleUtils.setEnableComponent(lstCell, 5);
    }

    public void onCancel(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        setDisableComponent(lstCell, 5);
        reload(lstCell);
    }

    /**
     * Save
     *
     * @param event
     */
    public void onSave(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        if (!valiDate(lstCell)) {
            BillViewDetail billViewDetail = rowSelected.getValue();
            getDataInRow(lstCell, billViewDetail);
            Bills bills = new Bills();
            bills.setBillCode(billViewDetail.getBillCode());
            bills.setBillID(billViewDetail.getBillID());
            save(bills);
            setDisableComponent(lstCell, 5);
        }

    }

    private void save(Bills bills) {

        bills.setCreateDate(new Date());
        billServices.update(bills.getBillCode(), bills.getBillID());

    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private void getDataInRow(List<Component> lstCell, BillViewDetail bills) {

        Component component;

        Textbox txtBillsCode = null;

        // ma phieu bom
        component = lstCell.get(1).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtBillsCode = (Textbox) component;
            bills.setBillCode(txtBillsCode.getValue());
        }

    }

    public static void setDisableComponent(List<Component> lstCell, int numberAction) {
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
                                view.setVisible(true);

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

    private void reload(List<Component> lstCell) {
        Component component;
        Component componentLast;

        // Khach hang
        component = lstCell.get(1).getFirstChild();
        componentLast = lstCell.get(1).getLastChild();
        if (component != null && component instanceof Textbox) {

            Label mesage = (Label) componentLast;

            mesage.setVisible(false);
            mesage.setHflex("0");
            mesage.setValue("");

        }
    }

    private boolean valiDate(List<Component> lstCell) {

        Component component;
        Component componentLast;

        Textbox txtBillCode = null;
        boolean isFalse = false;

        // Khach hang
        component = lstCell.get(1).getFirstChild();
        componentLast = lstCell.get(1).getLastChild();
        if (component != null && component instanceof Textbox) {
            txtBillCode = (Textbox) component;

            Label mesage = (Label) componentLast;
            if (ValidateUtils.validate(txtBillCode.getValue())) {
                txtBillCode.setHflex("1");
                mesage.setValue(Labels.getLabel("validate.code.bills"));
                mesage.setVisible(true);
                mesage.setHflex("1");
                txtBillCode.focus();
                isFalse = true;
            } else {
                mesage.setVisible(false);
                mesage.setHflex("0");
                mesage.setValue("");

            }

            if (checkExits(txtBillCode.getValue(), lstBill)) {

                mesage.setValue(Labels.getLabel("validate.code.duplicate"));
                mesage.setVisible(true);
                mesage.setHflex("1");
                txtBillCode.focus();
                isFalse = true;
            } else {
                mesage.setVisible(false);
                mesage.setHflex("0");
                mesage.setValue("");

            }

        }

        return isFalse;
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
}
