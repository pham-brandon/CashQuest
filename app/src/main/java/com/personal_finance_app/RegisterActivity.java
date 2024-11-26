package com.personal_finance_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;
import android.util.Patterns;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.CheckBox;

import com.personal_finance_app.ui.Onboarding.SuccessfulRegistration;


public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String EMAIL_KEY = "DefaultEmail";
    private EditText emailField, passwordEditText, confirmPasswordEditText, usernameEditText;
    private CheckBox termsCheckBox;
    private ImageButton registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameEditText = findViewById(R.id.full_name);
        emailField = findViewById(R.id.email);
        ImageButton registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginLinkButton);
        termsCheckBox = findViewById(R.id.termsCheckbox);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String storedEmail = sharedPreferences.getString(EMAIL_KEY, "email@domain.com");
        emailField.setText(storedEmail);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateRegistration()) {
                    // Prevent registration if validation fails
                    return;
                }

                // Proceed with registration (save data to server)
                registerUser();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    // Validate registration fields
    private boolean validateRegistration() {
        String username = usernameEditText.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // Check if username is valid
        if (!isValidUsername(username)) {
            return false;
        }

        // Check if email is valid
        if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            return false;
        }

        // Check if password is empty or too short
        if (password.isEmpty() || password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if terms & conditions are accepted
        if (!termsCheckBox.isChecked()) {
            Toast.makeText(this, "You must agree to the terms and conditions", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // All validations passed
    }

    // Check if username meets the requirements
    private boolean isValidUsername(String username) {
        if (username.length() < 3 || username.length() > 15) {
            Toast.makeText(this, "Username must be between 3 and 15 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Only allow letters, numbers, underscores, hyphens, or periods
        String regex = "^[a-zA-Z0-9._-]+$";
        if (!username.matches(regex)) {
            Toast.makeText(this, "Username can only contain letters, numbers, underscores, hyphens, or periods", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Proceed with registration logic
    private void registerUser() {
        String username = usernameEditText.getText().toString();

        // After successful registration show welcome screen
        Intent intent = new Intent(RegisterActivity.this, SuccessfulRegistration.class);
        intent.putExtra("USER_NAME", username);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume called in LoginActivity");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart called in LoginActivity");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause called in LoginActivity");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop called in LoginActivity");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy called in LoginActivity");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState called in LoginActivity");
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState called in LoginActivity");
    }
}