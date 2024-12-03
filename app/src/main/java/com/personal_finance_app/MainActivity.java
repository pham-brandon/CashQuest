package com.personal_finance_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.personal_finance_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (checkUserAuthentication()) {
            startActivity(new Intent(MainActivity.this, ExpensesActivity.class));
            //go directly to ExpenseActivity after login
            //initializeUI();
            //setupBottomNavigation();
        }

    }

    private boolean checkUserAuthentication() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        boolean onboardingComplete = prefs.getBoolean("onboarding_complete", false);

        if (onboardingComplete) {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            finish();
            return false;
        }

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return false;
        }

        return true;
    }

    private void initializeUI() {
        FloatingActionButton centerFab = findViewById(R.id.fab_add);
        centerFab.setVisibility(View.VISIBLE);
        centerFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.menu_expenses, R.id.menu_insights, R.id.menu_milestones, R.id.menu_goals)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navView.setOnNavigationItemSelectedListener(this::handleNavigation);
    }

    private boolean handleNavigation(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_expenses) {
            startActivity(new Intent(MainActivity.this, ExpensesActivity.class));
        } else if (id == R.id.menu_insights) {
            startActivity(new Intent(MainActivity.this, InsightsActivity.class));
        } else if (id == R.id.menu_milestones) {
            startActivity(new Intent(MainActivity.this, MilestonesActivity.class));
        } else if (id == R.id.menu_goals) {
            startActivity(new Intent(MainActivity.this, GoalsActivity.class));
        } else {
            return false;
        }
        return true;
    }
}