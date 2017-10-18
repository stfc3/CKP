package com.dvd.ckp.domain;

import java.util.Date;

import org.apache.log4j.Logger;

import com.dvd.ckp.common.Constants;
import com.dvd.ckp.utils.DateTimeUtils;

public class BillViewDetail {
	private static final Logger LOGGER = Logger.getLogger(BillViewDetail.class);
	private Long billID;
	private Long billDetailID;
	private Date fromDate;
	private String strFromDate;	
	private Date startTime;
	private String strStartTime;
	private Date endTime;
	private String strEndTime;
	private Date toDate;
	private String strToDate;
	private Double quantity;
	private Double quantityApprove;
	private Double quantityView;
	private String location;
	private String note;
	private String pump;

	public Long getBillID() {
		return billID;
	}

	public void setBillID(Long billID) {
		this.billID = billID;
	}

	public Long getBillDetailID() {
		return billDetailID;
	}

	public void setBillDetailID(Long billDetailID) {
		this.billDetailID = billDetailID;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getQuantityApprove() {
		return quantityApprove;
	}

	public void setQuantityApprove(Double quantityApprove) {
		this.quantityApprove = quantityApprove;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	
	
	
	public String getStrFromDate() {
		try {
			return DateTimeUtils.convertDateToString(fromDate, Constants.FORMAT_DATE_DD_MM_YYY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage(), e);
		}
		return "";
	}

	public void setStrFromDate(String strFromDate) {
		this.strFromDate = strFromDate;
	}

	
	public String getStrToDate() {
		try {
			return DateTimeUtils.convertDateToString(toDate, Constants.FORMAT_DATE_DD_MM_YYY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage(), e);
		}
		return "";
	}

	public void setStrToDate(String strToDate) {
		this.strToDate = strToDate;
	}

	
	public String getStrStartTime() {
		try {
			return DateTimeUtils.convertDateToString(startTime, Constants.FORMAT_HOUR);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage(), e);
		}
		return "";
	}

	public void setStrStartTime(String strStartTime) {
		this.strStartTime = strStartTime;
	}

	
	public String getStrEndTime() {
		try {
			return DateTimeUtils.convertDateToString(endTime, Constants.FORMAT_HOUR);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage(), e);
		}
		return "";
	}

	public void setStrEndTime(String strEndTime) {
		this.strEndTime = strEndTime;
	}

	public Double getQuantityView() {
		if(quantityApprove != null){
			quantityView = quantityApprove; 
		}else{
			quantityView = quantity;
		}
		return quantityView;
	}

	public void setQuantityView(Double quantityView) {
		this.quantityView = quantityView;
	}

	public String getPump() {
		return pump;
	}

	public void setPump(String pump) {
		this.pump = pump;
	}
	
	

}
