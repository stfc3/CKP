package com.dvd.ckp.business.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dvd.ckp.domain.Bills;
import com.dvd.ckp.domain.BillsDetail;
import com.dvd.ckp.domain.CalculatorRevenue;

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

	@Override
	public void save(BillsDetail billsDetail) {
		getCurrentSession().save(billsDetail);

	}

	@Override
	public void update(BillsDetail billsDetail) {
		try {
			StringBuilder builder = new StringBuilder("update bill_detail set ");
			builder.append(" pump_id = :pumpId, ");
			builder.append(" pump_type = :pumpType, ");
			builder.append(" location_id = :locationId, ");
			builder.append(" location_type = :locationType, ");
			builder.append(" quantity = :quantity, ");
			if (billsDetail.getQuantityApprove() != null) {
				builder.append(" quantity_approve = :quantityApprove, ");
			}
			builder.append(" shift = :shift, ");
			builder.append(" total = :total ");
			if (billsDetail.getTotalApprove() != null) {
				builder.append(" ,total_approve = :totalApprove ");
			}
			if (billsDetail.getIsFar() != null) {
				builder.append(" ,is_far = :isFar ");
			}
			if (billsDetail.getQuantityConvert() != null) {
				builder.append(" ,quantity_convert = :quantityConvert ");
			}
			builder.append(" where bill_detail_id = :id ");
			Query query = getCurrentSession().createSQLQuery(builder.toString());
			query.setParameter("pumpId", billsDetail.getPumpID());
			query.setParameter("pumpType", billsDetail.getPumpID());
			query.setParameter("locationId", billsDetail.getLocationId());
			query.setParameter("locationType", billsDetail.getLocationType());
			query.setParameter("quantity", billsDetail.getQuantity());
			if (billsDetail.getQuantityApprove() != null) {
				query.setParameter("quantityApprove", billsDetail.getQuantityApprove());
			}
			query.setParameter("shift", billsDetail.getShift());
			query.setParameter("total", billsDetail.getTotal());
			if (billsDetail.getTotalApprove() != null) {
				query.setParameter("totalApprove", billsDetail.getTotalApprove());
			}
			if (billsDetail.getIsFar() != null) {
				query.setParameter("isFar", billsDetail.getIsFar());
			}
			if (billsDetail.getQuantityConvert() != null) {
				query.setParameter("quantityConvert", billsDetail.getQuantityConvert());
			}
			query.setParameter("id", billsDetail.getBillDetailId());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	@Override
	public void delete(BillsDetail billsDetail) {
		try {
			StringBuilder builder = new StringBuilder("update bill_detail set ");
			builder.append(" status = :status ");
			builder.append(" where bill_detail_id = :id ");
			Query query = getCurrentSession().createSQLQuery(builder.toString());
			query.setParameter("status", billsDetail.getStatus());
			query.setParameter("id", billsDetail.getBillDetailId());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	@Override
	public List<CalculatorRevenue> calculatorRevenue(Long constructionId, Long pumpId, Long pumpType, Long locationType,
			Long locationID, Double quantity, int shift) {
		try {
			String sql = "CALL calculator_revenue(:construction,:pump,:pump_type,:location_type,:location_id,:quantity,:shift)";
			Query query = getCurrentSession().createSQLQuery(sql).addScalar("total_revenue", StandardBasicTypes.DOUBLE)
					.addScalar("description", StandardBasicTypes.STRING)
					.setResultTransformer(Transformers.aliasToBean(CalculatorRevenue.class));

			query.setParameter("construction", constructionId);
			query.setParameter("pump", pumpId).setParameter("pump_type", pumpType);
			query.setParameter("location_type", locationType);
			query.setParameter("location_id", locationID);
			query.setParameter("quantity", quantity);
			query.setParameter("shift", shift);

			List<CalculatorRevenue> lstData = query.list();
			return lstData;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}
