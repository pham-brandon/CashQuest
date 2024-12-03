package com.personal_finance_app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MilestoneAdapter extends RecyclerView.Adapter<MilestoneAdapter.MilestoneViewHolder> {

    private Context context;
    private List<Milestone> milestones;

    public MilestoneAdapter(Context context, List<Milestone> milestones) {
        this.context = context;
        this.milestones = milestones;
    }

    @NonNull
    @Override
    public MilestoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_milestone, parent, false);
        return new MilestoneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MilestoneViewHolder holder, int position) {
        Milestone milestone = milestones.get(position);

        holder.titleTextView.setText(milestone.getTitle());
        holder.descriptionTextView.setText(milestone.getDescription());

        // Display the appropriate icon based on unlock status
        if (milestone.isUnlocked()) {
            holder.iconImageView.setImageResource(milestone.getUnlockedIconResId());
            holder.container.setAlpha(1.0f); // Fully visible
        } else {
            holder.iconImageView.setImageResource(milestone.getLockedIconResId());
            holder.container.setAlpha(0.5f); // Keep fully visible, but use dimmed icon
        }
    }

    @Override
    public int getItemCount() {
        return milestones.size();
    }

    public static class MilestoneViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView;
        ImageView iconImageView;
        View container;

        public MilestoneViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.milestone_title);
            descriptionTextView = itemView.findViewById(R.id.milestone_description);
            iconImageView = itemView.findViewById(R.id.milestone_icon);
            container = itemView.findViewById(R.id.milestone_container);
        }
    }
}