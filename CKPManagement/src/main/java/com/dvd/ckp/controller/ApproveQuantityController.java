package com.dvd.ckp.controller;

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
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;

import com.dvd.ckp.business.service.BillsServices;
import com.dvd.ckp.utils.SpringConstant;

public class ApproveQuantityController extends GenericForwardComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -278079129703470694L;
	private static final Logger logger = Logger.getLogger(ApproveQuantityController.class);
	private Long billDetail;

	@WireVariable
	protected BillsServices billsServices;
	@Wire
	private Longbox txtBillID;
	@Wire
	private Doublebox quantityApprove;
	@Wire
	private Doublebox totalApprove;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		billDetail = txtBillID.getValue();
		billsServices = (BillsServices) SpringUtil.getBean(SpringConstant.BILL_SERVICES);
	}

	public void onAction(ForwardEvent event) {
		Messagebox.show(Labels.getLabel("staff.quantity.comfirm"), Labels.getLabel("comfirm"),
				Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
					@Override
					public void onEvent(Event e) {
						if (Messagebox.ON_YES.equals(e.getName())) {
							Double quantityApproveValue = quantityApprove.getValue();
							Double totalApproveValue = totalApprove.getValue();
							logger.info("bill detail id: " + billDetail);
							billsServices.upadte(quantityApproveValue, totalApproveValue, billDetail);
							quantityApprove.setValue(null);
							totalApprove.setValue(null);
						}
					}
				});
	}
}
