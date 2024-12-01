package com.personal_finance_app;

public class User {
    private int exp;
    private int level;

    // Default - level 1 and 0 EXP
    public User() {
        this.exp = 0;
        this.level = 1;
    }

    // Constructor with arguments
    public User(int exp, int level) {
        this.exp = exp;
        this.level = level;
    }

    // Getters and Setters
    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}