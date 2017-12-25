/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import com.dvd.ckp.business.service.BillsServices;
import com.dvd.ckp.business.service.ConstructionService;
import com.dvd.ckp.business.service.PumpServices;
import com.dvd.ckp.business.service.StaffServices;
import com.dvd.ckp.common.Constants;
import com.dvd.ckp.component.MyListModel;
import com.dvd.ckp.domain.BillViewDetail;
import com.dvd.ckp.domain.Bills;
import com.dvd.ckp.domain.BillsDetail;
import com.dvd.ckp.domain.Construction;
import com.dvd.ckp.domain.Pumps;
import com.dvd.ckp.domain.StaffQuantity;
import com.dvd.ckp.utils.DateTimeUtils;
import com.dvd.ckp.utils.SpringConstant;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Rows;

/**
 *
 * @author viettx
 */
public class ApproveController extends GenericForwardComposer {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ApproveController.class);
	@Autowired
	protected BillsServices billServices;

	@Autowired
	protected StaffServices staffServices;

	@Autowired
	protected ConstructionService contructionServices;

	@Autowired
	protected PumpServices pumpServices;

	@Wire
	private Grid gridApprove;
	@Wire
	private Combobox cbBillCode;
	private ListModelList<BillViewDetail> listApprove;

	private List<BillViewDetail> listData;
	private List<StaffQuantity> listStaff;

	private List<Bills> lstBill;

	private List<BillsDetail> lstDataBill;

	private ListModelList<String> modelListBills;

	private Combobox cbContruction;
	private MyListModel<Construction> modelListContruction;

	private Datebox dtFilterDate;

	private Combobox cbPump;
	private MyListModel<Pumps> modelListPump;

	private List<Construction> listContruction;
	private List<Pumps> listPumps;

	@Wire
	private Window approve;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		billServices = (BillsServices) SpringUtil.getBean(SpringConstant.BILL_SERVICES);
		staffServices = (StaffServices) SpringUtil.getBean(SpringConstant.STAFF_SERVICES);
		contructionServices = (ConstructionService) SpringUtil.getBean(SpringConstant.CONSTRUCTION_SERVICES);
		pumpServices = (PumpServices) SpringUtil.getBean(SpringConstant.PUMPS_SERVICES);

		listStaff = staffServices.getAll();

		lstBill = billServices.getAllData();

		listData = billServices.getApproveBill();
		// if (listData != null && !listData.isEmpty()) {
		// for (BillViewDetail viewDetail : listData) {
		//// viewDetail.setStaff(getStaffList(viewDetail.getBillDetailID(),
		// listStaff));
		// viewDetail.setFormatDate(
		// DateTimeUtils.convertDateToString(viewDetail.getPrdID(),
		// Constants.FORMAT_DATE_DD_MM_YYY));
		//// if (viewDetail.getQuantityApprove() != null) {
		//// viewDetail.setQuantityView(viewDetail.getQuantityApprove());
		//// } else {
		//// viewDetail.setQuantityView(viewDetail.getQuantity());
		//// }
		// }
		//
		// }
		listApprove = new ListModelList<>(listData);
		gridApprove.setModel(listApprove);

		listContruction = contructionServices.getAllConstruction();
		modelListContruction = new MyListModel<>(listContruction);
		cbContruction.setModel(modelListContruction);

		modelListBills = new ListModelList<>(getListBillCode(lstBill));
		cbBillCode.setModel(modelListBills);

		listPumps = pumpServices.getAllListData();
		modelListPump = new MyListModel<>(listPumps);
		cbPump.setModel(modelListPump);
	}

	private List<String> getListBillCode(List<Bills> listData) {
		List<String> listCode = new ArrayList<>();
		if (listData != null && !listData.isEmpty()) {
			for (Bills item : listData) {
				listCode.add(item.getBillCode());
			}
		}
		return listCode;
	}

	private String getStaffList(Long bill, List<StaffQuantity> listStaff) {
		String staffName = "";
		int index = 0;
		if (listStaff != null && !listStaff.isEmpty()) {
			for (StaffQuantity item : listStaff) {
				if (bill.equals(item.getBillId())) {
					if (index == 0) {
						staffName = item.getStaffName();
					} else {
						staffName += ", " + item.getStaffName();
					}
					index++;
				}

			}
		}
		return staffName;
	}

	private void reload() {
		listStaff = staffServices.getAll();
		listData.clear();
		listData = billServices.getApproveBill();
		// if (listData != null && !listData.isEmpty()) {
		// for (BillViewDetail viewDetail : listData) {
		//// viewDetail.setStaff(getStaffList(viewDetail.getBillDetailID(),
		// listStaff));
		// viewDetail.setFormatDate(
		// DateTimeUtils.convertDateToString(viewDetail.getPrdID(),
		// Constants.FORMAT_DATE_DD_MM_YYY));
		//// if (viewDetail.getQuantityApprove() != null) {
		//// viewDetail.setQuantityView(viewDetail.getQuantityApprove());
		//// } else {
		//// viewDetail.setQuantityView(viewDetail.getQuantity());
		//// }
		// }
		//
		// }
		listApprove = new ListModelList<>(listData);
		gridApprove.setModel(listApprove);
	}

	public void onAddStaff(ForwardEvent event) {
		Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
		BillViewDetail view = rowSelected.getValue();
		getDataInRow(view, 1);
	}

	public void onApprove(ForwardEvent event) {
		Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
		BillViewDetail view = rowSelected.getValue();
		getDataInRow(view, 2);
	}

	// public void onCloseWindown(ForwardEvent event) {
	// reload();
	// }

	private void getDataInRow(BillViewDetail data, int isApprove) {

		List<BillsDetail> lstDetail = billServices.getBillDetail(data.getBillID());
		lstDataBill = new ArrayList<>();
		if (lstDetail != null && !lstDetail.isEmpty()) {
			lstDataBill.addAll(lstDetail);
		}
		// TODO Auto-generated method stub
		Map<String, Object> arguments = new HashMap();
		arguments.put("billDetailID", data.getBillDetailID());
		arguments.put("bill", getBill(data.getBillID()));
		arguments.put("billDetai", getBillDetail(data.getBillDetailID()));
		if (isApprove == 1) {
			final Window windownUpload = (Window) Executions.createComponents("/manager/include/addStaff.zul", approve,
					arguments);
			windownUpload.doModal();
			windownUpload.setBorder(true);
			windownUpload.setBorder("normal");
			windownUpload.setClosable(true);
			// windownUpload.addForward("onDetach", windownUpload,
			// "onCloseWindown");
			// windownUpload.addEventListener(Events.ON_DROP, new
			// EventListener<Event>() {
			//
			// @Override
			// public void onEvent(Event event) throws Exception {
			// reload();
			// windownUpload.detach();
			//
			// }
			// });
			// reload();

		} else if (isApprove == 2) {
			Messagebox.show(Labels.getLabel("staff.quantity.comfirm.approve.message"), Labels.getLabel("comfirm"),
					Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
						@Override
						public void onEvent(Event e) {
							if (Messagebox.ON_NO.equals(e.getName())) {
								final Window windownUpload = (Window) Executions
										.createComponents("/manager/include/approveQuantity.zul", approve, arguments);
								windownUpload.doModal();
								windownUpload.setBorder(true);
								windownUpload.setBorder("normal");
								windownUpload.setClosable(true);
								// windownUpload.addEventListener(Events.ON_DROP,
								// new EventListener<Event>() {
								//
								// @Override
								// public void onEvent(Event event) throws
								// Exception {
								// reload();
								// windownUpload.detach();
								//
								// }
								// });
								// reload();
							} else if (Messagebox.ON_YES.equals(e.getName())) {
								try {
									BillsDetail billsDetail = new BillsDetail();
									billsDetail.setBillDetailId(data.getBillDetailID());
									billsDetail.setStatus(2);

									billServices.delete(billsDetail);
									reload();
								} catch (Exception e2) {
									logger.error(e2.getMessage(), e2);
								}
								Messagebox.show(Labels.getLabel("staff.quantity.comfirm.approve.message.ok"),
										Labels.getLabel("comfirm"), Messagebox.OK, Messagebox.INFORMATION);
							}
						}
					});

		}

	}

	private BillsDetail getBillDetail(Long billDetailID) {
		if (lstDataBill != null && !lstDataBill.isEmpty()) {
			for (BillsDetail billsDetail : lstDataBill) {
				if (billDetailID.equals(billsDetail.getBillDetailId())) {
					return billsDetail;
				}
			}
		}
		return null;
	}

	private Bills getBill(Long bill) {
		if (lstBill != null && !lstBill.isEmpty()) {
			for (Bills bills : lstBill) {
				if (bill != null && bill.equals(bills.getBillID())) {
					return bills;
				}
			}
		}
		return null;
	}

	public void onChange$cbBillCode() {
		String bill = null;
		if (cbBillCode.getSelectedItem() != null) {
			bill = cbBillCode.getSelectedItem().getValue();
		}
		Long contruction = null;
		if (cbContruction.getSelectedItem() != null) {
			contruction = cbContruction.getSelectedItem().getValue();
		}
		Date date = null;
		if (dtFilterDate != null) {
			date = dtFilterDate.getValue();
		}
		Long pump = null;
		if (cbPump.getSelectedItem() != null) {
			pump = cbPump.getSelectedItem().getValue();
		}

		filter(bill, contruction, date, pump);
	}

	public void onChange$cbContruction() {
		String bill = null;
		if (cbBillCode.getSelectedItem() != null) {
			bill = cbBillCode.getSelectedItem().getValue();
		}
		Long contruction = null;
		if (cbContruction.getSelectedItem() != null) {
			contruction = cbContruction.getSelectedItem().getValue();
		}
		Date date = null;
		if (dtFilterDate != null) {
			date = dtFilterDate.getValue();
		}
		Long pump = null;
		if (cbPump.getSelectedItem() != null) {
			pump = cbPump.getSelectedItem().getValue();
		}

		filter(bill, contruction, date, pump);
	}

	public void onChange$dtFilterDate() {
		String bill = null;
		if (cbBillCode.getSelectedItem() != null) {
			bill = cbBillCode.getSelectedItem().getValue();
		}
		Long contruction = null;
		if (cbContruction.getSelectedItem() != null) {
			contruction = cbContruction.getSelectedItem().getValue();
		}
		Date date = null;
		if (dtFilterDate != null) {
			date = dtFilterDate.getValue();
		}
		Long pump = null;
		if (cbPump.getSelectedItem() != null) {
			pump = cbPump.getSelectedItem().getValue();
		}

		filter(bill, contruction, date, pump);
	}

	public void onChange$cbPump() {
		String bill = null;
		if (cbBillCode.getSelectedItem() != null) {
			bill = cbBillCode.getSelectedItem().getValue();
		}
		Long contruction = null;
		if (cbContruction.getSelectedItem() != null) {
			contruction = cbContruction.getSelectedItem().getValue();
		}
		Date date = null;
		if (dtFilterDate != null) {
			date = dtFilterDate.getValue();
		}
		Long pump = null;
		if (cbPump.getSelectedItem() != null) {
			pump = cbPump.getSelectedItem().getValue();
		}

		filter(bill, contruction, date, pump);
	}

	private void filter(String bill, Long contruction, Date date, Long pump) {
		List<BillViewDetail> vlstData = new ArrayList<>();
		if (listData != null && !listData.isEmpty()) {
			if (bill == null && contruction == null && date == null && pump == null) {
				vlstData.addAll(listData);

			} else {
				for (BillViewDetail c : listData) {
					// tim theo bill code
					if (bill != null && contruction == null && date == null && pump == null) {
						if (bill.equals(c.getBillCode())) {
							vlstData.add(c);
						}
					} // tim theo cong trinh
					else if (bill == null && contruction != null && date == null && pump == null) {
						if (contruction.equals(c.getContruction())) {
							vlstData.add(c);
						}
					} // tim theo ngay bom
					else if (bill == null && contruction == null && date != null && pump == null) {
						String dateInput = DateTimeUtils.convertDateToString(date, Constants.FORMAT_DATE);
						if (dateInput.equals(c.getPrdID())) {
							vlstData.add(c);
						}
					} // Tim theo may bom
					else if (bill == null && contruction == null && date == null && pump != null) {
						if (pump.equals(c.getPumpID())) {
							vlstData.add(c);
						}
					} // Tim theo bill code va cong trinh
					else if (bill != null && contruction != null && date == null && pump == null) {
						if (bill.equals(c.getBillCode()) && contruction.equals(c.getContruction())) {
							vlstData.add(c);
						}
						// Tim theo bill code va ngay bom
					} else if (bill != null && contruction == null && date != null && pump == null) {
						String dateInput = DateTimeUtils.convertDateToString(date, Constants.FORMAT_DATE);
						if (bill.equals(c.getBillCode()) && dateInput.equals(c.getPrdID())) {
							vlstData.add(c);
						}
						// Tim theo bill code va may bom
					} else if (bill != null && contruction == null && date == null && pump != null) {
						if (bill.equals(c.getBillCode()) && pump.equals(c.getPumpID())) {
							vlstData.add(c);
						}
						// Tim theo cong trinh va ngay bom
					} else if (bill == null && contruction != null && date != null && pump == null) {
						String dateInput = DateTimeUtils.convertDateToString(date, Constants.FORMAT_DATE);
						if (contruction.equals(c.getContruction()) && dateInput.equals(c.getPrdID())) {
							vlstData.add(c);
						}
						// tim theo cong trinh va may bom
					} else if (bill == null && contruction != null && date == null && pump != null) {
						if (contruction.equals(c.getContruction()) && pump.equals(c.getPumpID())) {
							vlstData.add(c);
						}
						// tim theo ngay bom va may bom
					} else if (bill == null && contruction == null && date != null && pump != null) {
						String dateInput = DateTimeUtils.convertDateToString(date, Constants.FORMAT_DATE);
						if (dateInput.equals(c.getPrdID()) && pump.equals(c.getPumpID())) {
							vlstData.add(c);
						}
						// tim theo bill code va cong trinh va ngay bom
					} else if (bill != null && contruction != null && date != null && pump == null) {
						String dateInput = DateTimeUtils.convertDateToString(date, Constants.FORMAT_DATE);
						if (bill.equals(c.getBillCode()) && dateInput.equals(c.getPrdID())
								&& contruction.equals(c.getContruction())) {
							vlstData.add(c);
						}
						// tim theo bill code va cong trinh va may bom
					} else if (bill != null && contruction != null && date == null && pump != null) {
						if (bill.equals(c.getBillCode()) && contruction.equals(c.getContruction())
								&& pump.equals(c.getPumpID())) {
							vlstData.add(c);
						}
					} else if (bill == null && contruction != null && date != null && pump != null) {
						String dateInput = DateTimeUtils.convertDateToString(date, Constants.FORMAT_DATE);
						if (dateInput.equals(c.getPrdID()) && contruction.equals(c.getContruction())
								&& pump.equals(c.getPumpID())) {
							vlstData.add(c);
						}
					} else if (bill != null && contruction != null && date != null && pump != null) {
						String dateInput = DateTimeUtils.convertDateToString(date, Constants.FORMAT_DATE);
						if (bill.equals(c.getBillCode()) && dateInput.equals(c.getPrdID())
								&& contruction.equals(c.getContruction()) && pump.equals(c.getPumpID())) {
							vlstData.add(c);
						}
					}
				}
			}
		}
		listApprove = new ListModelList<>(vlstData);
		gridApprove.setModel(listApprove);

	}

	public void onClick$reloadData() {
		reload();
	}

}
