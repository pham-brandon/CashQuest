package com.personal_finance_app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GoalsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GoalsAdapter goalsAdapter;
    private List<Goal> goalsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.goals_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the goals list and adapter
        goalsList = new ArrayList<>();
        goalsAdapter = new GoalsAdapter(goalsList);
        recyclerView.setAdapter(goalsAdapter);

        Button addGoalButton = findViewById(R.id.add_goal_button);
        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptNewGoal();
            }
        });

        // Load goals data for testing
        // loadGoalsData();
    }

    private void loadGoalsData() {
        // Clear the list if necessary
        goalsList.clear();

        // Sample data
        goalsList.add(new Goal("Spend $10 less on coffee this month", "Due: 2024-12-31", 5));
        goalsList.add(new Goal("Eat out minimum once biweekly", "Due: 2024-11-15", 5));
        goalsList.add(new Goal("Buy groceries and cook dinner", "Due: 2025-01-01", 5));

        Log.d("GoalsActivity", "Goals Loaded: " + goalsList.size()); // Log the number of goals added

        // Notify adapter
        goalsAdapter.notifyDataSetChanged();
    }

    private void promptNewGoal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Goal");

        // Set up the input for the description
        final EditText input = new EditText(this);
        input.setHint("Enter goal description");
        builder.setView(input);

        // Set up the dialog buttons
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String description = input.getText().toString().trim();
                if (!description.isEmpty()) {
                    // After description, prompt for due date
                    showDatePicker(description);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private void showDatePicker(final String description) {
        // Get the current date as the default
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the date string
                    String dueDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;

                    // Add the new goal to the list
                    addGoal(description, dueDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void addGoal(String description, String dueDate) {
        // Add new goal to list
        goalsList.add(new Goal(description, "Due: " + dueDate, 5));

        // Notify the adapter to refresh the list
        goalsAdapter.notifyDataSetChanged();

        Log.d("GoalsActivity", "New goal added: " + description + ", Due: " + dueDate);
    }

}

