/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.dashboard.controller;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;

/**
 *
 * @author daond
 */
public class PageController extends SelectorComposer<Div> {

    @Wire
    private Include show;
    
    public void doAfterCompose(Div comp) throws Exception {
        super.doAfterCompose(comp);

//        contextPath = Executions.getCurrent().getContextPath();
//        initSidebar();
//        initSelectDemo();
//        initThemeChooser();
        show.setSrc("chart/line/linechart.zul");
        Clients.resize(show);
    }
}
