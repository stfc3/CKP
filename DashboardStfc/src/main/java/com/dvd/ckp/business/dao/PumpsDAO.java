package com.dvd.ckp.business.dao;

import java.util.List;

import com.dvd.ckp.domain.Pumps;

public interface PumpsDAO {

	public List<Pumps> getListPumps();

	public void savePumps(Pumps pumps);

	public int update(Pumps pumps);

	public int delete(Pumps pumps);

	public void importData(List<Pumps> vlstData);
	
	
	

}
