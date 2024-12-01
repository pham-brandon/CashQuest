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
    private ProgressBar expBar;
    private TextView levelTextView;
    private PreferenceHelper preferencesHelper;
    private int lastEXP = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        preferencesHelper = new PreferenceHelper(this);

        // Initialize the EXP and Level
        expBar = findViewById(R.id.progressBar);
        levelTextView = findViewById(R.id.user_level);


        // Get EXP and Level from preferences
        int exp = preferencesHelper.getUserExp();
        int level = preferencesHelper.getUserLevel();

        expBar.setProgress(exp % 15);
        levelTextView.setText("Level: " + level);

        goalsList = new ArrayList<>();
        loadGoalsFromPreferences();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.goals_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //User currentUser = preferencesHelper.getCurrentUser();

        // Reset the user's info for testing ...
        //currentUser.setExp(0);
        //currentUser.setLevel(1);
        //updateEXPBar(0);
        // goalsList.clear(); // Clear the goals list if needed

        goalsAdapter = new GoalsAdapter(goalsList,this);
        recyclerView.setAdapter(goalsAdapter);

        Button addGoalButton = findViewById(R.id.add_goal_button);
        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptNewGoal();
            }
        });

        // Initialize nav bar
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.menu_goals);

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
            Intent intent = new Intent(this, AddExpense.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // load EXP and level from SharedPreferences
        int exp = preferencesHelper.getUserExp();
        int level = preferencesHelper.getUserLevel();

        updateEXPBar(exp);
    }


    private void promptNewGoal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Goal");

        // Set up the input for the description
        final EditText input = new EditText(this);
        input.setHint("Enter goal description");
        builder.setView(input);

        builder.setPositiveButton("Next", (dialog, which) -> {
            String description = input.getText().toString().trim();
            if (!description.isEmpty()) {
                // After description, prompt for due date
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

        // Set the minimum date to today
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void addGoal(String description, String dueDate) {
        goalsList.add(new Goal(description, "Due: " + dueDate, 5));


        goalsAdapter.notifyDataSetChanged();
        saveGoalsToPreferences(); // Save updated goals list
    }

    public void updateEXPBar(int exp) {
        if (exp == lastEXP) return; // Avoid redundant updates
        lastEXP = exp;

        // Calculate level and progress
        int level = exp / 15;  // 15 EXP per level
        int progress = exp % 15;
        int progressPercentage = (progress * 100) / 15;

        // Save updated values to preferences
        preferencesHelper.setUserExp(exp);
        preferencesHelper.setUserLevel(level);

        // Update UI on main thread
        runOnUiThread(() -> {
            levelTextView.setText("Level: " + (level + 1)); // 1-based level
            expBar.setProgress(progressPercentage);
        });

        // Debug logs
        Log.d("EXP Progress", "Calculated progress: " + progressPercentage);
        Log.d("EXP Level", "Current level: " + (level + 1));
    }

    public void increaseEXP(int expIncrease) {
        // Get current EXP and update it
        int currentEXP = preferencesHelper.getUserExp();
        int newEXP = currentEXP + expIncrease;

        // Update the EXP bar and level
        updateEXPBar(newEXP);

        // Save the updated EXP to SharedPreferences
        preferencesHelper.setUserExp(newEXP);
    }
    public void saveGoalsToPreferences() {
        SharedPreferences prefs = getSharedPreferences("personal_finance_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(goalsList);

        editor.putString("goals_list", json);
        editor.apply();
    }

    private void loadGoalsFromPreferences() {
        SharedPreferences prefs = getSharedPreferences("personal_finance_prefs", MODE_PRIVATE);
        String json = prefs.getString("goals_list", null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Goal>>() {}.getType();
            goalsList = gson.fromJson(json, type);
        }
    }
}
