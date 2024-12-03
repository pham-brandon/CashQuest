package com.personal_finance_app;

import java.io.Serializable;

public class Expense implements Serializable {
    private String title;
    private double amount;
    private String type;
    private String frequency;

    public Expense(String title, double amount, String type, String frequency) {
        this.title = title;
        this.amount = amount;
        this.type = type;
        this.frequency = frequency;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getFrequency() {
        return frequency;
    }
}