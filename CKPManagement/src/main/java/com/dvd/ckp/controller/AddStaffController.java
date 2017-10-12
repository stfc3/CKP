package com.dvd.ckp.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.dvd.ckp.business.service.BillsServices;
import com.dvd.ckp.business.service.StaffServices;
import com.dvd.ckp.domain.Staff;
import com.dvd.ckp.domain.StaffQuantity;
import com.dvd.ckp.utils.SpringConstant;

public class AddStaffController extends GenericForwardComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -278079129703470694L;
	private static final Logger logger = Logger.getLogger(AddStaffController.class);
	@WireVariable
	protected StaffServices staffService;
	@WireVariable
	protected BillsServices billsServices;
	ListModelList<Staff> listDataStaff;

	private ListModelList<Staff> listDataModel;
	private List<Staff> lstStaff;

	private List<Staff> lstStaffAll;

	private ListModelList<Staff> listDataModelSelected;
	private List<Staff> lstStaffSelected;
	@Wire
	private Grid gridFullStaff;

	@Wire
	private Grid gridSelectStaff;
	@Wire
	private Label titleStaffSelected;
	@Wire
	private Radio yes;
	@Wire
	private Radio no;
	@Wire
	private Intbox intMaxStaff;
	@Wire
	private Doublebox dbQuantityConvert;
	@Wire
	private Longbox txtBillID;

	private Long billID;
	List<StaffQuantity> listQuantity;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		staffService = (StaffServices) SpringUtil.getBean(SpringConstant.STAFF_SERVICES);
		billsServices = (BillsServices) SpringUtil.getBean(SpringConstant.BILL_SERVICES);
		lstStaff = new ArrayList<Staff>();
		lstStaffAll = new ArrayList<Staff>();
		billID = txtBillID.getValue();
		List<Staff> vlstStaff = staffService.getAllData();
		if (vlstStaff != null) {
			lstStaff.addAll(vlstStaff);
			lstStaffAll.addAll(vlstStaff);

		}

		/*
		 * lay danh sach nhung cong nhan da duoc trong bang quantity_staff
		 */
		listQuantity = new ArrayList<>();
		List<StaffQuantity> lstStaffQuantity = staffService.getQuantity(billID);
		if (lstStaffQuantity != null && !lstStaffQuantity.isEmpty()) {
			listQuantity.addAll(lstStaffQuantity);
		}
		getDataSelected(lstStaffQuantity);

		listDataModelSelected = new ListModelList<Staff>(lstStaffSelected);
		gridSelectStaff.setModel(listDataModelSelected);

		/*
		 * loai bo nhung cong nhan da duoc chon khoi list full
		 */
		removeStaffSelected(lstStaffQuantity);
		listDataModel = new ListModelList<Staff>(lstStaff);
		gridFullStaff.setModel(listDataModel);
	}

	private void getDataSelected(List<StaffQuantity> staffQuantities) {
		lstStaffSelected = new ArrayList<>();

		if (lstStaffAll != null && !lstStaffAll.isEmpty()) {
			for (Staff staff : lstStaffAll) {
				if (staffQuantities != null && !staffQuantities.isEmpty()) {
					for (StaffQuantity quantity : staffQuantities) {
						if (quantity.getStaffId().equals(staff.getStaffId())) {
							// lstStaff.remove(getIndexStaff(staff.getStaffId()));
							lstStaffSelected.add(staff);
							break;
						}
					}
				}
			}
		}
	}

	private void removeStaffSelected(List<StaffQuantity> staffQuantities) {

		if (lstStaffAll != null && !lstStaffAll.isEmpty()) {
			for (Staff staff : lstStaffAll) {
				if (staffQuantities != null && !staffQuantities.isEmpty()) {
					for (StaffQuantity quantity : staffQuantities) {
						if (quantity.getStaffId().equals(staff.getStaffId())) {
							lstStaff.remove(staff);
							break;

						}
					}
				}
			}
		}
	}

	public void onAdd(ForwardEvent event) {
		Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
		List<Component> lstCell = rowSelected.getChildren();
		Staff c = rowSelected.getValue();
		getDataInRow(lstCell, c);
		lstStaffSelected.add(c);
		if (lstStaffSelected != null && !lstStaffSelected.isEmpty()) {
			gridSelectStaff.setVisible(true);
			titleStaffSelected.setVisible(true);
		}
		lstStaff.remove(getIndexStaff(c.getStaffId()));
		onReloadSelectGrid();
	}

	private void getDataInRow(List<Component> lstCell, Staff staff) {

		Textbox txtStaffCode = (Textbox) lstCell.get(1).getFirstChild();
		Textbox txtStaffName = (Textbox) lstCell.get(2).getFirstChild();
		Textbox txtPhone = (Textbox) lstCell.get(3).getFirstChild();
		Textbox txtEmail = (Textbox) lstCell.get(4).getFirstChild();
		Textbox txtAddress = (Textbox) lstCell.get(5).getFirstChild();
		staff.setStaffCode(txtStaffCode.getValue());
		staff.setStaffName(txtStaffName.getValue());
		staff.setPhone(txtPhone.getValue());
		staff.setAddress(txtAddress.getValue());
		staff.setEmail(txtEmail.getValue());

	}

	private void onReloadSelectGrid() {
		listDataModel = new ListModelList<Staff>(lstStaff);
		gridFullStaff.setModel(listDataModel);
		listDataModelSelected = new ListModelList<Staff>(lstStaffSelected);
		gridSelectStaff.setModel(listDataModelSelected);
	}

	private int getIndexStaff(Long staffID) {
		if (lstStaff != null && !lstStaff.isEmpty()) {
			for (Staff staff : lstStaff) {
				if (staffID.equals(staff.getStaffId())) {
					return lstStaff.indexOf(staff);
				}
			}
		}
		return -1;
	}

	private int getIndexStaffSelected(Long staffID) {
		if (lstStaffSelected != null && !lstStaffSelected.isEmpty()) {
			for (Staff staff : lstStaffSelected) {
				if (staffID.equals(staff.getStaffId())) {
					return lstStaffSelected.indexOf(staff);
				}
			}
		}
		return -1;
	}

	public void onDelete(ForwardEvent event) {
		Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
		List<Component> lstCell = rowSelected.getChildren();
		Staff c = rowSelected.getValue();
		getDataInRow(lstCell, c);
		lstStaff.add(c);
		lstStaffSelected.remove(getIndexStaffSelected(c.getStaffId()));

		onReloadSelectGrid();
	}

	public void onAction(ForwardEvent event) {
		Messagebox.show(Labels.getLabel("staff.quantity.comfirm"), Labels.getLabel("comfirm"),
				Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
					@Override
					public void onEvent(Event e) {
						if (Messagebox.ON_YES.equals(e.getName())) {
							int isFar;
							if (yes.isChecked()) {
								isFar = 1;
							} else {
								isFar = 0;
							}
							Integer maxStaff;
							// if (intMaxStaff.getValue() == null) {
							// Messagebox.show(Labels.getLabel("staff.quantity.comfirm"),
							// Labels.getLabel("comfirm"),
							// Messagebox.OK, Messagebox.ERROR);
							// return;
							// }
							maxStaff = intMaxStaff.getValue();

							Double quantityConvert;
							// if (intMaxStaff.getValue() == null) {
							// Messagebox.show(Labels.getLabel("staff.quantity.comfirm"),
							// Labels.getLabel("comfirm"),
							// Messagebox.OK, Messagebox.ERROR);
							// return;
							// }
							quantityConvert = dbQuantityConvert.getValue();
							billsServices.update(isFar, quantityConvert, maxStaff, billID);

							// staffService.delete(billID);
							List<StaffQuantity> lstQuantity = new ArrayList<>();

							if (lstStaffSelected != null && !lstStaffSelected.isEmpty()) {
								for (Staff staff : lstStaffSelected) {
									StaffQuantity quantity = new StaffQuantity();
									quantity.setStaffId(staff.getStaffId());
									quantity.setBillId(billID);
									if (!isExist(staff.getStaffId())) {
										lstQuantity.add(quantity);
									}
								}
							}
							staffService.save(lstQuantity);
							billsServices.getQuantity(billID).get(0).getV_quantity();
							onReloadSelectGrid();
						}
					}
				});
	}

	private boolean isExist(Long staffId) {
		if (listQuantity != null && !listQuantity.isEmpty()) {
			for (StaffQuantity staffQuantity : listQuantity) {
				if (staffId.equals(staffQuantity.getStaffId())) {
					return true;
				}
			}
		}
		return false;
	}
}
