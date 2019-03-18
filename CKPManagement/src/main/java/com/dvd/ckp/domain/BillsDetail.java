package com.dvd.ckp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import com.dvd.ckp.utils.StringUtils;

@Entity
@Table(name = "bill_detail")
@NamedQueries({
		@NamedQuery(name = "BillsDetail.getAllBillDetail", query = "FROM BillsDetail u where status in (1,2) order by createDate desc"),
		@NamedQuery(name = "BillsDetail.getAllBillDetailByID", query = "FROM BillsDetail u where billId = :billId and status in (1,2) order by createDate desc") })
@NamedNativeQueries({
		@NamedNativeQuery(name = "callCalculatorRevenue", query = "CALL calculator_revenue(:construction,:pump,:pump_type,:location_type,:location_id,:quantity,:shift)", resultClass = BillsDetail.class) })
public class BillsDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4893628034815040774L;
	private Long billDetailId;
	private Long billId;
	private Long pumpID;
	private Long pumpTypeId;
	private Long locationId;
	private Long locationType;
	private Double quantity;
	private Double quantityApprove;
	private Double quantityView;
	private Double shift;
	private Double total;
	private Double totalApprove;
	private String totalView;
	private Integer maxStaff;
	private Integer isFar;
	private Integer status;
	private Double quantityConvert;
	private Date createDate;
	private Integer autoConvert;
	private Double numSwitch;

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
	public Double getQuantity() {
		if (quantity != null) {
			return quantity;
		} else {
			return 0d;
		}
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Column(name = "shift")
	public Double getShift() {
		return shift;
	}

	public void setShift(Double shift) {
		this.shift = shift;
	}

	@Column(name = "total")
	public Double getTotal() {
		if (total != null) {
			return total;
		} else {
			return 0d;
		}
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	@Column(name = "max_staff")
	public Integer getMaxStaff() {
		return maxStaff;
	}

	public void setMaxStaff(Integer maxStaff) {
		this.maxStaff = maxStaff;
	}

	@Column(name = "is_far")
	public Integer getIsFar() {
		return isFar;
	}

	public void setIsFar(Integer isFar) {
		this.isFar = isFar;
	}

	@Column(name = "quantity_convert")
	public Double getQuantityConvert() {
		return quantityConvert;
	}

	public void setQuantityConvert(Double quantityConvert) {
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
	public Double getQuantityApprove() {
		return quantityApprove;
	}

	public void setQuantityApprove(Double quantityApprove) {
		this.quantityApprove = quantityApprove;
	}

	@Column(name = " total_approve")
	public Double getTotalApprove() {
		return totalApprove;
	}

	public void setTotalApprove(Double totalApprove) {
		this.totalApprove = totalApprove;
	}

	@Column(name = " status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "create_date")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Transient
	public Double getQuantityView() {
		if (quantityApprove != null) {
			quantityView = quantityApprove;
		} else {
			quantityView = quantity;
		}
		return quantityView;
	}

	public void setQuantityView(Double quantityView) {
		this.quantityView = quantityView;
	}

	@Transient
	public String getTotalView() {

		return totalView;
	}

	public void setTotalView(String totalView) {
		if (totalApprove != null) {
			this.totalView = StringUtils.formatPrice(totalApprove);
		} else {
			this.totalView = StringUtils.formatPrice(total);
		}
	}

	@Column(name = "is_auto")
	public Integer getAutoConvert() {
		return autoConvert;
	}

	public void setAutoConvert(Integer autoconvert) {
		this.autoConvert = autoconvert;
	}

	@Column(name = "switch")
	public Double getNumSwitch() {
		return numSwitch;
	}

	public void setNumSwitch(Double numSwitch) {
		this.numSwitch = numSwitch;
	}

}
