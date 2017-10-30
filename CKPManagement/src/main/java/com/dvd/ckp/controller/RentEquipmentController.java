/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

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
import org.zkoss.zk.ui.event.Events;
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
import com.dvd.ckp.business.service.RentServices;
import com.dvd.ckp.business.service.UtilsService;
import com.dvd.ckp.domain.Construction;
import com.dvd.ckp.domain.Contract;
import com.dvd.ckp.domain.Customer;
import com.dvd.ckp.domain.Param;
import com.dvd.ckp.domain.Price;
import com.dvd.ckp.domain.RentEquiment;
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
	protected RentServices rentServices;

	@WireVariable
	protected UtilsService utilsServices;

	@WireVariable
	protected ContractService contractServices;

	@Wire
	private Grid gridRent;

	// Model grid in window bill
	ListModelList<RentEquiment> listDataModel;
	private List<RentEquiment> lstRents;

	private List<Construction> lstConstructions;
	private List<Customer> lstCustomer;
	private List<Contract> listContact;
	private List<Param> listRentType;

	List<Price> listPriceByContact;

	private Construction defaultConstruction;
	private Customer defaultCustomer;
	private Param defaultParam;
	// Vi tri cac column trong grid

	private final int rentType = 1;
	private final int customer = 2;
	private final int construction = 3;
	private final int indexStartDate = 4;
	private final int indexEndDate = 5;
	private final int aveagePrice = 6;
	private final int aveageValue = 7;

	private static boolean isInsert = false;

	@Wire
	private Combobox cbFilterCustomer;
	private ListModelList<Customer> modelListCustomer;
	@Wire
	private Combobox cbFilterConstruction;
	private ListModelList<Construction> modelListConstruction;
	@Wire
	private Datebox dtFilterStartDate;
	@Wire
	private Datebox dtFilterEndDate;

	Double price = null;
	Label averagePrice = null;
	Combobox cbCustomer = null;
	Combobox cbxConstruction = null;
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

		listPriceByContact = contractServices.getAllPrice();
		modelListCustomer = new ListModelList<>(lstCustomer);
		cbFilterCustomer.setModel(modelListCustomer);

		modelListConstruction = new ListModelList<>(lstConstructions);
		cbFilterConstruction.setModel(modelListConstruction);

		// list danh sach thue
		lstRents = new ArrayList<>();

		List<RentEquiment> lstData = rentServices.getAllRentPumps();
		if (lstData != null && !lstData.isEmpty()) {
			for (RentEquiment item : lstData) {
				item.setConstructionName(getConstructionByID(item.getConstructionID()));
				item.setCustomerName(getCustomerByID(item.getCustomerID()));
				item.setRentTypeName(getRentType(item.getRentType()).getParamName());
				item.setAveragePriceView(StringUtils.formatPrice(item.getAveragePrice()));

			}
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
		onChangeData(lstCell);
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
		RentEquiment value = rowSelected.getValue();
		getDataInRow(lstCell, value);
		value.setStatus(1);
		save(value);
		StyleUtils.setDisableComponent(lstCell, 4);
		reloadGrid();

	}

	private void save(RentEquiment value) {
		if (isInsert) {
			value.setStatus(1);
			rentServices.insert(value);
			lstRents.add(value);
		} else {
			value.setCreateDate(new Date());
			rentServices.update(value);
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
		listDataModel.add(0, rent);
		gridRent.setModel(listDataModel);
		gridRent.renderAll();
		List<Component> lstCell = gridRent.getRows().getChildren().get(0).getChildren();
		setDataDefaultInGrid();
		StyleUtils.setEnableComponent(lstCell, 4);
		onChangeData(lstCell);
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
		Combobox cbCustomer = null;
		Combobox cbxConstruction = null;
		Datebox startDate = null;
		Datebox endDate = null;

		// loai thue bom
		component = lstCell.get(rentType).getFirstChild();
		if (component != null && component instanceof Textbox) {
			cbrentType = (Combobox) component;
			value.setRentType(cbrentType.getSelectedItem().getValue());

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

		component = lstCell.get(aveageValue).getFirstChild();
		if (component != null && component instanceof Doublebox) {
			averageValue = (Doublebox) component;
			Double priceValue = calculateAveragePriceOfDay();
			averageValue.setValue(priceValue);
			value.setAveragePrice(priceValue);
		}

	}

	/**
	 * Reload grid
	 */
	private void reloadGrid() {
		lstRents.clear();
		List<RentEquiment> lstData = rentServices.getAllRentPumps();
		if (lstData != null && !lstData.isEmpty()) {
			for (RentEquiment item : lstData) {

				item.setConstructionName(getConstructionByID(item.getConstructionID()));
				item.setCustomerName(getCustomerByID(item.getCustomerID()));
				item.setRentTypeName(getRentType(item.getRentType()).getParamName());

				item.setAveragePriceView(StringUtils.formatPrice(item.getAveragePrice()));

			}
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
					}

					// tim theo khachs hang
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
					}
					// Tim theo ngay bat dau
					else if (rent.getCustomerID() == null && rent.getConstructionID() == null
							&& rent.getStartDate() != null && rent.getEndDate() == null) {
						String startDate = DateTimeUtils.convertDateToString(rent.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String startDateData = DateTimeUtils.convertDateToString(c.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						if (startDate.equals(startDateData)) {
							vlstRent.add(c);
						}
					}
					// Tim theo ngay ket thuc
					else if (rent.getCustomerID() == null && rent.getConstructionID() == null
							&& rent.getStartDate() == null && rent.getEndDate() != null) {
						String endDate = DateTimeUtils.convertDateToString(rent.getEndDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String endDateData = DateTimeUtils.convertDateToString(c.getEndDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						if (endDate.equals(endDateData)) {
							vlstRent.add(c);
						}
					}

					// Tim theo khach hang va ngay bat dau
					else if (rent.getCustomerID() != null && rent.getConstructionID() == null
							&& rent.getStartDate() != null && rent.getEndDate() == null) {
						String startDate = DateTimeUtils.convertDateToString(rent.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String startDateData = DateTimeUtils.convertDateToString(c.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						if (rent.getCustomerID().equals(c.getCustomerID()) && startDate.equals(startDateData)) {
							vlstRent.add(c);
						}
					}

					// Tim theo ngay bat dau va ngay ket thuc
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
					}

					// Tim theo khach hang ngay bat dau va ngay ket thuc
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
					}
					// Tim theo cong trih ngay bat dau va ngay ket thuc
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
					}

					// Tim theo cong trinh va ngay bat dau
					else if (rent.getCustomerID() == null && rent.getConstructionID() != null
							&& rent.getStartDate() != null && rent.getEndDate() == null) {
						String startDate = DateTimeUtils.convertDateToString(rent.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						String startDateData = DateTimeUtils.convertDateToString(c.getStartDate(),
								com.dvd.ckp.common.Constants.FORMAT_DATE);
						if (rent.getConstructionID().equals(c.getConstructionID()) && startDate.equals(startDateData)) {
							vlstRent.add(c);
						}
					}

					// TIm tat ca dieu kien
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
			ListModelList listDataModel = new ListModelList(listRentType);
			listDataModel.setSelection(selectedIndex);
			combobox.setModel(listDataModel);

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

	private Double calculateAveragePriceOfDay() {
		Long customerID = null;
		Long constructtion = null;
		Date dtStartDate = null;
		Date dtEndDate = null;
		if (cbCustomer != null) {
			customerID = cbCustomer.getSelectedItem().getValue();
		}
		if (cbxConstruction != null) {
			constructtion = cbxConstruction.getSelectedItem().getValue();
		}
		if (startDate != null) {
			dtStartDate = startDate.getValue();
		}
		if (endDate != null) {
			dtEndDate = endDate.getValue();
		}
		if (customerID == -1 || constructtion == -1 || dtStartDate == null || dtEndDate == null) {
			return 0d;
		}
		int lastDayOfMonth = DateTimeUtils.getLastDayOfMonth(dtStartDate);
		Long diff = DateTimeUtils.getDifferenceDay(dtStartDate, dtEndDate);
		Double priceOfContact = 0d;

		Construction item = getConstruction(constructtion);
		if (item != null) {
			Price price = getPrices(item.getContractId());
			if (listPriceByContact != null && !listPriceByContact.isEmpty()) {
				if (listPriceByContact.get(0).getPriceRent() != null) {
					priceOfContact = price.getPriceRent();
				}
			}
		}
		Double price = diff * priceOfContact / lastDayOfMonth;

		return price;
	}

	private void onChangeData(List<Component> lstCell) {

		Component component;

		component = lstCell.get(aveagePrice).getFirstChild();
		if (component != null && component instanceof Label) {
			averagePrice = (Label) component;
		}

		// Khach hang
		component = lstCell.get(customer).getFirstChild();
		if (component != null && component instanceof Textbox) {
			cbCustomer = (Combobox) component;

		}
		// Cong trinh
		component = lstCell.get(construction).getFirstChild();
		if (component != null && component instanceof Combobox) {
			cbxConstruction = (Combobox) component;
		}

		// Ngay bat dau
		component = lstCell.get(indexStartDate).getFirstChild();
		if (component != null && component instanceof Datebox) {
			startDate = (Datebox) component;
		}
		// Ngay ket thuc
		component = lstCell.get(indexEndDate).getFirstChild();
		if (component != null && component instanceof Datebox) {
			endDate = (Datebox) component;
		}

		// cbCustomer.addEventListener(Events.ON_CHANGE, new
		// EventListener<Event>() {
		//
		// @Override
		// public void onEvent(Event event) throws Exception {
		// // TODO Auto-generated method stub
		// price = calculateAveragePriceOfDay();
		// averagePrice.setValue(StringUtils.formatPrice(price));
		//
		// }
		// });
		cbxConstruction.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				price = calculateAveragePriceOfDay();
				averagePrice.setValue(StringUtils.formatPrice(price));

			}
		});

		startDate.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				price = calculateAveragePriceOfDay();
				averagePrice.setValue(StringUtils.formatPrice(price));

			}
		});

		endDate.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				price = calculateAveragePriceOfDay();
				averagePrice.setValue(StringUtils.formatPrice(price));

			}
		});

	}

	private Price getPrices(Long contactId) {
		if (listPriceByContact != null && !listPriceByContact.isEmpty()) {
			for (Price item : listPriceByContact) {
				if (contactId != null && contactId.equals(item.getContractId())) {
					return item;
				}
			}
		}
		return null;
	}

}
