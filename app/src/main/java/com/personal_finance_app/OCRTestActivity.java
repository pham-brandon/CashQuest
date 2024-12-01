package com.personal_finance_app;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.net.Uri;

import android.content.Intent;

public class OCRTestActivity extends AppCompatActivity {
    private static final String TAG = "OCRTestActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri imageUri = getIntent().getParcelableExtra("imageUri");
        if (imageUri != null) {
            try {
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                if (bitmap != null) {
                    recognizeTextFromImage(bitmap);
                } else {
                    Log.e(TAG, "Bitmap is null");
                    setResult(RESULT_CANCELED);
                    finish();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error loading image: " + e.getMessage());
                setResult(RESULT_CANCELED);
                finish();
            }
        } else {
            Log.e(TAG, "Image URI is null");
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private void recognizeTextFromImage(Bitmap bitmap) {
        try {
            InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
            TextRecognizer textRecognizer = TextRecognition.getClient(new TextRecognizerOptions.Builder().build());

            textRecognizer.process(inputImage)
                    .addOnSuccessListener(this::processTextRecognitionResult)
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Text recognition failed: " + e.getMessage());
                        setResult(RESULT_CANCELED);
                        finish();
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error recognizing text: " + e.getMessage());
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private void processTextRecognitionResult(Text result) {
        List<Text.TextBlock> blocks = result.getTextBlocks();
        if (blocks.isEmpty()) {
            Log.d(TAG, "No text found.");
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        StringBuilder combinedText = new StringBuilder();
        for (Text.TextBlock block : blocks) {
            combinedText.append(block.getText()).append("\n");
        }

        String preprocessedText = combinedText.toString();
        Log.d(TAG, "Combined text blocks:\n" + preprocessedText);

        String totalAmount = extractTotal(preprocessedText);

        if (totalAmount != null) {
            Log.d(TAG, "Extracted Total: " + totalAmount);
            returnResultToAddExpenseActivity(totalAmount);
        } else {
            Log.d(TAG, "Total not found in text.");
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private void returnResultToAddExpenseActivity(String totalAmount) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("extractedAmount", totalAmount);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private String extractTotal(String text) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            if (line.matches("(?i).*\\b(total|amount|balance|gratuity|invoice|payment)\\b.*")) {
                String possibleAmount = extractNumberFromLine(line);
                if (possibleAmount != null) {
                    return possibleAmount;
                }
            }
        }
        return null;
    }

    private String extractNumberFromLine(String line) {
        String[] tokens = line.split("\\s+");
        for (String token : tokens) {
            if (token.matches("[\\$€¥£]?\\d{1,3}(,\\d{3})*(\\.\\d{2})?")) {
                return token.replaceAll("[^\\d.]", "");
            }
        }
        return null;
    }
}



