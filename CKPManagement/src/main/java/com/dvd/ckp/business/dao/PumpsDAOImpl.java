package com.dvd.ckp.business.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dvd.ckp.domain.Pumps;

@Repository
public class PumpsDAOImpl implements PumpsDAO {
	private static final Logger logger = Logger.getLogger(PumpsDAOImpl.class);
	@Autowired
	SessionFactory sessionFactory;

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<Pumps> getListPumps() {
		// TODO Auto-generated method stub
		try {
			Query query = getCurrentSession().getNamedQuery("Pump.getAllPumps");
			List<Pumps> lstData = query.list();
			if (lstData != null && !lstData.isEmpty()) {
				return lstData;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	@Override
	public void savePumps(Pumps pumps) {
		try {
			getCurrentSession().saveOrUpdate(pumps);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	@Override
	public int update(Pumps pumps) {
		try {
			StringBuilder strSQL = new StringBuilder("update pumps set ");
			strSQL.append(" pump_code = :pumpCode, ");
			strSQL.append(" pump_name = :pumpName, ");
			strSQL.append(" pump_capacity = :pumpCapacity, ");
			strSQL.append(" pump_high = :pumpHight, ");
			strSQL.append(" pump_far = :pumpFar, ");
			strSQL.append(" status = :status ");
			strSQL.append(" where pump_id = :pumpId");

			Query query = getCurrentSession().createSQLQuery(strSQL.toString());
			query.setParameter("pumpCode", pumps.getPumpsCode());
			query.setParameter("pumpName", pumps.getPumpsName());
			query.setParameter("pumpCapacity", pumps.getPumpsCapacity());
			query.setParameter("pumpFar", pumps.getPumpsFar());
			query.setParameter("pumpHight", pumps.getPumpsHight());
			query.setParameter("status", pumps.getStatus());
			query.setParameter("pumpId", pumps.getPumpsID());
			int numberRow = query.executeUpdate();
			return numberRow;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return -1;
	}

	@Override
	public int delete(Pumps pumps) {
		try {
			StringBuilder strSQL = new StringBuilder("update pumps set ");
			strSQL.append(" status = :status ");
			strSQL.append(" where pump_id = :pumpId");
			Query query = getCurrentSession().createSQLQuery(strSQL.toString());
			query.setParameter("status", pumps.getStatus());
			query.setParameter("pumpId", pumps.getPumpsID());
			int numberRow = query.executeUpdate();
			return numberRow;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return -1;
	}

	@Override
	public void importData(List<Pumps> vlstData) {
		Session session = getCurrentSession();
		try {

			if (vlstData != null && !vlstData.isEmpty()) {
				for (Pumps pumps : vlstData) {
					session.saveOrUpdate(pumps);
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
