package com.personal_finance_app;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;


public class AddExpenseActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int OCR_REQUEST_CODE = 2;

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
            openGallery();
        });

        cameraReceiptButton.setOnClickListener(v -> {
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            Intent ocrIntent = new Intent(AddExpenseActivity.this, OCRTestActivity.class);
            ocrIntent.putExtra("imageUri", selectedImageUri);
            startActivityForResult(ocrIntent, OCR_REQUEST_CODE);
        }

        if (requestCode == OCR_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String extractedAmount = data.getStringExtra("extractedAmount");
            if (extractedAmount != null) {
                amount.setText(extractedAmount);
            }
        }
    }
}


