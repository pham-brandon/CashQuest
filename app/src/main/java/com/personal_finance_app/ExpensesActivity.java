package com.personal_finance_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ExpensesActivity extends AppCompatActivity {
    private UserProfileFragment userProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        // Initialize the profile fragment
        userProfileFragment = (UserProfileFragment) getSupportFragmentManager().findFragmentById(R.id.user_profile_fragment);
        if (userProfileFragment == null) {
            userProfileFragment = new UserProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.profile_container, userProfileFragment)
                    .commit();
        }

        // Get EXP and Level from preferences
        SharedPreferences prefs = getSharedPreferences("personal_finance_prefs", MODE_PRIVATE);
        int exp = prefs.getInt("user_exp", 0);
        int level = prefs.getInt("user_level", 1);

        // Update the EXP bar in the fragment
        if (userProfileFragment != null) {
            int progress = exp % 15;
            int progressPercentage = (progress * 100) / 15;
            userProfileFragment.updateUserProfile(level + 1, progressPercentage);
        }

        // Initialize BottomNavigationView
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Set selected item for the current activity
        navView.setSelectedItemId(R.id.menu_expenses);

        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_expenses) {
                // Current activity
                return true;
            } else if (itemId == R.id.menu_insights) {
                startActivity(new Intent(this, InsightsActivity.class));
                overridePendingTransition(0, 0); // No animation
                return true;
            } else if (itemId == R.id.menu_goals) {
                startActivity(new Intent(this, GoalsActivity.class));
                overridePendingTransition(0, 0); // No animation
                return true;
            } else if (itemId == R.id.menu_milestones) {
                startActivity(new Intent(this, MilestonesActivity.class));
                overridePendingTransition(0, 0); // No animation
                return true;
            }

            return false;
        });

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> {
            // Navigate to the desired page
            Intent intent = new Intent(this, AddExpenseActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });


    }
    @Override
    public void onBackPressed() {

        // Initialize BottomNavigationView
        BottomNavigationView navView = findViewById(R.id.nav_view);


        // Set selected item for the current activity
        navView.setSelectedItemId(R.id.menu_expenses);

        if (R.id.menu_expenses != navView.getSelectedItemId()) {
            navView.setSelectedItemId(R.id.menu_expenses); // Go to the default tab
        } else {
            super.onBackPressed(); // Exit app
        }
    }


}