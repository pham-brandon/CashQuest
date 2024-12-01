package com.personal_finance_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.personal_finance_app.models.User;
import com.personal_finance_app.ui.Onboarding.SuccessfulRegistration;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private EditText usernameEditText, emailField, passwordEditText, confirmPasswordEditText;
    private ImageButton registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize UI elements
        usernameEditText = findViewById(R.id.full_name);
        emailField = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.registerButton);

        // Set register button click listener
        registerButton.setOnClickListener(v -> {
            if (!validateRegistration()) {
                return;
            }
            registerUserWithFirebase(); // New method for registration
        });
    }

    private boolean validateRegistration() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validate username
        if (username.isEmpty() || username.length() < 3 || username.length() > 15) {
            Toast.makeText(this, "Username must be between 3 and 15 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate email
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate password
        if (password.isEmpty() || password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate confirm password
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // All validations passed
    }

    private void registerUserWithFirebase() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Use Firebase Authentication to create a user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Registration successful
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            saveUserToDatabase(firebaseUser, username);
                        }

                        // Navigate to SuccessfulRegistration activity
                        Intent intent = new Intent(RegisterActivity.this, SuccessfulRegistration.class);
                        intent.putExtra("USER_NAME", username);
                        startActivity(intent);
                        finish();
                    } else {
                        // Registration failed
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserToDatabase(FirebaseUser firebaseUser, String username) {
        String email = firebaseUser.getEmail();
        String userId = firebaseUser.getUid();

        // Create a User object
        User user = new User(username, email);

        // Save user data in Realtime Database
        databaseReference.child(userId).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RegisterActivity.this, "User data saved successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}