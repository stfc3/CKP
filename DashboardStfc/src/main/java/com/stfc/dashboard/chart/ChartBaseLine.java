/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.dashboard.chart;

import com.stfc.dashboard.chart.data.SeriesModel;
import com.stfc.dashboard.chart.marker.PointMarker;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Marker;
import org.zkoss.chart.Series;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;
import com.google.common.base.Strings;
import org.zkoss.zk.ui.util.Clients;

/**
 *
 * @author daond
 */
public class ChartBaseLine extends SelectorComposer<Window> {

    @Wire
    Charts chart;

    /*
    ý tưởng đẩy dữ liệu từ db vào các PointMarker. Trong PointMarker có value và symbol
    Sau đó biểu đồ có bao nhiêu đường thì set bấy nhiêu SeriesModel. SeriesModel gồm tên đường và dữ liệu của đường
    gọi hàm buildChart với list SeriesModel. list có bao nhiêu phần tử thì bấy nhiêu đường
     */
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        List<PointMarker> point1 = new ArrayList<>();
        PointMarker p1 = new PointMarker("1", "url(https://www.highcharts.com/demo/gfx/sun.png)");
        PointMarker p2 = new PointMarker("2", "square");
        PointMarker p3 = new PointMarker("3", "square");
        PointMarker p4 = new PointMarker("4", "square");
        PointMarker p5 = new PointMarker("5", "square");
        PointMarker p6 = new PointMarker("6", "url(https://www.highcharts.com/demo/gfx/snow.png)");
        PointMarker p7 = new PointMarker("7", "square");
        PointMarker p8 = new PointMarker("8", "square");
        PointMarker p9 = new PointMarker("9", "square");
        PointMarker p10 = new PointMarker("10", "square");
        PointMarker p11 = new PointMarker("11", "square");
        PointMarker p12 = new PointMarker("12", "square");
        point1.add(p1);
        point1.add(p2);
        point1.add(p3);
        point1.add(p4);
        point1.add(p5);
        point1.add(p6);
        point1.add(p7);
        point1.add(p8);
        point1.add(p9);
        point1.add(p10);
        point1.add(p11);
        point1.add(p12);

        List<PointMarker> point21 = new ArrayList<>();
        PointMarker p21 = new PointMarker("12", "square");
        PointMarker p22 = new PointMarker("24", "square");
        PointMarker p23 = new PointMarker("33", "square");
        PointMarker p24 = new PointMarker("43", "square");
        PointMarker p25 = new PointMarker("15", "square");
        PointMarker p26 = new PointMarker("36", "square");
        PointMarker p27 = new PointMarker("27", "square");
        PointMarker p28 = new PointMarker("48", "square");
        PointMarker p29 = new PointMarker("29", "square");
        PointMarker p210 = new PointMarker("16", "url(https://www.highcharts.com/demo/gfx/snow.png)");
        PointMarker p211 = new PointMarker("12", "square");
        PointMarker p212 = new PointMarker("19", "square");
        point21.add(p21);
        point21.add(p22);
        point21.add(p23);
        point21.add(p24);
        point21.add(p25);
        point21.add(p26);
        point21.add(p27);
        point21.add(p28);
        point21.add(p29);
        point21.add(p210);
        point21.add(p211);
        point21.add(p212);
        List<SeriesModel> lstseries = new ArrayList<>();
        SeriesModel ser1 = new SeriesModel();
        ser1.setSeriesName("daond1");
        ser1.setLstPoint(point1);
        lstseries.add(ser1);
        SeriesModel ser21 = new SeriesModel();
        ser21.setSeriesName("daond2");
        ser21.setLstPoint(point21);

        lstseries.add(ser21);
        chart = buildChart(chart, 1, "bieu do", "sub title", "daond", "", true, "#666666", 1, 4, lstseries);
        Clients.resize(chart);
//        Toolkit toolkit = Toolkit.getDefaultToolkit();

        // Get the current screen size
//        Dimension scrnsize = toolkit.getScreenSize();
//
//        String strHeight = Integer.toString(scrnsize.height);
//        int width = (scrnsize.width - 210 - 20)/2;
//        
//        chart.setWidth(width);
    }

    private Charts buildChart(Charts pchart, int categoryType, String chartTitle, String chartSubTitle, String yAXisTitle, String labelFormat, boolean shared,
            String color, int lineWidth, int markerRadius, List<SeriesModel> lstseries) {
        if (categoryType == 1) {
            pchart.getXAxis().setCategories("1", "2", "3", "4", "5",
                    "6", "7", "8", "9", "10", "11", "12");
        } else {
            pchart.getXAxis().setCategories("Jan", "Feb", "Mar", "Apr", "May",
                    "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        }
        pchart.setTitle(chartTitle);
        pchart.setType("line");
        if (!Strings.isNullOrEmpty(chartSubTitle)) {
            pchart.setSubtitle(chartSubTitle);
        }
        pchart.getYAxis().setTitle(yAXisTitle);
        pchart.getYAxis().getLabels().setFormat("{value}°");
        pchart.getTooltip().setShared(shared);
        pchart.getTooltip().setCrosshairs(true);

        Marker marker = pchart.getPlotOptions().getSpline().getMarker();
        marker.setRadius(markerRadius);
        marker.setLineColor(color);
        marker.setLineWidth(lineWidth);
        for (int i = 0; i < lstseries.size(); i++) {
            Series series = chart.getSeries(i);
            lstseries.get(i).process(series);
        }
        return pchart;
    }
}
