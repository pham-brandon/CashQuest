package com.personal_finance_app;

public class User {
    private int exp;
    private int level;
    private String username;

    // Default - level 1 and 0 EXP
    public User() {
        this.exp = 0;
        this.level = 1;
        this.username = "User";
    }

    // Constructor with arguments
    public User(int exp, int level, String username) {
        this.exp = exp;
        this.level = level;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(int level) {
        this.username = username;
    }

}