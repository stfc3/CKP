/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.CustomerService;
import com.dvd.ckp.domain.Customer;
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


    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        customerService = (CustomerService) SpringUtil.getBean(SpringConstant.CUSTOMER_SERVICES);
        reloadGrid();
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
        customer.setStatus(Constants.STATUS_ACTIVE);
        customer.setCreateDate(new Date());
        customerService.insertOrUpdateCustomer(customer);
        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGrid();
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
                    reloadGrid();
                }
            }
        });
    }

    /**
     * Add row
     */
    public void onClick$add() {
        Customer customer = new Customer();
        listDataModel.add(Constants.FIRST_INDEX, customer);
        lstCustomer.setModel(listDataModel);
        lstCustomer.renderAll();
        List<Component> lstCell = lstCustomer.getRows().getFirstChild().getChildren();
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
        Textbox txtBankName = null;
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
        lstCustomers = customerService.getCustomerActive();
        listDataModel = new ListModelList(lstCustomers);
        lstCustomer.setModel(listDataModel);

        cbxCustomerFilter.setModel(listDataModel);
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
    }

    public void onImport(ForwardEvent event) {
        Messagebox.show("Chức năng chưa được hỗ trợ", "Thông báo", Messagebox.OK, Messagebox.INFORMATION);
    }

    public void onExport(ForwardEvent event) {
        Messagebox.show("Chức năng chưa được hỗ trợ", "Thông báo", Messagebox.OK, Messagebox.INFORMATION);
    }
}
