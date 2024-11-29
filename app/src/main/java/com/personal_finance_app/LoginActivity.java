package com.personal_finance_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.activity.EdgeToEdge;
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
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String EMAIL_KEY = "DefaultEmail";
    private EditText emailField;
    private ImageButton loginButton;
    private Button registerButton, forgotPasswordButton;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailField = findViewById(R.id.emailLogin);
        loginButton = findViewById(R.id.loginButton);
        passwordField = findViewById(R.id.passwordLogin);
        registerButton = findViewById(R.id.registerButton);
        forgotPasswordButton = findViewById(R.id.forgotPassword);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String storedEmail = sharedPreferences.getString(EMAIL_KEY, "email@domain.com");
        emailField.setText(storedEmail);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                if (!isValidEmail(email)) {
                    Toast.makeText(LoginActivity.this, getString(R.string.invalidEmail), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.emptyPassword), Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(EMAIL_KEY, email);
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, ExpensesActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // forgot password on click listener
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailField.getText().toString().trim();
                // Check if the email field is empty
                if (TextUtils.isEmpty(email)) {
                    // Show a Snackbar asking for email input
                    Snackbar.make(v, "Please enter your email address", Snackbar.LENGTH_LONG).show();
                } else {
                    // Show a Snackbar confirming password reset instructions
                    Snackbar.make(v, "Check your email for password reset instructions", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
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