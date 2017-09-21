/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.UserService;
import com.dvd.ckp.domain.Users;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.EncryptUtil;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

/**
 *
 * @author daond
 */
public class UserController extends GenericForwardComposer {

    private static final Logger logger = Logger.getLogger(UserController.class);
    @WireVariable
    protected UserService userService;
    @Wire
    private Grid lstUser;
    @Wire
    private Textbox txtFilterUserName;
    @Wire
    private Textbox txtFilterFullName;
    @Wire
    private Textbox txtFilterEmail;
    @Wire
    private Textbox txtFilterPhone;
    ListModelList<Users> listDataModel;
    private List<Users> lstUsers;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        userService = (UserService) SpringUtil.getBean(SpringConstant.USER_SERVICES);
        lstUsers = new ArrayList<>();
        List<Users> vlstUser = userService.getAllUser();
        if (vlstUser != null) {
            lstUsers.addAll(vlstUser);
        }
        listDataModel = new ListModelList(lstUsers);
        lstUser.setModel(listDataModel);
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
                        A resetPass = (A) c.getChildren().get(1);
                        resetPass.setVisible(false);
                        A save = (A) c.getChildren().get(2);
                        A cancel = (A) c.getChildren().get(3);
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
                        A resetPass = (A) c.getChildren().get(1);
                        resetPass.setVisible(true);
                        A save = (A) c.getChildren().get(2);
                        A cancel = (A) c.getChildren().get(3);
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
        Users c = rowSelected.getValue();
        Users user = getDataInRow(lstCell);
        user.setUserId(c.getUserId());
        user.setPassword(EncryptUtil.encrypt(c.getPassword()));
        userService.insertOrUpdateUser(user);
        setDisableComponent(lstCell);
        reloadGrid();
    }

    /**
     * Add row
     */
    public void onClick$add() {
        Users user = new Users();
        listDataModel.add(0, user);
        lstUser.setModel(listDataModel);
        lstUser.renderAll();
        List<Component> lstCell = lstUser.getRows().getChildren().get(0).getChildren();
        setEnableComponent(lstCell);
    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private Users getDataInRow(List<Component> lstCell) {
        Users user = new Users();
        Textbox txtUserName = (Textbox) lstCell.get(1).getFirstChild();
        Textbox txtFullName = (Textbox) lstCell.get(2).getFirstChild();
        Textbox txtEmail = (Textbox) lstCell.get(3).getFirstChild();
        Textbox txtPhone = (Textbox) lstCell.get(4).getFirstChild();
        Textbox txtPass = (Textbox) lstCell.get(5).getFirstChild();
        Textbox txtAddress = (Textbox) lstCell.get(6).getFirstChild();
        Textbox txtCard = (Textbox) lstCell.get(7).getFirstChild();
        Combobox cbxStatus = (Combobox) lstCell.get(8).getFirstChild();

        user.setUserName(txtUserName.getValue());
        user.setFullName(txtFullName.getValue());
        user.setEmail(txtEmail.getValue());
        user.setPhone(txtPhone.getValue());
        user.setPassword(txtPass.getValue());
        user.setAddress(txtAddress.getValue());
        user.setCard(txtCard.getValue());

        user.setStatus(Integer.valueOf(cbxStatus.getSelectedItem().getValue()));
        return user;
    }

    /**
     * Reload grid
     */
    private void reloadGrid() {
        List<Users> vlstUser = userService.getAllUser();
        listDataModel = new ListModelList(vlstUser);
        lstUser.setModel(listDataModel);
    }

    public void onChange$txtFilterUserName() {
        Users user = new Users();
        String vstrUserName = txtFilterUserName.getValue();
        user.setUserName(vstrUserName);
        String vstrFullName = txtFilterFullName.getValue();
        user.setFullName(vstrFullName);
        String vstrEmail = txtFilterEmail.getValue();
        user.setEmail(vstrEmail);
        String vstrPhone = txtFilterPhone.getValue();
        user.setPhone(vstrPhone);
        filter(user);
    }

    public void onChange$txtFilterFullName() {
        Users user = new Users();
        String vstrUserName = txtFilterUserName.getValue();
        user.setUserName(vstrUserName);
        String vstrFullName = txtFilterFullName.getValue();
        user.setFullName(vstrFullName);
        String vstrEmail = txtFilterEmail.getValue();
        user.setEmail(vstrEmail);
        String vstrPhone = txtFilterPhone.getValue();
        user.setPhone(vstrPhone);
        filter(user);
    }

    public void onChange$txtFilterEmail() {
        Users user = new Users();
        String vstrUserName = txtFilterUserName.getValue();
        user.setUserName(vstrUserName);
        String vstrFullName = txtFilterFullName.getValue();
        user.setFullName(vstrFullName);
        String vstrEmail = txtFilterEmail.getValue();
        user.setEmail(vstrEmail);
        String vstrPhone = txtFilterPhone.getValue();
        user.setPhone(vstrPhone);
        filter(user);
    }

    public void onChange$txtFilterPhone() {
        Users user = new Users();
        String vstrUserName = txtFilterUserName.getValue();
        user.setUserName(vstrUserName);
        String vstrFullName = txtFilterFullName.getValue();
        user.setFullName(vstrFullName);
        String vstrEmail = txtFilterEmail.getValue();
        user.setEmail(vstrEmail);
        String vstrPhone = txtFilterPhone.getValue();
        user.setPhone(vstrPhone);
        filter(user);
    }

    private void filter(Users user) {
        List<Users> vlstCustomer = new ArrayList<>();
        if (lstUsers != null && !lstUsers.isEmpty() && user != null) {
            if (!StringUtils.isValidString(user.getUserName())
                    && !StringUtils.isValidString(user.getFullName())
                    && !StringUtils.isValidString(user.getEmail())
                    && !StringUtils.isValidString(user.getPhone())) {
                vlstCustomer.addAll(lstUsers);
            } else {
                for (Users c : lstUsers) {
                    //tim theo ma va ten
                    if (StringUtils.isValidString(user.getUserName()) && StringUtils.isValidString(user.getFullName()) && StringUtils.isValidString(user.getEmail()) && StringUtils.isValidString(user.getPhone())) {
                        if ((StringUtils.isValidString(c.getUserName()) && c.getUserName().toLowerCase().contains(user.getUserName().toLowerCase()))
                                && (StringUtils.isValidString(c.getFullName()) && c.getFullName().toLowerCase().contains(user.getFullName().toLowerCase()))
                                && (StringUtils.isValidString(c.getEmail()) && c.getEmail().toLowerCase().contains(user.getEmail().toLowerCase()))
                                && (StringUtils.isValidString(c.getPhone()) && c.getPhone().toLowerCase().contains(user.getPhone().toLowerCase()))) {
                            vlstCustomer.add(c);
                        }
                    } //tim theo user
                    else if (StringUtils.isValidString(user.getUserName()) && !StringUtils.isValidString(user.getEmail())
                            && !StringUtils.isValidString(user.getFullName())
                            && !StringUtils.isValidString(user.getPhone())) {
                        if (StringUtils.isValidString(c.getUserName()) && c.getUserName().toLowerCase().contains(user.getUserName().toLowerCase())) {
                            vlstCustomer.add(c);
                        }
                    } //tim theo ful name
                    else if (!StringUtils.isValidString(user.getUserName()) && StringUtils.isValidString(user.getFullName())
                            && !StringUtils.isValidString(user.getEmail()) && !StringUtils.isValidString(user.getPhone())) {
                        if (StringUtils.isValidString(c.getFullName()) && c.getFullName().toLowerCase().contains(user.getFullName().toLowerCase())) {
                            vlstCustomer.add(c);
                        }
                    } //tim theo email
                    else if (!StringUtils.isValidString(user.getUserName()) && StringUtils.isValidString(user.getEmail())
                            && !StringUtils.isValidString(user.getFullName()) && !StringUtils.isValidString(user.getPhone())) {
                        if (StringUtils.isValidString(c.getEmail()) && c.getEmail().toLowerCase().contains(user.getEmail().toLowerCase())) {
                            vlstCustomer.add(c);
                        }
                    } else if (!StringUtils.isValidString(user.getUserName()) && !StringUtils.isValidString(user.getEmail())
                            && !StringUtils.isValidString(user.getFullName()) && StringUtils.isValidString(user.getPhone())) {
                        if (StringUtils.isValidString(c.getPhone()) && c.getPhone().toLowerCase().contains(user.getPhone().toLowerCase())) {
                            vlstCustomer.add(c);
                        }
                    }
                }
            }
        }
        listDataModel = new ListModelList(vlstCustomer);
        lstUser.setModel(listDataModel);

    }

    /**
     * Reset passowrd user
     *
     * @param event
     */
    public void onResetPassword(ForwardEvent event) {
        String vstrPassword = RandomStringUtils.random(8, Constants.RESET_RANDOM_PASSWORD);
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        Users c = rowSelected.getValue();
        Users user = getDataInRow(lstCell);
        user.setUserId(c.getUserId());
        user.setPassword(EncryptUtil.encrypt(vstrPassword));
//        userService.insertOrUpdateUser(user);
        Messagebox.show(Labels.getLabel("login.change.password.content.message", new String[]{vstrPassword}), Labels.getLabel("login.change.password.title.message"), Messagebox.OK, Messagebox.INFORMATION);
    }
}
