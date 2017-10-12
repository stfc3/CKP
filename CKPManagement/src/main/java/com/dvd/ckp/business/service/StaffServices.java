package com.dvd.ckp.business.service;

import java.util.List;

import com.dvd.ckp.domain.Staff;
import com.dvd.ckp.domain.StaffQuantity;

public interface StaffServices {

	public List<Staff> getAllData();

	public void save(Staff staff);

	public int update(Staff staff);

	public int detele(Staff staff);

	public void importData(List<Staff> staff);

	// staff quantity

	public List<StaffQuantity> getQuantity(Long billDetailID);

	public void save(List<StaffQuantity> quantity);
	
	public void delete(Long billDetailID);

	public int update(StaffQuantity quantity);

}
