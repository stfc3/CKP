package com.dvd.ckp.business.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dvd.ckp.domain.Location;

@Repository
public class LocationDAOImpl implements LocationDAO {

	@Autowired
	SessionFactory sessionFactory;

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<Location> getListLocation() {
		Query query = getCurrentSession().getNamedQuery("Location.getAll");
		List<Location> locations = query.list();
		if (locations != null && !locations.isEmpty()) {
			return locations;
		}
		return null;
	}

	@Override
	public void saveLocation(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public int update(Location location) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Location location) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void importData(List<Location> vlstData) {
		// TODO Auto-generated method stub

	}

}
