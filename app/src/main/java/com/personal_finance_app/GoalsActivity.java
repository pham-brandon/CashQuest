package com.personal_finance_app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GoalsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GoalsAdapter goalsAdapter;
    private List<Goal> goalsList;
    private PreferenceHelper preferencesHelper;
    private int lastEXP = -1;
    private UserProfileFragment userProfileFragment;

    private static final String PREFS_NAME = "GoalsPrefs";
    private static final String GOALS_KEY = "goalsList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        preferencesHelper = new PreferenceHelper(this);

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


        // Initialize goals list and RecyclerView
        goalsList = new ArrayList<>();


        recyclerView = findViewById(R.id.goals_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        goalsAdapter = new GoalsAdapter(goalsList, this);
        recyclerView.setAdapter(goalsAdapter);
        loadGoalsFromPreferences();

        Button addGoalButton = findViewById(R.id.add_goal_button);
        addGoalButton.setOnClickListener(v -> promptNewGoal());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.menu_goals);
        navView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_goals) return true;
            if (item.getItemId() == R.id.menu_expenses) {
                startActivity(new Intent(this, ExpensesActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            if (item.getItemId() == R.id.menu_insights) {
                startActivity(new Intent(this, InsightsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            if (item.getItemId() == R.id.menu_milestones) {
                startActivity(new Intent(this, MilestonesActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddExpenseActivity.class);
            startActivity(intent);
        });

        // Update the EXP and Level in the fragment
        updateEXPBar(exp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGoalsFromPreferences();
        int exp = preferencesHelper.getUserExp();
        int level = preferencesHelper.getUserLevel();
        String username = preferencesHelper.getUsername();
        updateEXPBar(exp);
    }

    public void updateEXPBar(int exp) {
        if (exp == lastEXP) return; // Avoid redundant updates
        lastEXP = exp;

        int level = exp / 15;  // 15 EXP per level
        int progress = exp % 15;
        int progressPercentage = (progress * 100) / 15;

        // Save updated values to preferences
        preferencesHelper.setUserExp(exp);
        preferencesHelper.setUserLevel(level);


        // Update the UserProfileFragment with new level and progress
        if (userProfileFragment != null) {
            userProfileFragment.updateUserProfile(level + 1, progressPercentage, preferencesHelper.getUsername()); // +1 for 1-based level
        }

        // Debug logs
        Log.d("EXP Progress", "Calculated progress: " + progressPercentage);
        Log.d("EXP Level", "Current level: " + (level + 1));
    }

    public void increaseEXP(int expIncrease) {
        int currentEXP = preferencesHelper.getUserExp();
        int newEXP = currentEXP + expIncrease;

        // Update the EXP bar and level
        updateEXPBar(newEXP);

        // Save the updated EXP to SharedPreferences
        preferencesHelper.setUserExp(newEXP);
    }

    private void promptNewGoal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Goal");

        final EditText input = new EditText(this);
        input.setHint("Enter goal description");
        builder.setView(input);

        builder.setPositiveButton("Next", (dialog, which) -> {
            String description = input.getText().toString().trim();
            if (!description.isEmpty()) {
                showDatePicker(description);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showDatePicker(final String description) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String dueDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    addGoal(description, dueDate);
                }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void addGoal(String description, String dueDate) {
        Goal newGoal = new Goal(description, "Due: " + dueDate, 5);
        goalsList.add(newGoal);
        goalsAdapter.notifyItemInserted(goalsList.size() - 1);
        saveGoalsToPreferences();
    }

    public void saveGoalsToPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("GoalsPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String goalsJson = gson.toJson(goalsList);  // Convert goal list to JSON string
        editor.putString("goals", goalsJson);
        editor.apply();  // Save the data
    }

    public void loadGoalsFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("GoalsPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String goalsJson = sharedPreferences.getString("goals", null);

        if (goalsJson != null) {
            Type type = new TypeToken<List<Goal>>(){}.getType();
            List<Goal> loadedGoals = gson.fromJson(goalsJson, type);

            // Clear and update the existing list to maintain the reference
            goalsList.clear();
            goalsList.addAll(loadedGoals);

            // Notify the adapter about data changes
            goalsAdapter.notifyDataSetChanged();
        }
    }
}
