package com.personal_finance_app.ui.Onboarding;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.personal_finance_app.R;
import com.personal_finance_app.RegisterActivity;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 onboardingViewPager;
    private TabLayout dotsIndicator;
    private Button nextButton;
    private Button skipButton;
    private OnboardingAdapter onboardingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // Initialize views
        onboardingViewPager = findViewById(R.id.onboardingViewPager);
        dotsIndicator = findViewById(R.id.dotsIndicator);
        nextButton = findViewById(R.id.nextButton);
        skipButton = findViewById(R.id.skipButton);

        // Set up the ViewPager with the adapter
        onboardingAdapter = new OnboardingAdapter(this);
        onboardingViewPager.setAdapter(onboardingAdapter);

        // Attach TabLayoutMediator
        new TabLayoutMediator(dotsIndicator, onboardingViewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(TabLayout.Tab tab, int position) {
                    }
                }).attach();

        // Set click listeners
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSlide();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRegistrationActivity();
            }
        });
    }

    private void nextSlide() {
        if (onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()) {
            onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);
        } else {
            launchRegistrationActivity();
        }
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to the welcome screen
        if (onboardingViewPager.getCurrentItem() == 0) {
            // If it's the first onboarding screen, exit the app or do nothing
            super.onBackPressed();
        } else {
            // Otherwise, go to the previous slide
            onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() - 1);
        }
    }

    private void launchRegistrationActivity() {
        // Update SharedPreferences to indicate onboarding is completed
        SharedPreferences prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("onboarding_complete", true).apply();

        // Start the RegistrationActivity
        Intent intent = new Intent(OnboardingActivity.this, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
