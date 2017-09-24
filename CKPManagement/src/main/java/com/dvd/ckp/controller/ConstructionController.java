/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.ConstructionService;
import com.dvd.ckp.business.service.ContractService;
import com.dvd.ckp.domain.Construction;
import com.dvd.ckp.domain.Contract;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;
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
public class ConstructionController extends GenericForwardComposer {

    private static final Logger logger = Logger.getLogger(ConstructionController.class);
    @WireVariable
    protected ConstructionService constructionService;
    @WireVariable
    protected ContractService contractService;
    @Wire
    private Grid lstConstruction;
    @Wire
    private Textbox txtFilterCode;
    @Wire
    private Textbox txtFilterName;
    ListModelList<Construction> listDataModel;
    List<Contract> lstContract;
    private List<Construction> lstConstructions;
    Contract defaultContract;
    private final int codeIndex = 1;
    private final int nameIndex = 2;
    private final int contractIndex = 3;
    private final int addressIndex = 4;
//    private final int convertIndex = 5;
//    private final int statusIndex = 6;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        constructionService = (ConstructionService) SpringUtil.getBean(SpringConstant.CONSTRUCTION_SERVICES);
        contractService = (ContractService) SpringUtil.getBean(SpringConstant.CONTRACT_SERVICES);
        lstConstructions = constructionService.getConstructionActive();
        listDataModel = new ListModelList(lstConstructions);
        lstConstruction.setModel(listDataModel);

        lstContract = contractService.getContractActive();
        defaultContract = new Contract();
        defaultContract.setContractId(Constants.DEFAULT_ID);
        defaultContract.setContractName(Labels.getLabel("option"));
        lstContract.add(Constants.FIRST_INDEX, defaultContract);
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
        Construction c = rowSelected.getValue();
        setDataCombobox(lstCell, getContractDefault(c.getContractId()), contractIndex);
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
        Construction c = rowSelected.getValue();
        Construction construction = getDataInRow(lstCell);
        construction.setConstructionId(c.getConstructionId());
        construction.setCreateDate(new Date());
        constructionService.insertOrUpdateConstruction(construction);
        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGrid();
    }

    /**
     * Add row
     */
    public void onClick$add() {
        Construction construction = new Construction();
        listDataModel.add(Constants.FIRST_INDEX, construction);
        lstConstruction.setModel(listDataModel);
        lstConstruction.renderAll();
        List<Component> lstCell = lstConstruction.getRows().getFirstChild().getChildren();
        setDataDefaultInGrid();
        StyleUtils.setEnableComponent(lstCell, 4);
    }

    public void onDelete(ForwardEvent event) {
        Messagebox.show(Labels.getLabel("message.confirm.delete.content"), Labels.getLabel("message.confirm.delete.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event e) {
                if (Messagebox.ON_YES.equals(e.getName())) {
                    Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
                    List<Component> lstCell = rowSelected.getChildren();
                    Construction c = rowSelected.getValue();
                    Construction construction = getDataInRow(lstCell);
                    construction.setConstructionId(c.getConstructionId());
                    construction.setStatus(Constants.STATUS_INACTIVE);
                    construction.setCreateDate(new Date());
                    constructionService.insertOrUpdateConstruction(construction);
                    reloadGrid();
                }
            }
        });
    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private Construction getDataInRow(List<Component> lstCell) {
        Construction construction = new Construction();
        Component component;
        Textbox txtConstructionCode = null;
        Textbox txtConstructionName = null;
        Combobox cbxContract = null;
        Textbox txtConstructionAddress = null;
//        Combobox cbxConstructionFar = null;
//        Textbox txtConstructionConvert = null;
//        Combobox cbxStatus = null;
        component = lstCell.get(codeIndex).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtConstructionCode = (Textbox) component;
            construction.setConstructionCode(txtConstructionCode.getValue());
        }
        component = lstCell.get(nameIndex).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtConstructionName = (Textbox) component;
            construction.setConstructionName(txtConstructionName.getValue());
        }
        component = lstCell.get(contractIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxContract = (Combobox) component;
            construction.setContractId(cbxContract.getSelectedItem().getValue());
        }
        component = lstCell.get(addressIndex).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtConstructionAddress = (Textbox) component;
            construction.setConstructionAddress(txtConstructionAddress.getValue());
        }
        return construction;
    }

    /**
     * Reload grid
     */
    private void reloadGrid() {
        lstConstructions = constructionService.getConstructionActive();
        listDataModel = new ListModelList(lstConstructions);
        lstConstruction.setModel(listDataModel);
        setDataDefaultInGrid();
    }

    public void onOK$txtFilterCode() {
        Construction construction = new Construction();
        String vstrConstructionCode = txtFilterCode.getValue();
        construction.setConstructionCode(vstrConstructionCode);
        String vstrConstructionName = txtFilterName.getValue();
        construction.setConstructionName(vstrConstructionName);
        filter(construction);
    }

    public void onOK$txtFilterName() {
        Construction construction = new Construction();
        String vstrConstructionCode = txtFilterCode.getValue();
        construction.setConstructionCode(vstrConstructionCode);
        String vstrConstructionName = txtFilterName.getValue();
        construction.setConstructionName(vstrConstructionName);
        filter(construction);
    }

    private void filter(Construction construction) {
        List<Construction> vlstConstruction = new ArrayList<>();
        if (lstConstructions != null && !lstConstructions.isEmpty() && construction != null) {
            if (!StringUtils.isValidString(construction.getConstructionCode()) && !StringUtils.isValidString(construction.getConstructionName())) {
                vlstConstruction.addAll(lstConstructions);
            } else {
                for (Construction c : lstConstructions) {
                    //tim theo ma va ten
                    if (StringUtils.isValidString(construction.getConstructionCode()) && StringUtils.isValidString(construction.getConstructionName())) {
                        if ((StringUtils.isValidString(c.getConstructionCode()) && c.getConstructionCode().toLowerCase().contains(construction.getConstructionCode().toLowerCase()))
                                && (StringUtils.isValidString(c.getConstructionName()) && c.getConstructionName().toLowerCase().contains(construction.getConstructionName().toLowerCase()))) {
                            vlstConstruction.add(c);
                        }
                    } //tim theo ma
                    else if (StringUtils.isValidString(construction.getConstructionCode()) && !StringUtils.isValidString(construction.getConstructionName())) {
                        if (StringUtils.isValidString(c.getConstructionCode()) && c.getConstructionCode().toLowerCase().contains(construction.getConstructionCode().toLowerCase())) {
                            vlstConstruction.add(c);
                        }
                    } //tim theo ten
                    else if (!StringUtils.isValidString(construction.getConstructionCode()) && StringUtils.isValidString(construction.getConstructionName())) {
                        if (StringUtils.isValidString(c.getConstructionName()) && c.getConstructionName().toLowerCase().contains(construction.getConstructionName().toLowerCase())) {
                            vlstConstruction.add(c);
                        }
                    }
                }
            }
        }
        listDataModel = new ListModelList(vlstConstruction);
        lstConstruction.setModel(listDataModel);

    }

    private void setDataCombobox(List<Component> lstCell, List<Contract> selectedIndex, int columnIndex) {
        Combobox cbxContract = null;
        Component component = lstCell.get(columnIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            cbxContract = (Combobox) component;
            ListModelList listDataModelContract = new ListModelList(lstContract);
            listDataModelContract.setSelection(selectedIndex);
            cbxContract.setModel(listDataModelContract);
        }

    }

    private List<Contract> getContractDefault(Long contractId) {
        List<Contract> contractSelected = new ArrayList<>();
        if (contractId != null && lstContract != null && !lstContract.isEmpty()) {
            for (Contract contract : lstContract) {
                if (contractId.equals(contract.getContractId())) {
                    contractSelected.add(contract);
                    break;
                }
            }
        }
        if (contractSelected.isEmpty()) {
            contractSelected.add(defaultContract);
        }
        return contractSelected;
    }

    private void setDataDefaultInGrid() {
        lstConstruction.renderAll();
        List<Component> lstRows = lstConstruction.getRows().getChildren();
        if (lstRows != null && !lstRows.isEmpty()) {
            for (int i = 0; i < lstRows.size(); i++) {
                Construction construction = listDataModel.get(i);
                Component row = lstRows.get(i);
                List<Component> lstCell = row.getChildren();
                setDataCombobox(lstCell, getContractDefault(construction.getContractId()), contractIndex);
            }
        }
    }

    public void onImport(ForwardEvent event) {
        Messagebox.show("Chức năng chưa được hỗ trợ", "Thông báo", Messagebox.OK, Messagebox.INFORMATION);
    }

    public void onExport(ForwardEvent event) {
        Messagebox.show("Chức năng chưa được hỗ trợ", "Thông báo", Messagebox.OK, Messagebox.INFORMATION);
    }
}
