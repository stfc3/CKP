package com.dvd.ckp.business.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dvd.ckp.domain.Staff;
import com.dvd.ckp.domain.StaffQuantity;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;

@Repository
public class StaffDAOImpl implements StaffDAO {

    private static final Logger logger = Logger.getLogger(StaffDAOImpl.class);
    @Autowired
    SessionFactory sessionFactory;

    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Staff> getData() {
        // TODO Auto-generated method stub
        try {
            Query query = getCurrentSession().getNamedQuery("Staff.getAllStaff");
            List<Staff> lstData = query.list();
            if (lstData != null && !lstData.isEmpty()) {
                return lstData;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public void save(Staff staff) {
        try {
            getCurrentSession().saveOrUpdate(staff);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    @Override
    public int update(Staff staff) {
        try {
            StringBuilder strSQL = new StringBuilder("update staff set ");
            strSQL.append(" staff_code = :code, ");
            strSQL.append(" staff_name = :name, ");
            strSQL.append(" phone = :phone, ");
            strSQL.append(" email = :email, ");
            strSQL.append(" address = :address, ");
            strSQL.append(" birthday = :birthday, ");
            strSQL.append(" department = :department, ");
            strSQL.append(" position = :position, ");
            strSQL.append(" status = :status, ");
            strSQL.append(" create_date = :createDate ");
            strSQL.append(" where staff_id = :id");

            Query query = getCurrentSession().createSQLQuery(strSQL.toString());
            query.setParameter("code", staff.getStaffCode());
            query.setParameter("name", staff.getStaffName());
            query.setParameter("phone", staff.getPhone());
            query.setParameter("email", staff.getEmail());
            query.setParameter("address", staff.getAddress());
            query.setParameter("birthday", staff.getBirthday());
            query.setParameter("department", staff.getDepartment());
            query.setParameter("position", staff.getPosition());
            query.setParameter("status", staff.getStatus());
            query.setParameter("createDate", staff.getCreateDate());
            query.setParameter("id", staff.getStaffId());
            int numberRow = query.executeUpdate();
            return numberRow;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return -1;
    }

    @Override
    public int delete(Staff staff) {
        try {
            StringBuilder strSQL = new StringBuilder("update staff set ");
            strSQL.append(" status = :status ");
            strSQL.append(" where staff_id = :id");
            Query query = getCurrentSession().createSQLQuery(strSQL.toString());
            query.setParameter("status", staff.getStatus());
            query.setParameter("id", staff.getStaffId());
            int numberRow = query.executeUpdate();
            return numberRow;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return -1;
    }

    @Override
    public void importData(List<Staff> vlstData) {
        Session session = getCurrentSession();
        try {

            if (vlstData != null && !vlstData.isEmpty()) {
                for (Staff staff : vlstData) {
                    session.saveOrUpdate(staff);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            session.beginTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public List<StaffQuantity> getQuantity(Long billDetailID) {
        // TODO Auto-generated method stub
        try {
            Query query = getCurrentSession().getNamedQuery("StaffQuantity.getAll");
            query.setParameter("billId", billDetailID);
            @SuppressWarnings("unchecked")
            List<StaffQuantity> lstData = query.list();
            if (lstData != null && !lstData.isEmpty()) {
                return lstData;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void save(List<StaffQuantity> quantity) {
        // TODO Auto-generated method stub
        Session session = getCurrentSession();
        if (quantity != null && !quantity.isEmpty()) {
            for (StaffQuantity staffQuantity : quantity) {
                session.saveOrUpdate(staffQuantity);
            }
        }

    }

    @Override
    public int update(StaffQuantity quantity) {
        try {
            StringBuilder strSQL = new StringBuilder("update quantity_staff set ");
            strSQL.append(" staff_id = :staff, ");
            strSQL.append(" bill_detail_id = :bill, ");
            strSQL.append(" quantity = :quantity ");
            strSQL.append(" where id = :id");
            Query query = getCurrentSession().createSQLQuery(strSQL.toString());

            query.setParameter("staff", quantity.getStaffId());
            query.setParameter("bill", quantity.getBillId());
            query.setParameter("quantity", quantity.getQuantity());
            query.setParameter("id", quantity.getId());
            int numberRow = query.executeUpdate();
            return numberRow;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return -1;
    }

    @Override
    public void delete(Long billDetailId) {
        try {
            StringBuilder strDelete = new StringBuilder("delete from quantity_staff where bill_detail_id = :billId");
            Query query = getCurrentSession().createSQLQuery(strDelete.toString());
            query.setParameter("billId", billDetailId);
            query.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
            logger.error(e.getMessage(), e);
        }

    }

    @Override
    public List<StaffQuantity> getAll() {
        try {
            StringBuilder builder = new StringBuilder("select q.id as id, q.bill_detail_id as billId,s.staff_id as staffId,s.staff_code as staffCode ");
            builder.append(" ,s.staff_name as staffName,q.quantity as quantity from quantity_staff q, staff s ");
            builder.append(" where q.staff_id=s.staff_id  order by q.bill_detail_id,s.staff_id,s.staff_name");
            Query query = getCurrentSession().createSQLQuery(builder.toString())
                    .addScalar("id", StandardBasicTypes.LONG)
                    .addScalar("billId", StandardBasicTypes.LONG)
                    .addScalar("staffId", StandardBasicTypes.LONG)
                    .addScalar("staffCode", StandardBasicTypes.STRING)
                    .addScalar("staffName", StandardBasicTypes.STRING)
                    .addScalar("quantity", StandardBasicTypes.DOUBLE)
                    .setResultTransformer(Transformers.aliasToBean(StaffQuantity.class));;
            List<StaffQuantity> lstData = query.list();
            if (lstData != null && !lstData.isEmpty()) {
                return lstData;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

}
