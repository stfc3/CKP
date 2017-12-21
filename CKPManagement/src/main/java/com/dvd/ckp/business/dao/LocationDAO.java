package com.dvd.ckp.business.dao;

import java.util.List;

import com.dvd.ckp.domain.Location;

public interface LocationDAO {

	public List<Location> getListLocation();

	public void saveLocation(Location location);

	public int update(Location location);

	public int delete(Location location);

	public void importData(List<Location> vlstData);

}
