package com.personal_finance_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExpensesActivity extends AppCompatActivity {
    private UserProfileFragment userProfileFragment;
    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;
    private ArrayList<Expense> expenseList = new ArrayList<>();
    private PreferenceHelper preferencesHelper;
    private static final int ADD_EXPENSE_REQUEST_CODE = 1;
    private static final String PREFS_NAME = "personal_finance_prefs";
    private static final String EXPENSES_KEY = "expenses";
    private Spinner frequencySpinner;

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


        // initialize spinner
        frequencySpinner = findViewById(R.id.expenseFilterSpinner);

        //load the expenses
        loadExpenses();

        //resetExpenses(); //future? maybe add a "clear all" button??

        // manually adding to test
        expenseList.add(new Expense("Eating Out", 120, "Food", "Weekly"));
        expenseList.add(new Expense("Transportation", 60, "Travel", "Weekly"));
        expenseList.add(new Expense("Grocery", 150, "Food", "Weekly"));
        expenseList.add(new Expense("Utilities", 180, "Housing", "Monthly"));
        expenseList.add(new Expense("Rent", 1000, "Housing", "Monthly"));
        expenseList.add(new Expense("Entertainment", 50, "Entertainment", "Monthly"));
        expenseList.add(new Expense("Healthcare", 200, "Healthcare", "Monthly"));
        expenseList.add(new Expense("Education", 300, "Education", "Monthly"));
        expenseList.add(new Expense("Shopping", 250, "Shopping", "Monthly"));
        expenseList.add(new Expense("Subscriptions", 30, "Other", "Monthly"));
        expenseList.add(new Expense("Insurance", 120, "Housing", "Monthly"));
        expenseList.add(new Expense("Mortgage", 1500, "Housing", "Monthly"));
        expenseList.add(new Expense("Debt Payments", 500, "Other", "Monthly"));
        expenseList.add(new Expense("Charity/Donations", 100, "Other", "Monthly"));
        expenseList.add(new Expense("Investment", 200, "Other", "Monthly"));
        expenseList.add(new Expense("Pet Care", 50, "Other", "Weekly"));
        expenseList.add(new Expense("Travel/Vacation", 300, "Travel", "Monthly"));
        expenseList.add(new Expense("Other", 75, "Other", "Weekly"));


        // Get the new Expense object passed from AddExpenseActivity
        Intent intent = getIntent();
        if (intent.hasExtra("newExpense")) {
            Expense newExpense = (Expense) intent.getSerializableExtra("newExpense");
            if (newExpense != null) {
                expenseList.add(newExpense);
                expenseAdapter.notifyDataSetChanged(); // Refresh the adapter to display new data
                Toast.makeText(this, "Expense added: " + newExpense.getTitle(), Toast.LENGTH_SHORT).show();
            } else {
                Log.e("ExpensesActivity", "New Expense is null");
            }
        }

        // Update the expense list in DataManager
        DataManager.getInstance().setExpenseList(expenseList);
        // Set default selection
        frequencySpinner.setSelection(0);

        // Listener for Spinner selection
        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFrequency = parent.getItemAtPosition(position).toString();
                filterExpensesByFrequency(selectedFrequency);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterExpensesByFrequency("All");
            }
        });

        // Initialize BottomNavigationView
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.menu_expenses);

        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_expenses) {
                return true;
            } else if (itemId == R.id.menu_insights) {
                startActivity(new Intent(this, InsightsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.menu_goals) {
                startActivity(new Intent(this, GoalsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.menu_milestones) {
                startActivity(new Intent(this, MilestonesActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> {
            overridePendingTransition(0, 0);
            Intent addExpenseIntent = new Intent(this, AddExpenseActivity.class);
            startActivityForResult(addExpenseIntent, ADD_EXPENSE_REQUEST_CODE); // Add request code
        });
    }
    private void resetExpenses() {
        // Clear the list
        expenseList.clear();

        // Update the adapter
        expenseAdapter.updateExpenses(expenseList);
        expenseAdapter.notifyDataSetChanged();

        // Save the empty list to SharedPreferences
        saveExpenses();

        Toast.makeText(this, "Expense list has been cleared", Toast.LENGTH_SHORT).show();
    }

    private void filterExpensesByFrequency(String frequency) {
        if (frequency.equals("All")) {
            // Show all expenses
            expenseAdapter.updateExpenses(expenseList);
        } else {
            // Filter based on the frequency
            List<Expense> filteredList = new ArrayList<>();
            for (Expense expense : expenseList) {
                if (expense.getFrequency().equalsIgnoreCase(frequency)) {
                    filteredList.add(expense);
                }
            }
            expenseAdapter.updateExpenses(filteredList);
        }
        expenseAdapter.notifyDataSetChanged(); // Refresh the RecyclerView
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EXPENSE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Expense newExpense = (Expense) data.getSerializableExtra("newExpense");
            if (newExpense != null) {
                expenseList.add(newExpense);
                expenseAdapter.notifyDataSetChanged(); // Refresh the adapter
                saveExpenses();
                Toast.makeText(this, "Expense added: " + newExpense.getTitle(), Toast.LENGTH_SHORT).show();
            } else {
                Log.e("ExpensesActivity", "New Expense is null");
            }
        }
    }

    // Load expenses from SharedPreferences
    private void loadExpenses() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String expensesJson = prefs.getString(EXPENSES_KEY, null);

        if (expensesJson != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Expense>>(){}.getType();
            expenseList = gson.fromJson(expensesJson, listType);
            expenseAdapter.updateExpenses(expenseList);
        }
    }

    // Save expenses to SharedPreferences
    public void saveExpenses() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String expensesJson = gson.toJson(expenseList);

        editor.putString(EXPENSES_KEY, expensesJson);
        editor.apply();
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
