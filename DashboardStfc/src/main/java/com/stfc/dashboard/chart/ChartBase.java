/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.dashboard.chart;

import com.stfc.dashboard.chart.data.SeriesModel;
import java.util.List;
import org.zkoss.chart.Charts;

/**
 *
 * @author daond
 */
public class ChartBase {

    public Charts buildLineChart(Charts pchart, String categoryType, String chartTitle, String chartSubTitle, String yAXisTitle, String labelFormat, boolean shared,
            String color, int lineWidth, int markerRadius, List<SeriesModel> lstseries) {
        return null;
    }

    public Charts buildPieChart(Charts pchart, String chartTitle, String chartSubTitle, boolean allowPointSelect,
            String cursor, boolean dataLabel, boolean showInLegend, List<SeriesModel> lstseries) {
        return null;
    }

}
