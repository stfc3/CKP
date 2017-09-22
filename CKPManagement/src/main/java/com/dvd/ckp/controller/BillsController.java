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
import org.zkoss.zk.ui.WrongValueException;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

import com.dvd.ckp.business.service.BillsServices;
import com.dvd.ckp.business.service.ConstructionService;
import com.dvd.ckp.business.service.CustomerService;
import com.dvd.ckp.common.Constants;
import com.dvd.ckp.domain.Bills;
import com.dvd.ckp.domain.BillsDetail;
import com.dvd.ckp.domain.Construction;
import com.dvd.ckp.domain.Customer;
import com.dvd.ckp.excel.ExcelWriter;
import com.dvd.ckp.utils.DateTimeUtils;
import com.dvd.ckp.utils.FileUtils;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;
import com.dvd.ckp.utils.StyleUtils;

/**
 *
 * @author viettx
 */
public class BillsController extends GenericForwardComposer {

	private static final Logger logger = Logger.getLogger(ConstructionController.class);
	@WireVariable
	protected ConstructionService constructionService;
	@WireVariable
	protected CustomerService customerService;
	@WireVariable
	protected BillsServices billsServices;
	@Wire
	private Grid gridBills;
	@Wire
	private Textbox txtFilterCustomer;
	@Wire
	private Textbox txtFilterConstruction;
	@Wire
	private Datebox dtFilterDate;

	ListModelList<Bills> listDataModel;

	private List<Bills> lstBills;
	private List<Bills> lstBillsFilter;

	private List<Construction> lstConstructions;
	private List<Customer> lstCustomer;

	private Construction defaultConstruction;
	private Customer defaultCustomer;

	private List<BillsDetail> lstBillDetail;

	private final int billsCode = 1;
	private final int customerID = 2;
	private final int constructionID = 3;
	private final int prd = 4;
	private final int intfilePath = 5;
	private final int intFromDate = 7;
	private final int intStartDate = 8;
	private final int intEndDate = 9;
	private final int intTodate = 10;
	private final int statusIndex = 11;

	private static final String SAVE_PATH = "/Bills/";

	private static String filePathBill = "";
	private static String fileName = "";
	private static int insertOrUpdate = 0;

	private Window bills;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		// khai bao services
		constructionService = (ConstructionService) SpringUtil.getBean(SpringConstant.CONSTRUCTION_SERVICES);
		customerService = (CustomerService) SpringUtil.getBean(SpringConstant.CUSTOMER_SERVICES);
		billsServices = (BillsServices) SpringUtil.getBean(SpringConstant.BILL_SERVICES);

		// list danh sach cong trinh
		lstConstructions = new ArrayList<>();
		lstConstructions.addAll(constructionService.getConstructionActive());

		// list danh sach khach hang
		lstCustomer = new ArrayList<>();
		lstCustomer.addAll(customerService.getCustomerActive());

		// list danh sach chi tiet hoa don
		lstBillDetail = billsServices.getBillDetail();

		// list danh sach hoa don
		lstBills = new ArrayList<>();
		// list de export data theo du lieu filter
		lstBillsFilter = new ArrayList<>();
		List<Bills> lstData = billsServices.getAllData();
		if (lstData != null && !lstData.isEmpty()) {
			for (Bills bills : lstData) {
				if (bills.getPrdID() != 0) {
					bills.setDateInput(
							DateTimeUtils.convertStringToTime(String.valueOf(bills.getPrdID()), Constants.FORMAT_DATE));
				} else {
					bills.setDateInput(null);
				}

				bills.setConstructionName(getConstructionByID(bills.getConstructionID()));
				bills.setCustomerName(getCustomerByID(bills.getCustomerID()));
				bills.setCost(getTotalPrice(bills.getBillID()));

			}
			lstBills.addAll(lstData);
			lstBillsFilter.addAll(lstData);
		}

		// cong trinh default
		defaultConstruction = new Construction();
		defaultConstruction.setConstructionId(0l);
		defaultConstruction.setConstructionName(Labels.getLabel("option"));
		lstConstructions.add(0, defaultConstruction);

		// khach hang default
		defaultCustomer = new Customer();
		defaultCustomer.setCustomerId(0l);
		defaultCustomer.setCustomerName(Labels.getLabel("option"));
		lstCustomer.add(0, defaultCustomer);

		// set model
		listDataModel = new ListModelList(lstBills);
		gridBills.setModel(listDataModel);
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
		Bills c = rowSelected.getValue();
		setDataConstruction(lstCell, getConstructionDefault(c.getConstructionID()), constructionID);
		setDataCustomer(lstCell, getCustomerDefault(c.getCustomerID()), customerID);
		setEnableComponent(lstCell);
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
		Bills c = rowSelected.getValue();
		Bills bills = getDataInRow(lstCell);
		bills.setBillID(c.getBillID());
		if (insertOrUpdate == 1) {
			billsServices.save(bills);
		} else {
			billsServices.update(bills);
		}
		setDisableComponent(lstCell);
		reloadGrid();
		insertOrUpdate = 0;
	}

	public void onDelete(ForwardEvent event) {
		Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
		List<Component> lstCell = rowSelected.getChildren();
		Bills c = rowSelected.getValue();
		Bills bills = getDataInRow(lstCell);
		bills.setBillID(c.getBillID());
		bills.setStatus(3);
		billsServices.delete(bills);
		reloadGrid();

	}

	/**
	 * Add row
	 */
	public void onClick$add() {
		Bills construction = new Bills();
		listDataModel.add(0, construction);
		gridBills.setModel(listDataModel);
		gridBills.renderAll();
		List<Component> lstCell = gridBills.getRows().getChildren().get(0).getChildren();
		setDataDefaultInGrid();
		setEnableComponent(lstCell);
		insertOrUpdate = 1;
	}

	/**
	 * Get object customer
	 *
	 * @param lstCell
	 * @return
	 */
	private Bills getDataInRow(List<Component> lstCell) {
		Bills bills = new Bills();
		Component component;

		Textbox txtBillsCode = null;
		Combobox cbCustomer = null;
		Combobox cbxConstruction = null;
		Datebox dtPrdID = null;
		Datebox fromdate = null;
		Datebox startDate = null;
		Datebox endDate = null;
		Datebox toDate = null;
		Combobox cbxStatus = null;

		// ma phieu bom
		component = lstCell.get(billsCode).getFirstChild();
		if (component != null && component instanceof Textbox) {
			txtBillsCode = (Textbox) component;
			bills.setBillCode(txtBillsCode.getValue());
		}
		// Khach hang
		component = lstCell.get(customerID).getFirstChild();
		if (component != null && component instanceof Textbox) {
			cbCustomer = (Combobox) component;
			bills.setCustomerID(cbCustomer.getSelectedItem().getValue());
		}
		// Cong trinh
		component = lstCell.get(constructionID).getFirstChild();
		if (component != null && component instanceof Combobox) {
			cbxConstruction = (Combobox) component;
			bills.setConstructionID(cbxConstruction.getSelectedItem().getValue());
		}
		bills.setFileName(fileName);
		bills.setFilePath(filePathBill);
		// Ngay bom
		component = lstCell.get(prd).getFirstChild();
		if (component != null && component instanceof Datebox) {
			try {
				dtPrdID = (Datebox) component;
				bills.setPrdID(
						Integer.valueOf(DateTimeUtils.convertDateToString(dtPrdID.getValue(), Constants.FORMAT_DATE)));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		}
		// Ngay den cong truong
		component = lstCell.get(intFromDate).getFirstChild();
		if (component != null && component instanceof Datebox) {
			fromdate = (Datebox) component;
			bills.setFromDate(fromdate.getValue());
		}
		// Thoi gian bat dau
		component = lstCell.get(intStartDate).getFirstChild();
		if (component != null && component instanceof Timebox) {
			startDate = (Datebox) component;
			bills.setStartTime(startDate.getValue());
		}
		// Thoi gian bom xong
		component = lstCell.get(intEndDate).getFirstChild();
		if (component != null && component instanceof Timebox) {
			endDate = (Datebox) component;
			bills.setEndTime(endDate.getValue());
		}

		// Thoi gian bom xong
		component = lstCell.get(intTodate).getFirstChild();
		if (component != null && component instanceof Datebox) {
			toDate = (Datebox) component;
			bills.setToDate(toDate.getValue());
		}

		// Trang thai
		component = lstCell.get(statusIndex).getFirstChild();
		if (component != null && component instanceof Combobox) {
			cbxStatus = (Combobox) component;
			bills.setConstructionID(Long.valueOf(cbxStatus.getSelectedItem().getValue()));
		}
		logger.info(bills.toString());
		return bills;
	}

	/**
	 * Reload grid
	 */
	private void reloadGrid() {
		lstBills.clear();
		;
		List<Bills> lstData = billsServices.getAllData();
		if (lstData != null && !lstData.isEmpty()) {
			for (Bills bills : lstData) {
				if (bills.getPrdID() != 0) {
					bills.setDateInput(
							DateTimeUtils.convertStringToTime(String.valueOf(bills.getPrdID()), Constants.FORMAT_DATE));
				} else {
					bills.setDateInput(null);
				}

				bills.setConstructionName(getConstructionByID(bills.getConstructionID()));
				bills.setCustomerName(getCustomerByID(bills.getCustomerID()));
				bills.setCost(getTotalPrice(bills.getBillID()));

			}
			lstBills.addAll(lstData);
			lstBillsFilter.addAll(lstData);
		}
		listDataModel = new ListModelList(lstBills);
		gridBills.setModel(listDataModel);
		setDataDefaultInGrid();
	}

	public void onChange$txtFilterCustomer() {
		Bills bills = new Bills();
		String vstrCustomer = txtFilterCustomer.getValue();
		bills.setCustomerName(vstrCustomer);

		String vstrConstructionName = txtFilterConstruction.getValue();
		bills.setConstructionName(vstrConstructionName);

		try {
			if (dtFilterDate.getValue() != null) {
				int dateFilter = Integer
						.valueOf(DateTimeUtils.convertDateToString(dtFilterDate.getValue(), Constants.FORMAT_DATE));
				bills.setPrdID(dateFilter);
			} else {
				bills.setPrdID(0);
			}
		} catch (WrongValueException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}

		filter(bills);
	}

	public void onChange$txtFilterConstruction() {
		Bills bills = new Bills();
		String vstrCustomer = txtFilterCustomer.getValue();
		bills.setCustomerName(vstrCustomer);

		String vstrConstructionName = txtFilterConstruction.getValue();
		bills.setConstructionName(vstrConstructionName);
		try {
			if (dtFilterDate.getValue() != null) {
				int dateFilter = Integer
						.valueOf(DateTimeUtils.convertDateToString(dtFilterDate.getValue(), Constants.FORMAT_DATE));
				bills.setPrdID(dateFilter);
			} else {
				bills.setPrdID(0);
			}
		} catch (WrongValueException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}

		filter(bills);
	}

	public void onChange$dtFilterDate() {
		Bills bills = new Bills();
		String vstrCustomer = txtFilterCustomer.getValue();
		bills.setCustomerName(vstrCustomer);

		String vstrConstructionName = txtFilterConstruction.getValue();
		bills.setConstructionName(vstrConstructionName);
		try {
			if (dtFilterDate.getValue() != null) {
				int dateFilter = Integer
						.valueOf(DateTimeUtils.convertDateToString(dtFilterDate.getValue(), Constants.FORMAT_DATE));
				bills.setPrdID(dateFilter);
			} else {
				bills.setPrdID(0);
			}
		} catch (WrongValueException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}

		filter(bills);
	}

	private void filter(Bills bills) {
		List<Bills> vlstBills = new ArrayList<>();
		lstBillsFilter.clear();
		if (lstBills != null && !lstBills.isEmpty() && bills != null) {
			if (!StringUtils.isValidString(bills.getCustomerName())
					&& !StringUtils.isValidString(bills.getConstructionName()) && bills.getPrdID() == 0) {
				vlstBills.addAll(lstBills);

			} else {
				List<Long> lstCustomerId = getCustomerByName(bills.getCustomerName());
				List<Long> lstConstructionId = getConstructionByName(bills.getConstructionName());
				for (Bills c : lstBills) {
					// tim theo khach hang va cong trinh
					if (StringUtils.isValidString(bills.getCustomerName())
							&& StringUtils.isValidString(bills.getConstructionName()) && bills.getPrdID() == 0) {
						if (lstCustomerId.contains(c.getCustomerID())
								&& lstConstructionId.contains(c.getConstructionID())) {
							vlstBills.add(c);
						}
					} // tim theo khachs hang
					else if (StringUtils.isValidString(bills.getCustomerName())
							&& !StringUtils.isValidString(bills.getConstructionName()) && bills.getPrdID() == 0) {
						if (lstCustomerId.contains(c.getCustomerID())) {
							vlstBills.add(c);
						}
					} // tim theo cong trinh
					else if (!StringUtils.isValidString(bills.getCustomerName())
							&& StringUtils.isValidString(bills.getConstructionName()) && bills.getPrdID() == 0) {
						if (lstConstructionId.contains(c.getConstructionID())) {
							vlstBills.add(c);
						}
					}
					// Tim theo ngay
					else if (!StringUtils.isValidString(bills.getCustomerName())
							&& !StringUtils.isValidString(bills.getConstructionName()) && bills.getPrdID() != 0) {
						if (bills.getPrdID() == c.getPrdID()) {
							vlstBills.add(c);
						}
					}
					// Tim theo khach hang va ngay in hoa don
					else if (StringUtils.isValidString(bills.getCustomerName())
							&& !StringUtils.isValidString(bills.getConstructionName()) && bills.getPrdID() != 0) {
						if (lstCustomerId.contains(c.getCustomerID()) && bills.getPrdID() == c.getPrdID()) {
							vlstBills.add(c);
						}
					} else if (!StringUtils.isValidString(bills.getCustomerName())
							&& StringUtils.isValidString(bills.getConstructionName()) && bills.getPrdID() != 0) {
						if (lstConstructionId.contains(c.getConstructionID()) && bills.getPrdID() == c.getPrdID()) {
							vlstBills.add(c);
						}
					}
				}
			}
		}
		lstBillsFilter.addAll(vlstBills);
		listDataModel = new ListModelList(vlstBills);
		gridBills.setModel(listDataModel);
		setDataDefaultInGrid();

	}

	private List<Long> getCustomerByName(String customerName) {
		List<Long> lstDataReturn = new ArrayList<>();
		if (lstCustomer != null && !lstCustomer.isEmpty()) {
			for (Customer customer : lstCustomer) {
				if (StringUtils.isValidString(customerName)
						&& customer.getCustomerName().toLowerCase().contains(customerName.toLowerCase())) {
					lstDataReturn.add(customer.getCustomerId());
				}
			}
		}
		return lstDataReturn;
	}

	private String getCustomerByID(Long customerId) {
		List<String> lstDataReturn = new ArrayList<>();
		if (lstCustomer != null && !lstCustomer.isEmpty()) {
			for (Customer customer : lstCustomer) {
				if (customerId != null && customerId.equals(customer.getCustomerId())) {
					return customer.getCustomerName();
				}
			}
		}
		return "";
	}

	private String getConstructionByID(Long constructionId) {
		List<String> lstDataReturn = new ArrayList<>();
		if (lstConstructions != null && !lstConstructions.isEmpty()) {
			for (Construction construction : lstConstructions) {
				if (constructionId != null && constructionId.equals(construction.getConstructionId())) {
					return construction.getConstructionName();
				}
			}
		}
		return "";
	}

	private List<Long> getConstructionByName(String constructionName) {
		List<Long> lstDataReturn = new ArrayList<>();
		if (lstConstructions != null && !lstConstructions.isEmpty()) {
			for (Construction construction : lstConstructions) {
				if (StringUtils.isValidString(constructionName)
						&& construction.getConstructionName().toLowerCase().contains(constructionName.toLowerCase())) {
					lstDataReturn.add(construction.getConstructionId());
				}
			}
		}
		return lstDataReturn;
	}

	// Set data for combobox construction
	private void setDataConstruction(List<Component> lstCell, List<Construction> selectedIndex, int columnIndex) {
		Combobox combobox = null;
		Component component = lstCell.get(columnIndex).getFirstChild();
		if (component != null && component instanceof Combobox) {
			combobox = (Combobox) component;
			ListModelList listDataModel = new ListModelList(lstConstructions);
			listDataModel.setSelection(selectedIndex);
			combobox.setModel(listDataModel);
		}

	}

	// Set data for combobox customer
	private void setDataCustomer(List<Component> lstCell, List<Customer> selectedIndex, int columnIndex) {
		Combobox combobox = null;
		Component component = lstCell.get(columnIndex).getFirstChild();
		if (component != null && component instanceof Combobox) {
			combobox = (Combobox) component;
			ListModelList listDataModel = new ListModelList(lstCustomer);
			listDataModel.setSelection(selectedIndex);
			combobox.setModel(listDataModel);
		}

	}

	/**
	 * Save
	 *
	 * @param event
	 */
	public void onUpload(ForwardEvent event) {
		Media media = ((UploadEvent) event.getOrigin()).getMedia();
		String filePath = "";
		if (media == null) {
			Messagebox.show(Labels.getLabel("uploadExcel.selectFile"), Labels.getLabel("ERROR"), Messagebox.OK,
					Messagebox.ERROR);
			return;
		}
		if (media != null) {
			filePath = media.getName();
		}
		Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
		List<Component> lstCell = rowSelected.getChildren();

		final String vstrFileName = media.getName();

		FileUtils fileUtils = new FileUtils();
		fileUtils.setSaveFilePath(SAVE_PATH);
		fileUtils.saveFile(media);
		filePathBill = fileUtils.getFilePath();
		fileName = fileUtils.getFileName();
		setPath(lstCell, fileName, intfilePath);
	}

	private void setPath(List<Component> lstCell, String fileName, int columnIndex) {
		Button btnFileName = null;
		Component component = lstCell.get(columnIndex).getFirstChild();
		if (component != null && component instanceof Button) {
			btnFileName = (Button) component;
			btnFileName.setLabel(fileName);
		}

	}

	// get Construction default
	private List<Construction> getConstructionDefault(Long constructionId) {
		List<Construction> constructionSelected = new ArrayList<>();
		if (constructionId != null && lstConstructions != null && !lstConstructions.isEmpty()) {
			for (Construction construction : lstConstructions) {
				if (constructionId.equals(construction.getConstructionId())) {
					constructionSelected.add(construction);
					break;
				}
			}
		}
		if (constructionSelected.isEmpty()) {
			constructionSelected.add(defaultConstruction);
		}
		return constructionSelected;
	}

	// get Customer default
	private List<Customer> getCustomerDefault(Long customerId) {
		List<Customer> customerSelected = new ArrayList<>();
		if (customerId != null && lstCustomer != null && !lstCustomer.isEmpty()) {
			for (Customer customer : lstCustomer) {
				if (customerId.equals(customer.getCustomerId())) {
					customerSelected.add(customer);
					break;
				}
			}
		}
		if (customerSelected.isEmpty()) {
			customerSelected.add(defaultCustomer);
		}
		return customerSelected;
	}

	private void setDataDefaultInGrid() {
		gridBills.renderAll();
		List<Component> lstRows = gridBills.getRows().getChildren();
		if (lstRows != null && !lstRows.isEmpty()) {
			for (int i = 0; i < lstRows.size(); i++) {
				Bills bills = listDataModel.get(i);
				Component row = lstRows.get(i);
				List<Component> lstCell = row.getChildren();
				setDataCustomer(lstCell, getCustomerDefault(bills.getCustomerID()), customerID);
				setDataConstruction(lstCell, getConstructionDefault(bills.getConstructionID()), constructionID);
			}
		}
	}

	public void onClick$btnExport(Event event) {
		ExcelWriter<Bills> excelWriter = new ExcelWriter<Bills>();
		try {
			int index = 0;
			for (Bills staff : lstBillsFilter) {
				index++;
				staff.setIndex(index);
			}
			String pathFileInput = Constants.PATH_FILE + "file/template/export/bills_data_export.xlsx";
			String pathFileOut = Constants.PATH_FILE + "file/export/bills_data_export.xlsx";

			excelWriter.write(lstBillsFilter, pathFileInput, pathFileOut);
			File file = new File(pathFileOut);
			Filedownload.save(file, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}

	}

	public void onImport(ForwardEvent event) {
		Messagebox.show(Labels.getLabel("not.support"), Labels.getLabel("comfirm"), Messagebox.OK,
				Messagebox.INFORMATION);
	}

	public static void setEnableComponent(List<Component> lstCell) {
		if (lstCell != null && !lstCell.isEmpty()) {
			for (Component c : lstCell) {
				if (c instanceof Cell) {
					Component child = c.getFirstChild();
					if (child instanceof Combobox) {
						((Combobox) child).setButtonVisible(true);
						((Combobox) child).setInplace(false);
					} else if (child instanceof Datebox) {
						((Datebox) child).setButtonVisible(true);
						((Datebox) child).setInplace(false);
					} else if (child instanceof Textbox) {
						((Textbox) child).setReadonly(false);
						((Textbox) child).setInplace(false);
					} else if (child instanceof A && c.getChildren().size() > 1) {
						A edit = (A) child;
						edit.setVisible(false);
						A save = (A) c.getChildren().get(1);
						save.setVisible(true);
						A cancel = (A) c.getChildren().get(2);
						cancel.setVisible(true);
						A addDetail = (A) c.getChildren().get(3);
						addDetail.setVisible(true);
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
	public static void setDisableComponent(List<Component> lstCell) {
		if (lstCell != null && !lstCell.isEmpty()) {
			for (Component c : lstCell) {
				if (c instanceof Cell) {
					Component child = c.getFirstChild();
					if (child instanceof Combobox) {
						((Combobox) child).setButtonVisible(false);
						((Combobox) child).setInplace(true);
					} else if (child instanceof Datebox) {
						((Datebox) child).setButtonVisible(false);
						((Datebox) child).setInplace(true);
					} else if (child instanceof Textbox) {
						((Textbox) child).setReadonly(true);
						((Textbox) child).setInplace(true);
					} else if (child instanceof A && child instanceof Button && c.getChildren().size() > 1) {
						A link = (A) child;
						link.setVisible(true);
						Button upload = (Button) c.getChildren().get(1);
						upload.setVisible(true);
					} else if (child instanceof A && c.getChildren().size() > 1) {

						A edit = (A) child;
						edit.setVisible(true);
						A save = (A) c.getChildren().get(1);
						save.setVisible(false);
						A cancel = (A) c.getChildren().get(2);
						cancel.setVisible(false);
						A addDetail = (A) c.getChildren().get(3);
						addDetail.setVisible(false);
					}
				}
			}
		}
	}

	private double getTotalPrice(Long billID) {
		double totalPrice = 0;
		if (lstBillDetail != null && !lstBillDetail.isEmpty()) {
			for (BillsDetail detail : lstBillDetail) {
				if (billID != null && billID.equals(detail.getBillId())) {
					totalPrice += detail.getTotal();
				}
			}
		}

		return totalPrice;
	}

	public void onView(ForwardEvent event) {
		final Window windownUpload = (Window) Executions.createComponents("/manager/billDetailView.zul", bills, null);
		windownUpload.doModal();
		windownUpload.setBorder(true);
		windownUpload.setBorder("normal");
		windownUpload.setClosable(true);
		windownUpload.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				windownUpload.detach();

			}
		});
	}

	public void onAddDetail(ForwardEvent event) {
		final Window windownUpload = (Window) Executions.createComponents("/manager/billDetail.zul", bills, null);
		windownUpload.doModal();
		windownUpload.setBorder(true);
		windownUpload.setBorder("normal");
		windownUpload.setClosable(true);
		windownUpload.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				windownUpload.detach();

			}
		});
	}
}
