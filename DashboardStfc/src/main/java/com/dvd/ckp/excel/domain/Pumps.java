package com.dvd.ckp.excel.domain;

import java.io.Serializable;

import com.dvd.ckp.excel.annotation.ExcelColumn;
import com.dvd.ckp.excel.annotation.ExcelEntity;

@ExcelEntity(dataStartRowIndex = 9, signalConstant = "Pumps")
public class Pumps implements Serializable {
	/**
	 * @author viettx
	 * @since 03/09/2017
	 */
	private static final long serialVersionUID = 1L;	
	private int index;
	private String pumpsCode;
	private String pumpsName;
	private String pumpsCapacity;
	private String pumpsHight;
	private String pumpsFar;
	private String description;

	
	@ExcelColumn(name = "B", nullable = true)
	public String getPumpsCode() {
		return pumpsCode;
	}

	public void setPumpsCode(String pumpsCode) {
		this.pumpsCode = pumpsCode;
	}

	@ExcelColumn(name = "C", nullable = true)
	public String getPumpsName() {
		return pumpsName;
	}

	public void setPumpsName(String pumpsName) {
		this.pumpsName = pumpsName;
	}

	@ExcelColumn(name = "D", nullable = true)
	public String getPumpsCapacity() {
		return pumpsCapacity;
	}

	public void setPumpsCapacity(String pumpsCapacity) {
		this.pumpsCapacity = pumpsCapacity;
	}

	@ExcelColumn(name = "E", nullable = true)
	public String getPumpsHight() {
		return pumpsHight;
	}

	public void setPumpsHight(String pumpsHight) {
		this.pumpsHight = pumpsHight;
	}

	@ExcelColumn(name = "F", nullable = true)
	public String getPumpsFar() {
		return pumpsFar;
	}

	public void setPumpsFar(String pumpsFar) {
		this.pumpsFar = pumpsFar;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	

}
