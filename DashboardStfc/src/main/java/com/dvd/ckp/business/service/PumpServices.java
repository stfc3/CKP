package com.dvd.ckp.business.service;

import java.util.List;

import com.dvd.ckp.domain.Pumps;

public interface PumpServices {
	public List<Pumps> getAllListData();

	public void savePumps(Pumps pumps);
	
	public int update(Pumps pumps);
	
	public int detele(Pumps pumps);
	
	public void importData(List<Pumps> pumps);
}
