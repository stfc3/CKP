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

import com.dvd.ckp.bean.QuantityValue;
import com.dvd.ckp.domain.BillViewDetail;
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
			builder.append("create_date = :createDate, ");
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
			query.setParameter("createDate", bills.getCreateDate());
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
			builder.append(" ,create_date = :create_date ");
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
			query.setParameter("create_date", billsDetail.getCreateDate());
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

	@Override
	public List<BillsDetail> getBillDetail(Long billID) {
		try {
			Query query = getCurrentSession().getNamedQuery("BillsDetail.getAllBillDetailByID");
			query.setParameter("billId", billID);
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
	public List<BillViewDetail> getDataView(Long billID) {
		try {
			StringBuilder builder = new StringBuilder(
					"select b.bill_id as billID,d.bill_detail_id as billDetailID,b.from_time as fromDate,b.start_time as startTime, ");
			builder.append(
					" b.end_time as endTime, b.to_time as toDate,d.quantity as quantity,d.quantity_approve as quantityApprove, l.location_name as location, p.pump_name as pump from bills b ");
			builder.append(" left join bill_detail d ");
			builder.append(" on b.bill_id = d.bill_id ");
			builder.append(" left join location l ");
			builder.append(" on d.location_id = l.location_id ");
			builder.append(" left join pumps p ");
			builder.append(" on d.pump_id = p.pump_id ");			
			builder.append(" where b.bill_id = :billID ");
			builder.append(" and b.status = 1 ");
			builder.append(" and d.status = 1 ");
			builder.append(" and l.status = 1 ");
			Query query = getCurrentSession().createSQLQuery(builder.toString())
					.addScalar("billID", StandardBasicTypes.LONG).addScalar("billDetailID", StandardBasicTypes.LONG)
					.addScalar("fromDate", StandardBasicTypes.DATE).addScalar("startTime", StandardBasicTypes.DATE)
					.addScalar("endTime", StandardBasicTypes.DATE).addScalar("toDate", StandardBasicTypes.DATE)
					.addScalar("quantity", StandardBasicTypes.DOUBLE)
					.addScalar("quantityApprove", StandardBasicTypes.DOUBLE)
					.addScalar("location", StandardBasicTypes.STRING)
					.addScalar("pump", StandardBasicTypes.STRING)
					.setResultTransformer(Transformers.aliasToBean(BillViewDetail.class));
			query.setParameter("billID", billID);
			List<BillViewDetail> lstData = query.list();
			if (lstData != null && !lstData.isEmpty()) {
				return lstData;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	@Override
	public List<QuantityValue> getQuantity(Long billDetailId) {
		// TODO Auto-generated method stub
		try {
			StringBuilder strSQL = new StringBuilder("CALL calculator_quantity(:billDetail)");
			Query query = getCurrentSession().createSQLQuery(strSQL.toString())
					.addScalar("v_quantity", StandardBasicTypes.DOUBLE)
					.setResultTransformer(Transformers.aliasToBean(QuantityValue.class));
			query.setParameter("billDetail", billDetailId);
			@SuppressWarnings("unchecked")
			List<QuantityValue> lstData = query.list();
			return lstData;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void update(Integer isFar, Double quantityConvert, Integer maxStaff, Long billDetail) {
		try {
			StringBuilder builder = new StringBuilder("update bill_detail set ");
			builder.append(" max_staff = :maxStaff, ");
			builder.append(" is_far = :isFar, ");
			builder.append(" quantity_convert = :quantityConvert ");
			builder.append(" where bill_detail_id = :billDetailId ");
			Query query = getCurrentSession().createSQLQuery(builder.toString());
			query.setParameter("maxStaff", maxStaff);
			query.setParameter("isFar", isFar);
			query.setParameter("quantityConvert", quantityConvert);
			query.setParameter("billDetailId", billDetail);
			query.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
		} finally {
			// TODO: handle finally clause
		}

	}

	@Override
	public void upadte(Double quantityApprove, Double totalApprove, Long billDetailID) {
		// TODO Auto-generated method stub
		try {
			StringBuilder builder = new StringBuilder("update bill_detail set ");
			builder.append(" quantity_approve = :quantityApprove, ");
			builder.append(" total_approve = :totalApprove ");
			builder.append(" where bill_detail_id = :billDetailId ");
			Query query = getCurrentSession().createSQLQuery(builder.toString());
			query.setParameter("quantityApprove", quantityApprove);
			query.setParameter("totalApprove", totalApprove);
			query.setParameter("billDetailId", billDetailID);
			query.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
		} finally {
			// TODO: handle finally clause
		}
	}

}
