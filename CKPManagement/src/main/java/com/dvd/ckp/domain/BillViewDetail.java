package com.dvd.ckp.domain;

import java.util.Date;

public class BillViewDetail {
	private Long billID;
	private Long billDetailID;
	private Date fromDate;
	private Date startTime;
	private Date endTime;
	private Date toDate;
	private Double quantity;
	private Double quantityApprove;
	private String location;
	private String note;

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

}
