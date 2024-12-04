package com.personal_finance_app;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private static final String PREF_NAME = "personal_finance_prefs";
    private static final String USER_EXP = "user_exp";
    private static final String USER_LEVEL = "user_level";
    private static final String USER_NAME = "user_name";

    private final SharedPreferences sharedPreferences;

    public PreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("personal_finance_prefs", Context.MODE_PRIVATE);
    }

    public int getUserExp() {
        return sharedPreferences.getInt("user_exp", 0); // Default value is 0
    }

    public void setUserExp(int exp) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_exp", exp);
        editor.apply();
    }

    public int getUserLevel() {
        return sharedPreferences.getInt("user_level", 1); // Default value is 1
    }

    public void setUserLevel(int level) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(USER_LEVEL, level);
        editor.apply();
    }
    public String getUsername() {
        return sharedPreferences.getString(USER_NAME, "User");  // Default username is "User"
    }

    public void setUsername(String username) {
        sharedPreferences.edit()
                .putString(USER_NAME, username)
                .apply();
    }

    public User getCurrentUser() {
        int exp = getUserExp();
        int level = getUserLevel();
        String username = getUsername();
        return new User(exp, level, username);
    }




}