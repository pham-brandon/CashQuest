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
}