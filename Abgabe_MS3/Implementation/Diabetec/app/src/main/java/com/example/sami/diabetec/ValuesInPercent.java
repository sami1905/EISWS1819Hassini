package com.example.sami.diabetec;

import com.google.gson.annotations.SerializedName;

public class ValuesInPercent {

    @SerializedName("nValues")
    private int nValues;

    @SerializedName("nVeryLow")
    private int nVeryLow;

    @SerializedName("nLow")
    private int nLow;

    @SerializedName("nWithinRange")
    private int nWithinRange;

    @SerializedName("nHigh")
    private int nHigh;

    @SerializedName("percentVeryLow")
    private float percentVeryLow;

    @SerializedName("percentLow")
    private float percentLow;

    @SerializedName("percentWithinRange")
    private float percentWithinRange;

    @SerializedName("percentHigh")
    private float percentHigh;

    public int getnValues() {
        return nValues;
    }

    public int getnVeryLow() {
        return nVeryLow;
    }

    public int getnLow() {
        return nLow;
    }

    public int getnWithinRange() {
        return nWithinRange;
    }

    public int getnHigh() {
        return nHigh;
    }

    public float getPercentVeryLow() {
        return percentVeryLow;
    }

    public float getPercentLow() {
        return percentLow;
    }

    public float getPercentWithinRange() {
        return percentWithinRange;
    }

    public float getPercentHigh() {
        return percentHigh;
    }


    public ValuesInPercent(int nValues, int nVeryLow, int nLow, int nWithinRange, int nHigh, float percentVeryLow, float percentLow, float percentWithinRange, float percentHigh) {
        this.nValues = nValues;
        this.nVeryLow = nVeryLow;
        this.nLow = nLow;
        this.nWithinRange = nWithinRange;
        this.nHigh = nHigh;
        this.percentVeryLow = percentVeryLow;
        this.percentLow = percentLow;
        this.percentWithinRange = percentWithinRange;
        this.percentHigh = percentHigh;
    }
}
