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
public class ChartLineEntity {

    private String categoryType;
    private String chartTitle;
    private String chartSubTitle;
    private String yAXisTitle;
    private String labelFormat;
    private boolean shared;
    private String color;
    private int lineWidth;
    private int markerRadius;

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

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

    public String getyAXisTitle() {
        return yAXisTitle;
    }

    public void setyAXisTitle(String yAXisTitle) {
        this.yAXisTitle = yAXisTitle;
    }

    public String getLabelFormat() {
        return labelFormat;
    }

    public void setLabelFormat(String labelFormat) {
        this.labelFormat = labelFormat;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getMarkerRadius() {
        return markerRadius;
    }

    public void setMarkerRadius(int markerRadius) {
        this.markerRadius = markerRadius;
    }
    
}
