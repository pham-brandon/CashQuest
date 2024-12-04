package com.personal_finance_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;

public class InsightsActivity extends AppCompatActivity {

    private UserProfileFragment userProfileFragment;
    private BarChart barChart;
    private Spinner insightsFilterSpinner;
    private static final String PREFS_NAME = "personal_finance_prefs";
    private static final String EXPENSES_KEY = "expenses";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights);

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
        ArrayList<BarEntry> entries = new ArrayList<>();
        List<Expense> expenseList = getExpensesFromPreferences();

        // Example data
        // if (filterType.equals("Weekly")) {
        //    entries.add(new BarEntry(1, 50));  // Week 1
        //   entries.add(new BarEntry(2, 75));  // Week 2
           // entries.add(new BarEntry(3, 100)); // Week 3
           // entries.add(new BarEntry(4, 150)); // Week 4
        // } else if (filterType.equals("Monthly")) {
            //entries.add(new BarEntry(1, 500)); // January
            //entries.add(new BarEntry(2, 700)); // February
            //entries.add(new BarEntry(3, 900)); // March
            //entries.add(new BarEntry(4, 1200));// April
        //}

        // Group expenses by frequency
        //HashMap<String, Float> groupedExpenses = groupExpensesByFrequency(expenseList);

        // Create chart data
        ArrayList<BarEntry> entries = new ArrayList<>();
        if (filterType.equals("Weekly")) {
            entries.add(new BarEntry(1, groupedExpenses.getOrDefault("Weekly", 0f)));
        } else if (filterType.equals("Monthly")) {
            entries.add(new BarEntry(1, groupedExpenses.getOrDefault("Monthly", 0f)));
        }

        // Create dataset
        BarDataSet dataSet = new BarDataSet(entries, filterType + " Expenses");
        dataSet.setColor(Color.MAGENTA);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);

        // Attach dataset to BarData and then to BarChart
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // Set bar width

        barChart.setData(barData);
        barChart.invalidate(); // Refresh the chart
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
                loadChartData(selectedFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }


}
