package com.dvd.ckp.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dvd.ckp.business.dao.PumpsDAO;
import com.dvd.ckp.domain.Pumps;

public class PumpSeviceImpl implements PumpServices {
	@Autowired
	PumpsDAO pumpsDAO;

	@Override
	public List<Pumps> getAllListData() {
		// TODO Auto-generated method stub

		return pumpsDAO.getListPumps();
	}

	@Override
	public void savePumps(Pumps pumps) {
		pumpsDAO.savePumps(pumps);

	}

	@Override
	public int update(Pumps pumps) {
		// TODO Auto-generated method stub
		return pumpsDAO.update(pumps);
	}

	@Override
	public int detele(Pumps pumps) {
		// TODO Auto-generated method stub
		return pumpsDAO.delete(pumps);
	}

	@Override
	public void importData(List<Pumps> pumps) {
		pumpsDAO.importData(pumps);

	}

}
