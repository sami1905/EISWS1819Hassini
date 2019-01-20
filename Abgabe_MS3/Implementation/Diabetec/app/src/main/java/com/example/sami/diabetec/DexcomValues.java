package com.example.sami.diabetec;

import com.google.gson.annotations.SerializedName;

public class DexcomValues {

    @SerializedName("displayTime")
    private String date;

    @SerializedName("value")
    private int value;

    public String getDate() {
        return date;
    }

    public int getValue() {
        return value;
    }

    public DexcomValues(String date, int value) {
        this.date = date;
        this.value = value;
    }
}
