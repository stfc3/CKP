package com.dvd.ckp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.apache.log4j.Logger;


@Entity
@Table(name = "rent_equipment")
@NamedQuery(name = "RentEquipment.getAll", query = "FROM RentEquiment u where status = 1 order by createDate desc")
public class RentEquiment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6517823601232262665L;
	private static final Logger LOGGER = Logger.getLogger(RentEquiment.class);
	private Long rentID;
	private Long rentType;
	private String rentTypeName;
	private Long customerID;
	private String customerName;
	private Long constructionID;
	private String constructionName;
	private Date startDate;
	private Date endDate;
	private Double averagePrice;
	private String averagePriceView;
	private Integer status;
	private Date createDate;
	private Long majority;
	private Long monitoring;
	private Long distribute;
	private String prdID;
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rent_id", nullable = false, unique = false)
	public Long getRentID() {
		return rentID;
	}

	public void setRentID(Long rentID) {
		this.rentID = rentID;
	}

	@Column(name = "rent_type")
	public Long getRentType() {
		return rentType;
	}

	public void setRentType(Long rentType) {
		this.rentType = rentType;
	}

	@Column(name = " customers_id")
	public Long getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Long customerID) {
		this.customerID = customerID;
	}

	@Column(name = "construction_id")
	public Long getConstructionID() {
		return constructionID;
	}

	public void setConstructionID(Long constructionID) {
		this.constructionID = constructionID;
	}

	@Column(name = "start_date")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "end_date")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "average_price")
	public Double getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(Double averagePrice) {
		this.averagePrice = averagePrice;
	}

	@Column(name = "status")
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
	public String getRentTypeName() {
		return rentTypeName;
	}

	public void setRentTypeName(String rentTypeName) {
		this.rentTypeName = rentTypeName;
	}

	@Transient
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Transient
	public String getConstructionName() {
		return constructionName;
	}

	public void setConstructionName(String constructionName) {
		this.constructionName = constructionName;
	}

	@Transient
	public String getAveragePriceView() {
		return averagePriceView;
	}

	public void setAveragePriceView(String averagePriceView) {
		this.averagePriceView = averagePriceView;
	}

	@Column(name = "majority")
	public Long getMajority() {
		return majority;
	}

	public void setMajority(Long majority) {
		this.majority = majority;
	}

	@Column(name = "monitoring")
	public Long getMonitoring() {
		return monitoring;
	}

	
	public void setMonitoring(Long monitoring) {
		this.monitoring = monitoring;
	}

	@Column(name = "distribute_id")
	public Long getDistribute() {
		return distribute;
	}

	public void setDistribute(Long distribute) {
		this.distribute = distribute;
	}

	@Column(name = "prd_id")
	public String getPrdID() {
		return prdID;
	}

	public void setPrdID(String prdID) {
		this.prdID = prdID;
	}
	

}
