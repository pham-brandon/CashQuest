package com.personal_finance_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserProfileFragment extends Fragment {

    // UI components
    private ImageView avatarImageView;
    private TextView usernameTextView;
    private TextView levelTextView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        // Initialize UI components
        avatarImageView = view.findViewById(R.id.avatarImageView);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        levelTextView = view.findViewById(R.id.levelTextView);
        progressBar = view.findViewById(R.id.progressBar);

        // Set up default values (replace with dynamic data)
        avatarImageView.setImageResource(R.drawable.default_avatar);
        usernameTextView.setText("JohnDoe_01");
        levelTextView.setText("lvl: 3");
        progressBar.setProgress(50); // Set progress dynamically based on user's level progress

        return view;
    }

    public void setUserProfile(String username, int level, int progress, int avatarResId) {
        // Update UI components
        usernameTextView.setText(username);
        levelTextView.setText("lvl: " + level);
        progressBar.setProgress(progress);
        avatarImageView.setImageResource(avatarResId);

        // Save data to SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("personal_finance_prefs", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("user_level", level);
        editor.putInt("user_exp", progress); // Assuming progress is equivalent to EXP
        editor.apply();  // Commit changes
    }

    public void updateUserProfile(int level, int progress) {
        levelTextView.setText("lvl: " + level);
        progressBar.setProgress(progress);
    }

}