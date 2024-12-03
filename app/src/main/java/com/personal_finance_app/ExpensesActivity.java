package com.personal_finance_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ExpensesActivity extends AppCompatActivity {
    private UserProfileFragment userProfileFragment;
    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;
    private ArrayList<Expense> expenseList = new ArrayList<>();
    private PreferenceHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

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

        // Update the EXP bar in the fragment
        if (userProfileFragment != null) {
            int progress = exp % 15;
            int progressPercentage = (progress * 100) / 15;
            userProfileFragment.updateUserProfile(level + 1, progressPercentage, username);
        }


        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.expenses_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        expenseAdapter = new ExpenseAdapter(this, expenseList);
        recyclerView.setAdapter(expenseAdapter);
        expenseAdapter.notifyDataSetChanged(); // Refresh the adapter to display data

        // Add some default expenses
        expenseList.add(new Expense("Rent", 1200.00, "Rent", "Monthly"));
        expenseList.add(new Expense("Groceries", 250.50, "Grocery", "Weekly"));

        // Get the new Expense object passed from AddExpenseActivity
        Intent intent = getIntent();
        if (intent.hasExtra("newExpense")) {
            Expense newExpense = (Expense) intent.getSerializableExtra("newExpense");
            expenseList.add(newExpense);
            expenseAdapter.notifyDataSetChanged(); // Refresh the adapter to display new data

            Toast.makeText(this, "Expense added: " + newExpense.getTitle(), Toast.LENGTH_SHORT).show();
        }

        // Initialize BottomNavigationView
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.menu_expenses);

        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_expenses) {
                return true;
            } else if (itemId == R.id.menu_insights) {
                startActivity(new Intent(this, InsightsActivity.class));
                return true;
            } else if (itemId == R.id.menu_goals) {
                startActivity(new Intent(this, GoalsActivity.class));
                return true;
            } else if (itemId == R.id.menu_milestones) {
                startActivity(new Intent(this, MilestonesActivity.class));
                return true;
            }
            return false;
        });

        // Add Floating Action Button listener
        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> {
            Intent addExpenseIntent = new Intent(this, AddExpenseActivity.class);
            startActivity(addExpenseIntent);
        });
    }

    @Override
    public void onBackPressed(){
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
