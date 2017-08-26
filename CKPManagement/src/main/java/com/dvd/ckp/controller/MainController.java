/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.UserService;
import com.dvd.ckp.utils.Constants;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 *
 * @author dmin
 */
public class MainController extends SelectorComposer<Component> {

    @WireVariable
    protected UserService userService;
    private Session session;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        session = Sessions.getCurrent();
        if (session.getAttribute(Constants.TOKEN) == null) {
            Executions.sendRedirect(Constants.PAGE_LOGIN);
        }
    }

    @Listen("onClick = #btnLogout")
    public void logout() {
        session.invalidate();
        Executions.sendRedirect(Constants.PAGE_LOGIN);
    }

}
