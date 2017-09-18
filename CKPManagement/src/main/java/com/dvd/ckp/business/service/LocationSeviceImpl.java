package com.dvd.ckp.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dvd.ckp.business.dao.LocationDAO;
import com.dvd.ckp.business.dao.PumpsDAO;
import com.dvd.ckp.domain.Location;
import com.dvd.ckp.domain.Pumps;

public class LocationSeviceImpl implements LocationServices {
	@Autowired
	LocationDAO locationDAO;

	@Override
	public List<Location> getListLocation() {
		// TODO Auto-generated method stub
		return locationDAO.getListLocation();
	}

	

}
