package com.dvd.ckp.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dvd.ckp.business.dao.PumpsDAO;
import com.dvd.ckp.business.dao.StaffDAO;
import com.dvd.ckp.domain.Pumps;
import com.dvd.ckp.domain.Staff;
import com.dvd.ckp.domain.StaffQuantity;

public class StaffSeviceImpl implements StaffServices {
	@Autowired
	StaffDAO staffDAO;

	@Override
	public List<Staff> getAllData() {
		// TODO Auto-generated method stub
		return staffDAO.getData();
	}

	@Override
	public void save(Staff staff) {
		// TODO Auto-generated method stub
		staffDAO.save(staff);

	}

	@Override
	public int update(Staff staff) {
		// TODO Auto-generated method stub
		return staffDAO.update(staff);
	}

	@Override
	public int detele(Staff staff) {
		// TODO Auto-generated method stub
		return staffDAO.delete(staff);
	}

	@Override
	public void importData(List<Staff> staff) {
		// TODO Auto-generated method stub
		staffDAO.importData(staff);

	}

	@Override
	public List<StaffQuantity> getQuantity() {
		// TODO Auto-generated method stub
		return staffDAO.getQuantity();
	}

	@Override
	public void save(StaffQuantity quantity) {
		// TODO Auto-generated method stub
		staffDAO.save(quantity);

	}

	@Override
	public int update(StaffQuantity quantity) {
		// TODO Auto-generated method stub
		return staffDAO.update(quantity);
	}

}
