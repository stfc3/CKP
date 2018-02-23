/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.dashboard.chart.entity;

/**
 *
 * @author daond
 */
public class ChartPieEntity {
    private String chartTitle;
    private String chartSubTitle;
    private boolean allowPointSelect;
    private String cursor;
    private boolean dataLabel;
    private boolean showInLegend;

    public String getChartTitle() {
        return chartTitle;
    }

    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }

    public String getChartSubTitle() {
        return chartSubTitle;
    }

    public void setChartSubTitle(String chartSubTitle) {
        this.chartSubTitle = chartSubTitle;
    }

    public boolean isAllowPointSelect() {
        return allowPointSelect;
    }

    public void setAllowPointSelect(boolean allowPointSelect) {
        this.allowPointSelect = allowPointSelect;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public boolean isDataLabel() {
        return dataLabel;
    }

    public void setDataLabel(boolean dataLabel) {
        this.dataLabel = dataLabel;
    }

    public boolean isShowInLegend() {
        return showInLegend;
    }

    public void setShowInLegend(boolean showInLegend) {
        this.showInLegend = showInLegend;
    }
    
}
