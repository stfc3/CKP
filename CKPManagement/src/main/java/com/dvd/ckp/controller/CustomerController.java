/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.CustomerService;
import com.dvd.ckp.business.service.UtilsService;
import com.dvd.ckp.component.MyListModel;
import com.dvd.ckp.domain.Customer;
import com.dvd.ckp.domain.Param;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StyleUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

/**
 *
 * @author dmin
 */
public class CustomerController extends GenericForwardComposer {

    private static final Logger logger = Logger.getLogger(CustomerController.class);
    @WireVariable
    protected CustomerService customerService;
    @WireVariable
    protected UtilsService utilsService;
    @Wire
    private Grid lstCustomer;
    @Wire
    private Combobox cbxCustomerFilter;
    ListModelList<Customer> listDataModel;
    private List<Customer> lstCustomers;

    private final int codeIndex = 1;
    private final int nameIndex = 2;
    private final int phoneIndex = 3;
    private final int taxIndex = 4;
    private final int addressIndex = 5;
    private final int accountIndex = 6;
    private final int bankIndex = 7;

    Param defaultParam;
    List<Param> lstBanks;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        customerService = (CustomerService) SpringUtil.getBean(SpringConstant.CUSTOMER_SERVICES);
        utilsService = (UtilsService) SpringUtil.getBean(SpringConstant.UTILS_SERVICES);

        defaultParam = new Param();
        defaultParam.setParamValue(Constants.DEFAULT_ID);
        defaultParam.setParamName(Labels.getLabel("option"));

        //list loai may bom
        lstBanks = utilsService.getParamByKey(Constants.PRAM_BANK);
        if (lstBanks == null) {
            lstBanks = new ArrayList<>();
        }
        lstBanks.add(Constants.FIRST_INDEX, defaultParam);

        reloadGrid(0);
    }

    /**
     * Edit row
     *
     * @param event
     */
    public void onEdit(ForwardEvent event) {

        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        Customer customer = rowSelected.getValue();
        setComboboxParam(lstCell, getParamDefault(customer.getBankId(), bankIndex), bankIndex);
        StyleUtils.setEnableComponent(lstCell, 4);
    }

    /**
     * Cancel
     *
     * @param event
     */
    public void onCancel(ForwardEvent event) {

//        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
//        List<Component> lstCell = rowSelected.getChildren();
//        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGrid(0);
        cbxCustomerFilter.setFocus(true);

    }

    /**
     * Save
     *
     * @param event
     */
    public void onSave(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        Customer c = rowSelected.getValue();
        Customer customer = getDataInRow(lstCell);
        customer.setCustomerId(c.getCustomerId());
        customer.setStatus(Constants.STATUS_ACTIVE);
        customer.setCreateDate(new Date());
        customerService.insertOrUpdateCustomer(customer);
//        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGrid(1);
        cbxCustomerFilter.setFocus(true);
    }

    public void onDelete(ForwardEvent event) {
        Messagebox.show(Labels.getLabel("message.confirm.delete.content"), Labels.getLabel("message.confirm.delete.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event e) {
                if (Messagebox.ON_YES.equals(e.getName())) {
                    Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
                    Customer customer = rowSelected.getValue();
                    customer.setStatus(Constants.STATUS_INACTIVE);
                    customer.setCreateDate(new Date());
                    customerService.insertOrUpdateCustomer(customer);
                    reloadGrid(1);
                }
                cbxCustomerFilter.setFocus(true);
            }
        });

    }

    /**
     * Add row
     */
    public void onClick$add() {
        Customer customer = new Customer();
        listDataModel.add(Constants.FIRST_INDEX, customer);
        lstCustomer.setActivePage(Constants.FIRST_INDEX);
        lstCustomer.setModel(listDataModel);
        lstCustomer.renderAll();
        List<Component> lstCell = lstCustomer.getRows().getFirstChild().getChildren();
        setDataDefaultInGrid();
        StyleUtils.setEnableComponent(lstCell, 4);
    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private Customer getDataInRow(List<Component> lstCell) {
        Customer customer = new Customer();
        Component component;
        Textbox txtCustomerCode = null;
        Textbox txtCustomerName = null;
        Textbox txtCustomerPhone = null;
        Textbox txtTaxCode = null;
        Textbox txtCustomerAddress = null;
        Textbox txtAccountNumber = null;
        Combobox cbxBank = null;
        component = lstCell.get(codeIndex).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtCustomerCode = (Textbox) component;
            customer.setCustomerCode(txtCustomerCode.getValue());
        }
        component = lstCell.get(nameIndex).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtCustomerName = (Textbox) component;
            customer.setCustomerName(txtCustomerName.getValue());
        }
        component = lstCell.get(phoneIndex).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtCustomerPhone = (Textbox) component;
            customer.setCustomerPhone(txtCustomerPhone.getValue());
        }
        component = lstCell.get(taxIndex).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtTaxCode = (Textbox) component;
            customer.setTaxCode(txtTaxCode.getValue());
        }
        component = lstCell.get(addressIndex).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtCustomerAddress = (Textbox) component;
            customer.setCustomerAddress(txtCustomerAddress.getValue());
        }
        component = lstCell.get(accountIndex).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtAccountNumber = (Textbox) component;
            customer.setAccountNumber(txtAccountNumber.getValue());
        }
        component = lstCell.get(bankIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxBank = (Combobox) component;
            customer.setBankId(cbxBank.getSelectedItem().getValue());
        }
        return customer;
    }

    /**
     * Reload grid
     */
    private void reloadGrid(int loadCache) {
        if (loadCache == 1) {
            Memory.loadCustomer();
        }
        lstCustomers = new ArrayList<>(Memory.lstCustomerCache.values());
        listDataModel = new ListModelList(lstCustomers);
        lstCustomer.setModel(listDataModel);

        cbxCustomerFilter.setModel(new MyListModel(lstCustomers));

        setDataDefaultInGrid();
    }

    public void onSelect$cbxCustomerFilter() {
        Long vstrCustomerId = null;
        if (cbxCustomerFilter.getSelectedItem() != null) {
            vstrCustomerId = cbxCustomerFilter.getSelectedItem().getValue();
        }
        filter(vstrCustomerId);
    }

    private void filter(Long pstrCustomerId) {
        List<Customer> vlstCustomer = new ArrayList<>();
        if (lstCustomers != null && !lstCustomers.isEmpty()) {
            if (pstrCustomerId == null) {
                vlstCustomer.addAll(lstCustomers);
            } else {
                for (Customer c : lstCustomers) {
                    if (pstrCustomerId.equals(c.getCustomerId())) {
                        vlstCustomer.add(c);
                    }
                }
            }
        }
        listDataModel = new ListModelList(vlstCustomer);
        lstCustomer.setModel(listDataModel);
        setDataDefaultInGrid();
    }

    private void setDataDefaultInGrid() {
        lstCustomer.renderAll();
        List<Component> lstRows = lstCustomer.getRows().getChildren();
        if (lstRows != null && !lstRows.isEmpty()) {
            for (int i = 0; i < lstRows.size(); i++) {
                Customer customer = listDataModel.get(i);
                Component row = lstRows.get(i);
                List<Component> lstCell = row.getChildren();
                setComboboxParam(lstCell, getParamDefault(customer.getBankId(), bankIndex), bankIndex);
            }
        }
    }

    private List<Param> getParamDefault(Long paramValue, int type) {
        List<Param> paramSelected = new ArrayList<>();
        List<Param> lstParam = null;
        switch (type) {
            case bankIndex:
                lstParam = lstBanks;
                break;
            default:
                break;
        }
        if (paramValue != null && lstParam != null && !lstParam.isEmpty()) {
            for (Param vParam : lstParam) {
                if (paramValue.equals(vParam.getParamValue())) {
                    paramSelected.add(vParam);
                    break;
                }
            }
        }
        if (paramSelected.isEmpty()) {
            paramSelected.add(defaultParam);
        }
        return paramSelected;
    }

    private void setComboboxParam(List<Component> lstCell, List<Param> selectedIndex, int columnIndex) {
        Combobox cbxParam = null;
        Component component = lstCell.get(columnIndex).getFirstChild();
        List<Param> lstParam = null;
        switch (columnIndex) {
            case bankIndex:
                lstParam = lstBanks;
                break;
            default:
                break;
        }
        if (component != null && component instanceof Combobox) {
            cbxParam = (Combobox) component;
            MyListModel listDataModelParam = new MyListModel(lstParam);
            listDataModelParam.setSelection(selectedIndex);
            cbxParam.setModel(listDataModelParam);
            cbxParam.setTooltiptext(selectedIndex.get(Constants.FIRST_INDEX).getParamName());
        }
    }

    public void onImport(ForwardEvent event) {
        Messagebox.show("Chức năng chưa được hỗ trợ", "Thông báo", Messagebox.OK, Messagebox.INFORMATION);
    }

    public void onExport(ForwardEvent event) {
        Messagebox.show("Chức năng chưa được hỗ trợ", "Thông báo", Messagebox.OK, Messagebox.INFORMATION);
    }
}
