/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.dashboard.view;

import com.stfc.dashboard.constant.DashboardConstant;
import com.stfc.dashboard.chart.ChartViewController;
import java.util.Calendar;
import java.util.Date;
import org.zkoss.chart.Charts;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

/**
 *
 * @author daond
 */
public class DashboardView extends SelectorComposer<Div> {

    @Wire
    private Datebox dataDate;

    @Wire
    Div dashboardView;

    public void doAfterCompose(Div comp) throws Exception {
        super.doAfterCompose(comp);
        Date today = Calendar.getInstance().getTime();
        dataDate.setValue(today);

        addContentDemo("LINE");
        addContentDemo("PIE");
        addContentDemo("LINE");
        addContentDemo("LINE");

    }

    private void buildContent() {
        for (int i = 0; i < 4; i++) {
            addContent();
        }
    }

    private void addContent() {
        Div dashboardContent = new Div();
        dashboardContent.setClass("dashboard-content");
        dashboardContent.setParent(dashboardView);

        Panel panelContent = new Panel();
        panelContent.setBorder("none");
        panelContent.setCollapsible(true);
        panelContent.setClass("transparent");
        panelContent.setParent(dashboardContent);

        Caption contentCaption = new Caption();
        contentCaption.setIconSclass("z-icon-star orange");
        contentCaption.setClass("dashboard-content-title");
        contentCaption.setLabel("Popular Domains");
        contentCaption.setParent(panelContent);

        Panelchildren panel = new Panelchildren();
        ChartViewController chartView = new ChartViewController();
        Charts chart = chartView.buildChart();
        chart.setParent(panel);

        panel.setParent(panelContent);
    }
    
    private void addContentDemo(String chartType) {
        Div dashboardContent = new Div();
        dashboardContent.setClass("dashboard-content");
        dashboardContent.setParent(dashboardView);

        Panel panelContent = new Panel();
        panelContent.setBorder("none");
        panelContent.setCollapsible(true);
        panelContent.setClass("transparent");
        panelContent.setParent(dashboardContent);

        Caption contentCaption = new Caption();
        contentCaption.setIconSclass("z-icon-star orange");
        contentCaption.setClass("dashboard-content-title");
        contentCaption.setLabel("Popular Domains");
        contentCaption.setParent(panelContent);

        Panelchildren panel = new Panelchildren();
        ChartViewController chartView = new ChartViewController();
        Charts chart = chartView.buildChartDemo(chartType);
        chart.setParent(panel);

        panel.setParent(panelContent);
    }
}
