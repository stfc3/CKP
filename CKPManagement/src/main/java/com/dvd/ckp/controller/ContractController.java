/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.ContractService;
import com.dvd.ckp.business.service.CustomerService;
import com.dvd.ckp.domain.Contract;
import com.dvd.ckp.domain.Customer;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;
import com.dvd.ckp.utils.StyleUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.A;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author dmin
 */
public class ContractController extends GenericForwardComposer {

    private static final Logger logger = Logger.getLogger(ContractController.class);

    @WireVariable
    protected ContractService contractService;
    @WireVariable
    protected CustomerService customerService;
    @Wire
    private Grid lstContract;
    @Wire
    private Textbox txtFilterCode;
    @Wire
    private Textbox txtFilterName;
    @Wire
    private Window mainContract;
    ListModelList<Contract> listDataModel;
    List<Customer> lstCustomers;
    private List<Contract> lstContracts;
    Customer defaultCustomer;
    private final int codeIndex = 1;
    private final int nameIndex = 2;
    private final int customerIndex = 3;
    private final int vatIndex = 4;
    private final int discountIndex = 5;
    private final int billIndex = 6;
    private final int fileIndex = 7;
    private final int priceIndex = 8;
    private final int effectiveIndex = 9;
    private final int expirationIndex = 10;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        contractService = (ContractService) SpringUtil.getBean(SpringConstant.CONTRACT_SERVICES);
        customerService = (CustomerService) SpringUtil.getBean(SpringConstant.CUSTOMER_SERVICES);
        lstContracts = contractService.getAllContract();
        listDataModel = new ListModelList(lstContracts);
        lstContract.setModel(listDataModel);

        lstCustomers = customerService.getCustomerActive();
        defaultCustomer = new Customer();
        defaultCustomer.setCustomerId(0l);
        defaultCustomer.setCustomerName(Labels.getLabel("option"));
        lstCustomers.add(0, defaultCustomer);
        setDataDefaultInGrid();
    }

    /**
     * Edit row
     *
     * @param event
     */
    public void onEdit(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        Contract c = rowSelected.getValue();
        setDataCombobox(lstCell, getCustomerDefault(c.getContractId()), customerIndex);
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
        Contract c = rowSelected.getValue();
        Contract contract = getDataInRow(lstCell);
        contract.setContractId(c.getContractId());
        contractService.insertOrUpdateContract(contract);
        StyleUtils.setDisableComponent(lstCell);
        reloadGrid();
    }

    /**
     * Add row
     */
    public void onClick$add() {
        Contract contract = new Contract();
        listDataModel.add(0, contract);
        lstContract.setModel(listDataModel);
        lstContract.renderAll();
        List<Component> lstCell = lstContract.getRows().getChildren().get(0).getChildren();
        setDataDefaultInGrid();
        StyleUtils.setEnableComponent(lstCell);
    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private Contract getDataInRow(List<Component> lstCell) {
        Contract contract = new Contract();
        Component component;
        Textbox txtContractCode = null;
        Textbox txtContractName = null;
        Combobox cbxCustomer = null;
        Textbox txtVat = null;
        Textbox txtDiscount = null;
        Textbox txtBillMoney = null;
        A aFileName = null;
        Datebox dteEffective = null;
        Datebox dteExpiration = null;
        component = lstCell.get(codeIndex).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtContractCode = (Textbox) component;
            contract.setContractCode(txtContractCode.getValue());
        }
        component = lstCell.get(nameIndex).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtContractName = (Textbox) component;
            contract.setContractName(txtContractName.getValue());
        }
        component = lstCell.get(customerIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxCustomer = (Combobox) component;
            contract.setCustomerId(cbxCustomer.getSelectedItem().getValue());
        }
        component = lstCell.get(vatIndex).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtVat = (Textbox) component;
            contract.setVat(Integer.valueOf(txtVat.getValue()));
        }
        component = lstCell.get(discountIndex).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtDiscount = (Textbox) component;
            contract.setDiscount(Integer.valueOf(txtDiscount.getValue()));
        }
        component = lstCell.get(billIndex).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtBillMoney = (Textbox) component;
            contract.setBillMoney(Integer.valueOf(txtBillMoney.getValue()));
        }
        component = lstCell.get(fileIndex).getFirstChild();
        if (component != null && component instanceof A) {
            aFileName = (A) component;
            contract.setFilePath(aFileName.getLabel());
        }
        component = lstCell.get(effectiveIndex).getFirstChild();
        if (component != null && component instanceof Datebox) {
            dteEffective = (Datebox) component;
            contract.setEffectiveDate(dteEffective.getValue());
        }
        component = lstCell.get(expirationIndex).getFirstChild();
        if (component != null && component instanceof Datebox) {
            dteExpiration = (Datebox) component;
            contract.setExpirationDate(dteExpiration.getValue());
        }
        return contract;
    }

    /**
     * Reload grid
     */
    private void reloadGrid() {
        lstContracts = contractService.getAllContract();
        listDataModel = new ListModelList(lstContracts);
        lstContract.setModel(listDataModel);
        setDataDefaultInGrid();
    }

    public void onOK$txtFilterCode() {
        Contract contract = new Contract();
        String vstrContractCode = txtFilterCode.getValue();
        contract.setContractCode(vstrContractCode);
        String vstrContractName = txtFilterName.getValue();
        contract.setContractName(vstrContractName);
        filter(contract);
    }

    public void onOK$txtFilterName() {
        Contract contract = new Contract();
        String vstrContractCode = txtFilterCode.getValue();
        contract.setContractCode(vstrContractCode);
        String vstrContractName = txtFilterName.getValue();
        contract.setContractName(vstrContractName);
        filter(contract);
    }

    private void filter(Contract contract) {
        List<Contract> vlstContracts = new ArrayList<>();
        if (lstContracts != null && !lstContracts.isEmpty() && contract != null) {
            if (!StringUtils.isValidString(contract.getContractCode()) && !StringUtils.isValidString(contract.getContractName())) {
                vlstContracts.addAll(lstContracts);
            } else {
                for (Contract c : lstContracts) {
                    //tim theo ma va ten
                    if (StringUtils.isValidString(contract.getContractCode()) && StringUtils.isValidString(contract.getContractName())) {
                        if ((StringUtils.isValidString(c.getContractCode()) && c.getContractCode().toLowerCase().contains(contract.getContractCode().toLowerCase()))
                                && (StringUtils.isValidString(c.getContractName()) && c.getContractName().toLowerCase().contains(contract.getContractName().toLowerCase()))) {
                            vlstContracts.add(c);
                        }
                    } //tim theo ma
                    else if (StringUtils.isValidString(contract.getContractCode()) && !StringUtils.isValidString(contract.getContractName())) {
                        if (StringUtils.isValidString(c.getContractCode()) && c.getContractCode().toLowerCase().contains(contract.getContractCode().toLowerCase())) {
                            vlstContracts.add(c);
                        }
                    } //tim theo ten
                    else if (!StringUtils.isValidString(contract.getContractCode()) && StringUtils.isValidString(contract.getContractName())) {
                        if (StringUtils.isValidString(c.getContractName()) && c.getContractName().toLowerCase().contains(contract.getContractName().toLowerCase())) {
                            vlstContracts.add(c);
                        }
                    }
                }
            }
        }
        listDataModel = new ListModelList(vlstContracts);
        lstContract.setModel(listDataModel);

    }

    private void setDataCombobox(List<Component> lstCell, List<Customer> selectedIndex, int columnIndex) {
        Combobox cbxCustomer = null;
        Component component = lstCell.get(columnIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxCustomer = (Combobox) component;
            ListModelList listDataModelCustomer = new ListModelList(lstCustomers);
            listDataModelCustomer.setSelection(selectedIndex);
            cbxCustomer.setModel(listDataModelCustomer);
            cbxCustomer.setTooltiptext(selectedIndex.get(0).getCustomerName());
        }

    }

    private List<Customer> getCustomerDefault(Long customerId) {
        List<Customer> customerSelected = new ArrayList<>();
        if (customerId != null && lstCustomers != null && !lstCustomers.isEmpty()) {
            for (Customer customer : lstCustomers) {
                if (customerId.equals(customer.getCustomerId())) {
                    customerSelected.add(customer);
                    break;
                }
            }
        }
        if (customerSelected.isEmpty()) {
            customerSelected.add(defaultCustomer);
        }
        return customerSelected;
    }

    private void setDataDefaultInGrid() {
        lstContract.renderAll();
        List<Component> lstRows = lstContract.getRows().getChildren();
        if (lstRows != null && !lstRows.isEmpty()) {
            for (int i = 0; i < lstRows.size(); i++) {
                Contract contract = listDataModel.get(i);
                Component row = lstRows.get(i);
                List<Component> lstCell = row.getChildren();
                setDataCombobox(lstCell, getCustomerDefault(contract.getCustomerId()), customerIndex);
            }
        }
    }

    public void onPrice(ForwardEvent event) {
        Map<String, Object> arguments = new HashMap<>();

        Window winAddUser = (Window) Executions.createComponents(
                "/manager/include/price.zul", mainContract, arguments);

        winAddUser.setBorder(true);
        winAddUser.setBorder("normal");
        winAddUser.setClosable(true);

        winAddUser.doModal();
    }
}
