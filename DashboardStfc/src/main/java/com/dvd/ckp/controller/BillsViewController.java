package com.dvd.ckp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.dvd.ckp.business.service.BillsServices;
import com.dvd.ckp.domain.BillViewDetail;
import com.dvd.ckp.domain.Bills;
import com.dvd.ckp.domain.BillsDetail;
import com.dvd.ckp.utils.SpringConstant;
import org.zkoss.zul.A;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

public class BillsViewController extends GenericForwardComposer<Component> {

	/**
	 *
	 */
	private static final Logger LOGGER = Logger.getLogger(BillsViewController.class);
	private static final long serialVersionUID = -3785065052690864441L;
	@WireVariable
	protected BillsServices billsServices;

	@Wire
	private Grid gridBillsDetail;
	@Wire
	private Intbox roleApprove;

	// Danh sach hoa don chi tiet
	private List<BillViewDetail> lstBillDetail;

	private List<BillsDetail> lstDataBill;

	private List<Bills> lstBill;

	// Model grid in window bill detail
	ListModelList<BillViewDetail> listDataModelDetail;

	// Vi tri cac column trong grid bills detail
	private Longbox txtBillID;
	private String billDetailId;

	private Window windowViewBillDetail;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		Long billId = txtBillID.getValue();
		billsServices = (BillsServices) SpringUtil.getBean(SpringConstant.BILL_SERVICES);
		List<BillViewDetail> lstData = billsServices.getDataView(billId);
		List<BillsDetail> lstDetail = billsServices.getBillDetail(billId);
		lstBillDetail = new ArrayList<>();
		lstDataBill = new ArrayList<>();
		if (lstData != null && !lstData.isEmpty()) {
			lstBillDetail.addAll(lstData);
		}
		if (lstDetail != null && !lstDetail.isEmpty()) {
			lstDataBill.addAll(lstDetail);
		}
		listDataModelDetail = new ListModelList(lstBillDetail);
		gridBillsDetail.setModel(listDataModelDetail);

		lstBill = billsServices.getAllData();

		if (roleApprove.getValue() == 0) {
			hideActionApprove();
		}
	}

	private void reload() {
		Long billId = txtBillID.getValue();
		List<BillViewDetail> lstData = billsServices.getDataView(billId);
		lstBillDetail = new ArrayList<>();

		if (lstData != null && !lstData.isEmpty()) {
			lstBillDetail.addAll(lstData);
		}

		listDataModelDetail = new ListModelList<BillViewDetail>(lstBillDetail);
		gridBillsDetail.setModel(listDataModelDetail);
	}

	public void onAddStaff(ForwardEvent event) {
		Vlayout rowSelected = (Vlayout) event.getOrigin().getTarget().getParent().getParent().getParent();
		List<Component> lstCell = rowSelected.getChildren();
		getDataInRow(lstCell, 1);
	}

	public void onApprove(ForwardEvent event) {
		Vlayout rowSelected = (Vlayout) event.getOrigin().getTarget().getParent().getParent().getParent();
		List<Component> lstCell = rowSelected.getChildren();
		getDataInRow(lstCell, 2);
	}

	private void getDataInRow(List<Component> lstCell, int isApprove) {

		Component component;

		// ma phieu bom
		component = lstCell.get(0);
		if (component instanceof Textbox) {
			final Textbox billID = (Textbox) component;
			billDetailId = billID.getValue();
		}

		// TODO Auto-generated method stub
		Map<String, Object> arguments = new HashMap();
		arguments.put("billDetailID", billDetailId);
		arguments.put("bill", getBill());
		arguments.put("billDetai", getBillDetail(Long.valueOf(billDetailId)));
		if (isApprove == 1) {
			final Window windownUpload = (Window) Executions.createComponents("/manager/include/addStaff.zul",
					windowViewBillDetail, arguments);
			windownUpload.doModal();
			windownUpload.setBorder(true);
			windownUpload.setBorder("normal");
			windownUpload.setClosable(true);
			windownUpload.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					reload();
					windownUpload.detach();

				}
			});
		} else if (isApprove == 2) {
			Messagebox.show(Labels.getLabel("staff.quantity.comfirm.approve.message"), Labels.getLabel("comfirm"),
					Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
						@Override
						public void onEvent(Event e) {
							if (Messagebox.ON_NO.equals(e.getName())) {
								final Window windownUpload = (Window) Executions.createComponents(
										"/manager/include/approveQuantity.zul", windowViewBillDetail, arguments);
								windownUpload.doModal();
								windownUpload.setBorder(true);
								windownUpload.setBorder("normal");
								windownUpload.setClosable(true);
								windownUpload.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

									@Override
									public void onEvent(Event event) throws Exception {
										LOGGER.info("Reload");
										reload();
										windownUpload.detach();

									}
								});
							} else if (Messagebox.ON_YES.equals(e.getName())) {
								try {
									BillsDetail billsDetail = new BillsDetail();
									billsDetail.setBillDetailId(Long.valueOf(billDetailId));
									billsDetail.setStatus(2);

									billsServices.delete(billsDetail);
								} catch (Exception e2) {
									LOGGER.error(e2.getMessage(), e2);
								}
								Messagebox.show(Labels.getLabel("staff.quantity.comfirm.approve.message.ok"),
										Labels.getLabel("comfirm"), Messagebox.OK, Messagebox.INFORMATION);
							}
						}
					});

		}

	}

	private BillsDetail getBillDetail(Long billDetailID) {
		if (lstBillDetail != null && !lstBillDetail.isEmpty()) {
			for (BillsDetail billsDetail : lstDataBill) {
				if (billDetailID.equals(billsDetail.getBillDetailId())) {
					return billsDetail;
				}
			}
		}
		return null;
	}

	private Bills getBill() {
		Long billID = txtBillID.getValue();
		if (lstBill != null && !lstBill.isEmpty()) {
			for (Bills bills : lstBill) {
				if (billID != null && billID.equals(bills.getBillID())) {
					return bills;
				}
			}
		}
		return null;
	}

	private void hideActionApprove() {
		gridBillsDetail.renderAll();
		Rows vrows = gridBillsDetail.getRows();
		List<Component> lstRows = vrows.getChildren();
		if (lstRows != null && !lstRows.isEmpty()) {
			for (Component row : lstRows) {
				if (row instanceof Row) {
					Component btnStaff = row.getFirstChild().getChildren().get(1).getFirstChild().getChildren().get(2);
					if (btnStaff instanceof A) {
						((A) btnStaff).setVisible(false);
					}
					Component btnApprove = row.getFirstChild().getChildren().get(1).getFirstChild().getChildren()
							.get(3);
					if (btnApprove instanceof A) {
						((A) btnApprove).setVisible(false);
					}
				}
			}
		}
	}

}
