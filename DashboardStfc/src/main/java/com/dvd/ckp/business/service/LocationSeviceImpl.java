package com.dvd.ckp.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dvd.ckp.business.dao.LocationDAO;
import com.dvd.ckp.domain.Location;

public class LocationSeviceImpl implements LocationServices {
	@Autowired
	LocationDAO locationDAO;

	@Override
	public List<Location> getListLocation() {
		// TODO Auto-generated method stub
		return locationDAO.getListLocation();
	}

	@Override
	public void save(Location location) {
		// TODO Auto-generated method stub
		locationDAO.saveLocation(location);

	}

	@Override
	public int update(Location location) {
		// TODO Auto-generated method stub
		return locationDAO.update(location);
	}

	@Override
	public int detele(Location location) {
		// TODO Auto-generated method stub
		return locationDAO.delete(location);
	}

	@Override
	public void importData(List<Location> lstLocation) {
		// TODO Auto-generated method stub
		locationDAO.importData(lstLocation);
	}

}
