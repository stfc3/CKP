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
	private int pumpsID;
	private String pumpsCode;
	private String pumpsName;
	private int pumpsCapacity;
	private int pumpsHight;
	private int pumpsFar;
	private int status;
	private String statusName;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pump_id")
	public int getPumpsID() {
		return pumpsID;
	}

	public void setPumpsID(int pumpsID) {
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
	public int getPumpsCapacity() {
		return pumpsCapacity;
	}

	public void setPumpsCapacity(int pumpsCapacity) {
		this.pumpsCapacity = pumpsCapacity;
	}

	@Column(name = "pump_high")
	public int getPumpsHight() {
		return pumpsHight;
	}

	public void setPumpsHight(int pumpsHight) {
		this.pumpsHight = pumpsHight;
	}

	@Column(name = "pump_far")
	public int getPumpsFar() {
		return pumpsFar;
	}

	public void setPumpsFar(int pumpsFar) {
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

	public Pumps(int pumpsID, String pumpsCode, String pumpsName, int pumpsCapacity, int pumpsHight, int pumpsFar,
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
