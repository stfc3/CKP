/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.dvd.ckp.business.service.ConstructionService;
import com.dvd.ckp.business.service.ContractService;
import com.dvd.ckp.business.service.CustomerService;
import com.dvd.ckp.business.service.DistributeService;
import com.dvd.ckp.business.service.RentServices;
import com.dvd.ckp.business.service.StaffServices;
import com.dvd.ckp.business.service.UtilsService;
import com.dvd.ckp.component.MyListModel;
import com.dvd.ckp.domain.Construction;
import com.dvd.ckp.domain.Customer;
import com.dvd.ckp.domain.Distribute;
import com.dvd.ckp.domain.Param;
import com.dvd.ckp.domain.Price;
import com.dvd.ckp.domain.Rent;
import com.dvd.ckp.domain.RentEquiment;
import com.dvd.ckp.domain.Staff;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.DateTimeUtils;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StringUtils;
import com.dvd.ckp.utils.StyleUtils;

/**
 *
 * @author viettx
 */
public class RentEquipmentController extends GenericForwardComposer<Component> {

	/**
	 *
	 */
	private static final long serialVersionUID = 457994854668836097L;
	private static final Logger logger = Logger.getLogger(RentEquipmentController.class);
	ServletContext context;
	@WireVariable
	protected ConstructionService constructionService;
	@WireVariable
	protected CustomerService customerService;
	@WireVariable
	protected StaffServices staffServices;

	@WireVariable
	protected RentServices rentServices;

	@WireVariable
	protected UtilsService utilsServices;

	@WireVariable
	protected ContractService contractServices;

	@WireVariable
	protected DistributeService distributeService;

	@Wire
	private Grid gridRent;

	// Model grid in window bill
	ListModelList<RentEquiment> listDataModel;
	private List<RentEquiment> lstRents;

	private List<Construction> lstConstructions;
	private List<Customer> lstCustomer;
	private List<Param> listRentType;
	private List<Distribute> listDistribute;

	private List<Staff> listStaff;

	List<Price> listPriceByContact;

	private Construction defaultConstruction;
	private Customer defaultCustomer;
	private Param defaultParam;
	private Staff defaultStaff;
	private Distribute distribute;
	// Vi tri cac column trong grid

	private final int rentType = 1;
	private final int indexDistribute = 2;
	private final int customer = 3;
	private final int construction = 4;
	private final int majority = 5;
	private final int monitoring = 6;
	private final int indexStartDate = 7;
	private final int indexEndDate = 8;
	private final int aveagePrice = 9;
	private static boolean isInsert = false;

	@Wire
	private Combobox cbFilterCustomer;
	private MyListModel<Customer> modelListCustomer;
	@Wire
	private Combobox cbFilterConstruction;
	private MyListModel<Construction> modelListConstruction;
	@Wire
	private Datebox dtFilterStartDate;
	@Wire
	private Datebox dtFilterEndDate;

	Double price = null;
	Combobox cbCustomer = null;
	Combobox cbxConstruction = null;
	Combobox cbxMajority = null;
	Combobox cbxMonitoring = null;
	Datebox startDate = null;
	Datebox endDate = null;
	Doublebox averageValue = null;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		context = Sessions.getCurrent().getWebApp().getServletContext();
		// khai bao services
		constructionService = (ConstructionService) SpringUtil.getBean(SpringConstant.CONSTRUCTION_SERVICES);
		customerService = (CustomerService) SpringUtil.getBean(SpringConstant.CUSTOMER_SERVICES);
		rentServices = (RentServices) SpringUtil.getBean(SpringConstant.RENT_SERVICES);
		contractServices = (ContractService) SpringUtil.getBean(SpringConstant.CONTRACT_SERVICES);
		utilsServices = (UtilsService) SpringUtil.getBean(SpringConstant.UTILS_SERVICES);
		staffServices = (StaffServices) SpringUtil.getBean(SpringConstant.STAFF_SERVICES);

		distributeService = (DistributeService) SpringUtil.getBean(SpringConstant.DISTRIBUTE_SERVICES);

		// list danh sach cong trinh
		lstConstructions = new ArrayList<>();
		List<Construction> lstCon = constructionService.getConstructionActive();
		if (lstCon != null && !lstCon.isEmpty()) {
			lstConstructions.addAll(lstCon);
		}

		// list danh sach khach hang
		lstCustomer = new ArrayList<>();
		List<Customer> lstCus = customerService.getCustomerActive();
		if (lstCus != null && !lstCus.isEmpty()) {
			lstCustomer.addAll(lstCus);
		}

		// list loai thue
		listRentType = new ArrayList<>();
		List<Param> listType = utilsServices.getParamByKey(com.dvd.ckp.utils.Constants.RENT_TYPE);
		if (listType != null && !listType.isEmpty()) {
			listRentType.addAll(listType);
		}

		// list nhan vien
		listStaff = new ArrayList<>();
		List<Staff> lstStaff = staffServices.getAllData();
		if (lstStaff != null && !lstStaff.isEmpty()) {
			listStaff.addAll(lstStaff);
		}
		// list can phan phoi
		listDistribute = new ArrayList<>();
		List<Distribute> lstDistribute = distributeService.getDistributeActive();
		if (lstDistribute != null && !lstDistribute.isEmpty()) {
			listDistribute.addAll(lstDistribute);
			// for (Distribute item : lstDistribute) {
			// item.setDistributeName(item.getDistributeCode() + "-" +
			// item.getDistributeName());
			// }
		}

		listPriceByContact = contractServices.getAllPrice();
		modelListCustomer = new MyListModel<>(lstCustomer);
		cbFilterCustomer.setModel(modelListCustomer);

		modelListConstruction = new MyListModel<>(lstConstructions);
		cbFilterConstruction.setModel(modelListConstruction);

		// list danh sach thue
		lstRents = new ArrayList<>();

		List<RentEquiment> lstData = rentServices.getAllRentPumps();
		if (lstData != null && !lstData.isEmpty()) {

			lstRents.addAll(lstData);
		}

		// cong trinh default
		defaultConstruction = new Construction();
		defaultConstruction.setConstructionId(-1l);
		defaultConstruction.setConstructionName(Labels.getLabel("option"));
		lstConstructions.add(0, defaultConstruction);

		// khach hang default
		defaultCustomer = new Customer();
		defaultCustomer.setCustomerId(-1l);
		defaultCustomer.setCustomerName(Labels.getLabel("option"));
		lstCustomer.add(0, defaultCustomer);

		// pump type default
		defaultParam = new Param();
		defaultParam.setParamValue(-1l);
		defaultParam.setParamName(Labels.getLabel("option"));
		listRentType.add(0, defaultParam);

		// staff type default
		defaultStaff = new Staff();
		defaultStaff.setStaffId(-1l);
		defaultStaff.setStaffName(Labels.getLabel("option"));
		listStaff.add(0, defaultStaff);

		distribute = new Distribute();
		distribute.setDistributeId(-1l);
		distribute.setDistributeName(Labels.getLabel("option"));
		listDistribute.add(0, distribute);
		// set model
		listDataModel = new ListModelList(lstRents);
		gridRent.setModel(listDataModel);
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
		// onChangeData(lstCell);
		RentEquiment c = rowSelected.getValue();
		setDataConstruction(lstCell, getConstructionDefault(c.getConstructionID()), construction);
		setDataCustomer(lstCell, getCustomerDefault(c.getCustomerID()), customer);
		setDataRentType(lstCell, getRentTypeDefault(c.getRentType()), rentType);
		StyleUtils.setEnableComponent(lstCell, 4);

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
	 * Save
	 *
	 * @param event
	 */
	public void onSave(ForwardEvent event) {
		Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
		List<Component> lstCell = rowSelected.getChildren();
		if (!valiDate(lstCell)) {
			RentEquiment value = rowSelected.getValue();
			getDataInRow(lstCell, value);
			value.setStatus(1);
			save(value, lstCell);
		}

	}

	private void save(RentEquiment value, List<Component> lstCell) {
		BigInteger rentID = null;
		if (isInsert) {
			value.setStatus(1);
			rentServices.insert(value);
			lstRents.add(value);
			rentID = rentServices.getMaxID();
			Rent rent = calculateAveragePriceOfDay(rentID.longValue());
			onChangeData(rent, lstCell);
			StyleUtils.setDisableComponent(lstCell, 4);

		} else {
			value.setCreateDate(new Date());
			rentServices.update(value);
			calculateAveragePriceOfDay(value.getRentID());
			reloadGrid();
		}

		isInsert = false;
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
							RentEquiment item = rowSelected.getValue();
							getDataInRow(lstCell, item);
							item.setStatus(0);
							rentServices.delete(item);
							lstRents.remove(getIndex(item.getRentID()));
							reloadGrid();
						}
					}
				});

	}

	/**
	 * Add row
	 */
	public void onAdd(ForwardEvent event) {
		RentEquiment rent = new RentEquiment();
		rent.setAveragePrice(0d);
		rent.setStatus(1);
		listDataModel.add(Constants.FIRST_INDEX, rent);
		gridRent.setActivePage(Constants.FIRST_INDEX);
		gridRent.setModel(listDataModel);
		gridRent.renderAll();
		List<Component> lstCell = gridRent.getRows().getChildren().get(0).getChildren();
		setDataDefaultInGrid();
		StyleUtils.setEnableComponent(lstCell, 4);
		// onChangeData(lstCell);
		isInsert = true;
	}

	/**
	 * Get object customer
	 *
	 * @param lstCell
	 * @return
	 */
	private void getDataInRow(List<Component> lstCell, RentEquiment value) {

		Component component;

		Combobox cbrentType = null;
		Combobox cbDistribute = null;
		Combobox cbCustomer = null;
		Combobox cbxConstruction = null;
		Combobox cbxMajority = null;
		Combobox cbxMonitoring = null;
		Datebox startDate = null;
		Datebox endDate = null;

		// loai thue bom
		component = lstCell.get(rentType).getFirstChild();
		if (component != null && component instanceof Textbox) {
			cbrentType = (Combobox) component;
			value.setRentType(cbrentType.getSelectedItem().getValue());

		}

		component = lstCell.get(indexDistribute).getFirstChild();
		if (component != null && component instanceof Textbox) {
			cbDistribute = (Combobox) component;
			value.setDistribute(cbDistribute.getSelectedItem().getValue());

		}
		// Khach hang
		component = lstCell.get(customer).getFirstChild();
		if (component != null && component instanceof Textbox) {
			cbCustomer = (Combobox) component;
			Long customerID = cbCustomer.getSelectedItem().getValue();
			value.setCustomerID(customerID);

		}
		// Cong trinh
		component = lstCell.get(construction).getFirstChild();
		if (component != null && component instanceof Combobox) {
			cbxConstruction = (Combobox) component;
			Long constructtion = cbxConstruction.getSelectedItem().getValue();
			value.setConstructionID(constructtion);

		}
		// To truong
		component = lstCell.get(majority).getFirstChild();
		if (component != null && component instanceof Combobox) {
			cbxMajority = (Combobox) component;
			Long lnMajority = null;
			if (cbxMajority.getSelectedItem() != null) {
				lnMajority = cbxMajority.getSelectedItem().getValue();
			}
			value.setMajority(lnMajority);

		}

		// Giam sat
		component = lstCell.get(monitoring).getFirstChild();
		if (component != null && component instanceof Combobox) {
			cbxMonitoring = (Combobox) component;
			Long lnMonitoring = null;
			if (cbxMonitoring.getSelectedItem() != null) {
				lnMonitoring = cbxMonitoring.getSelectedItem().getValue();

			}
			value.setMonitoring(lnMonitoring);

		}

		// Ngay bat dau
		component = lstCell.get(indexStartDate).getFirstChild();
		if (component != null && component instanceof Datebox) {
			startDate = (Datebox) component;
			Date dtStartDate = startDate.getValue();
			value.setStartDate(dtStartDate);

		}
		// Ngay ket thuc
		component = lstCell.get(indexEndDate).getFirstChild();
		if (component != null && component instanceof Datebox) {
			endDate = (Datebox) component;
			Date dtEndDate = endDate.getValue();
			value.setEndDate(dtEndDate);

		}

		// component = lstCell.get(aveageValue).getFirstChild();
		// if (component != null && component instanceof Doublebox) {
		// averageValue = (Doublebox) component;
		// Double priceValue = calculateAveragePriceOfDay();
		// averageValue.setValue(priceValue);
		// value.setAveragePrice(priceValue);
		// }

	}

	/**
	 * Reload grid
	 */
	private void reloadGrid() {
		lstRents.clear();
		List<RentEquiment> lstData = rentServices.getAllRentPumps();
		if (lstData != null && !lstData.isEmpty()) {
			lstRents.addAll(lstData);
		}
		listDataModel = new ListModelList(lstRents);
		gridRent.setModel(listDataModel);
		setDataDefaultInGrid();
	}

	public void onChange$cbFilterCustomer() throws Exception {
		RentEquiment rent = new RentEquiment();
		Long customerID = null;
		if (cbFilterCustomer.getSelectedItem() != null) {
			customerID = cbFilterCustomer.getSelectedItem().getValue();
		}
		rent.setCustomerID(customerID);

		Long constructionID = null;
		if (cbFilterConstruction.getSelectedItem() != null) {
			constructionID = cbFilterConstruction.getSelectedItem().getValue();
		}
		rent.setConstructionID(constructionID);
		Date startDate = null;
		if (dtFilterStartDate.getValue() != null) {
			startDate = dtFilterStartDate.getValue();
		}
		rent.setStartDate(startDate);

		Date endDate = null;
		if (dtFilterEndDate.getValue() != null) {
			endDate = dtFilterEndDate.getValue();
		}
		rent.setEndDate(endDate);

		filter(rent);
	}

	public void onChange$cbFilterConstruction() throws Exception {

		RentEquiment rent = new RentEquiment();
		Long customerID = null;
		if (cbFilterCustomer.getSelectedItem() != null) {
			customerID = cbFilterCustomer.getSelectedItem().getValue();
		}
		rent.setCustomerID(customerID);

		Long constructionID = null;
		if (cbFilterConstruction.getSelectedItem() != null) {
			constructionID = cbFilterConstruction.getSelectedItem().getValue();
		}
		rent.setConstructionID(constructionID);
		Date startDate = null;
		if (dtFilterStartDate.getValue() != null) {
			startDate = dtFilterStartDate.getValue();
		}
		rent.setStartDate(startDate);

		Date endDate = null;
		if (dtFilterEndDate.getValue() != null) {
			endDate = dtFilterEndDate.getValue();
		}
		rent.setEndDate(endDate);

		filter(rent);
	}

	public void onChange$dtFilterStartDate() throws Exception {
		RentEquiment rent = new RentEquiment();
		Long customerID = null;
		if (cbFilterCustomer.getSelectedItem() != null) {
			customerID = cbFilterCustomer.getSelectedItem().getValue();
		}
		rent.setCustomerID(customerID);

		Long constructionID = null;
		if (cbFilterConstruction.getSelectedItem() != null) {
			constructionID = cbFilterConstruction.getSelectedItem().getValue();
		}
		rent.setConstructionID(constructionID);
		Date startDate = null;
		if (dtFilterStartDate.getValue() != null) {
			startDate = dtFilterStartDate.getValue();
		}
		rent.setStartDate(startDate);

		Date endDate = null;
		if (dtFilterEndDate.getValue() != null) {
			endDate = dtFilterEndDate.getValue();
		}
		rent.setEndDate(endDate);

		filter(rent);
	}

	public void onChange$dtFilterEndDate() throws Exception {
		RentEquiment rent = new RentEquiment();
		Long customerID = null;
		if (cbFilterCustomer.getSelectedItem() != null) {
			customerID = cbFilterCustomer.getSelectedItem().getValue();
		}
		rent.setCustomerID(customerID);

		Long constructionID = null;
		if (cbFilterConstruction.getSelectedItem() != null) {
			constructionID = cbFilterConstruction.getSelectedItem().getValue();
		}
		rent.setConstructionID(constructionID);
		Date startDate = null;
		if (dtFilterStartDate.getValue() != null) {
			startDate = dtFilterStartDate.getValue();
		}
		rent.setStartDate(startDate);

		Date endDate = null;
		if (dtFilterEndDate.getValue() != null) {
			endDate = dtFilterEndDate.getValue();
		}
		rent.setEndDate(endDate);

		filter(rent);
	}

	private void filter(RentEquiment rent) throws Exception {
		List<RentEquiment> vlstRent = new ArrayList<>();
		if (lstRents != null && !lstRents.isEmpty() && rent != null) {
			if (rent.getCustomerID() == null && rent.getConstructionID() == null && rent.getStartDate() == null
					&& rent.getEndDate() == null) {
				vlstRent.addAll(lstRents);

			} else {
				for (RentEquiment c : lstRents) {
					// tim theo khach hang va cong trinh
					if (rent.getCustomerID() != null && rent.getConstructionID() != null && rent.getStartDate() == null
							&& rent.getEndDate() == null) {
						if (rent.getCustomerID().equals(c.getCustomerID())
								&& rent.getConstructionID().equals(c.getConstructionID())) {
							vlstRent.add(c);
						}
					}
					// tim theo khach hang va cong trinh va ngay bat dau
					if (rent.getCustomerID() != null && rent.getConstructionID() != null && rent.getStartDate() != null
							&& rent.getEndDate() == null) {
						String startDate = DateTimeUtils.convertDateToString(rent.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String startDateData = DateTimeUtils.convertDateToString(c.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						if (rent.getCustomerID().equals(c.getCustomerID())
								&& rent.getConstructionID().equals(c.getConstructionID())
								&& startDate.equals(startDateData)) {
							vlstRent.add(c);
						}
					}

					// tim theo khach hang va cong trinh va ngay ket thuc
					if (rent.getCustomerID() != null && rent.getConstructionID() != null && rent.getStartDate() == null
							&& rent.getEndDate() != null) {
						String endDate = DateTimeUtils.convertDateToString(rent.getEndDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String endDateData = DateTimeUtils.convertDateToString(c.getEndDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						if (rent.getCustomerID().equals(c.getCustomerID())
								&& rent.getConstructionID().equals(c.getConstructionID())
								&& endDate.equals(endDateData)) {
							vlstRent.add(c);
						}
					} // tim theo khachs hang
					else if (rent.getCustomerID() != null && rent.getConstructionID() == null
							&& rent.getStartDate() == null && rent.getEndDate() == null) {
						if (rent.getCustomerID().equals(c.getCustomerID())) {
							vlstRent.add(c);
						}
					} // tim theo cong trinh
					else if (rent.getCustomerID() == null && rent.getConstructionID() != null
							&& rent.getStartDate() == null && rent.getEndDate() == null) {
						if (rent.getConstructionID().equals(c.getConstructionID())) {
							vlstRent.add(c);
						}
					} // Tim theo ngay bat dau
					else if (rent.getCustomerID() == null && rent.getConstructionID() == null
							&& rent.getStartDate() != null && rent.getEndDate() == null) {
						String startDate = DateTimeUtils.convertDateToString(rent.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String startDateData = DateTimeUtils.convertDateToString(c.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						if (startDate.equals(startDateData)) {
							vlstRent.add(c);
						}
					} // Tim theo ngay ket thuc
					else if (rent.getCustomerID() == null && rent.getConstructionID() == null
							&& rent.getStartDate() == null && rent.getEndDate() != null) {
						String endDate = DateTimeUtils.convertDateToString(rent.getEndDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String endDateData = DateTimeUtils.convertDateToString(c.getEndDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						if (endDate.equals(endDateData)) {
							vlstRent.add(c);
						}
					} // Tim theo khach hang va ngay bat dau
					else if (rent.getCustomerID() != null && rent.getConstructionID() == null
							&& rent.getStartDate() != null && rent.getEndDate() == null) {
						String startDate = DateTimeUtils.convertDateToString(rent.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String startDateData = DateTimeUtils.convertDateToString(c.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						if (rent.getCustomerID().equals(c.getCustomerID()) && startDate.equals(startDateData)) {
							vlstRent.add(c);
						}
					} // Tim theo ngay bat dau va ngay ket thuc
					else if (rent.getCustomerID() == null && rent.getConstructionID() == null
							&& rent.getStartDate() != null && rent.getEndDate() != null) {
						String startDate = DateTimeUtils.convertDateToString(rent.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String startDateData = DateTimeUtils.convertDateToString(c.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String endDate = DateTimeUtils.convertDateToString(rent.getEndDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String endDateData = DateTimeUtils.convertDateToString(c.getEndDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						if (startDate.equals(startDateData) && endDate.equals(endDateData)) {
							vlstRent.add(c);
						}
					} // Tim theo khach hang ngay bat dau va ngay ket thuc
					else if (rent.getCustomerID() != null && rent.getConstructionID() == null
							&& rent.getStartDate() != null && rent.getEndDate() != null) {
						String startDate = DateTimeUtils.convertDateToString(rent.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String startDateData = DateTimeUtils.convertDateToString(c.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String endDate = DateTimeUtils.convertDateToString(rent.getEndDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String endDateData = DateTimeUtils.convertDateToString(c.getEndDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						if (rent.getCustomerID().equals(c.getCustomerID()) && startDate.equals(startDateData)
								&& endDate.equals(endDateData)) {
							vlstRent.add(c);
						}
					} // Tim theo cong trih ngay bat dau va ngay ket thuc
					else if (rent.getCustomerID() == null && rent.getConstructionID() != null
							&& rent.getStartDate() != null && rent.getEndDate() != null) {
						String startDate = DateTimeUtils.convertDateToString(rent.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String startDateData = DateTimeUtils.convertDateToString(c.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String endDate = DateTimeUtils.convertDateToString(rent.getEndDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String endDateData = DateTimeUtils.convertDateToString(c.getEndDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						if (rent.getConstructionID().equals(c.getConstructionID()) && startDate.equals(startDateData)
								&& endDate.equals(endDateData)) {
							vlstRent.add(c);
						}
					} // Tim theo cong trinh va ngay bat dau
					else if (rent.getCustomerID() == null && rent.getConstructionID() != null
							&& rent.getStartDate() != null && rent.getEndDate() == null) {
						String startDate = DateTimeUtils.convertDateToString(rent.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String startDateData = DateTimeUtils.convertDateToString(c.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						if (rent.getConstructionID().equals(c.getConstructionID()) && startDate.equals(startDateData)) {
							vlstRent.add(c);
						}
					} // TIm tat ca dieu kien
					else if (rent.getCustomerID() != null && rent.getConstructionID() != null
							&& rent.getStartDate() != null && rent.getEndDate() != null) {
						String startDate = DateTimeUtils.convertDateToString(rent.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String startDateData = DateTimeUtils.convertDateToString(c.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String endDate = DateTimeUtils.convertDateToString(rent.getEndDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String endDateData = DateTimeUtils.convertDateToString(c.getEndDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						if (rent.getCustomerID().equals(c.getCustomerID())
								&& rent.getConstructionID().equals(c.getConstructionID())
								&& startDate.equals(startDateData) && endDate.equals(endDateData)) {
							vlstRent.add(c);
						}
					}
				}
			}
		}

		listDataModel = new ListModelList(vlstRent);
		gridRent.setModel(listDataModel);
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
			MyListModel listDataModelContruction = new MyListModel(lstConstructions);
			listDataModelContruction.setSelection(selectedIndex);
			combobox.setModel(listDataModelContruction);
		}

	}

	// Set data for combobox customer
	private void setDataCustomer(List<Component> lstCell, List<Customer> selectedIndex, int columnIndex) {
		Combobox combobox = null;
		Component component = lstCell.get(columnIndex).getFirstChild();
		if (component != null && component instanceof Combobox) {
			combobox = (Combobox) component;
			MyListModel listDataModelCustomer = new MyListModel(lstCustomer);
			listDataModelCustomer.setSelection(selectedIndex);
			combobox.setModel(listDataModelCustomer);
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
		gridRent.renderAll();
		List<Component> lstRows = gridRent.getRows().getChildren();
		if (lstRows != null && !lstRows.isEmpty()) {
			for (int i = 0; i < lstRows.size(); i++) {
				RentEquiment rentID = listDataModel.get(i);
				Component row = lstRows.get(i);
				List<Component> lstCell = row.getChildren();
				setDataCustomer(lstCell, getCustomerDefault(rentID.getCustomerID()), customer);
				setDataConstruction(lstCell, getConstructionDefault(rentID.getConstructionID()), construction);
				setDataRentType(lstCell, getRentTypeDefault(rentID.getRentType()), rentType);
				setDataMajority(lstCell, getMajorityDefault(rentID.getMajority()), majority);
				setDataMonitoring(lstCell, getMonitoringDefault(rentID.getMonitoring()), monitoring);
				setDataDistribute(lstCell, getDistributeDefault(rentID.getDistribute()), indexDistribute);
			}
		}
	}

	/**
	 * Action export excel data
	 *
	 * @param event
	 */
	public void onExport(ForwardEvent event) {

		Messagebox.show(Labels.getLabel("not.support"), Labels.getLabel("comfirm"), Messagebox.OK,
				Messagebox.INFORMATION);
	}

	/**
	 * Action import excel for bills
	 *
	 * @param event
	 */
	public void onImport(ForwardEvent event) {
		Messagebox.show(Labels.getLabel("not.support"), Labels.getLabel("comfirm"), Messagebox.OK,
				Messagebox.INFORMATION);
	}

	/**
	 * Lay thon tin cong trinh
	 *
	 * @param constructionId
	 * @return
	 */
	private Construction getConstruction(Long constructionId) {

		if (lstConstructions != null && !lstConstructions.isEmpty()) {
			for (Construction construction : lstConstructions) {
				if (constructionId != null && constructionId.equals(construction.getConstructionId())) {
					return construction;

				}
			}
		}
		return null;
	}

	private int getIndex(Long rentPumpID) {
		if (lstRents != null && !lstRents.isEmpty()) {
			for (RentEquiment item : lstRents) {
				if (rentPumpID.equals(item.getRentID())) {
					return lstRents.indexOf(item);
				}
			}
		}
		return -1;
	}

	private Param getRentType(Long paramID) {
		if (listRentType != null && !listRentType.isEmpty()) {
			for (Param item : listRentType) {
				if (paramID.equals(item.getParamValue())) {
					return item;
				}
			}
		}
		return null;
	}

	/**
	 * set data for combobox pump type in windown bill detail
	 *
	 * @param lstCell
	 * @param selectedIndex
	 * @param columnIndex
	 */
	private void setDataRentType(List<Component> lstCell, List<Param> selectedIndex, int columnIndex) {
		Combobox combobox = null;
		Component component = lstCell.get(columnIndex).getFirstChild();
		if (component != null && component instanceof Combobox) {
			combobox = (Combobox) component;
			MyListModel listDataModelType = new MyListModel(listRentType);
			listDataModelType.setSelection(selectedIndex);
			combobox.setModel(listDataModelType);

		}

	}

	// get pump type default
	private List<Param> getRentTypeDefault(Long typePumpId) {
		List<Param> paramSelected = new ArrayList<>();
		if (typePumpId != null && listRentType != null && !listRentType.isEmpty()) {
			for (Param param : listRentType) {
				if (typePumpId.equals(param.getParamValue())) {
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

	private Rent calculateAveragePriceOfDay(Long rentID) {
		// Long customerID = null;
		// Long constructtion = null;
		// Date dtStartDate = null;
		// Date dtEndDate = null;
		// if (cbCustomer != null) {
		// customerID = cbCustomer.getSelectedItem().getValue();
		// }
		// if (cbxConstruction != null) {
		// constructtion = cbxConstruction.getSelectedItem().getValue();
		// }
		// if (startDate != null) {
		// dtStartDate = startDate.getValue();
		// }
		// if (endDate != null) {
		// dtEndDate = endDate.getValue();
		// }
		// logger.debug("{customerID:" + customerID + "," + "}");
		// if (customerID == -1 || constructtion == -1 || dtStartDate == null ||
		// dtEndDate == null) {
		// return 0d;
		// }
		// int lastDayOfMonth = DateTimeUtils.getLastDayOfMonth(dtStartDate);
		// Long diff = DateTimeUtils.getDifferenceDay(dtStartDate, dtEndDate);
		// Double priceOfContact = 0d;
		//
		// Construction item = getConstruction(constructtion);
		// if (item != null) {
		// Price price = getPrices(item.getContractId());
		// if (price != null && price.getPriceRent() != null) {
		// priceOfContact = price.getPriceRent();
		//
		// }
		// }
		// Double price = diff * priceOfContact / lastDayOfMonth;
		List<Rent> listData = rentServices.storeRent(rentID);

		return listData.get(0);
	}

	private void onChangeData(Rent rent, List<Component> lstCell) {

		Component component;

		component = lstCell.get(aveagePrice).getFirstChild();
		if (component != null && component instanceof Doublebox) {
			averageValue = (Doublebox) component;
			averageValue.setValue(rent.getRevenue());
		}

	}

	private void setDataMajority(List<Component> lstCell, List<Staff> selectedIndex, int columnIndex) {
		Combobox combobox = null;
		Component component = lstCell.get(columnIndex).getFirstChild();
		if (component != null && component instanceof Combobox) {
			combobox = (Combobox) component;

			MyListModel listDataModelMajority = new MyListModel(listStaff);
			listDataModelMajority.setSelection(selectedIndex);
			combobox.setModel(listDataModelMajority);

		}

	}

	// get pump type default
	private List<Staff> getMajorityDefault(Long majorityId) {
		List<Staff> paramSelected = new ArrayList<>();

		if (majorityId != null && listStaff != null && !listStaff.isEmpty()) {
			for (Staff param : listStaff) {
				if (majorityId.equals(param.getStaffId())) {
					paramSelected.add(param);
					break;
				}
			}
		}
		if (paramSelected.isEmpty()) {
			paramSelected.add(defaultStaff);
		}
		return paramSelected;
	}

	private void setDataMonitoring(List<Component> lstCell, List<Staff> selectedIndex, int columnIndex) {
		Combobox combobox = null;
		Component component = lstCell.get(columnIndex).getFirstChild();

		if (component != null && component instanceof Combobox) {
			combobox = (Combobox) component;
			MyListModel listDataModelMonitor = new MyListModel(listStaff);
			listDataModelMonitor.setSelection(selectedIndex);
			combobox.setModel(listDataModelMonitor);

		}

	}

	// get pump type default
	private List<Staff> getMonitoringDefault(Long monitoringId) {
		List<Staff> paramSelected = new ArrayList<>();
		// List<Staff> lstData = getListMajority();
		if (monitoringId != null && listStaff != null && !listStaff.isEmpty()) {
			for (Staff param : listStaff) {
				if (monitoringId.equals(param.getStaffId())) {
					paramSelected.add(param);
					break;
				}
			}
		}
		if (paramSelected.isEmpty()) {
			paramSelected.add(defaultStaff);
		}
		return paramSelected;
	}

	/**
	 * viettx bo sung 06/01/2018 validate compoment
	 */
	private boolean valiDate(List<Component> lstCell) {

		Component component;
		Component componentLast;

		Combobox cbxRentType = null;
		Combobox cbxDistribute = null;
		Combobox cbCustomer = null;
		Combobox cbxConstruction = null;
		Combobox cbxMajority = null;
		Combobox cbxMonitoring = null;

		Datebox dtFromDate = null;
		Datebox dtToDate = null;

		boolean isCheckFromDate = false;

		boolean isFalse = false;

		// loai bom
		component = lstCell.get(rentType).getFirstChild();
		componentLast = lstCell.get(rentType).getLastChild();
		if (component != null && component instanceof Textbox) {
			cbxRentType = (Combobox) component;
			Long value = null;
			if (cbxRentType.getSelectedItem() != null) {
				value = cbxRentType.getSelectedItem().getValue();
			}
			Label mesage = (Label) componentLast;
			if (value == null || value.equals(-1l)) {

				mesage.setValue(Labels.getLabel("validate.rent.type.empty"));
				mesage.setHflex("1");
				cbxRentType.focus();
				isFalse = true;
			} else {
				mesage.setVisible(false);
				mesage.setHflex("0");
				mesage.setValue("");

			}
		}
		// ten can phan phoi
		component = lstCell.get(indexDistribute).getFirstChild();
		componentLast = lstCell.get(indexDistribute).getLastChild();
		if (component != null && component instanceof Textbox) {
			cbxDistribute = (Combobox) component;
			Long value = null;
			if (cbxDistribute.getSelectedItem() != null) {
				value = cbxDistribute.getSelectedItem().getValue();
			}
			Label mesage = (Label) componentLast;
			if (value == null || value.equals(-1l)) {

				mesage.setValue(Labels.getLabel("validate.distribute.empty"));
				mesage.setHflex("1");
				cbxDistribute.focus();
				isFalse = true;
			} else {
				mesage.setVisible(false);
				mesage.setHflex("0");
				mesage.setValue("");

			}
		}
		// Khach hang
		component = lstCell.get(customer).getFirstChild();
		componentLast = lstCell.get(customer).getLastChild();
		if (component != null && component instanceof Textbox) {
			cbCustomer = (Combobox) component;
			Long value = null;
			if (cbCustomer.getSelectedItem() != null) {
				value = cbCustomer.getSelectedItem().getValue();
			}
			Label mesage = (Label) componentLast;
			if (value == null || value.equals(-1l)) {

				cbCustomer.setHflex("1");
				mesage.setValue(Labels.getLabel("validate.customer.empty"));
				mesage.setVisible(true);
				mesage.setHflex("1");
				cbCustomer.focus();
				isFalse = true;
			} else {
				mesage.setVisible(false);
				mesage.setHflex("0");
				mesage.setValue("");

			}

		}
		// Cong trinh
		component = lstCell.get(construction).getFirstChild();
		componentLast = lstCell.get(construction).getLastChild();
		if (component != null && component instanceof Combobox) {
			cbxConstruction = (Combobox) component;

			Long value = null;
			if (cbxConstruction.getSelectedItem() != null) {
				value = cbxConstruction.getSelectedItem().getValue();
			}
			Label mesage = (Label) componentLast;
			if (value == null || value.equals(-1l)) {

				mesage.setValue(Labels.getLabel("validate.construction.empty"));
				mesage.setVisible(true);
				mesage.setHflex("1");
				cbxConstruction.focus();
				isFalse = true;
			} else {
				mesage.setVisible(false);
				mesage.setHflex("0");
				mesage.setValue("");

			}

		}

		// to truong
		component = lstCell.get(majority).getFirstChild();
		componentLast = lstCell.get(majority).getLastChild();
		if (component != null && component instanceof Combobox) {
			cbxMajority = (Combobox) component;

			Long value = null;
			if (cbxMajority.getSelectedItem() != null) {
				value = cbxMajority.getSelectedItem().getValue();
			}
			Label mesage = (Label) componentLast;
			if (value == null || value.equals(-1l)) {

				mesage.setValue(Labels.getLabel("validate.majority.empty"));
				mesage.setVisible(true);
				mesage.setHflex("1");
				cbxMajority.focus();
				isFalse = true;
			} else {
				mesage.setVisible(false);
				mesage.setHflex("0");
				mesage.setValue("");

			}

		}

		// giam sat
		component = lstCell.get(monitoring).getFirstChild();
		componentLast = lstCell.get(monitoring).getLastChild();
		if (component != null && component instanceof Combobox) {
			cbxMonitoring = (Combobox) component;

			Long value = null;
			if (cbxMonitoring.getSelectedItem() != null) {
				value = cbxMonitoring.getSelectedItem().getValue();
			}
			Label mesage = (Label) componentLast;
			if (value == null || value.equals(-1l)) {
				mesage.setValue(Labels.getLabel("validate.monitoring.empty"));
				mesage.setVisible(true);
				mesage.setHflex("1");
				cbxMonitoring.focus();
				isFalse = true;
			} else {
				mesage.setVisible(false);
				mesage.setHflex("0");
				mesage.setValue("");

			}

		}
		// Ngay bat dau
		component = lstCell.get(indexStartDate).getFirstChild();
		componentLast = lstCell.get(indexStartDate).getLastChild();
		if (component != null && component instanceof Datebox) {
			dtFromDate = (Datebox) component;
			Label mesage = (Label) componentLast;
			if (dtFromDate.getValue() == null) {
				mesage.setValue(Labels.getLabel("validate.fromdate.empty"));
				mesage.setVisible(true);
				mesage.setHflex("1");
				dtFromDate.focus();
				isFalse = true;
				isCheckFromDate = true;
			} else {
				mesage.setVisible(false);
				mesage.setHflex("0");
				mesage.setValue("");

			}

		}

		// Ngay ket thuc
		component = lstCell.get(indexEndDate).getFirstChild();
		componentLast = lstCell.get(indexEndDate).getLastChild();
		if (component != null && component instanceof Datebox) {
			dtToDate = (Datebox) component;
			Label mesage = (Label) componentLast;
			if (dtToDate.getValue() == null) {
				mesage.setValue(Labels.getLabel("validate.todate.empty"));
				mesage.setVisible(true);
				mesage.setHflex("1");
				dtToDate.focus();
				isFalse = true;
				isCheckFromDate = true;
			} else {
				mesage.setVisible(false);
				mesage.setHflex("0");
				mesage.setValue("");

			}

		}

		// Ngay ket thuc nho hon ngay bat dau
		Component componentFromDate = lstCell.get(indexStartDate).getLastChild();
		Component componentToDate = lstCell.get(indexEndDate).getLastChild();

		Label mesageFromDate = (Label) componentFromDate;
		Label mesageToDate = (Label) componentToDate;
		if (!isCheckFromDate) {
			if (dtFromDate.getValue() != null && dtToDate.getValue() != null
					&& DateTimeUtils.compareDate(dtFromDate.getValue(), dtToDate.getValue())) {
				mesageFromDate.setValue(Labels.getLabel("validate.compare.start.date.end.date"));
				mesageFromDate.setVisible(true);
				mesageFromDate.setHflex("1");

				mesageToDate.setValue(Labels.getLabel("validate.compare.end.date.start.date"));
				mesageToDate.setVisible(true);
				mesageToDate.setHflex("1");
				dtToDate.focus();
				isFalse = true;
				isCheckFromDate = true;
			} else {
				mesageFromDate.setVisible(false);
				mesageFromDate.setHflex("0");
				mesageFromDate.setValue("");

				mesageToDate.setVisible(false);
				mesageToDate.setHflex("0");
				mesageToDate.setValue("");

			}
		}
		// check ngay bat dau va ngay ket thuc cung 1 thang
		if (!isCheckFromDate) {
			if (dtFromDate.getValue() != null && dtToDate.getValue() != null
					&& !DateTimeUtils.compareMonth(dtFromDate.getValue(), dtToDate.getValue())) {
				mesageFromDate.setValue(Labels.getLabel("validate.compare.month"));
				mesageFromDate.setVisible(true);
				mesageFromDate.setHflex("1");

				mesageToDate.setValue(Labels.getLabel("validate.compare.month"));
				mesageToDate.setVisible(true);
				mesageToDate.setHflex("1");
				dtToDate.focus();
				isFalse = true;
			} else {
				mesageFromDate.setVisible(false);
				mesageFromDate.setHflex("0");
				mesageFromDate.setValue("");

				mesageToDate.setVisible(false);
				mesageToDate.setHflex("0");
				mesageToDate.setValue("");

			}
		}

		return isFalse;
	}

	private void setDataDistribute(List<Component> lstCell, List<Distribute> selectedIndex, int columnIndex) {
		Combobox combobox = null;
		Component component = lstCell.get(columnIndex).getFirstChild();
		if (component != null && component instanceof Combobox) {
			combobox = (Combobox) component;

			MyListModel listDataModel = new MyListModel(listDistribute);
			listDataModel.setSelection(selectedIndex);
			combobox.setModel(listDataModel);

		}

	}

	// get Distribute default
	private List<Distribute> getDistributeDefault(Long distributeId) {
		List<Distribute> paramSelected = new ArrayList<>();

		if (distributeId != null && listDistribute != null && !listDistribute.isEmpty()) {
			for (Distribute param : listDistribute) {
				if (distributeId != null && distributeId.equals(param.getDistributeId())) {
					paramSelected.add(param);
					break;
				}
			}
		}
		if (paramSelected.isEmpty()) {
			paramSelected.add(distribute);
		}

		return paramSelected;
	}

}
