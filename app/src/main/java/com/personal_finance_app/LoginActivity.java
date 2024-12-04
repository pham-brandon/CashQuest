package com.personal_finance_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.personal_finance_app.ui.Onboarding.SuccessfulRegistration;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText emailField, passwordField;
    private ImageButton loginButton;
    private Button registerButton, forgotPasswordButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate: Initializing FirebaseAuth");
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Bind UI elements
        emailField = findViewById(R.id.emailLogin);
        passwordField = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        forgotPasswordButton = findViewById(R.id.forgotPassword);

        // Set up button listeners
        setUpListeners();
    }

    private void setUpListeners() {
        // Login button listener
        loginButton.setOnClickListener(v -> {
            Log.d(TAG, "Login button clicked");
            loginUser();
        });

        // Register button listener
        registerButton.setOnClickListener(v -> {
            Log.d(TAG, "Register button clicked");
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        // Forgot password button listener
        forgotPasswordButton.setOnClickListener(v -> {
            Log.d(TAG, "Forgot password button clicked");
            String email = emailField.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email address", Toast.LENGTH_LONG).show();
            } else {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Password reset email sent successfully");
                                Toast.makeText(this, "Check your email for password reset instructions", Toast.LENGTH_LONG).show();
                            } else {
                                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                                Log.e(TAG, "Failed to send reset email: " + errorMessage);
                                Toast.makeText(this, "Failed to send reset email. Try again.", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    private void loginUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        // Validate email and password input
        if (!validateInput(email, password)) return;

        Log.d(TAG, "Attempting login with email: " + email);
        // Attempt login
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Login successful!");
                        handleSuccessfulLogin();
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Log.e(TAG, "Login failed: " + errorMessage);
                        Toast.makeText(LoginActivity.this, "Login failed: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean validateInput(String email, String password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Invalid email format");
            Log.e(TAG, "Invalid email format");
            return false;
        }

        if (password.isEmpty()) {
            passwordField.setError("Password cannot be empty");
            Log.e(TAG, "Password field is empty");
            return false;
        }

        return true;
    }

    private void handleSuccessfulLogin() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG, "Fetching user data for UID: " + currentUser.getUid());
            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(currentUser.getUid());

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String username = snapshot.child("username").getValue(String.class);
                        if (username == null) {
                            username = "Unknown User"; // Fallback if username is null
                        }

                        Log.d(TAG, "User data fetched successfully. Username: " + username);

                        // Navigate to SuccessfulRegistration screen
                        Intent intent = new Intent(LoginActivity.this, SuccessfulRegistration.class);
                        intent.putExtra("USER_NAME", username);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e(TAG, "User data not found in the database");
                        Toast.makeText(LoginActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e(TAG, "Database error: " + error.getMessage());
                    Toast.makeText(LoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e(TAG, "Current user is null");
            Toast.makeText(LoginActivity.this, "Failed to fetch user data.", Toast.LENGTH_LONG).show();
        }
    }
}