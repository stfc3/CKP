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
	private Long pumpTypeId;
	private Long locationId;
	private Long locationType;
	private double quantity;
	private double quantityApprove;
	private int shift;
	private double total;
	private double totalApprove;
	private int maxStaff;
	private int isFar;
	private double quantityConvert;

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
	
	@Column(name = "pump_type")
	public Long getPumpTypeId() {
		return pumpTypeId;
	}

	public void setPumpTypeId(Long pumpTypeId) {
		this.pumpTypeId = pumpTypeId;
	}

	@Column(name = "location_id")
	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
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

	@Column(name = "is_far")
	public int getIsFar() {
		return isFar;
	}

	public void setIsFar(int isFar) {
		this.isFar = isFar;
	}
	@Column(name = "quantity_convert")
	public double getQuantityConvert() {
		return quantityConvert;
	}

	public void setQuantityConvert(double quantityConvert) {
		this.quantityConvert = quantityConvert;
	}
	@Column(name = " location_type")
	public Long getLocationType() {
		return locationType;
	}

	public void setLocationType(Long locationType) {
		this.locationType = locationType;
	}

	@Column(name = " quantity_approve")
	public double getQuantityApprove() {
		return quantityApprove;
	}

	public void setQuantityApprove(double quantityApprove) {
		this.quantityApprove = quantityApprove;
	}

	@Column(name = " total_approve")
	public double getTotalApprove() {
		return totalApprove;
	}

	public void setTotalApprove(double totalApprove) {
		this.totalApprove = totalApprove;
	}
	
	

}
