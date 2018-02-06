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
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.dvd.ckp.business.service.LocationServices;
import com.dvd.ckp.business.service.UtilsService;
import com.dvd.ckp.common.Constants;
import com.dvd.ckp.component.MyListModel;
import com.dvd.ckp.domain.Location;
import com.dvd.ckp.domain.Param;
import com.dvd.ckp.excel.ExcelReader;
import com.dvd.ckp.excel.ExcelWriter;
import com.dvd.ckp.excel.domain.LocationExcel;
import com.dvd.ckp.utils.FileUtils;
import com.dvd.ckp.utils.NumberUtils;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;
import com.dvd.ckp.utils.StyleUtils;

/**
 *
 * @author viettx
 */
public class LocationController extends GenericForwardComposer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(LocationController.class);
    @WireVariable
    protected LocationServices locationServices;
    @WireVariable
    protected UtilsService utilsService;
    @Wire
    private Grid gridLocation;
    @Wire
    private Combobox cbFilterName;
    MyListModel<Location> listDataLocation;
    ListModelList<Location> listDataModel;
    private List<Location> lstLocation;
    private List<Location> lstFilter;
    private int insertOrUpdate = 0;

    // Danh sach loai vi tri
    private List<Param> lstTypeLocation;
    @Wire
    private Window location;

    private static final String SAVE_PATH = "/Location/";

    private Label linkFileName;
    private Textbox hiddenFileName;
    private Textbox hdFileName;

    public Textbox txtTotalRow;
    public Textbox txtTotalRowSucces;
    public Textbox txtTotalRowError;

    private List<LocationExcel> lstError = new ArrayList<LocationExcel>();
    public Button errorList;

    private Param defaultParam;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        lstFilter = new ArrayList<>();
        locationServices = (LocationServices) SpringUtil.getBean(SpringConstant.LOCATION_SERVICES);
        utilsService = (UtilsService) SpringUtil.getBean(SpringConstant.UTILS_SERVICES);
        // danh sach loai vi tri
        lstTypeLocation = utilsService.getParamByKey(com.dvd.ckp.utils.Constants.PRAM_LOCATION_TYPE);

        lstLocation = new ArrayList<>(Memory.lstLocationCache.values());
//        List<Location> vlstData = locationServices.getListLocation();
        if (lstLocation != null) {
//            lstLocation.addAll(vlstData);
            lstFilter.addAll(lstLocation);
        }
        listDataLocation = new MyListModel(lstLocation);
        cbFilterName.setModel(listDataLocation);
        // pump type default
        defaultParam = new Param();
        defaultParam.setParamValue(-1l);
        defaultParam.setParamName(Labels.getLabel("option"));
        lstTypeLocation.add(0, defaultParam);
        listDataModel = new ListModelList<>(lstLocation);
        gridLocation.setModel(listDataModel);
        setDataDefaultInGridViewDetail();
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
                    Location c = rowSelected.getValue();
                    Location location = getDataInRow(lstCell);
                    location.setLocationID(c.getLocationID());
                    location.setStatus(0);
                    locationServices.detele(location);
                    lstFilter.remove(getIndexLocationFilter(c.getLocationID()));
                    lstLocation.remove(getIndexLocation(c.getLocationID()));
                    StyleUtils.setDisableComponent(lstCell, 4);
                    reloadGrid(1);
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
        reloadGrid(0);
    }

    // get pump type default
    private List<Param> getLocationTypeDefault(Long locationTypeId) {
        List<Param> paramSelected = new ArrayList<>();
        if (locationTypeId != null && lstTypeLocation != null && !lstTypeLocation.isEmpty()) {
            for (Param param : lstTypeLocation) {
                if (locationTypeId.equals(param.getParamValue())) {
                    paramSelected.add(param);
                    break;
                }
            }
        }
        if (paramSelected.isEmpty()) {
            paramSelected.add(defaultParam);
        }
        return paramSelected;
    }

    /**
     * set data for combobox pump type in windown bill detail
     *
     * @param lstCell
     * @param selectedIndex
     * @param columnIndex
     */
    private void setDataLocationTypeDetail(List<Component> lstCell, List<Param> selectedIndex, int columnIndex) {
        Combobox combobox = null;
        Component component = lstCell.get(columnIndex).getFirstChild();
        if (component != null && component instanceof Combobox) {
            combobox = (Combobox) component;
            MyListModel listDataModelLocation = new MyListModel(lstTypeLocation);
            listDataModelLocation.setSelection(selectedIndex);
            combobox.setModel(listDataModelLocation);

        }

    }

    private void setDataDefaultInGridViewDetail() {
        gridLocation.renderAll();
        List<Component> lstRows = gridLocation.getRows().getChildren();
        if (lstRows != null && !lstRows.isEmpty()) {
            for (int i = 0; i < lstRows.size(); i++) {
                Location location = listDataModel.get(i);
                Component row = lstRows.get(i);
                List<Component> lstCell = row.getChildren();
                setDataLocationTypeDetail(lstCell, getLocationTypeDefault(location.getLocationType()), 4);
            }
        }
    }

    /**
     * Save
     *
     * @param event
     */
    public void onSave(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        Location c = rowSelected.getValue();
        Location location = getDataInRow(lstCell);
        location.setLocationID(c.getLocationID());
        if (insertOrUpdate == 1) {
            locationServices.save(location);
            lstFilter.add(location);
            lstLocation.add(location);
        } else {
            location.setCreateDate(new Date());
            locationServices.update(location);
        }
        StyleUtils.setDisableComponent(lstCell, 4);
        reloadGrid(1);
        insertOrUpdate = 0;
    }

    /**
     * Add row
     */
    public void onAdd(ForwardEvent event) {
        Location locationAddItem = new Location();
        locationAddItem.setStatus(1);
        listDataModel.add(0, locationAddItem);
        gridLocation.setActivePage(com.dvd.ckp.utils.Constants.FIRST_INDEX);
        gridLocation.setModel(listDataModel);
        gridLocation.renderAll();
        List<Component> lstCell = gridLocation.getRows().getChildren().get(0).getChildren();
        StyleUtils.setEnableComponent(lstCell, 4);
        insertOrUpdate = 1;
        setDataDefaultInGridViewDetail();
    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private Location getDataInRow(List<Component> lstCell) {
        Location location = new Location();
        Textbox txtLocationCode = (Textbox) lstCell.get(1).getFirstChild();
        Textbox txtLocationName = (Textbox) lstCell.get(2).getFirstChild();
        Longbox txtLocationValue = (Longbox) lstCell.get(3).getFirstChild();
        Combobox txtLocationType = (Combobox) lstCell.get(4).getFirstChild();

        location.setLocationCode(txtLocationCode.getValue());
        location.setLocationName(txtLocationName.getValue());
        location.setLocationValue(txtLocationValue.getValue());
        location.setLocationType(txtLocationType.getSelectedItem().getValue());
        location.setStatus(1);
        return location;
    }

    /**
     * Reload grid
     */
    private void reloadGrid(int loadCache) {
        if (loadCache == 1) {
            Memory.loadLocation();
        }
        List<Location> vlstData = new ArrayList<>(Memory.lstLocationCache.values());
        if (vlstData != null && !vlstData.isEmpty()) {
            listDataModel = new ListModelList(vlstData);
            listDataLocation = new MyListModel(vlstData);
        } else {
            listDataModel = new ListModelList(new ArrayList<>());
            listDataLocation = new MyListModel(new ArrayList<>());
        }

        gridLocation.setModel(listDataModel);

        cbFilterName.setModel(listDataLocation);

        setDataDefaultInGridViewDetail();
    }

    public void onChange$cbFilterName() {
        Location location = new Location();
        Long locationID = null;
        if (cbFilterName.getSelectedItem() != null) {
            locationID = cbFilterName.getSelectedItem().getValue();
        }
        location.setLocationID(locationID);

        filter(location);
    }

    private void filter(Location location) {
        int index = 0;
        List<Location> vlstData = new ArrayList<>();
        if (location.getLocationID() == null) {
            vlstData.addAll(lstLocation);
            lstFilter.clear();
            lstFilter.addAll(lstLocation);
        } else {
            if (lstLocation != null && !lstLocation.isEmpty()) {
                for (Location item : lstLocation) {
                    index++;
                    item.setIndex(index);
                    if (location.getLocationID() != null && location.getLocationID().equals(item.getLocationID())) {
                        vlstData.add(item);
                        lstFilter.clear();
                        lstFilter.add(item);
                    }
                }
            }
        }

        listDataModel = new ListModelList<Location>(vlstData);
        gridLocation.setModel(listDataModel);
    }

    public void onExport(ForwardEvent event) {

        ExcelWriter<Location> excelWriter = new ExcelWriter<Location>();
        try {
            String pathFileInput = Constants.PATH_FILE + "file/template/export/location_data_export.xlsx";
            String pathFileOut = Constants.PATH_FILE + "file/export/location_data_export.xlsx";
            excelWriter.write(lstFilter, pathFileInput, pathFileOut);
            File file = new File(pathFileOut);
            Filedownload.save(file, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }

    }

    public void onImport(ForwardEvent event) {

        final Window windownUpload = (Window) Executions.createComponents("/manager/uploadLocation.zul", location,
                null);
        windownUpload.doModal();
        windownUpload.setBorder(true);
        windownUpload.setBorder("normal");
        windownUpload.setClosable(true);
        windownUpload.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

            @Override
            public void onEvent(Event event) throws Exception {
                reloadGrid(1);

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
        int numberError = 0;
        lstError.clear();
        try {
            ExcelReader<LocationExcel> reader = new ExcelReader<>();
            String filePath = hiddenFileName.getValue();
            List<LocationExcel> listData = reader.read(filePath, LocationExcel.class);
            List<Location> vlstData = new ArrayList<>();
            if (listData != null && !listData.isEmpty()) {
                for (LocationExcel locationExcel : listData) {

                    if (!StringUtils.isValidString(locationExcel.getLocationCode())) {
                        numberError++;
                        locationExcel.setIndex(numberError);
                        locationExcel.setDescription(
                                Labels.getLabel("pump.not.empty", new String[]{Labels.getLabel("location.code")}));
                        lstError.add(locationExcel);
                        continue;
                    }

                    if (!StringUtils.isValidString(locationExcel.getLocationName())) {
                        numberError++;
                        locationExcel.setIndex(numberError);
                        locationExcel.setDescription(
                                Labels.getLabel("pump.not.empty", new String[]{Labels.getLabel("location.name")}));
                        lstError.add(locationExcel);
                        continue;
                    }

                    if (!StringUtils.isValidString(locationExcel.getLocationType())) {
                        numberError++;
                        locationExcel.setIndex(numberError);
                        locationExcel.setDescription(
                                Labels.getLabel("pump.not.empty", new String[]{Labels.getLabel("location.value")}));
                        lstError.add(locationExcel);
                        continue;
                    }
                    if (!NumberUtils.isNumber(locationExcel.getLocationType())) {
                        locationExcel.setIndex(numberError);
                        locationExcel.setDescription(
                                Labels.getLabel("pump.not.number", new String[]{Labels.getLabel("location.value")}));
                        lstError.add(locationExcel);
                        continue;
                    }
                    Location item = new Location();
                    item.setLocationCode(locationExcel.getLocationCode());
                    item.setLocationName(locationExcel.getLocationName());
                    item.setLocationType(Long.valueOf(locationExcel.getLocationType()));
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
            locationServices.importData(vlstData);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void onDownloadFile(ForwardEvent event) {
        try {
            String pathFileInput = Constants.PATH_FILE + "file/template/import/import_location_data.xlsx";

            File file = new File(pathFileInput);
            Filedownload.save(file, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }
    }

    public void onDownloadFileError(ForwardEvent event) {
        ExcelWriter<LocationExcel> writer = new ExcelWriter<>();
        try {
            String pathFileOutput = Constants.PATH_FILE + "file/export/error/error_location_data.xlsx";
            String pathFileInput = Constants.PATH_FILE + "file/template/error/error_location_data.xlsx";

            writer.write(lstError, pathFileInput, pathFileOutput);
            File file = new File(pathFileOutput);
            Filedownload.save(file, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }
    }

    private int getIndexLocation(Long locationID) {
        if (lstLocation != null && !lstLocation.isEmpty()) {
            for (Location location : lstLocation) {
                if (locationID.equals(location.getLocationID())) {
                    return lstLocation.indexOf(location);
                }
            }
        }
        return -1;
    }

    private int getIndexLocationFilter(Long locationID) {
        if (lstLocation != null && !lstLocation.isEmpty()) {
            for (Location location : lstLocation) {
                if (locationID.equals(location.getLocationID())) {
                    return lstLocation.indexOf(location);
                }
            }
        }
        return -1;
    }
}
