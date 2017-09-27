package com.dvd.ckp.domain;

public class QuantityBean {
	private Long billDetailID;
	private Long staffID;
	private int isFar;
	private Double quantityConvert;
	private Double quantity;

	public Long getBillDetailID() {
		return billDetailID;
	}

	public void setBillDetailID(Long billDetailID) {
		this.billDetailID = billDetailID;
	}

	public Long getStaffID() {
		return staffID;
	}

	public void setStaffID(Long staffID) {
		this.staffID = staffID;
	}

	public int getIsFar() {
		return isFar;
	}

	public void setIsFar(int isFar) {
		this.isFar = isFar;
	}

	public Double getQuantityConvert() {
		return quantityConvert;
	}

	public void setQuantityConvert(Double quantityConvert) {
		this.quantityConvert = quantityConvert;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

}
