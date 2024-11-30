package com.personal_finance_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SideMenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_side_menu, container, false);

        // Find the menu button in the fragment layout
        ImageButton menuButton = view.findViewById(R.id.menu_button);

        // Set OnClickListener for the menu button
        menuButton.setOnClickListener(v -> {
            // Create and show the popup menu
            PopupMenu popupMenu = new PopupMenu(requireContext(), menuButton);
            popupMenu.getMenuInflater().inflate(R.menu.side_menu, popupMenu.getMenu());

            // Handle menu item clicks
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.edit_profile) {
                    // Navigate to Edit Profile
                    startActivity(new Intent(getActivity(), EditProfileActivity.class));
                    return true;
                } else if (itemId == R.id.stats) {
                    // Navigate to Stats
                    startActivity(new Intent(getActivity(), InsightsActivity.class));
                    return true;
                } else if (itemId == R.id.expenses) {
                    // Navigate to Expenses
                    startActivity(new Intent(getActivity(), ExpensesActivity.class));
                    return true;
                } else if (itemId == R.id.logout) {
                    // Log out
                    Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                    return true;
                } else {
                    return false;
                }
            });

            popupMenu.show();
        });

        return view;
    }
}