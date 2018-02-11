package com.dvd.ckp.business.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dvd.ckp.domain.Rent;
import com.dvd.ckp.domain.RentEquiment;

@Repository
public class RentEquipmentDAOImpl implements RentEquimentDAO {
	private static final Logger logger = Logger.getLogger(RentEquipmentDAOImpl.class);
	@Autowired
	SessionFactory sessionFactory;

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<RentEquiment> getAllRentPumps() {
		// TODO Auto-generated method stub
		try {
			Query query = getCurrentSession().getNamedQuery("RentEquipment.getAll");
			List<RentEquiment> lstDataReturn = query.list();
			if (lstDataReturn != null && !lstDataReturn.isEmpty()) {
				return lstDataReturn;
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void insert(RentEquiment value) {
		// TODO Auto-generated method stub
		try {
			getCurrentSession().saveOrUpdate(value);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void update(RentEquiment value) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		try {

			StringBuilder builder = new StringBuilder();
			builder.append("update rent_equipment set rent_type = :rentType, ");
			builder.append("distribute_id = :distribute, ");
			builder.append("customers_id = :customer, ");
			builder.append("construction_id = :construction, ");
			builder.append("monitoring = :monitoring, ");
			builder.append("majority = :majority, ");
			builder.append("start_date = :startDate, ");
			builder.append("end_date = :endDate, ");
			builder.append("average_price = :averagePrice, ");
			builder.append("create_date = current_timestamp ");
			builder.append("where rent_id = :rentId ");
			Query query = session.createSQLQuery(builder.toString());
			query.setParameter("rentType", value.getRentType());
			query.setParameter("distribute", value.getDistribute());
			query.setParameter("customer", value.getCustomerID());
			query.setParameter("construction", value.getConstructionID());
			query.setParameter("monitoring", value.getMonitoring());
			query.setParameter("majority", value.getMajority());
			query.setParameter("startDate", value.getStartDate());
			query.setParameter("endDate", value.getEndDate());
			query.setParameter("averagePrice", value.getAveragePrice());
			query.setParameter("rentId", value.getRentID());
			query.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void delete(RentEquiment value) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		try {

			StringBuilder builder = new StringBuilder();
			builder.append("update rent_equipment set ");
			builder.append("status = :status, ");
			builder.append("create_date = current_timestamp ");
			builder.append("where rent_id = :rentId ");
			Query query = session.createSQLQuery(builder.toString());
			query.setParameter("status", value.getStatus());
			query.setParameter("rentId", value.getRentID());
			query.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public List<Rent> storeRent(Long rentID) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		try {
			StringBuilder strSQL = new StringBuilder("CALL calculator_rent(:rentID)");
			Query query = getCurrentSession().createSQLQuery(strSQL.toString())
					.addScalar("code", StandardBasicTypes.LONG).addScalar("message", StandardBasicTypes.STRING)
					.addScalar("revenue", StandardBasicTypes.DOUBLE)
					.setResultTransformer(Transformers.aliasToBean(Rent.class));
			query.setParameter("rentID", rentID);
			@SuppressWarnings("unchecked")
			List<Rent> lstData = query.list();
			return lstData;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public BigInteger getMaxID() {
		// TODO Auto-generated method stub
		try {
			StringBuilder strSQL = new StringBuilder("SELECT LAST_INSERT_ID()");
			Query query = getCurrentSession().createSQLQuery(strSQL.toString());
			return (BigInteger) query.list().get(0);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}
