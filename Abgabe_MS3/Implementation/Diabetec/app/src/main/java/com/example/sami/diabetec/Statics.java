package com.example.sami.diabetec;

import com.google.gson.annotations.SerializedName;

public class Statics {

    @SerializedName("nValues")
    int nValues;

    @SerializedName("minValue")
    int minValue;

    @SerializedName("maxValue")
    int maxValue;

    @SerializedName("averageValue")
    float averageValue;

    @SerializedName("HbA1c")
    float hba1c;

    public int getnValues() {
        return nValues;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public float getAverageValue() {
        return averageValue;
    }

    public float getHba1c() {
        return hba1c;
    }

    public Statics(int nValues, int minValue, int maxValue, float averageValue, float hba1c) {
        this.nValues = nValues;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.averageValue = averageValue;
        this.hba1c = hba1c;
    }
}
