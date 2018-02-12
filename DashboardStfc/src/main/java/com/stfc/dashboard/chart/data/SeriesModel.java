/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.dashboard.chart.data;

import com.stfc.dashboard.chart.marker.PointMarker;
import java.util.List;
import org.zkoss.chart.Point;
import org.zkoss.chart.Series;

/**
 *
 * @author daond
 */
public class SeriesModel {

    private List<PointMarker> lstPoint;
    private String seriesName;

    public void process(Series series) {
        for (int i = 0; i < lstPoint.size(); i++) {
            Point point;
            try {
                int pointValue = Integer.parseInt(lstPoint.get(i).getValue());
                point = new Point(pointValue);
            } catch (Exception e) {
                point = new Point(null);
            }
            point.getMarker().setSymbol(lstPoint.get(i).getSymbol());
            series.addPoint(point);
        }
        series.setName(seriesName);
    }

    public List<PointMarker> getLstPoint() {
        return lstPoint;
    }

    public void setLstPoint(List<PointMarker> lstPoint) {
        this.lstPoint = lstPoint;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

}
