package com.personal_finance_app.ui.Onboarding;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.personal_finance_app.R;

public class WelcomeActivity extends AppCompatActivity {

    private ImageButton startButton;
    private ViewPager2 onboardingViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchOnboardingActivity();
            }
        });
    }

    private void launchOnboardingActivity() {
        Intent intent = new Intent(WelcomeActivity.this, OnboardingActivity.class);
        startActivity(intent);
        finish();
    }
}