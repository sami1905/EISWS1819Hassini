package com.example.sami.diabetec;

import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

public class BookItem {

    private String day;
    private LineChart chart;
    private int nValues;
    private int averageValue;
    private int veryLowValuesPercents;
    private int lowValuesPercents;
    private int withinRangePercents;
    private int highValuesPercents;
    private String eventList;

    public BookItem(String day, LineChart chart, int nValues, int averageValue, int veryLowValuesPercents, int lowValuesPercents, int withinRangePercents, int highValuesPercents, String eventList) {
        this.day = day;
        this.chart = chart;
        this.nValues = nValues;
        this.averageValue = averageValue;
        this.veryLowValuesPercents = veryLowValuesPercents;
        this.lowValuesPercents = lowValuesPercents;
        this.withinRangePercents = withinRangePercents;
        this.highValuesPercents = highValuesPercents;
        this.eventList = eventList;
    }

    public String getDay() {
        return day;
    }

    public LineChart getChart() {
        return chart;
    }

    public int getnValues() {
        return nValues;
    }

    public int getAverageValue() {
        return averageValue;
    }

    public int getVeryLowValuesPercents() {
        return veryLowValuesPercents;
    }

    public int getLowValuesPercents() {
        return lowValuesPercents;
    }

    public int getWithinRangePercents() {
        return withinRangePercents;
    }

    public int getHighValuesPercents() {
        return highValuesPercents;
    }

    public String getEventList() {
        return eventList;
    }
}
