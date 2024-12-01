package com.personal_finance_app;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editUsername, editEmail, editPassword;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize UI elements
        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        saveButton = findViewById(R.id.saveButton);

        // Set save button click listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileChanges();
            }
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
            showToast("Profile updated successfully");
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
}