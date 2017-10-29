package com.dvd.ckp.business.dao;

import java.util.List;

import com.dvd.ckp.domain.RentEquiment;

public interface RentEquimentDAO {
	public List<RentEquiment> getAllRentPumps();

	public void insert(RentEquiment value);

	public void update(RentEquiment value);

	public void delete(RentEquiment value);
}
