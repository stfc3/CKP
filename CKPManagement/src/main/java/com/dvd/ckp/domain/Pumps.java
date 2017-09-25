package com.dvd.ckp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "pumps")
@NamedQuery(name = "Pump.getAllPumps", query = "FROM Pumps u WHERE status = 1")
public class Pumps 	{
	/**
	 * @author viettx
	 * @since 03/09/2017
	 */
	private static final long serialVersionUID = 1L;
	private int index;
	private Long pumpsID;
	private String pumpsCode;
	private String pumpsName;
	private String pumpsCapacity;
	private String pumpsHight;
	private String pumpsFar;
	private int status;
	private String statusName;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pump_id")
	public Long getPumpsID() {
		return pumpsID;
	}

	public void setPumpsID(Long pumpsID) {
		this.pumpsID = pumpsID;
	}

	@Column(name = "pump_code")
	public String getPumpsCode() {
		return pumpsCode;
	}

	public void setPumpsCode(String pumpsCode) {
		this.pumpsCode = pumpsCode;
	}

	@Column(name = "pump_name")
	public String getPumpsName() {
		return pumpsName;
	}

	public void setPumpsName(String pumpsName) {
		this.pumpsName = pumpsName;
	}

	@Column(name = "pump_capacity")
	public String getPumpsCapacity() {
		return pumpsCapacity;
	}

	public void setPumpsCapacity(String pumpsCapacity) {
		this.pumpsCapacity = pumpsCapacity;
	}

	@Column(name = "pump_high")
	public String getPumpsHight() {
		return pumpsHight;
	}

	public void setPumpsHight(String pumpsHight) {
		this.pumpsHight = pumpsHight;
	}

	@Column(name = "pump_far")
	public String getPumpsFar() {
		return pumpsFar;
	}

	public void setPumpsFar(String pumpsFar) {
		this.pumpsFar = pumpsFar;
	}

	@Column(name = "status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Pumps [pumpsID=" + pumpsID + ", pumpsCode=" + pumpsCode + ", pumpsName=" + pumpsName
				+ ", pumpsCapacity=" + pumpsCapacity + ", pumpsHight=" + pumpsHight + ", pumpsFar=" + pumpsFar
				+ ", status=" + status + "]";
	}


	public Pumps(Long pumpsID, String pumpsCode, String pumpsName, String pumpsCapacity, String pumpsHight, String pumpsFar,
			int status) {
		super();
		this.pumpsID = pumpsID;
		this.pumpsCode = pumpsCode;
		this.pumpsName = pumpsName;
		this.pumpsCapacity = pumpsCapacity;
		this.pumpsHight = pumpsHight;
		this.pumpsFar = pumpsFar;
		this.status = status;
	}

	public Pumps() {
		super();
	}

	@Transient
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Transient
	public String getStatusName() {
		if (status == 1) {
			statusName = "Hoạt động";
		} else if (status == 2) {
			statusName = "Không Hoạt động";
		}
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}
