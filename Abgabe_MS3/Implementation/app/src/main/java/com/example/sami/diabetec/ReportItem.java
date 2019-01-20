package com.example.sami.diabetec;

public class ReportItem {

    private String title;
    private String description;

    public ReportItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void changeTitle(String text){
        title = text;
    }
}
