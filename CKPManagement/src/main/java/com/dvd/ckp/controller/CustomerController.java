/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.CustomerService;
import com.dvd.ckp.domain.Customer;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;
import com.dvd.ckp.utils.StyleUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.zhtml.Messagebox;
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
    ListModelList<Customer> listDataModel;
    private List<Customer> lstCustomers;
    
    private final int codeIndex=1;
    private final int nameIndex=2;
    private final int phoneIndex=3;
    private final int taxIndex=4;
    private final int addressIndex=5;
    private final int accountIndex=6;
    private final int bankIndex=7;
//    private final int statusIndex=8;

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
        StyleUtils.setEnableComponent(lstCell, 4);
    }

    /**
     * Cancel
     *
     * @param event
     */
    public void onCancel(ForwardEvent event) {

        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        StyleUtils.setDisableComponent(lstCell, 4);
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
        Customer c = rowSelected.getValue();
        Customer customer = getDataInRow(lstCell);
        customer.setCustomerId(c.getCustomerId());
        customerService.insertOrUpdateCustomer(customer);
        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGrid();
    }

    /**
     * Add row
     */
    public void onClick$add() {
        Customer customer = new Customer();
        listDataModel.add(0,customer);
        lstCustomer.setModel(listDataModel);
        lstCustomer.renderAll();
        List<Component> lstCell = lstCustomer.getRows().getChildren().get(0).getChildren();
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
        Textbox txtCustomerCode=null;
        Textbox txtCustomerName=null;
        Textbox txtCustomerPhone=null;
        Textbox txtTaxCode=null;
        Textbox txtCustomerAddress=null;
        Textbox txtAccountNumber=null;
        Textbox txtBankName=null;
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
        if (component != null && component instanceof Textbox) {
            txtBankName = (Textbox) component;
            customer.setBankName(txtBankName.getValue());
        }
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
        Customer customer = new Customer();
        String vstrCustomerCode = txtFilterCode.getValue();
        customer.setCustomerCode(vstrCustomerCode);
        String vstrCustomerName = txtFilterName.getValue();
        customer.setCustomerName(vstrCustomerName);
        filter(customer);
    }

    public void onOK$txtFilterName() {
        Customer customer = new Customer();
        String vstrCustomerCode = txtFilterCode.getValue();
        customer.setCustomerCode(vstrCustomerCode);
        String vstrCustomerName = txtFilterName.getValue();
        customer.setCustomerName(vstrCustomerName);
        filter(customer);
    }

    private void filter(Customer customer) {
        List<Customer> vlstCustomer = new ArrayList<>();
        if (lstCustomers != null && !lstCustomers.isEmpty() && customer != null) {
            if (!StringUtils.isValidString(customer.getCustomerCode()) && !StringUtils.isValidString(customer.getCustomerName())) {
                vlstCustomer.addAll(lstCustomers);
            } else {
                for (Customer c : lstCustomers) {
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
    
    public void onImport(ForwardEvent event) {
        Messagebox.show("Chức năng chưa được hỗ trợ", "Thông báo", Messagebox.OK, Messagebox.INFORMATION);
    }

    public void onExport(ForwardEvent event) {
        Messagebox.show("Chức năng chưa được hỗ trợ", "Thông báo", Messagebox.OK, Messagebox.INFORMATION);
    }
}
