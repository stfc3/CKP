package com.dvd.ckp.controller;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.dvd.ckp.business.service.BillsServices;
import com.dvd.ckp.business.service.LocationServices;
import com.dvd.ckp.business.service.PumpServices;
import com.dvd.ckp.business.service.UtilsService;
import com.dvd.ckp.domain.BillsDetail;
import com.dvd.ckp.domain.Location;
import com.dvd.ckp.domain.Param;
import com.dvd.ckp.domain.Pumps;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.SpringConstant;
import com.dvd.ckp.utils.StyleUtils;

public class BillsDetailController extends GenericForwardComposer<Component> {
	/**
	 * 
	 */
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
	private final int pumpIdDetail = 1;
	private final int pumpTypeIdDetail = 2;
	private final int locationDetail = 3;
	private final int locationTypeDetail = 4;

	private String txt = "";
	private Textbox txtBillID;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		txt = txtBillID.getValue();
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
		List<BillsDetail> lstData = billsServices.getBillDetail();
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
		defaultParam.setParamId(-1l);
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
				if (typePumpId.equals(param.getParamId())) {
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
				if (locationTypeId.equals(param.getParamId())) {
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
				setDataLocationTypeDetail(lstCell, getLocationTypeDefault(billsDetail.getLocationType()),
						locationTypeDetail);
			}
		}
	}

	public void onClick$addBillDetail() {
		BillsDetail billsDetail = new BillsDetail();
		listDataModelDetail.add(0, billsDetail);
		gridBillsDetail.setModel(listDataModelDetail);
		gridBillsDetail.renderAll();
		List<Component> lstCell = gridBillsDetail.getRows().getChildren().get(0).getChildren();
		setDataDefaultInGridViewDetail();
		StyleUtils.setEnableComponent(lstCell, 4);
	}

	public void onStaff(ForwardEvent event) {
		final Window windownUpload = (Window) Executions.createComponents("/manager/include/quantityStaff.zul",
				windowAddDetail, null);
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
