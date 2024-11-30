package com.personal_finance_app;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText expenseTitle, amount;
    private Spinner expenseType, billFrequency;
    private Button createExpenseButton;
    private ImageButton uploadReceiptButton, cameraReceiptButton;

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

        createExpenseButton.setOnClickListener(v -> {
            String title = expenseTitle.getText().toString();
            String amountStr = amount.getText().toString();

            if (title.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(AddExpenseActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddExpenseActivity.this, "Expense Added", Toast.LENGTH_SHORT).show();
            }
        });

        uploadReceiptButton.setOnClickListener(v -> {
        });

        cameraReceiptButton.setOnClickListener(v -> {
        });
    }
}

