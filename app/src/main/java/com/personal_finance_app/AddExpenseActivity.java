package com.personal_finance_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String PREFS_NAME = "ExpensesPrefs";
    private static final String EXPENSES_KEY = "expensesList";

    private EditText expenseTitle, amount;
    private Spinner expenseType, billFrequency;
    private Button createExpenseButton;
    private ImageButton uploadReceiptButton, cameraReceiptButton;

    private List<Expense> expensesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        expenseTitle = findViewById(R.id.expenseTitle);
        amount = findViewById(R.id.amount);
        expenseType = findViewById(R.id.expenseType);
        billFrequency = findViewById(R.id.billFrequency);
        createExpenseButton = findViewById(R.id.createExpenseButton);
        uploadReceiptButton = findViewById(R.id.uploadReceiptButton);
        cameraReceiptButton = findViewById(R.id.cameraReceiptButton);

        loadExpensesFromPreferences();

        createExpenseButton.setOnClickListener(v -> {
            String title = expenseTitle.getText().toString().trim();
            String amountStr = amount.getText().toString().trim();

            if (title.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(AddExpenseActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                String selectedExpenseType = expenseType.getSelectedItem().toString();
                String selectedBillFrequency = billFrequency.getSelectedItem().toString();

                double expenseAmount;
                try {
                    expenseAmount = Double.parseDouble(amountStr);

                    // amount > 0
                    if (expenseAmount <= 0) {
                        Toast.makeText(AddExpenseActivity.this, "Amount must be greater than zero", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(AddExpenseActivity.this, "Please enter a valid numeric value for the amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                Expense newExpense = new Expense(title, Double.parseDouble(amountStr), selectedExpenseType, selectedBillFrequency);

                expensesList.add(newExpense);
                saveExpensesToPreferences();

                Toast.makeText(AddExpenseActivity.this, "Expense saved successfully", Toast.LENGTH_SHORT).show();
                finish();
                expenseTitle.setText("");
                amount.setText("");
                expenseType.setSelection(0);
                billFrequency.setSelection(0);
            }
        });

        uploadReceiptButton.setOnClickListener(v -> openGallery());
        cameraReceiptButton.setOnClickListener(v -> {
            // Add functionality for camera capture here
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void loadExpensesFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String expensesJson = sharedPreferences.getString(EXPENSES_KEY, null);

        if (expensesJson != null) {
            Type type = new TypeToken<List<Expense>>() {}.getType();
            expensesList = gson.fromJson(expensesJson, type);
        } else {
            expensesList = new ArrayList<>();
        }
    }

    private void saveExpensesToPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String expensesJson = gson.toJson(expensesList);
        editor.putString(EXPENSES_KEY, expensesJson);
        editor.apply();
    }
}


