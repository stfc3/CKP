package com.dvd.ckp.business.dao;

import java.util.List;

import com.dvd.ckp.domain.Staff;
import com.dvd.ckp.domain.StaffQuantity;

public interface StaffDAO {

	public List<Staff> getData();

	public void save(Staff staff);

	public int update(Staff staff);

	public int delete(Staff staff);

	public void importData(List<Staff> vlstData);

	// staff quantity

	public List<StaffQuantity> getQuantity();

	public void save(StaffQuantity quantity);

	public int update(StaffQuantity quantity);

}
