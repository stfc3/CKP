package com.dvd.ckp.controller;

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
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.dvd.ckp.business.service.BillsServices;
import com.dvd.ckp.business.service.StaffServices;
import com.dvd.ckp.domain.CalculatorRevenue;
import com.dvd.ckp.domain.StaffQuantity;
import com.dvd.ckp.utils.SpringConstant;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;

public class ApproveQuantityController extends GenericForwardComposer {

    /**
     *
     */
    private static final long serialVersionUID = -278079129703470694L;
    private static final Logger logger = Logger.getLogger(ApproveQuantityController.class);
    private Long billDetail;
    private Long bill;

    @WireVariable
    protected StaffServices staffService;

    @WireVariable
    protected BillsServices billsServices;
    @Wire
    private Longbox billID;
    @Wire
    private Longbox txtBillID;
    @Wire
    private Doublebox quantityApprove;
    @Wire
    private Window approveQuantity;
    @Wire
    private Longbox txtConstruction;

    @Wire
    private Longbox txtPumpId;

    @Wire
    private Longbox txtPumpTypeId;

    @Wire
    private Longbox txtLocationID;

    @Wire
    private Longbox txtLocationTypeID;

    @Wire
    private Doublebox txtShift;

    @Wire
    private Intbox txtMaxStaff;

    @Wire
    private Intbox txtIsAuto;

    @Wire
    private Doublebox txtSwitch;

    List<StaffQuantity> listQuantity;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        billDetail = txtBillID.getValue();
        bill = billID.getValue();
        billsServices = (BillsServices) SpringUtil.getBean(SpringConstant.BILL_SERVICES);
        staffService = (StaffServices) SpringUtil.getBean(SpringConstant.STAFF_SERVICES);

        listQuantity = staffService.getQuantity(billDetail);
    }

    public void onAction(ForwardEvent event) {
        Messagebox.show(Labels.getLabel("staff.quantity.comfirm.approve"), Labels.getLabel("comfirm"),
                Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event e) {
                if (Messagebox.ON_YES.equals(e.getName())) {
                    Double quantityApproveValue = quantityApprove.getValue();
                    Double totalApproveValue = null;
                    // billsServices.upadte(quantityApproveValue,
                    // totalApproveValue, billDetail);
//							if (listQuantity == null || listQuantity.isEmpty()) {
//								Messagebox.show(Labels.getLabel("staff.quantity.comfirm.approve.message.max.staff"),
//										Labels.getLabel("comfirm"), Messagebox.OK, Messagebox.ERROR);
//								return;
//							}
                    List<CalculatorRevenue> lstRevenue = billsServices.calculatorRevenue(
                            txtConstruction.getValue(), txtPumpTypeId.getValue(), txtLocationTypeID.getValue(),
                            txtLocationID.getValue(), quantityApproveValue, txtShift.getValue(),
                            txtSwitch.getValue(), txtIsAuto.getValue());
                    totalApproveValue = lstRevenue.get(0).getRevenue();
                    billsServices.upadte(quantityApproveValue, totalApproveValue, billDetail);

                    billsServices.getQuantity(billDetail);
                    // totalApprove.setValue(totalApproveValue);
                    // approveQuantity.onClose();
                    logger.info("Value: " + lstRevenue.get(0).getRevenue());
                    //reload data
                    Events.sendEvent("onClick", (Button) ((Window) approveQuantity.getParent()).getFellow("reloadData"), null);
                    approveQuantity.onClose();
                    approveQuantity.detach();
                }
            }
        });
    }
}
