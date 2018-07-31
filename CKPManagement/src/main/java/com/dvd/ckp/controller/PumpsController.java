/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.dvd.ckp.business.service.PumpServices;
import com.dvd.ckp.component.MyListModel;
import com.dvd.ckp.domain.Pumps;
import com.dvd.ckp.excel.ExcelReader;
import com.dvd.ckp.excel.ExcelWriter;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.FileUtils;
import com.dvd.ckp.utils.NumberUtils;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StyleUtils;

/**
 *
 * @author viettx
 */
public class PumpsController extends GenericForwardComposer {

    private static final Logger logger = Logger.getLogger(PumpsController.class);
    @WireVariable
    protected PumpServices pumpsService;
    @Wire
    private Grid gridPumps;
    @Wire
    private Combobox cbFilterName;
    MyListModel<Pumps> listDataPump;
    ListModelList<Pumps> listDataModel;
    private List<Pumps> lstPumps;
    private List<Pumps> lstPumpsFilter;
    private int insertOrUpdate = 0;

    private Window pumps;

    private static final String SAVE_PATH = "/Pumps/";

    private Label linkFileName;
    private Textbox hiddenFileName;
    private Textbox hdFileName;

    public Textbox txtTotalRow;
    public Textbox txtTotalRowSucces;
    public Textbox txtTotalRowError;

    public Button btnCancel;
    public Button errorList;
    // public Window uploadPump;

    List<com.dvd.ckp.excel.domain.Pumps> lstError = new ArrayList<com.dvd.ckp.excel.domain.Pumps>();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        lstPumpsFilter = new ArrayList<Pumps>();
        pumpsService = (PumpServices) SpringUtil.getBean(SpringConstant.PUMPS_SERVICES);

        lstPumps = new ArrayList<>();
        List<Pumps> vlstPumps = pumpsService.getAllListData();
        if (vlstPumps != null) {
            lstPumps.addAll(vlstPumps);
            lstPumpsFilter.addAll(vlstPumps);
        }
        listDataModel = new ListModelList<>(lstPumps);
        gridPumps.setModel(listDataModel);
        listDataPump = new MyListModel<>(lstPumps);
        cbFilterName.setModel(listDataPump);

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
                    Pumps c = rowSelected.getValue();
//                    Pumps pumps = getDataInRow(lstCell);
//                    pumps.setPumpsID(c.getPumpsID());
                    c.setStatus(0);
                    pumpsService.detele(c);
                    lstPumpsFilter.remove(getIndexPumpFilter(c.getPumpsID()));
                    lstPumps.remove(getIndexPump(c.getPumpsID()));
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
        Pumps c = rowSelected.getValue();
        Pumps pumps = getDataInRow(lstCell);
        pumps.setPumpsID(c.getPumpsID());
        if (insertOrUpdate == 1) {
            pumpsService.savePumps(pumps);
            lstPumps.add(pumps);
            lstPumpsFilter.add(pumps);
        } else {
            pumps.setCreateDate(new Date());
            pumpsService.update(pumps);
        }
        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGrid();
        insertOrUpdate = 0;
    }

    /**
     * Add row
     */
    public void onAdd(ForwardEvent event) {
        Pumps pumpAddItem = new Pumps();
        pumpAddItem.setStatus(1);
        listDataModel.add(0, pumpAddItem);
        gridPumps.setActivePage(com.dvd.ckp.utils.Constants.FIRST_INDEX);
        gridPumps.setModel(listDataModel);
        gridPumps.renderAll();
        List<Component> lstCell = gridPumps.getRows().getChildren().get(0).getChildren();
        StyleUtils.setEnableComponent(lstCell, 4);
        insertOrUpdate = 1;
    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private Pumps getDataInRow(List<Component> lstCell) {
        Pumps pump = new Pumps();
        Component component;

        Textbox txtPumpsCode = null;
        Textbox txtPumpsName = null;
        Textbox txtPumpsCapacity = null;
        Textbox txtPumpsHight = null;
        Textbox txtPumpsFar = null;

        // may bom
        component = lstCell.get(1).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtPumpsCode = (Textbox) component;
            pump.setPumpsCode(txtPumpsCode.getValue());
        }
        // loai may bom
        component = lstCell.get(2).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtPumpsName = (Textbox) component;
            pump.setPumpsName(txtPumpsName.getValue());
        }
        // vi tri bom
        component = lstCell.get(3).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtPumpsCapacity = (Textbox) component;
            pump.setPumpsCapacity(txtPumpsCapacity.getValue());
        }
        // loai vi tri
        component = lstCell.get(4).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtPumpsHight = (Textbox) component;
            pump.setPumpsHight(txtPumpsHight.getValue());
        }
        // loai vi tri
        component = lstCell.get(5).getFirstChild();
        if (component != null && component instanceof Textbox) {
            txtPumpsFar = (Textbox) component;
            pump.setPumpsFar(txtPumpsFar.getValue());
        }
        pump.setStatus(1);

        return pump;
    }

    /**
     * Reload grid
     */
    private void reloadGrid() {
        List<Pumps> vlstData = new ArrayList<>();
        if (pumpsService.getAllListData() != null && !pumpsService.getAllListData().isEmpty()) {
            vlstData.addAll(pumpsService.getAllListData());
        }
        listDataModel = new ListModelList<>(vlstData);
        gridPumps.setModel(listDataModel);

        listDataPump = new MyListModel<>(vlstData);
        cbFilterName.setModel(listDataPump);
    }

    public void onChange$cbFilterName() {
        Pumps pumps = new Pumps();
        Long pumpID = null;
        if (cbFilterName.getSelectedItem() != null) {
            pumpID = cbFilterName.getSelectedItem().getValue();
        }
        pumps.setPumpsID(pumpID);
        filter(pumps);
    }

    private void filter(Pumps pumps) {
        int index = 0;
        List<Pumps> vlstData = new ArrayList<>();
        if (pumps.getPumpsID() == null) {
            vlstData.addAll(lstPumps);
            lstPumpsFilter.clear();
            lstPumpsFilter.addAll(lstPumps);
        } else {
            if (lstPumps != null && !lstPumps.isEmpty()) {
                for (Pumps item : lstPumps) {
                    index++;
                    pumps.setIndex(index);
                    if (pumps.getPumpsID() != null && pumps.getPumpsID().equals(item.getPumpsID())) {
                        vlstData.add(item);
                        lstPumpsFilter.clear();
                        lstPumpsFilter.add(item);
                    }
                }
            }
        }
        listDataModel = new ListModelList<Pumps>(vlstData);
        gridPumps.setModel(listDataModel);
    }

    public void onExport(ForwardEvent event) {

        ExcelWriter<Pumps> excelWriter = new ExcelWriter<Pumps>();
        try {

            String pathFileInput = Constants.PATH_FILE + "file/template/export/pumps_data_export.xlsx";
            String pathFileOut = Constants.PATH_FILE + "file/export/pumps_data_export.xlsx";

            excelWriter.write(lstPumpsFilter, pathFileInput, pathFileOut);
            File file = new File(pathFileOut);
            Filedownload.save(file, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }

    }

    public void onImport(ForwardEvent event) {

        final Window windownUpload = (Window) Executions.createComponents("/manager/uploadPumps.zul", pumps, null);
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

    public void onUpload$uploadbtn(UploadEvent evt) {
        Media media = evt.getMedia();

        if (media == null) {
            Messagebox.show(Labels.getLabel("uploadExcel.selectFile"), Labels.getLabel("ERROR"), Messagebox.OK,
                    Messagebox.ERROR);
            return;
        }
        final String vstrFileName = media.getName();

        hdFileName.setValue(vstrFileName);
        linkFileName.setValue(vstrFileName);
        FileUtils fileUtils = new FileUtils();
        fileUtils.setSaveFilePath(SAVE_PATH);
        fileUtils.saveFile(media);
        hdFileName.setValue(fileUtils.getFileName());
        hiddenFileName.setValue(fileUtils.getFilePath());
    }

    public void onClick$btnSave() {
        int numberSucces = 0;
        int numberRowError = 1;
        lstError.clear();
        try {
            ExcelReader<com.dvd.ckp.excel.domain.Pumps> reader = new ExcelReader<>();
            String filePath = hiddenFileName.getValue();
            List<com.dvd.ckp.excel.domain.Pumps> listData = reader.read(filePath, com.dvd.ckp.excel.domain.Pumps.class);
            List<Pumps> vlstData = new ArrayList<>();
            if (listData != null && !listData.isEmpty()) {
                for (com.dvd.ckp.excel.domain.Pumps pumps : listData) {

                    if (!NumberUtils.isNumber(pumps.getPumpsCapacity())) {
                        pumps.setDescription(
                                Labels.getLabel("pump.not.number", new String[]{Labels.getLabel("pump.capacity")}));
                        pumps.setIndex(numberRowError);
                        lstError.add(pumps);
                        numberRowError++;
                        continue;
                    }

                    if (!NumberUtils.isNumber(pumps.getPumpsHight())) {
                        pumps.setDescription(
                                Labels.getLabel("pump.not.number", new String[]{Labels.getLabel("pump.hight")}));
                        pumps.setIndex(numberRowError);
                        lstError.add(pumps);
                        numberRowError++;
                        continue;
                    }

                    if (!NumberUtils.isNumber(pumps.getPumpsFar())) {
                        pumps.setDescription(
                                Labels.getLabel("pump.not.number", new String[]{Labels.getLabel("pump.far")}));
                        pumps.setIndex(numberRowError);
                        lstError.add(pumps);
                        numberRowError++;
                        continue;
                    }
                    Pumps item = new Pumps();
                    item.setPumpsCode(pumps.getPumpsCode());
                    item.setPumpsName(pumps.getPumpsName());
                    item.setPumpsCapacity(pumps.getPumpsCapacity());
                    item.setPumpsHight(pumps.getPumpsHight());
                    item.setPumpsFar(pumps.getPumpsFar());
                    item.setStatus(1);
                    vlstData.add(item);
                    numberSucces++;
                }
            }
            txtTotalRow.setValue(String.valueOf(vlstData.size()));
            txtTotalRowSucces.setValue(String.valueOf(numberSucces));

            if (lstError != null && !lstError.isEmpty()) {
                errorList.setVisible(true);
                txtTotalRowError.setValue(String.valueOf(lstError.size()));
            }
            pumpsService.importData(vlstData);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void onDownloadFile(ForwardEvent event) {
        try {
            String pathFileInput = Constants.PATH_FILE + "file/template/import/import_pump_data.xlsx";

            File file = new File(pathFileInput);
            Filedownload.save(file, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }
    }

    public void onDownloadFileError(ForwardEvent event) {
        ExcelWriter<com.dvd.ckp.excel.domain.Pumps> writer = new ExcelWriter<>();
        try {
            String pathFileOutput = Constants.PATH_FILE + "file/export/error/error_pumps_data.xlsx";
            String pathFileInput = Constants.PATH_FILE + "file/template/error/error_pumps_data.xlsx";

            writer.write(lstError, pathFileInput, pathFileOutput);
            File file = new File(pathFileOutput);
            Filedownload.save(file, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }
    }

    private int getIndexPump(Long pumpID) {
        if (lstPumps != null && !lstPumps.isEmpty()) {
            for (Pumps pump : lstPumps) {
                if (pumpID.equals(pump.getPumpsID())) {
                    return lstPumps.indexOf(pump);
                }
            }
        }
        return -1;
    }

    private int getIndexPumpFilter(Long pumpID) {
        if (lstPumpsFilter != null && !lstPumpsFilter.isEmpty()) {
            for (Pumps pump : lstPumpsFilter) {
                if (pumpID.equals(pump.getPumpsID())) {
                    return lstPumpsFilter.indexOf(pump);
                }
            }
        }
        return -1;
    }

}
