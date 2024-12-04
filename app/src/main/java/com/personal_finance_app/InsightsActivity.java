package com.personal_finance_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InsightsActivity extends AppCompatActivity {

    private UserProfileFragment userProfileFragment;
    private BarChart barChart;
    private Spinner insightsFilterSpinner;
    private static final String PREFS_NAME = "personal_finance_prefs";
    private static final String EXPENSES_KEY = "expenses";
    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;
    private ArrayList<Expense> expenseList = new ArrayList<>();
    private PreferenceHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights);

        preferencesHelper = new PreferenceHelper(this);

        // Load user level or EXP if needed for milestones
        int userLevel = preferencesHelper.getUserLevel();
        int userExp = preferencesHelper.getUserExp();

        Log.d("ExpensesActivity", "User EXP: " + userExp);
        Log.d("ExpensesActivity", "User level: " + userLevel);
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

        // Initialize BottomNavigationView
        BottomNavigationView navView = findViewById(R.id.nav_view);


        // Set selected item for the current activity
        navView.setSelectedItemId(R.id.menu_insights);

        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_expenses) {
                startActivity(new Intent(this, ExpensesActivity.class));
                overridePendingTransition(0, 0); // No animation
                return true;
            } else if (itemId == R.id.menu_insights) {
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

        // Initialize BarChart and Spinner
        barChart = findViewById(R.id.insights_chart);
        insightsFilterSpinner = findViewById(R.id.insightsFilterSpinner);

        // Get the expenses list from DataManager
        expenseList = DataManager.getInstance().getExpenseList();

        // Set up the BarChart
        setupBarChart();
        loadChartData("Weekly"); // Default data load (Weekly)

        // Set up the Spinner for Weekly/Monthly filtering
        setupInsightFilterSpinner();
    }

    /**
     * Sets up the BarChart with initial configurations.
     */
    private void setupBarChart() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);

        // Customize X-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE);

        // Customize Y-axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextColor(Color.WHITE);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false); // Hide the right Y-axis

        barChart.setExtraOffsets(5f, 10f, 5f, 5f);
        barChart.animateY(1500);
    }

    /**
     * Loads the chart data dynamically based on the selected filter type.
     *
     * @param filterType The type of data to display ("Weekly" or "Monthly").
     */
    private void loadChartData(String filterType) {
        // Log the filter type for debugging
        Log.d("loadChartData", "Filter type: " + filterType);

        // Map to hold the total amounts by expense type
        Map<String, Float> expenseTotalsByType = new HashMap<>();

        // Calculate total amounts for each expense type, filtered by frequency (weekly/monthly)
        for (Expense expense : expenseList) {
            Log.d("loadChartData", "Expense: " + expense.getType() + ", Frequency: " + expense.getFrequency());
            if (expense.getFrequency().equalsIgnoreCase(filterType)) {
                // If this expense type is already in the map, add the amount to the total
                expenseTotalsByType.put(expense.getType(),
                        expenseTotalsByType.getOrDefault(expense.getType(), 0f) + (float) expense.getAmount());
            }
        }

        // Log the totals for debugging
        Log.d("loadChartData", "Expense totals by type: " + expenseTotalsByType);
        Log.d("loadChartData", "Expense list size: " + expenseList.size());

        // Create BarEntries (x-axis is the index, y-axis is the total amount for that type)
        ArrayList<BarEntry> entries = new ArrayList<>();
        int index = 0;

        // Adding BarEntries for each expense type
        for (Map.Entry<String, Float> entry : expenseTotalsByType.entrySet()) {
            // Each entry is a BarEntry with x (index) and y (total amount)
            entries.add(new BarEntry(index++, entry.getValue()));
        }

        // Create a BarDataSet for the BarChart
        BarDataSet dataSet = new BarDataSet(entries, filterType + " Expenses by Type");
        dataSet.setColor(Color.MAGENTA); // Set the color for the bars
        dataSet.setValueTextColor(Color.WHITE); // Set the text color for the values
        dataSet.setValueTextSize(12f); // Set the size of the value text

        // Create BarData and set the bar width
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // Set the bar width

        // Set the data to the chart
        barChart.setData(barData);

        // Set x-axis labels to be the expense types
        ArrayList<String> xLabels = new ArrayList<>(expenseTotalsByType.keySet());
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        xAxis.setGranularity(1f); // Set the granularity for the x-axis (one label per bar)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // Ensure labels are at the bottom
        xAxis.setLabelRotationAngle(45);  // Rotate labels if they overlap

        // Refresh the chart
        barChart.invalidate();
    }

    /**
     * Sets up the insight filter spinner with a listener to handle data filtering.
     */
    private void setupInsightFilterSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.weeklyMonthlyInsights,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        insightsFilterSpinner.setAdapter(adapter);

        insightsFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilter = parent.getItemAtPosition(position).toString();
                loadChartData(selectedFilter); // Update chart based on selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }
}
