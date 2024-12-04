package com.personal_finance_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editUsername, editEmail, editPassword;
    private Button saveButton;
    private TextView headerUsernameTextView;
    private UserProfileFragment userProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //retrieve the current username from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("personal_finance_prefs", MODE_PRIVATE);
        String username = prefs.getString("user_name", "User");

        // Initialize UI elements
        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        saveButton = findViewById(R.id.saveButton);
        headerUsernameTextView = findViewById(R.id.headerUsernameTextView);

        //Set to current username instead of the placeholder
        headerUsernameTextView.setText(username);

        // Set save button click listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileChanges();
            }
        });

        // Initialize BottomNavigationView
        BottomNavigationView navView = findViewById(R.id.nav_view);


        // Set selected item for the current activity
        navView.setSelectedItemId(R.id.menu_expenses);

        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_expenses) {
                startActivity(new Intent(this, ExpensesActivity.class));
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

    }

    private void saveProfileChanges() {
        // Fetch input values
        String username = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(username)) {
            showToast("Username cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Enter a valid email address");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            showToast("Password must be at least 6 characters long");
            return;
        }

        // Save changes (pseudo-code, replace with real implementation)
        // For example, save to backend or local storage
        boolean success = saveToDatabase(username, email, password);
        if (success) {
            // Save the new username to SharedPreferences
            SharedPreferences prefs = getSharedPreferences("personal_finance_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("user_name", username); // Save updated username
            editor.apply(); // Commit the changes
            UserProfileFragment userProfileFragment = (UserProfileFragment) getSupportFragmentManager().findFragmentById(R.id.user_profile_fragment);

            // Notify the UserProfileFragment to update the UI (reload data)
            if (userProfileFragment != null) {
                userProfileFragment.updateUsername(username);
            }
            showToast("Profile updated successfully");

            // Send the updated username back to ExpensesActivity
            Intent intent = new Intent(EditProfileActivity.this, ExpensesActivity.class);
            startActivity(intent); // Launch ExpensesActivity
            finish(); // Close activity after saving

        } else {
            showToast("Failed to update profile. Please try again.");
        }
    }

    private boolean saveToDatabase(String username, String email, String password) {
        // Implement actual save logic here (e.g., API call or database update)
        // Placeholder for demonstration purposes
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

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