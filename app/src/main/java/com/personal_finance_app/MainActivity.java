package com.personal_finance_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.personal_finance_app.databinding.ActivityMainBinding;
import com.personal_finance_app.ui.Onboarding.WelcomeActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Intent intent = new Intent(this, GoalsActivity.class);
        //startActivity(intent);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FloatingActionButton centerFab = findViewById(R.id.fab_add);
        centerFab.setVisibility(View.VISIBLE);
        centerFab.setOnClickListener(v -> {
            // Navigate to a new activity or perform an action
            Intent intent = new Intent(MainActivity.this, AddExpense.class); // Replace with the desired activity
            startActivity(intent);
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.menu_expenses, R.id.menu_insights, R.id.menu_milestones, R.id.menu_goals)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Set a listener for navigation item clicks
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_expenses) {
                    // Navigate to ExpensesActivity
                    startActivity(new Intent(MainActivity.this, ExpensesActivity.class));
                    return true;
                } else if (id == R.id.menu_insights) {
                    // Navigate to InsightsActivity
                    startActivity(new Intent(MainActivity.this, InsightsActivity.class));
                    return true;
                } else if (id == R.id.menu_milestones) {
                    // Navigate to MilestonesActivity
                    startActivity(new Intent(MainActivity.this, MilestonesActivity.class));
                    return true;
                } else if (id == R.id.menu_goals) {
                    // Navigate to GoalsActivity
                    startActivity(new Intent(MainActivity.this, GoalsActivity.class));
                    return true;
                }
                return false;
            }
        });

        SharedPreferences prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        boolean onboardingComplete = prefs.getBoolean("onboarding_complete", false);

        if (onboardingComplete) {
            // Onboarding is complete, proceed to RegistrationActivity
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Show WelcomeActivity
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
