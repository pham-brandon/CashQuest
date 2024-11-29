package com.personal_finance_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MilestonesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestones);


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
            Intent intent = new Intent(this, AddExpense.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });
    }
}
