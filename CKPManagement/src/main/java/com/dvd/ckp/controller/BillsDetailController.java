package com.dvd.ckp.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import com.dvd.ckp.business.service.BillsServices;
import com.dvd.ckp.business.service.LocationServices;
import com.dvd.ckp.business.service.PumpServices;
import com.dvd.ckp.business.service.UtilsService;
import com.dvd.ckp.domain.BillsDetail;
import com.dvd.ckp.domain.CalculatorRevenue;
import com.dvd.ckp.domain.Location;
import com.dvd.ckp.domain.Param;
import com.dvd.ckp.domain.Pumps;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StyleUtils;

public class BillsDetailController extends GenericForwardComposer {
	/**
	 * 
	 */
	private static final Logger LOGGER = Logger.getLogger(BillsDetailController.class);
	private static final long serialVersionUID = -3785065052690864441L;
	@WireVariable
	protected PumpServices pumpServices;
	@WireVariable
	protected UtilsService utilsService;
	@WireVariable
	protected BillsServices billsServices;
	@WireVariable
	protected LocationServices locationServices;

	private Pumps defaultPumps;
	private Param defaultParam;
	private Location defaultLocation;

	// Danh sach loai bom
	private List<Param> lstTypePump;

	// Danh sach loai vi tri
	private List<Param> lstTypeLocation;

	@Wire
	private Grid gridBillsDetail;
	@Wire
	private Window windowAddDetail;

	// Danh sach hoa don chi tiet
	private List<BillsDetail> lstBillDetail;

	// Danh sach vi tri bom
	private List<Location> lstLocation;

	// Model grid in window bill detail
	ListModelList<BillsDetail> listDataModelDetail;

	// Danh sach bom
	private List<Pumps> lstPumps;

	// Vi tri cac column trong grid bills detail
	private final int pumpTypeIdDetail = 1;
	private final int pumpIdDetail = 2;
	private final int locationTypeDetail = 3;
	private final int locationDetail = 4;

	private Longbox txtBillID;

	private Longbox txtConstruction;

	private static int insertOrUpdate = 0;

	private Combobox cbPump = null;
	private Combobox cbPumpType = null;
	private Combobox cbLocation = null;
	private Combobox cbLocationType = null;
	private Doublebox txtQuantity = null;
	private Intbox txtShift = null;
	private Label txtTotal;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);

		pumpServices = (PumpServices) SpringUtil.getBean(SpringConstant.PUMPS_SERVICES);
		utilsService = (UtilsService) SpringUtil.getBean(SpringConstant.UTILS_SERVICES);
		billsServices = (BillsServices) SpringUtil.getBean(SpringConstant.BILL_SERVICES);
		locationServices = (LocationServices) SpringUtil.getBean(SpringConstant.LOCATION_SERVICES);

		// danh sach bom
		lstPumps = pumpServices.getAllListData();

		// danh sach loai bom
		lstTypePump = utilsService.getParamByKey(com.dvd.ckp.utils.Constants.PRAM_PUMP_TYPE);

		// danh sach loai vi tri
		lstTypeLocation = utilsService.getParamByKey(Constants.PRAM_LOCATION_TYPE);

		// danh sach vi tri bom
		lstLocation = locationServices.getListLocation();

		// list danh sach chi tiet hoa don
		lstBillDetail = new ArrayList<>();
		List<BillsDetail> lstData = billsServices.getBillDetail(txtBillID.getValue());
		if (lstData != null && !lstData.isEmpty()) {
			lstBillDetail.addAll(lstData);
		}
		// pump default
		defaultPumps = new Pumps();
		defaultPumps.setPumpsID(-1l);
		defaultPumps.setPumpsName(Labels.getLabel("option"));
		lstPumps.add(0, defaultPumps);

		// pump type default
		defaultParam = new Param();
		defaultParam.setParamValue(-1l);
		defaultParam.setParamName(Labels.getLabel("option"));
		lstTypePump.add(0, defaultParam);
		lstTypeLocation.add(0, defaultParam);

		// location default
		defaultLocation = new Location();
		defaultLocation.setLocationID(-1l);
		defaultLocation.setLocationName(Labels.getLabel("option"));
		lstLocation.add(0, defaultLocation);

		listDataModelDetail = new ListModelList(lstBillDetail);
		gridBillsDetail.setModel(listDataModelDetail);
		setDataDefaultInGridViewDetail();
	}

	/**
	 * Set data for combobox pumps in bill detail
	 * 
	 * @param lstCell
	 * @param selectedIndex
	 * @param columnIndex
	 */
	private void setDataPumpsDetail(List<Component> lstCell, List<Pumps> selectedIndex, int columnIndex) {
		Combobox combobox = null;
		Component component = lstCell.get(columnIndex).getFirstChild();
		if (component != null && component instanceof Combobox) {
			combobox = (Combobox) component;
			ListModelList listDataModel = new ListModelList(lstPumps);
			listDataModel.setSelection(selectedIndex);
			combobox.setModel(listDataModel);

		}

	}

	// get Customer default
	private List<Pumps> getPumpsDefault(Long pumpId) {
		List<Pumps> pumpSelected = new ArrayList<>();
		if (pumpId != null && lstPumps != null && !lstPumps.isEmpty()) {
			for (Pumps pumps : lstPumps) {
				if (pumpId.equals(pumps.getPumpsID())) {
					pumpSelected.add(pumps);
					break;
				}
			}
		}
		if (pumpSelected.isEmpty()) {
			pumpSelected.add(defaultPumps);
		}
		return pumpSelected;
	}

	// get pump type default
	private List<Param> getPumpsTypeDefault(Long typePumpId) {
		List<Param> paramSelected = new ArrayList<>();
		if (typePumpId != null && lstTypePump != null && !lstTypePump.isEmpty()) {
			for (Param param : lstTypePump) {
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

	/**
	 * set data for combobox pump type in windown bill detail
	 * 
	 * @param lstCell
	 * @param selectedIndex
	 * @param columnIndex
	 */
	private void setDataPumpsTypeDetail(List<Component> lstCell, List<Param> selectedIndex, int columnIndex) {
		Combobox combobox = null;
		Component component = lstCell.get(columnIndex).getFirstChild();
		if (component != null && component instanceof Combobox) {
			combobox = (Combobox) component;
			ListModelList listDataModel = new ListModelList(lstTypePump);
			listDataModel.setSelection(selectedIndex);
			combobox.setModel(listDataModel);

		}

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
			ListModelList listDataModel = new ListModelList(lstTypeLocation);
			listDataModel.setSelection(selectedIndex);
			combobox.setModel(listDataModel);

		}

	}

	// get Location type default
	private List<Location> getLocationDefault(Long locationID) {
		List<Location> paramSelected = new ArrayList<>();
		if (locationID != null && lstLocation != null && !lstLocation.isEmpty()) {
			for (Location location : lstLocation) {
				if (locationID.equals(location.getLocationID())) {
					paramSelected.add(location);
					break;
				}
			}
		}
		if (paramSelected.isEmpty()) {
			paramSelected.add(defaultLocation);
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
	private void setLocationDetail(List<Component> lstCell, List<Location> selectedIndex, int columnIndex) {
		Combobox combobox = null;
		Component component = lstCell.get(columnIndex).getFirstChild();
		if (component != null && component instanceof Combobox) {
			combobox = (Combobox) component;
			ListModelList listDataModel = new ListModelList(lstLocation);
			listDataModel.setSelection(selectedIndex);
			combobox.setModel(listDataModel);

		}

	}

	/**
	 * 
	 */
	private void setDataDefaultInGridViewDetail() {
		gridBillsDetail.renderAll();
		List<Component> lstRows = gridBillsDetail.getRows().getChildren();
		if (lstRows != null && !lstRows.isEmpty()) {
			for (int i = 0; i < lstRows.size(); i++) {
				BillsDetail billsDetail = listDataModelDetail.get(i);
				Component row = lstRows.get(i);
				List<Component> lstCell = row.getChildren();
				setDataPumpsDetail(lstCell, getPumpsDefault(billsDetail.getPumpID()), pumpIdDetail);
				setDataPumpsTypeDetail(lstCell, getPumpsTypeDefault(billsDetail.getPumpTypeId()), pumpTypeIdDetail);
				setLocationDetail(lstCell, getLocationDefault(billsDetail.getLocationId()), locationDetail);
				setDataLocationTypeDetail(lstCell, getLocationTypeDefault(billsDetail.getLocationType()), 3);
			}
		}
	}

	public void onAdd(ForwardEvent event) {
		BillsDetail billsDetail = new BillsDetail();
		listDataModelDetail.add(0, billsDetail);
		gridBillsDetail.setModel(listDataModelDetail);
		gridBillsDetail.renderAll();
		List<Component> lstCell = gridBillsDetail.getRows().getChildren().get(0).getChildren();
		onChangeData(lstCell);
		setDataDefaultInGridViewDetail();
		StyleUtils.setEnableComponent(lstCell, 4);
		insertOrUpdate = 1;
	}

	/**
	 * Get object customer
	 *
	 * @param lstCell
	 * @return
	 */
	private BillsDetail getDataInRow(List<Component> lstCell) {
		BillsDetail billsDetail = new BillsDetail();
		Component component;

		Combobox cbPump = null;
		Combobox cbPumpType = null;
		Combobox cbLocation = null;
		Combobox cbLocationType = null;
		Doublebox txtQuantity = null;
		Intbox txtShift = null;
		Label txtTotal = null;
		// loai may bom
		component = lstCell.get(pumpTypeIdDetail).getFirstChild();
		if (component != null && component instanceof Combobox) {
			cbPumpType = (Combobox) component;
			billsDetail.setPumpTypeId(cbPumpType.getSelectedItem().getValue());
		}
		// may bom
		component = lstCell.get(pumpIdDetail).getFirstChild();
		if (component != null && component instanceof Combobox) {
			cbPump = (Combobox) component;
			billsDetail.setPumpID(cbPump.getSelectedItem().getValue());
		}
		// loai vi tri
		component = lstCell.get(3).getFirstChild();
		if (component != null && component instanceof Combobox) {
			cbLocationType = (Combobox) component;
			billsDetail.setLocationType(cbLocationType.getSelectedItem().getValue());
		}
		// vi tri bom
		component = lstCell.get(locationDetail).getFirstChild();
		if (component != null && component instanceof Combobox) {
			cbLocation = (Combobox) component;
			billsDetail.setLocationId(cbLocation.getSelectedItem().getValue());
		}

		// khoi luong bom
		component = lstCell.get(5).getFirstChild();
		if (component != null && component instanceof Doublebox) {
			txtQuantity = (Doublebox) component;
			billsDetail.setQuantity(txtQuantity.getValue());
		}
		// ca cho
		component = lstCell.get(6).getFirstChild();
		if (component != null && component instanceof Intbox) {
			txtShift = (Intbox) component;
			billsDetail.setShift(txtShift.getValue());
		}
		component = lstCell.get(7).getFirstChild();
		if (component != null && component instanceof Label) {
			txtTotal = (Label) component;
			billsDetail.setTotal(Double.valueOf(txtTotal.getValue()));
		}

		billsDetail.setBillId(txtBillID.getValue());
		return billsDetail;
	}

	public void onSave(ForwardEvent event) {
		Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
		List<Component> lstCell = rowSelected.getChildren();

		BillsDetail c = rowSelected.getValue();
		BillsDetail billsDetail = getDataInRow(lstCell);
		billsDetail.setBillDetailId(c.getBillDetailId());
		save(billsDetail);
		StyleUtils.setDisableComponent(lstCell, 4);
		reloadGrid();

	}

	private void save(BillsDetail billsDetail) {

		if (insertOrUpdate == 1) {
			billsDetail.setStatus(1);
			billsServices.save(billsDetail);
			lstBillDetail.add(billsDetail);
		} else {
			billsServices.update(billsDetail);
		}
		insertOrUpdate = 0;
	}

	/**
	 * Reload grid
	 */
	private void reloadGrid() {
		lstBillDetail.clear();
		List<BillsDetail> lstData = billsServices.getBillDetail(txtBillID.getValue());
		if (lstData != null && !lstData.isEmpty()) {
			lstBillDetail.addAll(lstData);
		}
		listDataModelDetail = new ListModelList(lstBillDetail);
		gridBillsDetail.setModel(listDataModelDetail);
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
		onChangeData(lstCell);
		BillsDetail c = rowSelected.getValue();
		setDataPumpsDetail(lstCell, getPumpsDefault(c.getPumpID()), pumpIdDetail);
		setDataPumpsTypeDetail(lstCell, getPumpsTypeDefault(c.getPumpTypeId()), pumpTypeIdDetail);
		setLocationDetail(lstCell, getLocationDefault(c.getLocationId()), locationDetail);
		setDataLocationTypeDetail(lstCell, getLocationTypeDefault(c.getLocationType()), locationTypeDetail);
		StyleUtils.setEnableComponent(lstCell, 4);
	}

	/**
	 * Cancel
	 *
	 * @param event
	 */
	public void onDisable(ForwardEvent event) {
		insertOrUpdate = 0;
		Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
		List<Component> lstCell = rowSelected.getChildren();
		StyleUtils.setDisableComponent(lstCell, 4);
		reloadGrid();

	}

	public void onDelete(ForwardEvent event) {
		Messagebox.show(Labels.getLabel("message.confirm.delete.content"),
				Labels.getLabel("message.confirm.delete.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
				new EventListener() {
					@Override
					public void onEvent(Event e) {
						if (Messagebox.ON_YES.equals(e.getName())) {
							Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
							BillsDetail c = rowSelected.getValue();
							c.setStatus(0);
							billsServices.delete(c);
							reloadGrid();
						}
					}
				});

	}

	private void onChangeData(List<Component> lstCell) {

		Component component;

		// may bom
		component = lstCell.get(pumpTypeIdDetail).getFirstChild();
		if (component != null && component instanceof Combobox) {
			cbPumpType = (Combobox) component;

		}
		// loai may bom
		component = lstCell.get(pumpIdDetail).getFirstChild();
		if (component != null && component instanceof Combobox) {
			cbPump = (Combobox) component;
		}
		// vi tri bom
		component = lstCell.get(locationTypeDetail).getFirstChild();
		if (component != null && component instanceof Combobox) {
			cbLocationType = (Combobox) component;

		}
		// loai vi tri
		component = lstCell.get(locationDetail).getFirstChild();
		if (component != null && component instanceof Combobox) {
			cbLocation = (Combobox) component;
		}

		// khoi luong bom
		component = lstCell.get(5).getFirstChild();
		if (component != null && component instanceof Doublebox) {
			txtQuantity = (Doublebox) component;
		}
		// ca cho
		component = lstCell.get(6).getFirstChild();
		if (component != null && component instanceof Intbox) {
			txtShift = (Intbox) component;
		}
		component = lstCell.get(7).getFirstChild();
		if (component != null && component instanceof Label) {
			txtTotal = (Label) component;

		}
		cbPump.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				calculatorRevenue();
			}
		});
		cbPumpType.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				calculatorRevenue();
			}
		});
		cbLocationType.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				calculatorRevenue();
			}
		});
		cbLocation.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				calculatorRevenue();
			}
		});
		txtQuantity.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				calculatorRevenue();
			}
		});
		txtShift.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				calculatorRevenue();
			}
		});
	}

	private void calculatorRevenue() {
		Long pumpID = cbPump.getSelectedItem().getValue();
		Long pumpTypeID = cbPumpType.getSelectedItem().getValue();
		Long locationTypeID = cbLocationType.getSelectedItem().getValue();
		Long locationID = cbLocation.getSelectedItem().getValue();

		Double quantity = txtQuantity.getValue();
		Integer shift = txtShift.getValue();
		if (pumpID != -1l && pumpTypeID != -1l && locationID != -1l && locationTypeID != -1l && quantity != null
				&& shift != null) {
			List<CalculatorRevenue> calculatorRevenue = billsServices.calculatorRevenue(txtConstruction.getValue(),
					Long.valueOf(pumpID), Long.valueOf(pumpTypeID), Long.valueOf(locationID),
					Long.valueOf(locationTypeID), quantity, shift);
			if (calculatorRevenue != null && !calculatorRevenue.isEmpty()) {
				if (calculatorRevenue.get(0).getTotal_revenue() != null) {
					txtTotal.setValue(String.valueOf(calculatorRevenue.get(0).getTotal_revenue()));
				}
			}

		}
	}

}
