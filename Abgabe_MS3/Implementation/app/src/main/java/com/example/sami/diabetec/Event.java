package com.example.sami.diabetec;

import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("id")
    private Integer id;

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("value")
    private int value;

    @SerializedName("carbohydrates")
    private int carbohydrates;

    @SerializedName("be")
    private int be;

    @SerializedName("correction")
    private int correction;

    @SerializedName("meal_id")
    private int meal_id;

    @SerializedName("insulin_units")
    private int insulin_units;

    @SerializedName("insulin_type")
    private int insulin_type;


    public int getId() {
        return id;
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

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public int getBe() {
        return be;
    }

    public int getCorrection() {
        return correction;
    }

    public int getMeal_id() {
        return meal_id;
    }

    public int getInsulin_units() {
        return insulin_units;
    }

    public int getInsulin_type() {
        return insulin_type;
    }

    public Event(String date, String time, int value, int carbohydrates, int be, int correction, int meal_id, int insulin_units, int insulin_type) {
        this.date = date;
        this.time = time;
        this.value = value;
        this.carbohydrates = carbohydrates;
        this.be = be;
        this.correction = correction;
        this.meal_id = meal_id;
        this.insulin_units = insulin_units;
        this.insulin_type = insulin_type;
    }
}
