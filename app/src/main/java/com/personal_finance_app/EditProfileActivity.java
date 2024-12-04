package com.personal_finance_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editUsername, editEmail, editPassword;
    private Button saveButton;
    private TextView headerUsernameTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Firebase Authentication and Database initialization
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userDatabaseRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Retrieve the current username from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("personal_finance_prefs", MODE_PRIVATE);
        String username = prefs.getString("user_name", "User");

        // Initialize UI elements
        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        saveButton = findViewById(R.id.saveButton);
        headerUsernameTextView = findViewById(R.id.headerUsernameTextView);

        // Set to current username and email
        headerUsernameTextView.setText(username);
        editEmail.setText(currentUser != null ? currentUser.getEmail() : "");

        // Set save button click listener
        saveButton.setOnClickListener(v -> saveProfileChanges());

        // BottomNavigationView setup
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.menu_expenses);
        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_expenses) {
                startActivity(new Intent(this, ExpensesActivity.class));
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
            Intent intent = new Intent(this, AddExpenseActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }

    private void saveProfileChanges() {
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

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            showToast("User not authenticated");
            return;
        }

        // Update email
        currentUser.updateEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Update password
                        currentUser.updatePassword(password)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        // Save username to the database
                                        userDatabaseRef.child("username").setValue(username)
                                                .addOnCompleteListener(task2 -> {
                                                    if (task2.isSuccessful()) {
                                                        // Update SharedPreferences
                                                        SharedPreferences prefs = getSharedPreferences("personal_finance_prefs", MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = prefs.edit();
                                                        editor.putString("user_name", username);
                                                        editor.apply();

                                                        showToast("Profile updated successfully");

                                                        // Redirect to ExpensesActivity
                                                        Intent intent = new Intent(EditProfileActivity.this, ExpensesActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        showToast("Failed to update username in database");
                                                    }
                                                });
                                    } else {
                                        showToast("Failed to update password: " + task1.getException().getMessage());
                                    }
                                });
                    } else {
                        showToast("Failed to update email: " + task.getException().getMessage());
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.menu_expenses);
        if (R.id.menu_expenses != navView.getSelectedItemId()) {
            navView.setSelectedItemId(R.id.menu_expenses);
        } else {
            super.onBackPressed();
        }
    }
}