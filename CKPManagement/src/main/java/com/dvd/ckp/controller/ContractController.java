/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
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

import com.dvd.ckp.business.service.ContractService;
import com.dvd.ckp.business.service.CustomerService;
import com.dvd.ckp.domain.Contract;
import com.dvd.ckp.domain.Customer;
import com.dvd.ckp.domain.Price;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.FileUtils;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;
import com.dvd.ckp.utils.StyleUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import javax.servlet.ServletContext;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Filedownload;

/**
 *
 * @author dmin
 */
public class ContractController extends GenericForwardComposer {

    private static final Logger logger = Logger.getLogger(ContractController.class);

    ServletContext context;
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
    ListModelList<Price> listDataModelPrice;
    List<Customer> lstCustomers;
    private List<Contract> lstContracts;
    Customer defaultCustomer;

    ///
    private final int codeIndex = 1;
    private final int nameIndex = 2;
    private final int customerIndex = 3;
    private final int vatIndex = 4;
    private final int discountIndex = 5;
    private final int billIndex = 6;
    private final int fileIndex = 7;
    private final int effectiveIndex = 9;
    private final int expirationIndex = 10;
    ///

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        context = Sessions.getCurrent().getWebApp().getServletContext();
        contractService = (ContractService) SpringUtil.getBean(SpringConstant.CONTRACT_SERVICES);
        customerService = (CustomerService) SpringUtil.getBean(SpringConstant.CUSTOMER_SERVICES);
        lstContracts = contractService.getContractActive();
        listDataModel = new ListModelList(lstContracts);
        lstContract.setModel(listDataModel);

        lstCustomers = customerService.getCustomerActive();
        defaultCustomer = new Customer();
        defaultCustomer.setCustomerId(Constants.DEFAULT_ID);
        defaultCustomer.setCustomerName(Labels.getLabel("option"));
        lstCustomers.add(Constants.FIRST_INDEX, defaultCustomer);
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
        Contract c = rowSelected.getValue();
        Contract contract = getDataInRow(lstCell);
        contract.setContractId(c.getContractId());
        contract.setCreateDate(new Date());
        contractService.insertOrUpdateContract(contract);
        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGrid();
    }

    public void onDelete(ForwardEvent event) {
        Messagebox.show(Labels.getLabel("message.confirm.delete.content"), Labels.getLabel("message.confirm.delete.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event e) {
                if (Messagebox.ON_YES.equals(e.getName())) {
                    Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
                    List<Component> lstCell = rowSelected.getChildren();
                    Contract c = rowSelected.getValue();
                    Contract contract = getDataInRow(lstCell);
                    contract.setContractId(c.getContractId());
                    contract.setStatus(Constants.STATUS_INACTIVE);
                    contract.setCreateDate(new Date());
                    contractService.insertOrUpdateContract(contract);
                    reloadGrid();
                }
            }
        });
    }

    /**
     * Add row
     */
    public void onClick$add() {
        Contract contract = new Contract();
        listDataModel.add(Constants.FIRST_INDEX, contract);
        lstContract.setModel(listDataModel);
        lstContract.renderAll();
        List<Component> lstCell = lstContract.getRows().getFirstChild().getChildren();
        setDataDefaultInGrid();
        StyleUtils.setEnableComponent(lstCell, 4);
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
        Doublebox dbbVat = null;
        Doublebox dbbDiscount = null;
        Doublebox dbbBillMoney = null;
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
        if (component != null && component instanceof Doublebox) {
            dbbVat = (Doublebox) component;
            contract.setVat(dbbVat.getValue());
        }
        component = lstCell.get(discountIndex).getFirstChild();
        if (component != null && component instanceof Doublebox) {
            dbbDiscount = (Doublebox) component;
            contract.setDiscount(dbbDiscount.getValue());
        }
        component = lstCell.get(billIndex).getFirstChild();
        if (component != null && component instanceof Doublebox) {
            dbbBillMoney = (Doublebox) component;
            contract.setBillMoney(dbbBillMoney.getValue());
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
        lstContracts = contractService.getContractActive();
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
            cbxCustomer.setTooltiptext(selectedIndex.get(Constants.FIRST_INDEX).getCustomerName());
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
        Messagebox.show("Chức năng chưa được hỗ trợ", "Thông báo", Messagebox.OK, Messagebox.INFORMATION);

//        Map<String, Object> arguments = new HashMap<>();
//
//        Window winAddUser = (Window) Executions.createComponents(
//                "/manager/include/price.zul", mainContract, arguments);
//
//        winAddUser.setBorder(true);
//        winAddUser.setBorder("normal");
//        winAddUser.setClosable(true);
//
//        winAddUser.doModal();
    }

    public void onImport(ForwardEvent event) {
        Messagebox.show("Chức năng chưa được hỗ trợ", "Thông báo", Messagebox.OK, Messagebox.INFORMATION);
    }

    public void onExport(ForwardEvent event) {
        Messagebox.show("Chức năng chưa được hỗ trợ", "Thông báo", Messagebox.OK, Messagebox.INFORMATION);
    }

    public void onUploadFile(ForwardEvent event) {
        if (event.getOrigin() instanceof UploadEvent) {
            UploadEvent uploadEvent = (UploadEvent) event.getOrigin();
            Media media = uploadEvent.getMedia();
            Cell cell = (Cell) uploadEvent.getTarget().getParent();
            A aFileName = (A) cell.getFirstChild();
            aFileName.setLabel(media.getName());
            FileUtils fileUtils = new FileUtils();
            fileUtils.saveFile(media, context.getRealPath("file/contract"));
        }
    }

    public void onDownloadFile(ForwardEvent event) {
        try {
            A aFileName = (A) event.getOrigin().getTarget();
            File file = new File(context.getRealPath("file/contract/" + aFileName.getLabel()));
            Filedownload.save(file, null);
        } catch (FileNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
