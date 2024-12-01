package com.personal_finance_app;


public class Goal {
    private String description;
    private String date;
    private int exp;
    private boolean completed;

    public Goal(String description, String date, int exp) {
        this.description = description;
        this.date = date;
        this.exp = exp;
        this.completed = false;  // Default to not completed
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}