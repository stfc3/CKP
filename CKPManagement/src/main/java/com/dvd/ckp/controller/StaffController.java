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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.dvd.ckp.business.service.StaffServices;
import com.dvd.ckp.common.Constants;
import com.dvd.ckp.domain.Staff;
import com.dvd.ckp.excel.ExcelReader;
import com.dvd.ckp.excel.ExcelWriter;
import com.dvd.ckp.excel.domain.StaffExcel;
import com.dvd.ckp.utils.DateTimeUtils;
import com.dvd.ckp.utils.FileUtils;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;

/**
 *
 * @author viettx
 */
public class StaffController extends GenericForwardComposer {

	private static final Logger logger = Logger.getLogger(StaffController.class);
	@WireVariable
	protected StaffServices staffService;
	@Wire
	private Grid gridStaff;
	@Wire
	private Textbox txtFilterCode;
	@Wire
	private Textbox txtFilterName;

	@Wire
	private Textbox txtFilterPhone;

	@Wire
	private Textbox txtFilterEmail;

	private ListModelList<Staff> listDataModel;
	private List<Staff> lstStaff;
	private List<Staff> lstStaffFilter;
	public Button btnExport;
	public Button btnImport;
	private int insertOrUpdate = 0;

	private Window staff;

	private static final String SAVE_PATH = "/Staff/";

	private Label linkFileName;
	private Textbox hiddenFileName;
	private Textbox hdFileName;

	public Textbox txtTotalRow;
	public Textbox txtTotalRowSucces;
	public Textbox txtTotalRowError;

	public Button btnCancel;
	public Button errorList;
	// public Window uploadPump;

	List<StaffExcel> lstError = new ArrayList<StaffExcel>();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		lstStaffFilter = new ArrayList<Staff>();
		staffService = (StaffServices) SpringUtil.getBean(SpringConstant.STAFF_SERVICES);

		lstStaff = new ArrayList<Staff>();
		List<Staff> vlstStaff = staffService.getAllData();
		if (vlstStaff != null) {
			lstStaff.addAll(vlstStaff);
			lstStaffFilter.addAll(vlstStaff);
		}
		listDataModel = new ListModelList<Staff>(lstStaff);
		gridStaff.setModel(listDataModel);
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
		Staff c = rowSelected.getValue();
		Staff staff = getDataInRow(lstCell);
		staff.setStaffId(c.getStaffId());
		staff.setStatus(3);
		lstStaffFilter.remove(staff);
		staffService.detele(staff);
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
		Staff c = rowSelected.getValue();
		Staff staff = getDataInRow(lstCell);
		staff.setStaffId(c.getStaffId());
		if (insertOrUpdate == 1) {
			lstStaffFilter.add(staff);
			staffService.save(staff);
		} else {
			staffService.update(staff);
		}
		setDisableComponent(lstCell);
		reloadGrid();
		insertOrUpdate = 0;
	}

	/**
	 * Add row
	 */
	public void onClick$add() {
		Staff newItem = new Staff();
		newItem.setStatus(1);
		listDataModel.add(0, newItem);
		gridStaff.setModel(listDataModel);
		gridStaff.renderAll();
		List<Component> lstCell = gridStaff.getRows().getChildren().get(0).getChildren();
		setEnableComponent(lstCell);
		insertOrUpdate = 1;
	}

	/**
	 * Get object customer
	 *
	 * @param lstCell
	 * @return
	 */
	private Staff getDataInRow(List<Component> lstCell) {
		Staff staff = new Staff();
		Textbox txtStaffCode = (Textbox) lstCell.get(1).getFirstChild();
		Textbox txtStaffName = (Textbox) lstCell.get(2).getFirstChild();
		Textbox txtPhone = (Textbox) lstCell.get(4).getFirstChild();
		Textbox txtEmail = (Textbox) lstCell.get(5).getFirstChild();
		Textbox txtAddress = (Textbox) lstCell.get(6).getFirstChild();
		Datebox dateBirthday = (Datebox) lstCell.get(3).getFirstChild();
		Combobox cbxStatus = (Combobox) lstCell.get(7).getFirstChild();
		staff.setStaffCode(txtStaffCode.getValue());
		staff.setStaffName(txtStaffName.getValue());
		staff.setPhone(txtPhone.getValue());
		staff.setAddress(txtAddress.getValue());
		staff.setEmail(txtEmail.getValue());
		staff.setBirthday(dateBirthday.getValue());
		staff.setStatus(Integer.valueOf(cbxStatus.getSelectedItem().getValue()));
		return staff;
	}

	/**
	 * Reload grid
	 */
	private void reloadGrid() {
		List<Staff> vlstData = new ArrayList<Staff>();
		List<Staff> list = staffService.getAllData();
		if (list != null && !list.isEmpty()) {
			vlstData.addAll(list);
		}
		listDataModel = new ListModelList<Staff>(vlstData);
		gridStaff.setModel(listDataModel);
	}

	public void onChange$txtFilterCode() {
		Staff staff = new Staff();

		String vstrStaffCode = txtFilterCode.getValue();
		staff.setStaffCode(vstrStaffCode);

		String vstrStaffName = txtFilterName.getValue();
		staff.setStaffName(vstrStaffName);

		filter(staff);
	}

	public void onChange$txtFilterName() {
		Staff staff = new Staff();

		String vstrStaffCode = txtFilterCode.getValue();
		staff.setStaffCode(vstrStaffCode);

		String vstrStaffName = txtFilterName.getValue();
		staff.setStaffName(vstrStaffName);

		filter(staff);
	}

	private void filter(Staff staff) {
		List<Staff> vlstData = new ArrayList<>();
		if (!StringUtils.isValidString(staff.getStaffCode()) && !StringUtils.isValidString(staff.getStaffName())) {
			vlstData.addAll(lstStaff);
			lstStaffFilter.clear();
			lstStaffFilter.addAll(lstStaff);
		} else {
			if (lstStaff != null && !lstStaff.isEmpty()) {
				for (Staff item : lstStaff) {
					if (StringUtils.isValidString(staff.getStaffCode())
							&& item.getStaffCode().toLowerCase().contains(staff.getStaffCode().toLowerCase())
							&& StringUtils.isValidString(staff.getStaffName())
							&& item.getStaffName().toLowerCase().contains(staff.getStaffName().toLowerCase())) {
						vlstData.add(item);
						lstStaffFilter.clear();
						lstStaffFilter.add(item);
					} else if (!StringUtils.isValidString(staff.getStaffCode())
							&& StringUtils.isValidString(staff.getStaffName())
							&& item.getStaffName().toLowerCase().contains(staff.getStaffName().toLowerCase())) {
						vlstData.add(item);
						lstStaffFilter.clear();
						lstStaffFilter.add(item);
					} else if (StringUtils.isValidString(staff.getStaffCode())
							&& item.getStaffCode().toLowerCase().contains(staff.getStaffCode().toLowerCase())
							&& !StringUtils.isValidString(staff.getStaffName())) {
						vlstData.add(item);
						lstStaffFilter.clear();
						lstStaffFilter.add(item);
					}
				}
			}
		}
		listDataModel = new ListModelList<Staff>(vlstData);
		gridStaff.setModel(listDataModel);
	}

	public void onClick$btnExport(Event event) {
		Messagebox.show(Labels.getLabel("not.support"), Labels.getLabel("comfirm"), Messagebox.OK,
				Messagebox.INFORMATION);
//		ExcelWriter<Staff> excelWriter = new ExcelWriter<Staff>();
//		try {
//			int index = 0;
//			for (Staff staff : lstStaffFilter) {
//				index++;
//				staff.setIndex(index);
//				staff.setBirthdayString(
//						DateTimeUtils.convertDateToString(staff.getBirthday(), Constants.FORMAT_DATE_DD_MM_YYY));
//			}
//			String pathFileInput = Constants.PATH_FILE + "file/template/export/staff_data_export.xlsx";
//			String pathFileOut = Constants.PATH_FILE + "file/export/staff_data_export.xlsx";
//
//			excelWriter.write(lstStaffFilter, pathFileInput, pathFileOut);
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
//		final Window windownUpload = (Window) Executions.createComponents("/manager/uploadStaff.zul", staff, null);
//		windownUpload.doModal();
//		windownUpload.setBorder(true);
//		windownUpload.setBorder("normal");
//		windownUpload.setClosable(true);
//		windownUpload.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
//
//			@Override
//			public void onEvent(Event event) throws Exception {
//				reloadGrid();
//
//			}
//		});
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
		try {
			ExcelReader<StaffExcel> reader = new ExcelReader<>();
			String filePath = hiddenFileName.getValue();
			List<StaffExcel> listData = reader.read(filePath, StaffExcel.class);
			List<Staff> vlstData = new ArrayList<>();
			if (listData != null && !listData.isEmpty()) {
				for (StaffExcel staff : listData) {

					if (!StringUtils.isValidString(staff.getStaffCode())) {
						staff.setDescription(
								Labels.getLabel("pump.not.number", new String[] { Labels.getLabel("pump.capacity") }));
						staff.setIndex(numberRowError);
						lstError.add(staff);
						numberRowError++;
						continue;
					}

					if (!StringUtils.isValidString(staff.getStaffName())) {
						staff.setDescription(
								Labels.getLabel("pump.not.number", new String[] { Labels.getLabel("pump.capacity") }));
						staff.setIndex(numberRowError);
						lstError.add(staff);
						numberRowError++;
						continue;
					}

					if (!StringUtils.isValidString(staff.getPhone())) {
						staff.setDescription(
								Labels.getLabel("pump.not.number", new String[] { Labels.getLabel("pump.capacity") }));
						staff.setIndex(numberRowError);
						lstError.add(staff);
						numberRowError++;
						continue;
					}

					if (!StringUtils.isValidString(staff.getEmail())) {
						staff.setDescription(
								Labels.getLabel("pump.not.number", new String[] { Labels.getLabel("pump.capacity") }));
						staff.setIndex(numberRowError);
						lstError.add(staff);
						numberRowError++;
						continue;
					}
					Staff item = new Staff();
					item.setStaffCode(staff.getStaffCode());
					item.setStaffName(staff.getStaffName());
					item.setPhone(staff.getPhone());
					item.setEmail(staff.getEmail());
					item.setBirthday(DateTimeUtils.convertStringToTime(replateBirthDay(staff.getBirthday()),
							Constants.FORMAT_DATE_DDMMYYYY));
					item.setAddress(staff.getAddress());
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
			staffService.importData(vlstData);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void onDownloadFile(ForwardEvent event) {
		try {
			String pathFileInput = Constants.PATH_FILE + "file/template/import/import_staff_data.xlsx";

			File file = new File(pathFileInput);
			Filedownload.save(file, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
	}

	public void onDownloadFileError(ForwardEvent event) {
		ExcelWriter<StaffExcel> writer = new ExcelWriter<>();
		try {
			String pathFileOutput = Constants.PATH_FILE + "file/export/error/error_staff_data.xlsx";
			String pathFileInput = Constants.PATH_FILE + "file/template/error/error_staff_data.xlsx";

			writer.write(lstError, pathFileInput, pathFileOutput);
			File file = new File(pathFileOutput);
			Filedownload.save(file, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
	}

	private String replateBirthDay(String value) {
		return value.replace("/", "");
	}

}
