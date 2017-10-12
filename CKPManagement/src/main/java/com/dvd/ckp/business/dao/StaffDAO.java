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

	public List<StaffQuantity> getQuantity(Long billDetailId);

	public void save(List<StaffQuantity> quantity);
	
	public void delete(Long billDetailId);

	public int update(StaffQuantity quantity);

}
