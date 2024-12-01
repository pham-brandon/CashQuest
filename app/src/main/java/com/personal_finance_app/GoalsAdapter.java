package com.personal_finance_app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder> {

    private List<Goal> goalsList;
    private Context context;

    public GoalsAdapter(List<Goal> goalsList, Context context) {
        this.goalsList = goalsList;
        this.context = context;
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
        String expText = holder.itemView.getContext().getString(R.string.goal_exp_format, goal.getExp());
        holder.expTextView.setText(expText);

        //check if goal is completed and update the UI
        int textColor = goal.isCompleted()
                ? context.getResources().getColor(R.color.grey)
                : context.getResources().getColor(R.color.white);

        holder.descriptionTextView.setTextColor(textColor);
        holder.expTextView.setTextColor(textColor);
        holder.dateTextView.setTextColor(textColor);

        // Long click listener for options
        holder.itemView.setOnLongClickListener(v -> {
            showGoalOptionsDialog(goal, position);
            return true;
        });
    }

    private void showGoalOptionsDialog(final Goal goal, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose an option");

        builder.setItems(new String[]{"Edit Goal Description", "Edit Goal Date", "Delete", "Mark as Complete"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    editGoalDescription(goal);
                    break;
                case 1:
                    editGoalDate(goal);
                    break;
                case 2:
                    deleteGoal(position);
                    break;
                case 3:
                    markGoalAsComplete(goal, position);
                    break;
            }
        });

        builder.show();
    }
    private void editGoalDescription(Goal goal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Goal");

        final EditText input = new EditText(context);
        input.setText(goal.getDescription());
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newDescription = input.getText().toString();
            if (!newDescription.isEmpty()) {
                goal.setDescription(newDescription);
                notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void editGoalDate(Goal goal) {
        Calendar calendar = Calendar.getInstance();

        String goalDate = goal.getDate().replace("Due: ", "").trim();
        String[] dateParts = goalDate.split("-");
        if (dateParts.length == 3) {
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1;
            int day = Integer.parseInt(dateParts[2]);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String newDate = "Due: " + selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        goal.setDate(newDate);
                        notifyDataSetChanged();
                    }, year, month, day);

            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        }
    }

    private void markGoalAsComplete(Goal goal, int position) {
        goal.setCompleted(true);
        notifyItemChanged(position);

        // Update EXP in GoalsActivity
        if (context instanceof GoalsActivity) {
            GoalsActivity activity = (GoalsActivity) context;
            activity.increaseEXP(5);  // Increment EXP by 5 for completed goal
        }
    }
    public void deleteGoal(int position) {
        if (position >= 0 && position < goalsList.size()) {
            Log.d("DeleteGoal", "Before removal, Position: " + position + " List size: " + goalsList.size());

            goalsList.remove(position);
            notifyItemRemoved(position);

            if (position < goalsList.size()) {
                notifyItemRangeChanged(position, goalsList.size() - position);
            }
            Log.d("DeleteGoal", "After removal, List size: " + goalsList.size());
            GoalsActivity activity = (GoalsActivity) context;
            activity.saveGoalsToPreferences();
        } else {
            Log.d("DeleteGoal", "Invalid position: " + position + " List size: " + goalsList.size());
        }
    }

    @Override
    public int getItemCount() {
        return goalsList.size();
    }

    static class GoalsViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTextView;
        TextView dateTextView;
        TextView expTextView;

        public GoalsViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.goal_desc);
            dateTextView = itemView.findViewById(R.id.goal_date);
            expTextView = itemView.findViewById(R.id.goal_exp);
        }
    }
}
