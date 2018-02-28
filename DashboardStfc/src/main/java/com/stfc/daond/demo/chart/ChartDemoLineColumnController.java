/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.daond.demo.chart;

import org.zkoss.chart.Charts;
import org.zkoss.chart.Legend;
import org.zkoss.chart.Series;
import org.zkoss.chart.YAxis;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

/**
 *
 * @author daond
 */
public class ChartDemoLineColumnController extends SelectorComposer<Window> {

    @Wire
    Charts chart;

    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        chart.getXAxis().setCategories(DataDemoLineColumn.getCategories());
//        chart.getXAxis().setCro

        // Primary y axis
        YAxis yAxis1 = chart.getYAxis();
        yAxis1.getLabels().setFormat("{value}°C");
        yAxis1.getLabels().setStyle("color: " + chart.getColors().get(1).stringValue());
        yAxis1.setTitle("Temperature");
        yAxis1.getTitle().setStyle("color: " + chart.getColors().get(1).stringValue());

        // Secondary y axis
        YAxis yAxis2 = chart.getYAxis(1);
        yAxis2.setTitle("Rainfall");
        yAxis2.getTitle().setStyle("color: " + chart.getColors().get(0).stringValue());
        yAxis2.getLabels().setFormat("{value} mm");
        yAxis2.getLabels().setStyle("color: " + chart.getColors().get(0).stringValue());
        yAxis2.setOpposite(true);

        chart.getTooltip().setShared(true);

        Legend legend = chart.getLegend();
//        legend.setLayout("vertical");
//        legend.setAlign("left");
//        legend.setX(120);
//        legend.setVerticalAlign("top");
//        legend.setY(100);
//        legend.setFloating(true);
//        legend.setBackgroundColor("white");
        legend.setBorderWidth(0);
        initSeries();
    }

    private void initSeries() {
        Series rainfall = new Series("Rainfall");
        rainfall.setName("Rainfall");
        rainfall.setType("column");
        rainfall.setYAxis(1);
        rainfall.setData(DataDemoLineColumn.getRainfall());
        chart.addSeries(rainfall);
        chart.getPlotOptions().getColumn().getTooltip().setValueSuffix(" mm");

        Series temperature = new Series("Temperature");
        temperature.setName("Temperature");
        temperature.setType("spline");
        temperature.setData(DataDemoLineColumn.getTemperature());
        chart.addSeries(temperature);
        chart.getPlotOptions().getSpline().getTooltip().setValueSuffix("°C");
    }
}
