/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import java.io.File;
import java.util.ArrayList;
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
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.dvd.ckp.business.service.LocationServices;
import com.dvd.ckp.common.Constants;
import com.dvd.ckp.domain.Location;
import com.dvd.ckp.excel.ExcelReader;
import com.dvd.ckp.excel.ExcelWriter;
import com.dvd.ckp.excel.domain.LocationExcel;
import com.dvd.ckp.utils.FileUtils;
import com.dvd.ckp.utils.NumberUtils;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;

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
	@Wire
	private Grid gridLocation;
	@Wire
	private Textbox txtFilterCode;
	@Wire
	private Textbox txtFilterName;
	ListModelList<Location> listDataModel;
	private List<Location> lstLocation;
	private List<Location> lstFilter;
	public Button btnExport;
	public Button btnImport;
	private int insertOrUpdate = 0;

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

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		lstFilter = new ArrayList<Location>();
		locationServices = (LocationServices) SpringUtil.getBean(SpringConstant.LOCATION_SERVICES);

		lstLocation = new ArrayList<>();
		List<Location> vlstData = locationServices.getListLocation();
		if (vlstData != null) {
			lstLocation.addAll(vlstData);
			lstFilter.addAll(vlstData);
		}
		listDataModel = new ListModelList<Location>(lstLocation);
		gridLocation.setModel(listDataModel);
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

	public void onDelete(ForwardEvent event) {
		Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
		List<Component> lstCell = rowSelected.getChildren();
		Location c = rowSelected.getValue();
		Location location = getDataInRow(lstCell);
		location.setLocationID(c.getLocationID());
		location.setStatus(3);
		lstFilter.remove(location);
		locationServices.detele(location);
		setDisableComponent(lstCell);
		reloadGrid();
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
						A save = (A) c.getChildren().get(1);
						A cancel = (A) c.getChildren().get(2);

						edit.setVisible(false);
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
		Location c = rowSelected.getValue();
		Location location = getDataInRow(lstCell);
		location.setLocationID(c.getLocationID());
		if (insertOrUpdate == 1) {
			lstFilter.add(location);
			locationServices.save(location);
		} else {
			locationServices.update(location);
		}
		setDisableComponent(lstCell);
		reloadGrid();
		insertOrUpdate = 0;
	}

	/**
	 * Add row
	 */
	public void onClick$add() {
		Location locationAddItem = new Location();
		locationAddItem.setStatus(1);
		listDataModel.add(0, locationAddItem);
		gridLocation.setModel(listDataModel);
		gridLocation.renderAll();
		List<Component> lstCell = gridLocation.getRows().getChildren().get(0).getChildren();
		setEnableComponent(lstCell);
		insertOrUpdate = 1;
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
		Textbox txtLocationType = (Textbox) lstCell.get(3).getFirstChild();
		Combobox cbxStatus = (Combobox) lstCell.get(4).getFirstChild();
		location.setLocationCode(txtLocationCode.getValue());
		location.setLocationName(txtLocationName.getValue());
		location.setLocationType(Integer.valueOf(txtLocationType.getValue()));
		location.setStatus(Integer.valueOf(cbxStatus.getSelectedItem().getValue()));
		return location;
	}

	/**
	 * Reload grid
	 */
	private void reloadGrid() {
		List<Location> vlstData = new ArrayList<Location>();
		if (locationServices.getListLocation() != null && !locationServices.getListLocation().isEmpty()) {
			vlstData.addAll(locationServices.getListLocation());
		}
		listDataModel = new ListModelList<Location>(vlstData);
		gridLocation.setModel(listDataModel);
	}

	public void onChange$txtFilterCode() {
		Location location = new Location();
		String vstrLocationCode = txtFilterCode.getValue();
		location.setLocationCode(vstrLocationCode);
		String vstrLocationName = txtFilterName.getValue();
		location.setLocationName(vstrLocationName);
		filter(location);
	}

	public void onChange$txtFilterName() {
		Location location = new Location();
		String vstrLocationCode = txtFilterCode.getValue();
		location.setLocationCode(vstrLocationCode);
		String vstrLocationName = txtFilterName.getValue();
		location.setLocationName(vstrLocationName);
		filter(location);
	}

	private void filter(Location location) {
		int index = 0;
		List<Location> vlstData = new ArrayList<>();
		if (lstLocation != null && !lstLocation.isEmpty()) {
			for (Location item : lstLocation) {
				index++;
				item.setIndex(index);
				if (StringUtils.isValidString(location.getLocationCode())
						&& item.getLocationCode().toLowerCase().contains(location.getLocationCode().toLowerCase())) {
					vlstData.add(item);
					lstFilter.clear();
					lstFilter.add(item);
				} else if (StringUtils.isValidString(location.getLocationName())
						&& item.getLocationName().toLowerCase().contains(location.getLocationName().toLowerCase())) {
					vlstData.add(item);
					lstFilter.clear();
					lstFilter.add(item);
				}
			}
		}
		if (!StringUtils.isValidString(location.getLocationCode())
				&& !StringUtils.isValidString(location.getLocationName())) {
			vlstData.addAll(lstLocation);
			lstFilter.clear();
			lstFilter.addAll(lstLocation);
		}
		listDataModel = new ListModelList<Location>(vlstData);
		gridLocation.setModel(listDataModel);
	}

	public void onClick$btnExport(Event event) {
		Messagebox.show(Labels.getLabel("not.support"), Labels.getLabel("comfirm"), Messagebox.OK,
				Messagebox.INFORMATION);
//		ExcelWriter<Location> excelWriter = new ExcelWriter<Location>();
//		try {
//
//			String pathFileInput = Constants.PATH_FILE + "file/template/export/location_data_export.xlsx";
//			String pathFileOut = Constants.PATH_FILE + "file/export/location_data_export.xlsx";
//
//			excelWriter.write(lstFilter, pathFileInput, pathFileOut);
//			File file = new File(pathFileOut);
//			Filedownload.save(file, null);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			logger.error(e.getMessage(), e);
//		}

	}

	public void onImport(ForwardEvent event) {
		Messagebox.show(Labels.getLabel("not.support"), Labels.getLabel("comfirm"), Messagebox.OK,
				Messagebox.INFORMATION);
		// final Window windownUpload = (Window)
		// Executions.createComponents("/manager/uploadLocation.zul", location,
		// null);
		// windownUpload.doModal();
		// windownUpload.setBorder(true);
		// windownUpload.setBorder("normal");
		// windownUpload.setClosable(true);
		// windownUpload.addEventListener(Events.ON_CLOSE, new
		// EventListener<Event>() {
		//
		// @Override
		// public void onEvent(Event event) throws Exception {
		// reloadGrid();
		//
		// }
		// });
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
								Labels.getLabel("pump.not.empty", new String[] { Labels.getLabel("location.code") }));
						lstError.add(locationExcel);
						continue;
					}

					if (!StringUtils.isValidString(locationExcel.getLocationName())) {
						numberError++;
						locationExcel.setIndex(numberError);
						locationExcel.setDescription(
								Labels.getLabel("pump.not.empty", new String[] { Labels.getLabel("location.name") }));
						lstError.add(locationExcel);
						continue;
					}

					if (!StringUtils.isValidString(locationExcel.getLocationType())) {
						numberError++;
						locationExcel.setIndex(numberError);
						locationExcel.setDescription(
								Labels.getLabel("pump.not.empty", new String[] { Labels.getLabel("location.value") }));
						lstError.add(locationExcel);
						continue;
					}
					if (!NumberUtils.isNumber(locationExcel.getLocationType())) {
						locationExcel.setIndex(numberError);
						locationExcel.setDescription(
								Labels.getLabel("pump.not.number", new String[] { Labels.getLabel("location.value") }));
						lstError.add(locationExcel);
						continue;
					}
					Location item = new Location();
					item.setLocationCode(locationExcel.getLocationCode());
					item.setLocationName(locationExcel.getLocationName());
					item.setLocationType(Integer.valueOf(locationExcel.getLocationType()));
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
}
