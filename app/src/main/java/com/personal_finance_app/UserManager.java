package com.personal_finance_app;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_EXP = "user_exp";
    private static final String KEY_LEVEL = "user_level";

    private SharedPreferences sharedPreferences;

    public UserManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Save EXP and Level
    public void saveUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_EXP, user.getExp());
        editor.putInt(KEY_LEVEL, user.getLevel());
        editor.apply();
    }

    // Get the current EXP of the user
    public int getUserExp() {
        return sharedPreferences.getInt(KEY_EXP, 0);  // Default is 0 if not found
    }

    // Get the current level of the user
    public int getUserLevel() {
        return sharedPreferences.getInt(KEY_LEVEL, 1);  // Default is 1 if not found
    }

    // Create a new User object with the saved data
    public User getUser() {
        int exp = getUserExp();
        int level = getUserLevel();
        User user = new User();
        user.setExp(exp);
        user.setLevel(level);
        return user;
    }
}