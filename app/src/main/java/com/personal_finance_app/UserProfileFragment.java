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
        avatarImageView.setImageResource(R.drawable.default_avatar); // Default avatar
        levelTextView.setText("lvl: 1"); // Default level
        progressBar.setProgress(0); // Default progress
        usernameTextView.setText("User");

        // Retrieve the username from SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("personal_finance_prefs", getActivity().MODE_PRIVATE);
        String username = preferences.getString("user_name", "User"); // Default value: "User"


        return view;
    }


    public void setUserProfile(String username, int level, int progress, int avatarResId) {
        if (usernameTextView != null) {
            usernameTextView.setText(username);
        }
        if (levelTextView != null) {
            levelTextView.setText("lvl: " + level);
        }
        if (progressBar != null) {
            progressBar.setProgress(progress);
        }
        if (avatarImageView != null) {
            avatarImageView.setImageResource(avatarResId);
        }

        // Save data to SharedPreferences for persistence
        if (getActivity() != null) {
            SharedPreferences preferences = getActivity().getSharedPreferences("personal_finance_prefs", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("user_name", username);
            editor.putInt("user_level", level);
            editor.putInt("user_exp", progress);
            editor.apply();
        }
    }

    public void updateUserProfile(int level, int progress, String username) {
        usernameTextView.setText(username);
        levelTextView.setText("lvl: " + level);
        progressBar.setProgress(progress);

    }

    public void updateUsername(String username){
        usernameTextView.setText(username);
    }


}