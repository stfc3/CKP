/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.UserService;
import com.dvd.ckp.domain.Users;
import com.dvd.ckp.utils.Constants;
import com.dvd.ckp.utils.EncryptUtil;
import com.dvd.ckp.utils.SpringConstant;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

/**
 *
 * @author dmin
 */
public class LoginController extends SelectorComposer<Component> {

    private static final Logger logger = Logger.getLogger(LoginController.class);
    @WireVariable
    protected UserService userService;
    @Wire
    private Label mesg;
    @Wire
    private Label mesgChangePassSuccess;
    @Wire
    private Textbox txtUserName;
    @Wire
    private Textbox txtPassword;
    private Session session;
    @Wire
    Div changePassword;

    //Change Pasowrd
    @Wire
    private Textbox txtOldPassword;
    @Wire
    private Textbox txtNewPassword;
    @Wire
    private Textbox txtConfirmPassword;
    @Wire
    private Textbox captcha;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        session = Sessions.getCurrent();
        userService = (UserService) SpringUtil.getBean(SpringConstant.USER_SERVICES);
        if (session.getAttribute(Constants.TOKEN) != null) {
            Executions.sendRedirect(Constants.PAGE_HOME);
        }
    }

    @Listen("onClick = #btnSubmit; onOK = #txtPassword")
    public void login() {
        String vstrUserName = txtUserName.getValue();
        String vstrPassword = txtPassword.getValue();
        Users vuser = userService.getUserByName(vstrUserName);
        if (vuser == null) {
            mesg.setValue(Labels.getLabel("login.error"));
        } else if (!vstrPassword.equals(EncryptUtil.decrypt(vuser.getPassword()))) {
            mesg.setValue(Labels.getLabel("login.error"));
        } else {
            session.setAttribute(Constants.TOKEN, new String(Base64.encodeBase64(vstrUserName.getBytes())));
            session.setAttribute(Constants.SESSION_USER, vuser);
            Executions.sendRedirect(Constants.PAGE_HOME);
        }
    }

    @Listen("onClick = #changePassword")
    public void changePassword() throws IOException {
        Executions.sendRedirect(Constants.PAGE_CHANGE_PASSWORD);
    }

    @Listen("onClick = #btnChangePassword")
    public void updatePassowrd() {
        String vstrUserName = txtUserName.getValue();
        String vstrOldPassword = txtOldPassword.getValue();
        String vstrNewPassword = txtNewPassword.getValue();
        String vstrConfirmPassword = txtConfirmPassword.getValue();
        Users vuser = userService.getUserByName(vstrUserName);

        if (captcha.getValue() == null || "".equals(captcha.getValue())) {
            mesg.setValue(Labels.getLabel("login.change.password.captcha.empty"));
        }
        if (vuser == null) {
            mesg.setValue(Labels.getLabel("login.error"));
        } else if (!EncryptUtil.encrypt(vstrOldPassword).equals(vuser.getPassword())) {
            mesg.setValue(Labels.getLabel("login.old.password.error"));
//        } else if (!vstrNewPassword.matches(Constants.PASSWORD_PATTERN)) {
//            mesg.setValue(Labels.getLabel("login.new.password.error"));
        } else if (!vstrNewPassword.equals(vstrConfirmPassword)) {
            mesg.setValue(Labels.getLabel("login.confirm.password.error"));
        } else {
            try {
                vuser.setPassword(EncryptUtil.encrypt(vstrNewPassword));
                userService.insertOrUpdateUser(vuser);
                session.invalidate();
                Executions.sendRedirect(Constants.PAGE_LOGIN);
//                mesgChangePassSuccess.setValue(Labels.getLabel("login.change.password.success"));
            } catch (Exception e) {
                mesg.setValue(Labels.getLabel("login.change.password.fail"));
            }

        }
    }

}
