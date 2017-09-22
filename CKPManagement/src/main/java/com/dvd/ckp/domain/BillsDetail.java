package com.dvd.ckp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "bill_detail")
@NamedQuery(name = "BillsDetail.getAllBillDetail", query = "FROM BillsDetail u")
public class BillsDetail {
	private Long billDetailId;
	private Long billId;
	private Long pumpID;
	private Long locationId;
	private int priceType;
	private double quantity;
	private int shift;
	private double total;
	private int maxStaff;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bill_detail_id")
	public Long getBillDetailId() {
		return billDetailId;
	}

	public void setBillDetailId(Long billDetailId) {
		this.billDetailId = billDetailId;
	}

	@Column(name = "bill_id")
	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	@Column(name = "pump_id")
	public Long getPumpID() {
		return pumpID;
	}

	public void setPumpID(Long pumpID) {
		this.pumpID = pumpID;
	}

	@Column(name = "location_id")
	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	@Column(name = "price_type")
	public int getPriceType() {
		return priceType;
	}

	public void setPriceType(int priceType) {
		this.priceType = priceType;
	}

	@Column(name = "quantity")
	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	@Column(name = "shift")
	public int getShift() {
		return shift;
	}

	public void setShift(int shift) {
		this.shift = shift;
	}

	@Column(name = "total")
	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	@Column(name = "max_staff")
	public int getMaxStaff() {
		return maxStaff;
	}

	public void setMaxStaff(int maxStaff) {
		this.maxStaff = maxStaff;
	}

}
