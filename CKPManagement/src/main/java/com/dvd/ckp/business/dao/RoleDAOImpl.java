/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.dao;

import com.dvd.ckp.domain.Role;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author daond
 */
@Repository
public class RoleDAOImpl implements RoleDAO {

    @Autowired
    SessionFactory sessionFactory;

    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Role> getAllRole() {
        Query query = getCurrentSession().getNamedQuery("Role.getAllRole");
        List<Role> lstRoles = query.list();
        if (lstRoles != null && !lstRoles.isEmpty()) {
            return lstRoles;
        }
        return null;
    }

    @Override
    public void insertOrUpdateRole(Role role) {
        getCurrentSession().saveOrUpdate(role);
    }

}
