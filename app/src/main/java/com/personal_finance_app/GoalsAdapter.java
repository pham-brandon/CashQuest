package com.personal_finance_app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder> {

    private List<Goal> goalsList;

    public GoalsAdapter(List<Goal> goalsList) {
        this.goalsList = goalsList;
    }

    @NonNull
    @Override
    public GoalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goal, parent, false);
        return new GoalsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalsViewHolder holder, int position) {
        Goal goal = goalsList.get(position);
        holder.descriptionTextView.setText(goal.getDescription());
        holder.dateTextView.setText(goal.getDate());

        // Set the EXP value with a "+" in front and "EXP" at the end
        String expText = holder.itemView.getContext().getString(R.string.goal_exp_format, goal.getExp());
        holder.expTextView.setText(expText);
    }

    @Override
    public int getItemCount() {
        return goalsList.size();
    }

    static class GoalsViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTextView;
        TextView dateTextView;
        TextView expTextView;

        public GoalsViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.goal_desc);
            dateTextView = itemView.findViewById(R.id.goal_date);
            expTextView = itemView.findViewById(R.id.goal_exp);
        }
    }

}
