package com.dvd.ckp.business.service;

import java.util.List;

import com.dvd.ckp.domain.Location;

public interface LocationServices {
	public List<Location> getListLocation();
	
	public void save(Location location);
	
	public int update(Location location);
	
	public int detele(Location location);
	
	public void importData(List<Location> lstLocation);
}
	