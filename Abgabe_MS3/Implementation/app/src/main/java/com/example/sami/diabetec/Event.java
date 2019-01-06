package com.example.sami.diabetec;

public class Event {

    private Integer id;

    private String date;

    private int value;

    private int carbohydrates;

    private int be;

    private int correction;

    private int meal_id;

    private int insulin_units;

    private int insulin_type;

    public Event(String date, int value, int carbohydrates, int be, int correction, int meal_id, int insulin_units, int insulin_type) {
        this.date = date;
        this.value = value;
        this.carbohydrates = carbohydrates;
        this.be = be;
        this.correction = correction;
        this.meal_id = meal_id;
        this.insulin_units = insulin_units;
        this.insulin_type = insulin_type;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
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
}
