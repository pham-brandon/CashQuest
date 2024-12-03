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
    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getFrequency() {
        return frequency;
    }
    public void setFrequency(String frequency) {
        this.type = frequency;
    }
}