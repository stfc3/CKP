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

import com.dvd.ckp.business.service.PumpServices;
import com.dvd.ckp.common.Constants;
import com.dvd.ckp.domain.Pumps;
import com.dvd.ckp.excel.ExcelReader;
import com.dvd.ckp.excel.ExcelWriter;
import com.dvd.ckp.utils.FileUtils;
import com.dvd.ckp.utils.NumberUtils;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;

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
	private Textbox txtFilterCode;
	@Wire
	private Textbox txtFilterName;
	ListModelList<Pumps> listDataModel;
	private List<Pumps> lstPumps;
	private List<Pumps> lstPumpsFilter;
	public Button btnExport;
	public Button btnImport;
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
		listDataModel = new ListModelList<Pumps>(lstPumps);
		gridPumps.setModel(listDataModel);
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
		Pumps c = rowSelected.getValue();
		Pumps pumps = getDataInRow(lstCell);
		pumps.setPumpsID(c.getPumpsID());
		pumps.setStatus(3);
		lstPumpsFilter.remove(pumps);
		pumpsService.detele(pumps);
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
		Pumps c = rowSelected.getValue();
		Pumps pumps = getDataInRow(lstCell);
		pumps.setPumpsID(c.getPumpsID());
		if (insertOrUpdate == 1) {
			lstPumpsFilter.add(pumps);
			pumpsService.savePumps(pumps);
		} else {
			pumpsService.update(pumps);
		}
		setDisableComponent(lstCell);
		reloadGrid();
		insertOrUpdate = 0;
	}

	/**
	 * Add row
	 */
	public void onClick$add() {
		Pumps pumpAddItem = new Pumps();
		pumpAddItem.setStatus(1);
		listDataModel.add(0, pumpAddItem);
		gridPumps.setModel(listDataModel);
		gridPumps.renderAll();
		List<Component> lstCell = gridPumps.getRows().getChildren().get(0).getChildren();
		setEnableComponent(lstCell);
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
		Textbox txtPumpsCode = (Textbox) lstCell.get(1).getFirstChild();
		Textbox txtPumpsName = (Textbox) lstCell.get(2).getFirstChild();
		Textbox txtPumpsCapacity = (Textbox) lstCell.get(3).getFirstChild();
		Textbox txtPumpsHight = (Textbox) lstCell.get(4).getFirstChild();
		Textbox txtPumpsFar = (Textbox) lstCell.get(5).getFirstChild();
		Combobox cbxStatus = (Combobox) lstCell.get(6).getFirstChild();
		pump.setPumpsCode(txtPumpsCode.getValue());
		pump.setPumpsName(txtPumpsName.getValue());
		pump.setPumpsCapacity(Integer.valueOf(txtPumpsCapacity.getValue()));
		pump.setPumpsHight(Integer.valueOf(txtPumpsFar.getValue()));
		pump.setPumpsFar(Integer.valueOf(txtPumpsHight.getValue()));
		pump.setStatus(Integer.valueOf(cbxStatus.getSelectedItem().getValue()));
		return pump;
	}

	/**
	 * Reload grid
	 */
	private void reloadGrid() {
		List<Pumps> vlstData = new ArrayList<Pumps>();
		if (pumpsService.getAllListData() != null && !pumpsService.getAllListData().isEmpty()) {
			vlstData.addAll(pumpsService.getAllListData());
		}
		listDataModel = new ListModelList<Pumps>(vlstData);
		gridPumps.setModel(listDataModel);
	}

	public void onChange$txtFilterCode() {
		Pumps pumps = new Pumps();
		String vstrPumpsCode = txtFilterCode.getValue();
		pumps.setPumpsCode(vstrPumpsCode);
		String vstrPumpsName = txtFilterName.getValue();
		pumps.setPumpsName(vstrPumpsName);
		filter(pumps);
	}

	public void onChange$txtFilterName() {
		Pumps pumps = new Pumps();
		String vstrPumpsCode = txtFilterCode.getValue();
		pumps.setPumpsCode(vstrPumpsCode);
		String vstrPumpsName = txtFilterName.getValue();
		pumps.setPumpsName(vstrPumpsName);
		filter(pumps);
	}

	private void filter(Pumps pumps) {
		int index = 0;
		List<Pumps> vlstData = new ArrayList<>();
		if (lstPumps != null && !lstPumps.isEmpty()) {
			for (Pumps item : lstPumps) {
				index++;
				pumps.setIndex(index);
				if (StringUtils.isValidString(pumps.getPumpsCode())
						&& item.getPumpsCode().toLowerCase().contains(pumps.getPumpsCode().toLowerCase())) {
					vlstData.add(item);
					lstPumpsFilter.clear();
					lstPumpsFilter.add(item);
				} else if (StringUtils.isValidString(pumps.getPumpsName())
						&& item.getPumpsName().toLowerCase().contains(pumps.getPumpsName().toLowerCase())) {
					vlstData.add(item);
					lstPumpsFilter.clear();
					lstPumpsFilter.add(item);
				}
			}
		}
		if (!StringUtils.isValidString(pumps.getPumpsCode()) && !StringUtils.isValidString(pumps.getPumpsName())) {
			vlstData.addAll(lstPumps);
			lstPumpsFilter.clear();
			lstPumpsFilter.addAll(lstPumps);
		}
		listDataModel = new ListModelList<Pumps>(vlstData);
		gridPumps.setModel(listDataModel);
	}

	public void onClick$btnExport(Event event) {
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
								Labels.getLabel("pump.not.number", new String[] { Labels.getLabel("pump.capacity") }));
						pumps.setIndex(numberRowError);
						lstError.add(pumps);
						numberRowError++;
						continue;
					}

					if (!NumberUtils.isNumber(pumps.getPumpsHight())) {
						pumps.setDescription(
								Labels.getLabel("pump.not.number", new String[] { Labels.getLabel("pump.hight") }));
						pumps.setIndex(numberRowError);
						lstError.add(pumps);
						numberRowError++;
						continue;
					}

					if (!NumberUtils.isNumber(pumps.getPumpsFar())) {
						pumps.setDescription(
								Labels.getLabel("pump.not.number", new String[] { Labels.getLabel("pump.far") }));
						pumps.setIndex(numberRowError);
						lstError.add(pumps);
						numberRowError++;
						continue;
					}
					Pumps item = new Pumps();
					item.setPumpsCode(pumps.getPumpsCode());
					item.setPumpsName(pumps.getPumpsName());
					item.setPumpsCapacity(Integer.valueOf(pumps.getPumpsCapacity()));
					item.setPumpsHight(Integer.valueOf(pumps.getPumpsHight()));
					item.setPumpsFar(Integer.valueOf(pumps.getPumpsFar()));
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

}
