package com.personal_finance_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MilestonesActivity extends AppCompatActivity {
    private RecyclerView milestonesRecyclerView;
    private MilestoneAdapter milestoneAdapter;
    private List<Milestone> milestones;
    private List<Expense> expenses;

    private PreferenceHelper preferencesHelper;

    private UserProfileFragment userProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestones);

        preferencesHelper = new PreferenceHelper(this);

        // Load user level or EXP if needed for milestones
        int userLevel = preferencesHelper.getUserLevel();
        int userExp = preferencesHelper.getUserExp();

        Log.d("MilestonesActivity", "User EXP: " + userExp);

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
        String username = prefs.getString("user_name", "User");
        int exp = prefs.getInt("user_exp", 0);
        int level = prefs.getInt("user_level", 1);

        // Update the EXP bar in the fragment
        if (userProfileFragment != null) {
            int progress = exp % 15;
            int progressPercentage = (progress * 100) / 15;
            userProfileFragment.updateUserProfile(level + 1, progressPercentage, username);
        }

        // Initialize RecyclerView and Adapter
        milestones = new ArrayList<>();  // Initialize the milestones list
        initializeMilestones();  // Populate the milestones list
        milestoneAdapter = new MilestoneAdapter(this, milestones);  // Create the adapter
        RecyclerView recyclerView = findViewById(R.id.milestones_recycler_view);

        // Set LayoutManager before setting the adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  // Add this line
        recyclerView.setAdapter(milestoneAdapter);  // Set the adapter for the RecyclerView

        // Notify the adapter that the data has changed
        milestoneAdapter.notifyDataSetChanged();

        // Check if any milestone should be unlocked based on current EXP
        checkAndUnlockMilestones(exp);


        // Initialize BottomNavigationView
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Set selected item for the current activity
        navView.setSelectedItemId(R.id.menu_milestones);

        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_expenses) {
                startActivity(new Intent(this, ExpensesActivity.class));
                overridePendingTransition(0, 0); // No animation
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

    private void initializeMilestones() {
        milestones.add(new Milestone("Rookie", "Started your journey!", true, R.drawable.ic_goal_getter, R.drawable.ic_goal_getter_locked));
        milestones.add(new Milestone("Goal Getter", "Completed 5 goals", false, R.drawable.ic_novice_saver, R.drawable.ic_novice_saver_locked));
        milestones.add(new Milestone("Novice Saver", "Reached level 3", false, R.drawable.ic_budget_hustler, R.drawable.ic_budget_hustler_locked));
        milestones.add(new Milestone("Mission Accomplished", "Completed 10 goals", false, R.drawable.ic_treasure_hoarder, R.drawable.ic_treasure_hoarder_locked));
        milestones.add(new Milestone("Goal Champion", "Completed 15 goals", false, R.drawable.ic_goal_getter, R.drawable.ic_goal_getter_locked));
        milestones.add(new Milestone("Completion Champion", "Completed 30 goals", false, R.drawable.ic_budget_hustler, R.drawable.ic_budget_hustler_locked));
        milestones.add(new Milestone("Apprentice", "Reached level 10", false, R.drawable.ic_treasure_hoarder, R.drawable.ic_treasure_hoarder_locked));
        milestones.add(new Milestone("Mastermind", "Reached level 30", false, R.drawable.ic_novice_saver, R.drawable.ic_novice_saver_locked));
        milestones.add(new Milestone("Budget Hustler", "Completed 50 goals", false, R.drawable.ic_budget_hustler, R.drawable.ic_budget_hustler_locked));
        milestones.add(new Milestone("Maverick", "Tracked finances for 5 days", false, R.drawable.ic_novice_saver, R.drawable.ic_novice_saver_locked));
        milestones.add(new Milestone("Seasoned Financier", "Tracked finances for 15 days", false, R.drawable.ic_seasoned_financier, R.drawable.ic_seasoned_financier_locked));
        milestones.add(new Milestone("Receipt Hunter", "Scan 5 receipts", false, R.drawable.ic_receipt_hunter, R.drawable.ic_receipt_hunter_locked));
        milestones.add(new Milestone("Treasure Hoarder", "Scan 15 receipts", false, R.drawable.ic_treasure_hoarder, R.drawable.ic_treasure_hoarder_locked));

        Log.d("Milestones", "Milestones List Size: " + milestones.size());
    }

    private void checkAndUnlockMilestones(int totalExp) {

        int newLevel = 0;
        newLevel= preferencesHelper.getUserLevel() + 1;
        Log.d("MilestonesActivity", "User Level: " + newLevel);


        //goal getter - complete 5 goals, 25 exp
        if (totalExp >= 25) {
            for (int i = 0; i < milestones.size(); i++) {
                Milestone milestone = milestones.get(i);
                if (milestone.getTitle().equals("Goal Getter") && !milestone.isUnlocked()) {
                    milestone.setUnlocked(true);
                    milestoneAdapter.notifyItemChanged(i);
                }
            }
        }

        //novice saver - level 3
        if (newLevel >= 3) {
            for (int i = 0; i < milestones.size(); i++) {
                Milestone milestone = milestones.get(i);
                if (milestone.getTitle().equals("Novice Saver") && !milestone.isUnlocked()) {
                    milestone.setUnlocked(true);
                    milestoneAdapter.notifyItemChanged(i);
                }
            }
        }
        //mission accomplished - 10 goals , 50 exp
        if (totalExp >= 50) {
            for (int i = 0; i < milestones.size(); i++) {
                Milestone milestone = milestones.get(i);
                if (milestone.getTitle().equals("Mission Accomplished") && !milestone.isUnlocked()) {
                    milestone.setUnlocked(true);
                    milestoneAdapter.notifyItemChanged(i);
                }
            }
        }

        //goal champion - 15 goals, 75 exp
        if (totalExp>=75) {
            for (int i = 0; i < milestones.size(); i++) {
                Milestone milestone = milestones.get(i);
                if (milestone.getTitle().equals("Goal Champion") && !milestone.isUnlocked()) {
                    milestone.setUnlocked(true);
                    milestoneAdapter.notifyItemChanged(i);
                }
            }
        }

        //completion champion - 30, 150
        if (totalExp>=150) {
            for (int i = 0; i < milestones.size(); i++) {
                Milestone milestone = milestones.get(i);
                if (milestone.getTitle().equals("Completion Champion") && !milestone.isUnlocked()) {
                    milestone.setUnlocked(true);
                    milestoneAdapter.notifyItemChanged(i);
                }
            }
        }

        //Mastermind - level 30
        if (newLevel >= 30) {
            for (int i = 0; i < milestones.size(); i++) {
                Milestone milestone = milestones.get(i);
                if (milestone.getTitle().equals("Mastermind") && !milestone.isUnlocked()) {
                    milestone.setUnlocked(true);
                    milestoneAdapter.notifyItemChanged(i);
                }
            }
        }

        //Budget hustler - 50 goals, 250 exp
        if (newLevel >= 250) {
            for (int i = 0; i < milestones.size(); i++) {
                Milestone milestone = milestones.get(i);
                if (milestone.getTitle().equals("Budget Hustler") && !milestone.isUnlocked()) {
                    milestone.setUnlocked(true);
                    milestoneAdapter.notifyItemChanged(i);
                }
            }
        }

    }

}
