package com.dvd.ckp.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Longbox;

import com.dvd.ckp.business.service.BillsServices;
import com.dvd.ckp.domain.BillViewDetail;
import com.dvd.ckp.utils.SpringConstant;

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
	
	// Danh sach hoa don chi tiet
	private List<BillViewDetail> lstBillDetail;

	// Model grid in window bill detail
	ListModelList<BillViewDetail> listDataModelDetail;

	// Vi tri cac column trong grid bills detail

	private Longbox txtBillID;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		Long billId = txtBillID.getValue();
		billsServices = (BillsServices) SpringUtil.getBean(SpringConstant.BILL_SERVICES);
		List<BillViewDetail> lstData = billsServices.getDataView(billId);
		lstBillDetail = new ArrayList<>();
		
		if (lstData != null && !lstData.isEmpty()) {
			LOGGER.info(">>>>>>>>>>" + lstData.size());
			lstBillDetail.addAll(lstData);
		}

		listDataModelDetail = new ListModelList<BillViewDetail>(lstBillDetail);
		gridBillsDetail.setModel(listDataModelDetail);

	}

}
