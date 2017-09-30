/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.UserService;
import com.dvd.ckp.domain.User;
import com.dvd.ckp.excel.ExcelWriter;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.EncryptUtil;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;
import com.dvd.ckp.utils.StyleUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

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
//    @Wire
//    private Textbox txtFilterUserName;
//    @Wire
//    private Textbox txtFilterFullName;
//    @Wire
//    private Textbox txtFilterEmail;
    @Wire
    private Textbox txtFilterPhone;
    ListModelList<User> listDataModel;
    private List<User> lstUsers;
    private Window user;
    private boolean blnAddOrEdit = false;

    @Wire
    private Combobox cbxUserFilter;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        userService = (UserService) SpringUtil.getBean(SpringConstant.USER_SERVICES);
        lstUsers = new ArrayList<>();
        List<User> vlstUser = userService.getAllUser();
        if (vlstUser != null) {
            lstUsers.addAll(vlstUser);
        }
        listDataModel = new ListModelList(lstUsers);
        lstUser.setModel(listDataModel);
        cbxUserFilter.setModel(listDataModel);
    }

    /**
     * Edit row
     *
     * @param event
     */
    public void onEdit(ForwardEvent event) {
        blnAddOrEdit = false;
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        StyleUtils.setEnableComponent(lstCell, 5);
    }

    /**
     * Cancel
     *
     * @param event
     */
    public void onCancel(ForwardEvent event) {
        blnAddOrEdit = false;
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        StyleUtils.setDisableComponent(lstCell, 5);
        reloadGrid();

    }

    /**
     * Save
     *
     * @param event
     */
    public boolean onSave(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        User c = rowSelected.getValue();
        User user = getDataInRow(lstCell);
        user.setUserId(c.getUserId());
        String vstrPassword = "";
        if (blnAddOrEdit) {
            vstrPassword = RandomStringUtils.random(8, Constants.RESET_RANDOM_PASSWORD);
            user.setPassword(EncryptUtil.encrypt(vstrPassword));
        } else {
            user.setPassword(c.getPassword());
        }
        if (blnAddOrEdit) {
            for (User u : lstUsers) {
                if (u.getUserName().equalsIgnoreCase(user.getUserName())) {
                    Messagebox.show(Labels.getLabel("user.username.message.error", new String[]{u.getUserName()}), Labels.getLabel("login.change.password.title.message"), Messagebox.OK, Messagebox.INFORMATION);
                    return false;
                }
            }
        }
        user.setStatus(1);
        user.setCreateDate(new Date());
        userService.insertOrUpdateUser(user);
        StyleUtils.setDisableComponent(lstCell, 5);
        reloadGrid();
        if (blnAddOrEdit) {
            Messagebox.show(Labels.getLabel("user.add.password.message", new String[]{vstrPassword}), Labels.getLabel("login.change.password.title.message"), Messagebox.OK, Messagebox.INFORMATION);
        }
        return true;
    }

    /**
     * Delate
     *
     * @param event
     */
    public void onDelete(ForwardEvent event) {
        Messagebox.show("Bạn có chắc chắn muốn xóa không?", "Xác nhận", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
            public void onEvent(Event e) {
                if (Messagebox.ON_YES.equals(e.getName())) {
                    Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
                    User c = rowSelected.getValue();
                    c.setStatus(Constants.STATUS_INACTIVE);
                    userService.insertOrUpdateUser(c);
                    reloadGrid();
                }
            }
        });
    }

    /**
     * Add row
     */
    public void onClick$add() {
        blnAddOrEdit = true;
        User user = new User();
        listDataModel.add(0, user);
        lstUser.setModel(listDataModel);
        lstUser.renderAll();
        List<Component> lstCell = lstUser.getRows().getChildren().get(0).getChildren();
        StyleUtils.setEnableComponent(lstCell, 5);
    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private User getDataInRow(List<Component> lstCell) {
        User user = new User();
        Textbox txtUserName = (Textbox) lstCell.get(1).getFirstChild();
        Textbox txtFullName = (Textbox) lstCell.get(2).getFirstChild();
        Textbox txtEmail = (Textbox) lstCell.get(3).getFirstChild();
        Textbox txtPhone = (Textbox) lstCell.get(4).getFirstChild();
        Textbox txtAddress = (Textbox) lstCell.get(5).getFirstChild();
        Textbox txtCard = (Textbox) lstCell.get(6).getFirstChild();

        user.setUserName(txtUserName.getValue());
        user.setFullName(txtFullName.getValue());
        user.setEmail(txtEmail.getValue());
        user.setPhone(txtPhone.getValue());
        user.setAddress(txtAddress.getValue());
        user.setCard(txtCard.getValue());
        return user;
    }

    /**
     * Reload grid
     */
    private void reloadGrid() {
        List<User> vlstUser = userService.getAllUser();
        listDataModel = new ListModelList(vlstUser);
        lstUser.setModel(listDataModel);
        cbxUserFilter.setModel(listDataModel);
    }

    public void onSelect$cbxUserFilter() {
        Long vstrUserId = null;
        if (cbxUserFilter.getSelectedItem() != null) {
            vstrUserId = cbxUserFilter.getSelectedItem().getValue();
        }
        User user = new User();
        String vstrPhone = txtFilterPhone.getValue();
        user.setPhone(vstrPhone);
        user.setUserId(vstrUserId);
        filter(user);
    }

    public void onChange$txtFilterPhone() {
        Long vstrUserId = null;
        if (cbxUserFilter.getSelectedItem() != null) {
            vstrUserId = cbxUserFilter.getSelectedItem().getValue();
        }
        User user = new User();
        String vstrPhone = txtFilterPhone.getValue();
        user.setPhone(vstrPhone);
        user.setUserId(vstrUserId);
        filter(user);
    }

    private void filter(User user) {
        List<User> vlstCustomer = new ArrayList<>();
        if (lstUsers != null && !lstUsers.isEmpty() && user != null) {
            if ((Constants.DEFAULT_ID.equals(user.getUserId()) || user.getUserId() == null) && user.getPhone().equalsIgnoreCase("")) {
                vlstCustomer.addAll(lstUsers);
            } else {
                for (User c : lstUsers) {
                    if ((!Constants.DEFAULT_ID.equals(user.getUserId()) && user.getUserId() != null) && !user.getPhone().equalsIgnoreCase("")) {
                        if (user.getUserId().equals(c.getUserId()) && c.getPhone().toLowerCase().contains(user.getPhone().toLowerCase())) {
                            vlstCustomer.add(c);
                        }
                    }
                    else if ((!Constants.DEFAULT_ID.equals(user.getUserId()) || user.getUserId() != null) && user.getPhone().equalsIgnoreCase("")) {
                        if (user.getUserId().equals(c.getUserId())) {
                            vlstCustomer.add(c);
                        }
                    } else if ((Constants.DEFAULT_ID.equals(user.getUserId()) || user.getUserId() == null) && !user.getPhone().equalsIgnoreCase("")) {
                        if (c.getPhone().toLowerCase().contains(user.getPhone().toLowerCase())) {
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
        User c = rowSelected.getValue();
        User user = getDataInRow(lstCell);
        user.setUserId(c.getUserId());
        user.setPassword(EncryptUtil.encrypt(vstrPassword));
        userService.insertOrUpdateUser(user);
        Messagebox.show(Labels.getLabel("login.change.password.content.message", new String[]{vstrPassword}), Labels.getLabel("login.change.password.title.message"), Messagebox.OK, Messagebox.INFORMATION);
    }

    public void onClick$btnExport(Event event) {
        ExcelWriter<User> excelWriter = new ExcelWriter<User>();
        try {
            int index = 0;
            for (User user : lstUsers) {
                index++;
                user.setIndex(index);
            }
            String pathFileInput = com.dvd.ckp.common.Constants.PATH_FILE + "file/template/export/bills_data_export.xlsx";
            String pathFileOut = com.dvd.ckp.common.Constants.PATH_FILE + "file/export/bills_data_export.xlsx";

            excelWriter.write(lstUsers, pathFileInput, pathFileOut);
            File file = new File(pathFileOut);
            Filedownload.save(file, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }

    }

    public void onImport(ForwardEvent event) {
        final Window windownUpload = (Window) Executions.createComponents("/manager/uploadPumps.zul", user, null);
        windownUpload.doModal();
        windownUpload.setBorder(true);
        windownUpload.setBorder("normal");
        windownUpload.setClosable(true);
        windownUpload.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

            @Override
            public void onEvent(Event event) throws Exception {
                reloadGrid();

            }
        });
    }

    public void onDownloadFile(ForwardEvent event) {
        try {
            String pathFileInput = com.dvd.ckp.common.Constants.PATH_FILE + "file/template/import/import_pump_data.xlsx";

            File file = new File(pathFileInput);
            Filedownload.save(file, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }
    }

//    public void onDownloadFileError(ForwardEvent event) {
//        ExcelWriter<com.dvd.ckp.excel.domain.Pumps> writer = new ExcelWriter<>();
//        try {
//            String pathFileOutput = com.dvd.ckp.common.Constants.PATH_FILE + "file/export/error/error_pumps_data.xlsx";
//            String pathFileInput = com.dvd.ckp.common.Constants.PATH_FILE + "file/template/error/error_pumps_data.xlsx";
//
//            writer.write(lstError, pathFileInput, pathFileOutput);
//            File file = new File(pathFileOutput);
//            Filedownload.save(file, null);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            logger.error(e.getMessage(), e);
//        }
//    }
//    public void onUpload$uploadbtn(UploadEvent evt) {
//        Media media = evt.getMedia();
//
//        if (media == null) {
//            Messagebox.show(Labels.getLabel("uploadExcel.selectFile"), Labels.getLabel("ERROR"), Messagebox.OK,
//                    Messagebox.ERROR);
//            return;
//        }
//        final String vstrFileName = media.getName();
//
//        hdFileName.setValue(vstrFileName);
//        linkFileName.setValue(vstrFileName);
//        FileUtils fileUtils = new FileUtils();
//        fileUtils.setSaveFilePath(SAVE_PATH);
//        fileUtils.saveFile(media);
//        hdFileName.setValue(fileUtils.getFileName());
//        hiddenFileName.setValue(fileUtils.getFilePath());
//    }
//    public void onClick$btnSave() {
//        int numberSucces = 0;
//        int numberRowError = 1;
//        lstError.clear();
//        try {
//            ExcelReader<com.dvd.ckp.excel.domain.Pumps> reader = new ExcelReader<>();
//            String filePath = hiddenFileName.getValue();
//            List<com.dvd.ckp.excel.domain.Pumps> listData = reader.read(filePath, com.dvd.ckp.excel.domain.Pumps.class);
//            List<Pumps> vlstData = new ArrayList<>();
//            if (listData != null && !listData.isEmpty()) {
//                for (com.dvd.ckp.excel.domain.Pumps pumps : listData) {
//
//                    if (!NumberUtils.isNumber(pumps.getPumpsCapacity())) {
//                        pumps.setDescription(
//                                Labels.getLabel("pump.not.number", new String[]{Labels.getLabel("pump.capacity")}));
//                        pumps.setIndex(numberRowError);
//                        lstError.add(pumps);
//                        numberRowError++;
//                        continue;
//                    }
//
//                    if (!NumberUtils.isNumber(pumps.getPumpsHight())) {
//                        pumps.setDescription(
//                                Labels.getLabel("pump.not.number", new String[]{Labels.getLabel("pump.hight")}));
//                        pumps.setIndex(numberRowError);
//                        lstError.add(pumps);
//                        numberRowError++;
//                        continue;
//                    }
//
//                    if (!NumberUtils.isNumber(pumps.getPumpsFar())) {
//                        pumps.setDescription(
//                                Labels.getLabel("pump.not.number", new String[]{Labels.getLabel("pump.far")}));
//                        pumps.setIndex(numberRowError);
//                        lstError.add(pumps);
//                        numberRowError++;
//                        continue;
//                    }
//                    Pumps item = new Pumps();
//                    item.setPumpsCode(pumps.getPumpsCode());
//                    item.setPumpsName(pumps.getPumpsName());
//                    item.setPumpsCapacity(Integer.valueOf(pumps.getPumpsCapacity()));
//                    item.setPumpsHight(Integer.valueOf(pumps.getPumpsHight()));
//                    item.setPumpsFar(Integer.valueOf(pumps.getPumpsFar()));
//                    item.setStatus(1);
//                    vlstData.add(item);
//                    numberSucces++;
//                }
//            }
//            txtTotalRow.setValue(String.valueOf(vlstData.size()));
//            txtTotalRowSucces.setValue(String.valueOf(numberSucces));
//
//            if (lstError != null && !lstError.isEmpty()) {
//                errorList.setVisible(true);
//                txtTotalRowError.setValue(String.valueOf(lstError.size()));
//            }
//            pumpsService.importData(vlstData);
//
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//    }
}
