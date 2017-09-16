/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.ConstructionService;
import com.dvd.ckp.business.service.CustomerService;
import com.dvd.ckp.domain.Construction;
import com.dvd.ckp.domain.Customers;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.A;
import org.zkoss.zul.Cell;
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
    @Wire
    private Grid lstConstruction;
    @Wire
    private Textbox txtFilterCode;
    @Wire
    private Textbox txtFilterName;
    ListModelList<Construction> listDataModel;
    private List<Construction> lstConstructions;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        constructionService = (ConstructionService) SpringUtil.getBean(SpringConstant.CONSTRUCTION_SERVICES);
        lstConstructions = new ArrayList<>();
        List<Construction> vlstConstructions = constructionService.getAllConstruction();
        if (vlstConstructions != null) {
            lstConstructions.addAll(vlstConstructions);
        }
        listDataModel = new ListModelList(lstConstructions);
        lstConstruction.setModel(listDataModel);
    }

    /**
     * Edit row
     *
     * @param event
     */
    public void onEdit(ForwardEvent event) {

        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        setEnableComponent(lstCell);
    }

    /**
     * Set style enable edit
     *
     * @param lstCell
     */
    private void setEnableComponent(List<Component> lstCell) {
        if (lstCell != null && !lstCell.isEmpty()) {
            for (Component c : lstCell) {
                if (c instanceof Cell) {
                    Component child = c.getChildren().get(0);
                    if (child instanceof Combobox) {
                        ((Combobox) child).setButtonVisible(true);
                        ((Combobox) child).setInplace(false);
                    } else if (child instanceof Textbox) {
                        ((Textbox) child).setReadonly(false);
                        ((Textbox) child).setInplace(false);
                    } else if (child instanceof A) {
                        A edit = (A) child;
                        edit.setVisible(false);
                        A save = (A) c.getChildren().get(1);
                        A cancel = (A) c.getChildren().get(2);
                        save.setVisible(true);
                        cancel.setVisible(true);
                    }
                }
            }
        }
    }

    /**
     * Set style disable edit
     *
     * @param lstCell
     */
    private void setDisableComponent(List<Component> lstCell) {
        if (lstCell != null && !lstCell.isEmpty()) {
            for (Component c : lstCell) {
                if (c instanceof Cell) {
                    Component child = c.getChildren().get(0);
                    if (child instanceof Combobox) {
                        ((Combobox) child).setButtonVisible(false);
                        ((Combobox) child).setInplace(true);
                    } else if (child instanceof Textbox) {
                        ((Textbox) child).setReadonly(true);
                        ((Textbox) child).setInplace(true);
                    } else if (child instanceof A) {
                        A edit = (A) child;
                        edit.setVisible(true);
                        A save = (A) c.getChildren().get(1);
                        A cancel = (A) c.getChildren().get(2);
                        save.setVisible(false);
                        cancel.setVisible(false);
                    }
                }
            }
        }
    }

    /**
     * Cancel
     *
     * @param event
     */
    public void onCancel(ForwardEvent event) {

        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        setDisableComponent(lstCell);
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
        constructionService.insertOrUpdateConstruction(construction);
        setDisableComponent(lstCell);
        reloadGrid();
    }

    /**
     * Add row
     */
    public void onClick$add() {
        Construction construction = new Construction();
        listDataModel.add(0,construction);
        lstConstruction.setModel(listDataModel);
        lstConstruction.renderAll();
        List<Component> lstCell = lstConstruction.getRows().getChildren().get(0).getChildren();
        setEnableComponent(lstCell);
    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private Construction getDataInRow(List<Component> lstCell) {
        Construction construction = new Construction();
        Textbox txtConstructionCode = (Textbox) lstCell.get(1).getFirstChild();
        Textbox txtConstructionName = (Textbox) lstCell.get(2).getFirstChild();
        Combobox cbxContract = (Combobox) lstCell.get(3).getFirstChild();
        Combobox cbxConstructionFar = (Combobox) lstCell.get(4).getFirstChild();
        Textbox txtConstructionConvert = (Textbox) lstCell.get(5).getFirstChild();
        Combobox cbxStatus = (Combobox) lstCell.get(6).getFirstChild();
        construction.setConstructionCode(txtConstructionCode.getValue());
        construction.setConstructionName(txtConstructionName.getValue());
        construction.setContractId(Long.valueOf(cbxContract.getSelectedItem().getValue()));
        construction.setConstructionFar(Integer.valueOf(cbxConstructionFar.getSelectedItem().getValue()));
        construction.setConstructionConvert(Double.valueOf(txtConstructionConvert.getValue()));
        construction.setStatus(Integer.valueOf(cbxStatus.getSelectedItem().getValue()));
        return construction;
    }

    /**
     * Reload grid
     */
    private void reloadGrid() {
        List<Construction> vlstConstruction = constructionService.getAllConstruction();
        listDataModel = new ListModelList(vlstConstruction);
        lstConstruction.setModel(listDataModel);
    }

    public void onChange$txtFilterCode() {
        Construction construction = new Construction();
        String vstrConstructionCode = txtFilterCode.getValue();
        construction.setConstructionCode(vstrConstructionCode);
        String vstrConstructionName = txtFilterName.getValue();
        construction.setConstructionName(vstrConstructionName);
        filter(construction);
    }

    public void onChange$txtFilterName() {
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
}
