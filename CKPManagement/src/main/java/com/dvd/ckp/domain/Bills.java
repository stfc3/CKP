package com.dvd.ckp.domain;

import java.util.Date;
import java.util.List;

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

import com.dvd.ckp.common.Constants;
import com.dvd.ckp.utils.DateTimeUtils;

@Entity
@Table(name = "bills")
@NamedQuery(name = "Bills.getAllBills", query = "FROM Bills u where status = 1 order by createDate desc")
public class Bills {
	private static final Logger LOGGER = Logger.getLogger(Bills.class);
	private int index;
	// id phieu bom
	private Long billID;
	// ma phieu bom
	private String billCode;
	// Ma khach hang
	private Long customerID;
	private String customerName;
	// Thoi gian nhap phieu bom
	private int prdID;
	private Date dateInput;
	private String strDateInput;
	// ngay bat dau den cong truong
	private Date fromDate;
	private String strFromDate;
	// Ngay roi cong truong
	private Date toDate;
	private String strToDate;
	// Thoi gian bat dau bom
	private Date startTime;
	private String strStartTime;
	// Thoi gian ket th
	private Date endTime;
	private String strEndTime;
	// ma cong tri
	private Long constructionID;
	private String constructionName;
	// Trang thai hop dong
	private int status;
	// Duong dan file hoa don
	private String filePath;
	// File name
	private String fileName;

	// thanh tien
	private double cost;

	// danh sach khach hang
	private List<Customer> listCustomer;
	// danh sach cong trinh
	private List<Construction> listConstruction;

	private Date createDate;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bill_id")
	public Long getBillID() {
		return billID;
	}

	public void setBillID(Long billID) {
		this.billID = billID;
	}

	@Column(name = "customer_id")
	public Long getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Long customerID) {
		this.customerID = customerID;
	}

	@Column(name = "prd_id")
	public int getPrdID() {
		return prdID;
	}

	public void setPrdID(int prdID) {
		this.prdID = prdID;
	}

	@Column(name = "from_time")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	@Column(name = "to_time")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	@Column(name = "start_time")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "end_time")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name = "construction_id")
	public Long getConstructionID() {
		return constructionID;
	}

	public void setConstructionID(Long constructionID) {
		this.constructionID = constructionID;
	}

	@Column(name = "bill_code")
	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	@Column(name = "path")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "file_name")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Transient
	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	@Transient
	public List<Customer> getListCustomer() {
		return listCustomer;
	}

	public void setListCustomer(List<Customer> listCustomer) {
		this.listCustomer = listCustomer;
	}

	@Transient
	public List<Construction> getListConstruction() {
		return listConstruction;
	}

	public void setListConstruction(List<Construction> listConstruction) {
		this.listConstruction = listConstruction;
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
	public Date getDateInput() {
		return dateInput;
	}

	public void setDateInput(Date dateInput) {
		this.dateInput = dateInput;
	}

	@Transient
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Transient
	public String getStrDateInput() {
		try {
			return DateTimeUtils.convertDateToString(dateInput, Constants.FORMAT_DATE_DD_MM_YYY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage(), e);
		}
		return "";
	}

	public void setStrDateInput(String strDateInput) {
		this.strDateInput = strDateInput;
	}

	@Transient
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

	@Transient
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

	@Transient
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

	@Transient
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

	@Column(name = "create_date")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "Bills [billID=" + billID + ", billCode=" + billCode + ", customerID=" + customerID + ", prdID=" + prdID
				+ ", fromDate=" + fromDate + ", toDate=" + toDate + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", constructionID=" + constructionID + ", status=" + status + ", filePath=" + filePath + ", fileName="
				+ fileName + ", cost=" + cost + ", listCustomer=" + listCustomer + ", listConstruction="
				+ listConstruction + "]";
	}

}
