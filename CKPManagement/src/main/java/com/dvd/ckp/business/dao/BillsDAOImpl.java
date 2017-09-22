package com.dvd.ckp.business.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dvd.ckp.domain.Bills;
import com.dvd.ckp.domain.BillsDetail;

@Repository
public class BillsDAOImpl implements BillDAO {
	private static final Logger logger = Logger.getLogger(BillsDAOImpl.class);
	@Autowired
	SessionFactory sessionFactory;

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<Bills> getData() {
		// TODO Auto-generated method stub
		try {
			Query query = getCurrentSession().getNamedQuery("Bills.getAllBills");
			List<Bills> lstData = query.list();
			if (lstData != null && !lstData.isEmpty()) {
				return lstData;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	@Override
	public void save(Bills bills) {

		try {
			getCurrentSession().saveOrUpdate(bills);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	@Override
	public void update(Bills bills) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		try {
			StringBuilder builder = new StringBuilder("update bills set ");
			builder.append("bill_code = :billCode, ");
			builder.append("customer_id = :customerID, ");
			builder.append("prd_id = :prdID, ");
			builder.append("from_time = :fromDate, ");
			builder.append("to_time = :toDate, ");
			builder.append("start_time = :startTime, ");
			builder.append("end_time = :endTime, ");
			builder.append("construction_id = :constructionID, ");
			builder.append("status = :status, ");
			builder.append("path = :filePath, ");
			builder.append("file_name = :fileName where ");
			builder.append("bill_id = :billID ");
			Query query = session.createSQLQuery(builder.toString());
			query.setParameter("billCode", bills.getBillCode());
			query.setParameter("customerID", bills.getCustomerID());
			query.setParameter("prdID", bills.getPrdID());
			query.setParameter("fromDate", bills.getFromDate());
			query.setParameter("toDate", bills.getToDate());
			query.setParameter("startTime", bills.getStartTime());
			query.setParameter("endTime", bills.getEndTime());
			query.setParameter("constructionID", bills.getConstructionID());
			query.setParameter("status", bills.getStatus());
			query.setParameter("filePath", bills.getFilePath());
			query.setParameter("fileName", bills.getFileName());
			query.setParameter("billID", bills.getBillID());

			query.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.beginTransaction().rollback();
		}
	}

	@Override
	public void delete(Bills bills) {
		Session session = getCurrentSession();
		try {
			StringBuilder builder = new StringBuilder("update bills set ");
			builder.append("status = :status where ");
			builder.append("bill_id = :billID ");
			Query query = session.createSQLQuery(builder.toString());
			query.setParameter("status", bills.getStatus());
			query.setParameter("billID", bills.getBillID());

			query.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.beginTransaction().rollback();
		}

	}

	@Override
	public List<BillsDetail> getBillDetail() {
		// TODO Auto-generated method stub
		try {
			Query query = getCurrentSession().getNamedQuery("BillsDetail.getAllBillDetail");
			List<BillsDetail> lstData = query.list();
			if (lstData != null && !lstData.isEmpty()) {
				return lstData;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

}
