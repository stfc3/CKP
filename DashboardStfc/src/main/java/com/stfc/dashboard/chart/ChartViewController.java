/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.dashboard.chart;

import com.google.gson.Gson;
import com.stfc.dashboard.chart.ChartBase;
import com.stfc.dashboard.chart.data.SeriesModel;
import com.stfc.dashboard.chart.entity.ChartLineEntity;
import com.stfc.dashboard.chart.entity.ChartPieEntity;
import com.stfc.dashboard.chart.marker.PointMarker;
import com.stfc.dashboard.constant.DashboardConstant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.chart.Charts;

/**
 *
 * @author daond
 */
public class ChartViewController {

    //doc db
    String chartClassLine = "com.stfc.dashboard.chart.ChartBaseLine";
    String chartClassPie = "com.stfc.dashboard.chart.ChartBasePie";
    //Lấy từ 1 trường trong db. Là dạng json. Chứa các thông tin cấu hình chart như tên, format, color
    String chartLineDetail = "{\n"
            + "	\"categoryType\": \"0\",\n"
            + "	\"chartTitle\": \"Doanh thu\",\n"
            + "	\"chartSubTitle\": \"\",\n"
            + "	\"yAXisTitle\": \"Tỷ đồng\",\n"
            + "	\"labelFormat\": \"\",\n"
            + "	\"shared\": \"true\",\n"
            + "	\"color\": \"#666666\",\n"
            + "	\"lineWidth\": \"1\",\n"
            + "	\"markerRadius\": \"4\"\n"
//            sql
//           list series
//                   {
//                        name
//                        order: ten cot
//                           
//                   }
            + "}";
    String chartPieDetail = "{\n"
            + "	\"chartTitle\": \"Tỷ trọng bơm\",\n"
            + "	\"chartSubTitle\": \"\",\n"
            + "	\"allowPointSelect\": \"true\",\n"
            + "	\"cursor\": \"pointer\",\n"
            + "	\"dataLabel\": \"false\",\n"
            + "	\"showInLegend\": \"true\"\n"
            + "}";

    private Gson gson = new Gson();
    
    private String chartType = "PIE";

    public Charts buildChart() {
        Charts chart = new Charts();
        chart.setStyle("chartbase");
        chart.setZclass("chartbase");
        try {
            if (DashboardConstant.TYPE_CHART_LINE.equalsIgnoreCase(chartType)) {
                ChartBase chartView = (ChartBase) Class.forName(chartClassLine).newInstance();
                ChartLineEntity lineEntity = gson.fromJson(chartLineDetail, ChartLineEntity.class);

                //du lieu test
                List<SeriesModel> lstseries = dataLine();
                //build chart base line
                chart = chartView.buildLineChart(chart, lineEntity.getCategoryType(), lineEntity.getChartTitle(),
                        lineEntity.getChartSubTitle(), lineEntity.getyAXisTitle(), lineEntity.getLabelFormat(),
                        lineEntity.isShared(), lineEntity.getColor(), lineEntity.getLineWidth(), lineEntity.getMarkerRadius(),
                        lstseries);

            } else if (DashboardConstant.TYPE_CHART_PIE.equalsIgnoreCase(chartType)) {
                ChartBase chartView = (ChartBase) Class.forName(chartClassPie).newInstance();
                ChartPieEntity lineEntity = gson.fromJson(chartPieDetail, ChartPieEntity.class);

                //du lieu test
                List<SeriesModel> lstseries = dataPie();
                //build chart base pie
                chart = chartView.buildPieChart(chart, lineEntity.getChartTitle(), lineEntity.getChartSubTitle(),
                        lineEntity.isAllowPointSelect(), lineEntity.getCursor(), lineEntity.isDataLabel(),
                        lineEntity.isShowInLegend(), lstseries);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ChartViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return chart;
    }
    
    public Charts buildChartDemo(String chartType) {
        Charts chart = new Charts();
        try {
            if (DashboardConstant.TYPE_CHART_LINE.equalsIgnoreCase(chartType)) {
                ChartBase chartView = (ChartBase) Class.forName(chartClassLine).newInstance();
                ChartLineEntity lineEntity = gson.fromJson(chartLineDetail, ChartLineEntity.class);

                //du lieu test
                List<SeriesModel> lstseries = dataLine();
                //build chart base line
                chart = chartView.buildLineChart(chart, lineEntity.getCategoryType(), lineEntity.getChartTitle(),
                        lineEntity.getChartSubTitle(), lineEntity.getyAXisTitle(), lineEntity.getLabelFormat(),
                        lineEntity.isShared(), lineEntity.getColor(), lineEntity.getLineWidth(), lineEntity.getMarkerRadius(),
                        lstseries);

            } else if (DashboardConstant.TYPE_CHART_PIE.equalsIgnoreCase(chartType)) {
                ChartBase chartView = (ChartBase) Class.forName(chartClassPie).newInstance();
                ChartPieEntity lineEntity = gson.fromJson(chartPieDetail, ChartPieEntity.class);

                //du lieu test
                List<SeriesModel> lstseries = dataPie();
                //build chart base pie
                chart = chartView.buildPieChart(chart, lineEntity.getChartTitle(), lineEntity.getChartSubTitle(),
                        lineEntity.isAllowPointSelect(), lineEntity.getCursor(), lineEntity.isDataLabel(),
                        lineEntity.isShowInLegend(), lstseries);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ChartViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return chart;
    }

    private List<SeriesModel> dataLine() {

        List<PointMarker> point1 = new ArrayList<>();
        PointMarker p1 = new PointMarker("5.2", "square");
        PointMarker p2 = new PointMarker("4.9", "square");
        PointMarker p3 = new PointMarker("5.3", "square");
        PointMarker p4 = new PointMarker("5.6", "square");
        PointMarker p5 = new PointMarker("5.5", "square");
        PointMarker p6 = new PointMarker("5.8", "square");
        PointMarker p7 = new PointMarker("6.3", "square");
        PointMarker p8 = new PointMarker("5.7", "square");
        PointMarker p9 = new PointMarker("6.1", "square");
        PointMarker p10 = new PointMarker("6.5", "square");
        PointMarker p11 = new PointMarker("7.1", "square");
        PointMarker p12 = new PointMarker("8.3", "square");
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
        PointMarker p21 = new PointMarker("6.3", "diamond");
        PointMarker p22 = new PointMarker("5.9", "diamond");
        PointMarker p23 = new PointMarker("5.8", "diamond");
        PointMarker p24 = new PointMarker("6.7", "diamond");
        PointMarker p25 = new PointMarker("6.6", "diamond");
        PointMarker p26 = new PointMarker("6.3", "diamond");
        PointMarker p27 = new PointMarker("7.5", "diamond");
        PointMarker p28 = new PointMarker("7.7", "diamond");
        PointMarker p29 = new PointMarker("8.4", "diamond");
        PointMarker p210 = new PointMarker("9.5", "diamond");
        PointMarker p211 = new PointMarker("8.8", "diamond");
        PointMarker p212 = new PointMarker("10.2", "diamond");
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
        ser1.setSeriesName("Năm 2016");
        ser1.setLstPoint(point1);
        lstseries.add(ser1);
        SeriesModel ser21 = new SeriesModel();
        ser21.setSeriesName("Năm 2017");
        ser21.setLstPoint(point21);

        lstseries.add(ser21);
        return lstseries;
    }

    private List<SeriesModel> dataPie() {

        List<PointMarker> point1 = new ArrayList<>();
        PointMarker p1 = new PointMarker("58", "Bơm tĩnh");
        PointMarker p2 = new PointMarker("28", "Bơm cần");
        PointMarker p3 = new PointMarker("14", "Bơm cần phân phối");
        point1.add(p1);
        point1.add(p2);
        point1.add(p3);
        
        List<SeriesModel> lstseries = new ArrayList<>();
        SeriesModel ser1 = new SeriesModel();
        ser1.setSeriesName("Khối lượng bơm");
        ser1.setLstPoint(point1);
        lstseries.add(ser1);
        return lstseries;
    }

}
