package com.personal_finance_app;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OCRTestActivity extends AppCompatActivity {
    private static final String TAG = "OCR";
    //ML Kit(firebase maybe)? tesseract?
    //More than one language 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File testing = loadImageFromAssets("images/shop-receipt-template-vector-sticker-K7R18D.jpg"); // FOr testing

        if (testing != null && testing.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(testing.getAbsolutePath());
            recognizeTextFromImage(bitmap); // Process OCR
        } else {
            Log.e(TAG, "img file not found");
        }
    }

    private void recognizeTextFromImage(Bitmap bitmap) {
        try {
            InputImage inputImage = InputImage.fromBitmap(bitmap, 0);

            TextRecognizer textRecognizer = TextRecognition.getClient(new TextRecognizerOptions.Builder().build());

            // asynchronously
            textRecognizer.process(inputImage)
                    .addOnSuccessListener(this::processTextRecognitionResult)
                    .addOnFailureListener(e -> Log.e(TAG, "txt rec failed: " + e.getMessage()));
        } catch (Exception e) {
            Log.e(TAG, "error in recognizing the text: " + e.getMessage());
        }
    }

    private void processTextRecognitionResult(Text result) {
        List<Text.TextBlock> blocks = result.getTextBlocks();
        if (blocks.isEmpty()) {
            Log.d(TAG, "No text found.");
            return;
        }

        // Combining all the text blocks for preprocessing
        String preprocessedText = preprocessTextBlocks(blocks);
        Log.d(TAG, "Combined text blocks:\n" + preprocessedText);

        // Attempt
        String totalAmount = extractTotal(preprocessedText);

        // other
        if (totalAmount == null) {
            List<Text.Element> elements = getElementsFromBlocks(blocks);
            totalAmount = extractTotalFromElements(elements);
        }

        // log total amount
        if (totalAmount != null) {
            Log.d(TAG, "!!Extracted Total: " + totalAmount);
        } else {
            Log.d(TAG, "Total not found in text.");
        }
    }

    private String preprocessTextBlocks(List<Text.TextBlock> blocks) {
        StringBuilder combinedText = new StringBuilder();
        for (Text.TextBlock block : blocks) {
            combinedText.append(block.getText()).append(" ");
        }
        return combinedText.toString();
    }

    private List<Text.Element> getElementsFromBlocks(List<Text.TextBlock> blocks) {
        List<Text.Element> elements = new ArrayList<>();
        for (Text.TextBlock block : blocks) {
            for (Text.Line line : block.getLines()) {
                elements.addAll(line.getElements());
            }
        }
        return elements;
    }

    private String extractTotal(String text) {
        // ***ADD MORE FORMATS****
        String regex = "(?i)(\\b(total|grand total|balance|amount|cad|usd)\\b[:\\s]*)?(\\$?\\d{1,3}(,\\d{3})*\\.\\d{2})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String total = matcher.group(3);
            if (total != null) {
                return total.replaceAll("[^\\d.]", ""); // Strip unwanted characters
            }
        }
        return null;
    }

    private String extractTotalFromElements(List<Text.Element> elements) {
        for (int i = 0; i < elements.size(); i++) {
            Text.Element element = elements.get(i);
            String text = element.getText();

            // Look for keywords like TOTAL and the val
            if (text.matches("(?i)total|amount|usd|cad|subtotal")) {
                if (i + 1 < elements.size()) {
                    String possibleAmount = elements.get(i + 1).getText();
                    if (possibleAmount.matches("\\d{1,3}(,\\d{3})*\\.\\d{2}")) {
                        return possibleAmount;
                    }
                }
            }
        }
        return null;
    }

    private File loadImageFromAssets(String fileName) {
        File tempFile = new File(getCacheDir(), "temp_receipt.jpg");

        try (InputStream inputStream = getAssets().open(fileName);
             FileOutputStream outputStream = new FileOutputStream(tempFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            Log.d(TAG, "Image loaded from assets: " + tempFile.getAbsolutePath());
            return tempFile;

        } catch (IOException e) {
            Log.e(TAG, "Error loading image from assets: " + e.getMessage());
            return null;
        }
    }
}






