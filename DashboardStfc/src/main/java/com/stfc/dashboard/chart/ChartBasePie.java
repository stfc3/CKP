/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.dashboard.chart;

import com.stfc.dashboard.chart.data.SeriesModel;
import java.util.List;
import org.zkoss.chart.Chart;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Legend;
import org.zkoss.chart.Series;
import org.zkoss.chart.plotOptions.PiePlotOptions;

/**
 *
 * @author daond
 */
public class ChartBasePie extends ChartBase {
    
    @Override
    public Charts buildPieChart(Charts pchart, String chartTitle, String chartSubTitle, boolean allowPointSelect,
            String cursor, boolean dataLabel, boolean showInLegend, List<SeriesModel> lstseries) {
        
        Chart chartOptional = pchart.getChart();
        chartOptional.setPlotBorderWidth(0);
        chartOptional.setPlotShadow(false);
        
        pchart.setTitle(chartTitle);
        pchart.setSubtitle(chartSubTitle);
        pchart.getTooltip().setPointFormat(
                "{series.name}: <b>{point.percentage:.1f}%</b>");
        pchart.setType("pie");
        PiePlotOptions plotOptions = pchart.getPlotOptions().getPie();
        plotOptions.setAllowPointSelect(allowPointSelect);
        plotOptions.setCursor("pointer");
        plotOptions.setCursor(cursor);
        plotOptions.getDataLabels().setEnabled(dataLabel);
        plotOptions.setShowInLegend(showInLegend);
        plotOptions.setBorderWidth(0);
        for (int i = 0; i < lstseries.size(); i++) {
            Series series = pchart.getSeries(i);
            lstseries.get(i).processPieChart(series);
        }
        Legend legend = pchart.getLegend();
        legend.setBorderWidth(0);
        
        return pchart;
    }
    
}
