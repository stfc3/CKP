package com.dvd.ckp.controller;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;

import com.dvd.ckp.business.service.StaffServices;
import com.dvd.ckp.domain.QuantityBean;
import com.dvd.ckp.domain.Staff;
import com.dvd.ckp.utils.SpringConstant;

public class QuantityStaffController extends GenericForwardComposer<Component> {
	@WireVariable
	protected StaffServices staffServices;

	private List<Staff> lstStaff;

	@Wire
	private List<QuantityBean> lstData;	

	// Model grid in window bill detail
	ListModelList<Staff> listDataModelDetail;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ListModelList<Staff> getListDataModelDetail() {
		return listDataModelDetail;
	}	

	public void setListDataModelDetail(ListModelList<Staff> listDataModelDetail) {
		this.listDataModelDetail = listDataModelDetail;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		staffServices = (StaffServices) SpringUtil.getBean(SpringConstant.STAFF_SERVICES);
		lstStaff = staffServices.getAllData();
		listDataModelDetail = new ListModelList<>(lstStaff);

	}

}
