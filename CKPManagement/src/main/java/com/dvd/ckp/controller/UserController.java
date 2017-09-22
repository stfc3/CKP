/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.UserService;
import com.dvd.ckp.domain.Pumps;
import com.dvd.ckp.domain.User;
import com.dvd.ckp.excel.ExcelReader;
import com.dvd.ckp.excel.ExcelWriter;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.EncryptUtil;
import com.dvd.ckp.utils.FileUtils;
import com.dvd.ckp.utils.NumberUtils;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.A;
import org.zkoss.zul.Cell;
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
    @Wire
    private Textbox txtFilterUserName;
    @Wire
    private Textbox txtFilterFullName;
    @Wire
    private Textbox txtFilterEmail;
    @Wire
    private Textbox txtFilterPhone;
    ListModelList<User> listDataModel;
    private List<User> lstUsers;
    private Window user;
//    List<com.dvd.ckp.excel.domain> lstError = new ArrayList<com.dvd.ckp.excel.domain.Pumps>();

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
        User c = rowSelected.getValue();
        User user = getDataInRow(lstCell);
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
        User user = new User();
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
    private User getDataInRow(List<Component> lstCell) {
        User user = new User();
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
        List<User> vlstUser = userService.getAllUser();
        listDataModel = new ListModelList(vlstUser);
        lstUser.setModel(listDataModel);
    }

    public void onChange$txtFilterUserName() {
        User user = new User();
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
        User user = new User();
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
        User user = new User();
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
        User user = new User();
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

    private void filter(User user) {
        List<User> vlstCustomer = new ArrayList<>();
        if (lstUsers != null && !lstUsers.isEmpty() && user != null) {
            if (!StringUtils.isValidString(user.getUserName())
                    && !StringUtils.isValidString(user.getFullName())
                    && !StringUtils.isValidString(user.getEmail())
                    && !StringUtils.isValidString(user.getPhone())) {
                vlstCustomer.addAll(lstUsers);
            } else {
                for (User c : lstUsers) {
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
