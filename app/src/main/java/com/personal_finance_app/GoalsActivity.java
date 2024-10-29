package com.personal_finance_app;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
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

        // Load goals data
        loadGoalsData();
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


}

