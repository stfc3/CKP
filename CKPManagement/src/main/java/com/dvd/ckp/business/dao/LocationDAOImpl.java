package com.dvd.ckp.business.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dvd.ckp.domain.Location;

@Repository
public class LocationDAOImpl implements LocationDAO {
	private static final Logger logger = Logger.getLogger(LocationDAOImpl.class);
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
		getCurrentSession().saveOrUpdate(location);

	}

	@Override
	public int update(Location location) {
		try {
			StringBuilder builder = new StringBuilder("update location set ");
			builder.append("location_code = :code, ");
			builder.append("location_name = :name, ");
			builder.append("location_type = :type, ");
			builder.append("location_value = :value, ");
			builder.append("status = :status, ");
			builder.append("create_date = :create_date ");
			builder.append("where location_id = :id ");
			Query query = getCurrentSession().createSQLQuery(builder.toString());
			query.setParameter("code", location.getLocationCode());
			query.setParameter("name", location.getLocationName());
			query.setParameter("type", location.getLocationType());
			query.setParameter("value", location.getLocationValue());
			query.setParameter("status", location.getStatus());
			query.setParameter("create_date", location.getCreateDate() );
			query.setParameter("id", location.getLocationID());
			return query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return -1;
	}

	@Override
	public int delete(Location location) {
		try {
			StringBuilder builder = new StringBuilder("update location set ");
			builder.append("status = :status ");
			builder.append("where location_id = :id ");
			Query query = getCurrentSession().createSQLQuery(builder.toString());
			query.setParameter("status", location.getStatus());
			query.setParameter("id", location.getLocationID());
			return query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return -1;
	}

	@Override
	public void importData(List<Location> vlstData) {
		Session session = getCurrentSession();
		try {
			if (vlstData != null && !vlstData.isEmpty()) {
				for (Location location : vlstData) {
					session.save(location);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.beginTransaction().rollback();
		} finally {
			session.close();
		}

	}

}
