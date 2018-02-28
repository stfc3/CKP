/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.daond.demo.chart;

/**
 *
 * @author daond
 */
public class DataDemoLineColumn {

    private static String[] categories;
    private static Number[] rainfall, temperature, pressure;

    static {
        categories = new String[]{
            "Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec"
        };
        rainfall = new Double[]{
            49.9, 71.5, 106.4, 129.2,
            144.0, 176.0, 135.6, 148.5,
            216.4, 194.1, 95.6, 54.4};
        temperature = new Double[]{
            7.0, 6.9, 9.5, 14.5,
            18.2, 21.5, 25.2, 26.5,
            23.3, 18.3, 13.9, 9.6};
        pressure = new Number[]{
            1016, 1016, 1015.9, 1015.5,
            1012.3, 1009.5, 1009.6, 1010.2,
            1013.1, 1016.9, 1018.2, 1016.7};
    }

    public static Number[] getRainfall() {
        return rainfall;
    }

    public static Number[] getTemperature() {
        return temperature;
    }

    public static Number[] getPressure() {
        return pressure;
    }

    public static String[] getCategories() {
        return categories;
    }
}
