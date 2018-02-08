package com.dvd.ckp.business.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.dvd.ckp.business.dao.RentEquimentDAO;
import com.dvd.ckp.domain.Rent;
import com.dvd.ckp.domain.RentEquiment;

public class RentServicesImpl implements RentServices {
	private static final Logger logger = Logger.getLogger(RentServicesImpl.class);

	@Autowired
	RentEquimentDAO rentPumpDao;

	@Override
	public List<RentEquiment> getAllRentPumps() {
		try {
			return rentPumpDao.getAllRentPumps();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void insert(RentEquiment value) {
		// TODO Auto-generated method stub
		try {
			rentPumpDao.insert(value);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void update(RentEquiment value) {
		// TODO Auto-generated method stub
		try {
			rentPumpDao.update(value);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void delete(RentEquiment value) {
		// TODO Auto-generated method stub
		try {
			rentPumpDao.delete(value);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
		}

	}

	@Override
	public List<Rent> storeRent(Long rentID) {
		// TODO Auto-generated method stub

		return rentPumpDao.storeRent(rentID);

	}

	@Override
	public Long getMaxID() {
		// TODO Auto-generated method stub
		return rentPumpDao.getMaxID();
	}

}
