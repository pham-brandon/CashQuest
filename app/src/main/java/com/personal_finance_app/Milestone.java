package com.personal_finance_app;

public class Milestone {
    private String title;
    private String description;
    private boolean isUnlocked;
    private int unlockedIconResId;
    private int lockedIconResId;

    public Milestone(String title, String description, boolean isUnlocked, int unlockedIconResId, int lockedIconResId) {
        this.title = title;
        this.description = description;
        this.isUnlocked = isUnlocked;
        this.unlockedIconResId = unlockedIconResId;
        this.lockedIconResId = lockedIconResId;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isUnlocked() { return isUnlocked; }
    public int getUnlockedIconResId() { return unlockedIconResId; }
    public int getLockedIconResId() { return lockedIconResId; }
}