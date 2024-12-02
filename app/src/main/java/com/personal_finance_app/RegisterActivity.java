package com.personal_finance_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
    private Button loginLinkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        usernameEditText = findViewById(R.id.full_name);
        emailField = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.registerButton);
        loginLinkButton = findViewById(R.id.loginLinkButton);

        registerButton.setOnClickListener(v -> {
            if (!validateRegistration()) return;
            registerUserWithFirebase();
        });

        loginLinkButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean validateRegistration() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (username.isEmpty() || username.length() < 3 || username.length() > 15) {
            Toast.makeText(this, "Username must be between 3 and 15 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty() || password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void registerUserWithFirebase() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            saveUserToDatabase(firebaseUser, username);
                        }
                        Intent intent = new Intent(RegisterActivity.this, SuccessfulRegistration.class);
                        intent.putExtra("USER_NAME", username);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserToDatabase(FirebaseUser firebaseUser, String username) {
        String email = firebaseUser.getEmail();
        String userId = firebaseUser.getUid();

        User user = new User(username, email);

        databaseReference.child(userId).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "User data saved successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}