package com.personal_finance_app;

public class Goal {
    private final String description;
    private final String date;
    private final int exp;

    public Goal(String description, String date, int exp) {
        this.description = description;
        this.date = date;
        this.exp = exp;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public int getExp() {
        return exp;
    }
}