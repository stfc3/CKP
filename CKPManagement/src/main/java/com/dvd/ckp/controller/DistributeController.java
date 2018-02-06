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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.dvd.ckp.business.service.DistributeService;
import com.dvd.ckp.component.MyListModel;
import com.dvd.ckp.domain.Distribute;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StyleUtils;

/**
 *
 * @author viettx
 */
public class DistributeController extends GenericForwardComposer {

    private static final Logger logger = Logger.getLogger(DistributeController.class);

    @WireVariable
    protected DistributeService distributeService;
    @Wire
    private Grid gridDistribute;
    @Wire
    private Combobox cbFilterName;
    MyListModel<Distribute> listDataFilterName;

    // @Wire
    // private Combobox cbFilterCode;
    // MyListModel<Distribute> listDataFilterCode;
    ListModelList<Distribute> listDataModel;

    private List<Distribute> lstDistribute;
    private int insertOrUpdate = 0;

    private Window distribute;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        distributeService = (DistributeService) SpringUtil.getBean(SpringConstant.DISTRIBUTE_SERVICES);

        lstDistribute = new ArrayList<>();
        List<Distribute> vlstDistribute = distributeService.getDistributeActive();
        if (vlstDistribute != null) {
            lstDistribute.addAll(vlstDistribute);
        }
        listDataModel = new ListModelList<Distribute>(lstDistribute);
        gridDistribute.setModel(listDataModel);

        listDataFilterName = new MyListModel<>(lstDistribute);
        cbFilterName.setModel(listDataFilterName);
        //
        // listDataFilterCode = new MyListModel<>(lstDistribute);
        // cbFilterCode.setModel(listDataFilterCode);

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
        StyleUtils.setEnableComponent(lstCell, 4);
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
                    Distribute c = rowSelected.getValue();
                    getDataInRow(lstCell, c);
                    c.setStatus(0);
                    distributeService.insertOrUpdateDistribute(c);
                    StyleUtils.setDisableComponent(lstCell, 4);
                    reloadGrid();
                }
            }
        });

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
     * Cancel
     *
     * @param event
     */
    /**
     * Save
     *
     * @param event
     */
    public void onSave(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        Distribute c = rowSelected.getValue();
        getDataInRow(lstCell, c);

        distributeService.insertOrUpdateDistribute(c);
        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGrid();

    }

    /**
     * Add row
     */
    public void onAdd(ForwardEvent event) {
        Distribute distributeAddItem = new Distribute();
        distributeAddItem.setStatus(1);
        listDataModel.add(0, distributeAddItem);
        gridDistribute.setActivePage(Constants.FIRST_INDEX);
        gridDistribute.setModel(listDataModel);
        gridDistribute.renderAll();
        List<Component> lstCell = gridDistribute.getRows().getChildren().get(0).getChildren();
        StyleUtils.setEnableComponent(lstCell, 4);
        insertOrUpdate = 1;
    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private Distribute getDataInRow(List<Component> lstCell, Distribute data) {
        Component component;

        Textbox txtDistributeCode = null;
        Textbox txtDistributeName = null;
        Datebox dtYear = null;
        Intbox intboxRemote = null;
        Intbox intboxHandheld = null;

        // may bom
        component = lstCell.get(1).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtDistributeCode = (Textbox) component;
            data.setDistributeCode(txtDistributeCode.getValue());
        }
        // loai may bom
        component = lstCell.get(2).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtDistributeName = (Textbox) component;
            data.setDistributeName(txtDistributeName.getValue());
        }
        // vi tri bom
        component = lstCell.get(3).getFirstChild();
        if (component != null && component instanceof Datebox) {
            dtYear = (Datebox) component;
            data.setDistributeYear(dtYear.getValue());
        }
        // loai vi tri
        component = lstCell.get(4).getFirstChild();
        if (component != null && component instanceof Intbox) {
            intboxRemote = (Intbox) component;
            data.setDistributeRemote(intboxRemote.getValue());
        }
        // loai vi tri
        component = lstCell.get(5).getFirstChild();
        if (component != null && component instanceof Intbox) {
            intboxHandheld = (Intbox) component;
            data.setDistributeHandheld(intboxHandheld.getValue());
        }
        data.setStatus(1);

        return data;
    }

    /**
     * Reload grid
     */
    private void reloadGrid() {
        lstDistribute.clear();
        List<Distribute> vlstData = distributeService.getDistributeActive();

        if (vlstData != null && !vlstData.isEmpty()) {
            lstDistribute.addAll(vlstData);
        }
        listDataModel = new ListModelList<Distribute>(vlstData);
        gridDistribute.setModel(listDataModel);
    }

    public void onSelect$cbFilterName() {
        Long vstrDistributeId = null;
        if (cbFilterName.getSelectedItem() != null) {
            vstrDistributeId = cbFilterName.getSelectedItem().getValue();
        }
        filter(vstrDistributeId);
    }

    // public void onSelect$cbFilterCode() {
    // Long vstrDistributeId = null;
    // if (cbFilterCode.getSelectedItem() != null) {
    // vstrDistributeId = cbFilterCode.getSelectedItem().getValue();
    // }
    // filter(vstrDistributeId);
    // }
    private void filter(Long distributeId) {
        List<Distribute> vlstData = new ArrayList<>();
        if (distributeId == null) {
            vlstData.addAll(lstDistribute);
        } else {
            if (lstDistribute != null && !lstDistribute.isEmpty()) {
                for (Distribute item : lstDistribute) {
                    if (distributeId.equals(item.getDistributeId())) {
                        vlstData.add(item);
                    }
                }
            }
        }
        listDataModel = new ListModelList<Distribute>(vlstData);
        gridDistribute.setModel(listDataModel);
    }

    private void setDataDefaultInGrid() {
        gridDistribute.renderAll();
        List<Component> lstRows = gridDistribute.getRows().getChildren();
        if (lstRows != null && !lstRows.isEmpty()) {
            for (int i = 0; i < lstRows.size(); i++) {
                Distribute distribute = listDataModel.get(i);
                Component row = lstRows.get(i);
                List<Component> lstCell = row.getChildren();
                setDataYear(lstCell, 3);
            }
        }
    }

    private void setDataYear(List<Component> lstCell, int columnIndex) {
        Datebox dtYear = null;
        Component component = lstCell.get(columnIndex).getFirstChild();
        if (component != null && component instanceof Datebox) {
            dtYear = (Datebox) component;
            dtYear.setSclass("datebox-year-only");
            dtYear.setFormat("yyyy");
        }

    }

}
