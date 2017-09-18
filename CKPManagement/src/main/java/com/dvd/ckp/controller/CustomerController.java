/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.CustomerService;
import com.dvd.ckp.domain.Customers;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;
import com.dvd.ckp.utils.StyleUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
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
    @Wire
    private Grid lstCustomer;
    @Wire
    private Textbox txtFilterCode;
    @Wire
    private Textbox txtFilterName;
    ListModelList<Customers> listDataModel;
    private List<Customers> lstCustomers;
    
    private final int codeIndex=1;
    private final int nameIndex=2;
    private final int phoneIndex=3;
    private final int taxIndex=4;
    private final int addressIndex=5;
    private final int accountIndex=6;
    private final int bankIndex=7;
    private final int statusIndex=8;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        customerService = (CustomerService) SpringUtil.getBean(SpringConstant.CUSTOMER_SERVICES);
        lstCustomers = customerService.getAllCustomer();
        listDataModel = new ListModelList(lstCustomers);
        lstCustomer.setModel(listDataModel);
    }

    /**
     * Edit row
     *
     * @param event
     */
    public void onEdit(ForwardEvent event) {

        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        StyleUtils.setEnableComponent(lstCell);
    }

    /**
     * Cancel
     *
     * @param event
     */
    public void onCancel(ForwardEvent event) {

        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        StyleUtils.setDisableComponent(lstCell);
        reloadGrid();

    }

    /**
     * Save
     *
     * @param event
     */
    public void onSave(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        Customers c = rowSelected.getValue();
        Customers customer = getDataInRow(lstCell);
        customer.setCustomerId(c.getCustomerId());
        customerService.insertOrUpdateCustomer(customer);
        StyleUtils.setDisableComponent(lstCell);
        reloadGrid();
    }

    /**
     * Add row
     */
    public void onClick$add() {
        Customers customer = new Customers();
        listDataModel.add(0,customer);
        lstCustomer.setModel(listDataModel);
        lstCustomer.renderAll();
        List<Component> lstCell = lstCustomer.getRows().getChildren().get(0).getChildren();
        StyleUtils.setEnableComponent(lstCell);
    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private Customers getDataInRow(List<Component> lstCell) {
        Customers customer = new Customers();
        Textbox txtCustomerCode = (Textbox) lstCell.get(codeIndex).getFirstChild();
        Textbox txtCustomerName = (Textbox) lstCell.get(nameIndex).getFirstChild();
        Textbox txtCustomerPhone = (Textbox) lstCell.get(phoneIndex).getFirstChild();
        Textbox txtTaxCode = (Textbox) lstCell.get(taxIndex).getFirstChild();
        Textbox txtCustomerAddress = (Textbox) lstCell.get(addressIndex).getFirstChild();
        Textbox txtAccountNumber = (Textbox) lstCell.get(accountIndex).getFirstChild();
        Textbox txtBankName = (Textbox) lstCell.get(bankIndex).getFirstChild();
        Combobox cbxStatus = (Combobox) lstCell.get(statusIndex).getFirstChild();
        customer.setCustomerCode(txtCustomerCode.getValue());
        customer.setCustomerName(txtCustomerName.getValue());
        customer.setCustomerPhone(txtCustomerPhone.getValue());
        customer.setCustomerAddress(txtCustomerAddress.getValue());
        customer.setTaxCode(txtTaxCode.getValue());
        customer.setAccountNumber(txtAccountNumber.getValue());
        customer.setBankName(txtBankName.getValue());
        customer.setStatus(Integer.valueOf(cbxStatus.getSelectedItem().getValue()));
        return customer;
    }

    /**
     * Reload grid
     */
    private void reloadGrid() {
        lstCustomers = customerService.getAllCustomer();
        listDataModel = new ListModelList(lstCustomers);
        lstCustomer.setModel(listDataModel);
    }

    public void onOK$txtFilterCode() {
        Customers customer = new Customers();
        String vstrCustomerCode = txtFilterCode.getValue();
        customer.setCustomerCode(vstrCustomerCode);
        String vstrCustomerName = txtFilterName.getValue();
        customer.setCustomerName(vstrCustomerName);
        filter(customer);
    }

    public void onOK$txtFilterName() {
        Customers customer = new Customers();
        String vstrCustomerCode = txtFilterCode.getValue();
        customer.setCustomerCode(vstrCustomerCode);
        String vstrCustomerName = txtFilterName.getValue();
        customer.setCustomerName(vstrCustomerName);
        filter(customer);
    }

    private void filter(Customers customer) {
        List<Customers> vlstCustomer = new ArrayList<>();
        if (lstCustomers != null && !lstCustomers.isEmpty() && customer != null) {
            if (!StringUtils.isValidString(customer.getCustomerCode()) && !StringUtils.isValidString(customer.getCustomerName())) {
                vlstCustomer.addAll(lstCustomers);
            } else {
                for (Customers c : lstCustomers) {
                    //tim theo ma va ten
                    if (StringUtils.isValidString(customer.getCustomerCode()) && StringUtils.isValidString(customer.getCustomerName())) {
                        if ((StringUtils.isValidString(c.getCustomerCode()) && c.getCustomerCode().toLowerCase().contains(customer.getCustomerCode().toLowerCase()))
                                && (StringUtils.isValidString(c.getCustomerName()) && c.getCustomerName().toLowerCase().contains(customer.getCustomerName().toLowerCase()))) {
                            vlstCustomer.add(c);
                        }
                    } //tim theo ma
                    else if (StringUtils.isValidString(customer.getCustomerCode()) && !StringUtils.isValidString(customer.getCustomerName())) {
                        if (StringUtils.isValidString(c.getCustomerCode()) && c.getCustomerCode().toLowerCase().contains(customer.getCustomerCode().toLowerCase())) {
                            vlstCustomer.add(c);
                        }
                    } //tim theo ten
                    else if (!StringUtils.isValidString(customer.getCustomerCode()) && StringUtils.isValidString(customer.getCustomerName())) {
                        if (StringUtils.isValidString(c.getCustomerName()) && c.getCustomerName().toLowerCase().contains(customer.getCustomerName().toLowerCase())) {
                            vlstCustomer.add(c);
                        }
                    }
                }
            }
        }
        listDataModel = new ListModelList(vlstCustomer);
        lstCustomer.setModel(listDataModel);

    }
}
