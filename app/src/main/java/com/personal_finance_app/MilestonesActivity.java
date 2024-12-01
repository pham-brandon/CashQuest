package com.personal_finance_app;

import android.content.Intent;
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


    private PreferenceHelper preferencesHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestones);

        preferencesHelper = new PreferenceHelper(this);

        // Load user level or EXP if needed for milestones
        int userLevel = preferencesHelper.getUserLevel();
        Log.d("MilestonesActivity", "User Level: " + userLevel);

        int userExp = preferencesHelper.getUserExp();
        Log.d("MilestonesActivity", "User EXP: " + userExp);

        // Initialize BottomNavigationView
        BottomNavigationView navView = findViewById(R.id.nav_view);
    // Initialize RecyclerView
        milestonesRecyclerView = findViewById(R.id.milestones_recycler_view);
        milestonesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize milestones list
        milestones = new ArrayList<>();
        milestones.add(new Milestone("Novice Saver", "Tracked finances for 5 days", true, R.drawable.ic_novice_saver, R.drawable.ic_novice_saver_locked));
        milestones.add(new Milestone("Seasoned Financier", "Tracked finances for 15 days", false, R.drawable.ic_seasoned_financier, R.drawable.ic_seasoned_financier_locked));
        milestones.add(new Milestone("Receipt Hunter", "Scan 5 receipts", true, R.drawable.ic_receipt_hunter, R.drawable.ic_receipt_hunter_locked));
        milestones.add(new Milestone("Treasure Hoarder", "Scan 15 receipts", false, R.drawable.ic_treasure_hoarder, R.drawable.ic_treasure_hoarder_locked));
        milestones.add(new Milestone("Goal Getter", "Completed 5 goals", true, R.drawable.ic_goal_getter, R.drawable.ic_goal_getter_locked));
        milestones.add(new Milestone("Budget Hustler", "Completed 15 goals", false, R.drawable.ic_budget_hustler, R.drawable.ic_budget_hustler_locked));

        // Set up adapter
        milestoneAdapter = new MilestoneAdapter(this, milestones);
        milestonesRecyclerView.setAdapter(milestoneAdapter);

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
