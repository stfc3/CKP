/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.dashboard.chart.marker;

/**
 *
 * @author daond
 */
public class PointMarker {

    private String value;
    private String symbolOrPieName;
    private boolean pieSliced;
    private boolean pieSelected;

    public PointMarker() {
    }

    public PointMarker(String value, String symbol) {
        this.value = value;
        this.symbolOrPieName = symbol;
    }

    public PointMarker(String value, String symbol, boolean pieSliced, boolean pieSelected) {
        this.symbolOrPieName = symbol;
        this.value = value;
        this.pieSliced = pieSliced;
        this.pieSelected = pieSelected;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSymbolOrPieName() {
        return symbolOrPieName;
    }

    public void setSymbolOrPieName(String symbol) {
        this.symbolOrPieName = symbol;
    }

    public boolean isPieSliced() {
        return pieSliced;
    }

    public void setPieSliced(boolean pieSliced) {
        this.pieSliced = pieSliced;
    }

    public boolean isPieSelected() {
        return pieSelected;
    }

    public void setPieSelected(boolean pieSelected) {
        this.pieSelected = pieSelected;
    }

}
