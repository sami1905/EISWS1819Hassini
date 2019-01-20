package com.example.sami.diabetec;

import com.google.gson.annotations.SerializedName;

public class Value {

    @SerializedName("id")
    private Integer id;

    @SerializedName("type")
    private String type;

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("value")
    private int value;


    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getValue() {
        return value;
    }


    public Value(Integer id, String type, String date, String time, int value) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.time = time;
        this.value = value;
    }
}
