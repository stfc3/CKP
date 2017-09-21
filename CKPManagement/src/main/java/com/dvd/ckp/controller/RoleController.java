/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.RoleService;
import com.dvd.ckp.domain.Role;
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
 * @author daond
 */
public class RoleController extends GenericForwardComposer {

    private static final Logger logger = Logger.getLogger(RoleController.class);
    @WireVariable
    protected RoleService roleService;
    @Wire
    private Grid lstRole;
    @Wire
    private Textbox txtFilterRoleCode;
    @Wire
    private Textbox txtFilterRoleName;
    ListModelList<Role> listDataModel;
    private List<Role> lstRoles;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        roleService = (RoleService) SpringUtil.getBean(SpringConstant.ROLE_SERVICES);
        lstRoles = new ArrayList<>();
        List<Role> vlstRole = roleService.getAllRole();
        if (vlstRole != null) {
            lstRoles.addAll(vlstRole);
        }
        listDataModel = new ListModelList(lstRoles);
        lstRole.setModel(listDataModel);
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
        Role c = rowSelected.getValue();
        Role role = getDataInRow(lstCell);
        role.setRoleId(c.getRoleId());
        roleService.insertOrUpdateRole(role);
        setDisableComponent(lstCell);
        reloadGrid();
    }

    /**
     * Add row
     */
    public void onClick$add() {
        Role role = new Role();
        listDataModel.add(0, role);
        lstRole.setModel(listDataModel);
        lstRole.renderAll();
        List<Component> lstCell = lstRole.getRows().getChildren().get(0).getChildren();
        setEnableComponent(lstCell);
    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private Role getDataInRow(List<Component> lstCell) {
        Role role = new Role();
        Textbox txtRoleCode = (Textbox) lstCell.get(1).getFirstChild();
        Textbox txtRoleName = (Textbox) lstCell.get(2).getFirstChild();
        Textbox txtDes = (Textbox) lstCell.get(3).getFirstChild();
        Combobox cbxStatus = (Combobox) lstCell.get(8).getFirstChild();

        role.setRoleCode(txtRoleCode.getValue());
        role.setRoleName(txtRoleName.getValue());
        role.setDescription(txtDes.getValue());

        role.setStatus(Integer.valueOf(cbxStatus.getSelectedItem().getValue()));
        return role;
    }

    /**
     * Reload grid
     */
    private void reloadGrid() {
        List<Role> vlstRole = roleService.getAllRole();
        listDataModel = new ListModelList(vlstRole);
        lstRole.setModel(listDataModel);
    }

    public void onChange$txtFilterRoleCode() {
        Role role = new Role();
        String vstrRoleCode = txtFilterRoleCode.getValue();
        role.setRoleCode(vstrRoleCode);
        String vstrRoleName = txtFilterRoleName.getValue();
        role.setRoleName(vstrRoleName);
        filter(role);
    }

    public void onChange$txtFilterRoleName() {
        Role role = new Role();
        String vstrRoleCode = txtFilterRoleCode.getValue();
        role.setRoleCode(vstrRoleCode);
        String vstrRoleName = txtFilterRoleName.getValue();
        role.setRoleName(vstrRoleName);
        filter(role);
    }

    private void filter(Role role) {
        List<Role> vlstRole = new ArrayList<>();
        if (lstRoles != null && !lstRoles.isEmpty() && role != null) {
            if (!StringUtils.isValidString(role.getRoleCode())
                    && !StringUtils.isValidString(role.getRoleName())) {
                vlstRole.addAll(lstRoles);
            } else {
                for (Role c : lstRoles) {
                    //tim theo ma va ten
                    if (StringUtils.isValidString(role.getRoleCode()) && StringUtils.isValidString(role.getRoleName())) {
                        if ((StringUtils.isValidString(c.getRoleCode()) && c.getRoleCode().toLowerCase().contains(role.getRoleCode().toLowerCase()))
                                && (StringUtils.isValidString(c.getRoleName()) && c.getRoleName().toLowerCase().contains(role.getRoleName().toLowerCase()))) {
                            vlstRole.add(c);
                        }
                    } //tim theo role code
                    else if (StringUtils.isValidString(role.getRoleCode()) && !StringUtils.isValidString(role.getRoleName())) {
                        if (StringUtils.isValidString(c.getRoleCode()) && c.getRoleCode().toLowerCase().contains(role.getRoleCode().toLowerCase())) {
                            vlstRole.add(c);
                        }
                    } //tim theo role name
                    else if (!StringUtils.isValidString(role.getRoleCode()) && StringUtils.isValidString(role.getRoleName())) {
                        if (StringUtils.isValidString(c.getRoleName()) && c.getRoleName().toLowerCase().contains(role.getRoleName().toLowerCase())) {
                            vlstRole.add(c);
                        }
                    }
                }
            }
        }
        listDataModel = new ListModelList(vlstRole);
        lstRole.setModel(listDataModel);

    }
}
