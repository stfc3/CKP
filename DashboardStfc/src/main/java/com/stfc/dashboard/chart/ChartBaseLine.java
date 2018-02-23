/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.dashboard.chart;

import com.stfc.dashboard.chart.data.SeriesModel;
import java.util.List;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Marker;
import org.zkoss.chart.Series;
import com.google.common.base.Strings;
import org.zkoss.chart.Legend;

/**
 *
 * @author daond
 */
public class ChartBaseLine extends ChartBase {

    @Override
    public Charts buildLineChart(Charts pchart, String categoryType, String chartTitle, String chartSubTitle, String yAXisTitle, String labelFormat, boolean shared, String color, int lineWidth, int markerRadius, List<SeriesModel> lstseries) {
        if ("1".equals(categoryType)) {
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
            Series series = pchart.getSeries(i);
            lstseries.get(i).processLineChart(series);
        }
        Legend legend = pchart.getLegend();
        legend.setBorderWidth(0);
        return pchart;
    }

}
