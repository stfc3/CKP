/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.UserService;
import com.dvd.ckp.domain.Users;
import java.util.List;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author dmin
 */
public class UserManagement extends SelectorComposer<Component> {

    @WireVariable
    protected UserService userService;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }

    @Listen("onClick = #btnAdd")
    public void add() {
        userService = (UserService) SpringUtil.getBean("UserService");
        List<Users> allReordsInDB = null;
        allReordsInDB = userService.getAll(Users.class);
        Messagebox.show("Total Records : " + allReordsInDB.size());
    }

}
