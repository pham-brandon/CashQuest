package com.personal_finance_app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenses;
    private Context context;

    // Constructor
    public ExpenseAdapter(Context context, List<Expense> expenses) {
        this.context = context;
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenses.get(position);

        // Make sure the expense object is not null
        if (expense != null) {
            holder.titleTextView.setText(expense.getTitle());
            holder.amountTextView.setText(String.format("$%.2f", expense.getAmount()));
            holder.typeTextView.setText(expense.getType());
            holder.frequencyTextView.setText(expense.getFrequency());
            holder.iconImageView.setImageResource(getIconForExpenseType(expense.getType()));
        } else {
            // Handle null case or log for debugging
            Log.e("ExpenseAdapter", "Expense at position " + position + " is null.");
        }

        // Long click listener for options
        holder.itemView.setOnLongClickListener(v -> {
            showGoalOptionsDialog(expense, position);
            return true;
        });
    }


    // Method to determine which icon to use based on expense type
    private int getIconForExpenseType(String expenseType) {
        switch (expenseType) {
            case "Eating Out":
                return R.drawable.dinner_expense;
            case "Entertainment":
                return R.drawable.entertainment_expense;
            case "Grocery":
                return R.drawable.grocery_expense;
            case "Health":
                return R.drawable.health_expense;
            case "Rent":
                return R.drawable.rent_expense;
            case "Transportation":
                return R.drawable.transportation_expense;
            case "Utilities":
                return R.drawable.utilities_expense;
            default:
                return R.drawable.other_expenses;
        }
    }
    @Override
    public int getItemCount() {
        return expenses.size();
    }

    //update the list of expenses
    public void updateExpenses(List<Expense> newExpenses) {
        this.expenses = newExpenses;
        notifyDataSetChanged();
    }


    private void showGoalOptionsDialog(final Expense expense, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose an option");

        builder.setItems(new String[]{"Edit Expense title", "Edit expense amount", "Delete"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    editTitle(expense);
                    break;
                case 1:
                    editAmount(expense);
                    break;
                case 2:
                    deleteExpense(position);
                    break;
            }
        });

        builder.show();
    }
    private void editTitle(Expense expense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Expense Title");

        final EditText input = new EditText(context);
        input.setText(expense.getTitle());
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newTitle = input.getText().toString();
            if (!newTitle.isEmpty()) {
                expense.setTitle(newTitle);
                notifyDataSetChanged();
                Toast.makeText(context, "Expense title updated.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void editAmount(Expense expense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Expense Amount");

        // Create an EditText for numeric input
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setText(String.valueOf(expense.getAmount()));
        builder.setView(input);

        // Set up the "Save" button
        builder.setPositiveButton("Save", (dialog, which) -> {
            String newAmountString = input.getText().toString();
            if (!newAmountString.isEmpty()) {
                try {
                    double newAmount = Double.parseDouble(newAmountString); // Parse the input as a double
                    if (newAmount >= 0) { // Ensure the amount is not negative
                        expense.setAmount(newAmount);
                        notifyDataSetChanged(); // Refresh the UI
                        Toast.makeText(context, "Expense amount updated.", Toast.LENGTH_SHORT).show();
                        ExpensesActivity activity = (ExpensesActivity) context;
                        activity.saveExpenses(); // Save changes to SharedPreferences
                    } else {
                        Toast.makeText(context, "Amount must be non-negative.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Invalid amount. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Amount cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the "Cancel" button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    public void deleteExpense(int position) {
        if (position >= 0 && position < expenses.size()) {
            Log.d("DeleteExpense", "Before removal, Position: " + position + " List size: " + expenses.size());

            expenses.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Expense deleted.", Toast.LENGTH_SHORT).show();

            if (position < expenses.size()) {
                notifyItemRangeChanged(position, expenses.size() - position);
            }
            Log.d("DeleteExpense", "After removal, List size: " + expenses.size());
            Toast.makeText(context, "Expense deleted.", Toast.LENGTH_SHORT).show();
            ExpensesActivity activity = (ExpensesActivity) context;
            activity.saveExpenses();
        } else {
            Log.d("DeleteExpense", "Invalid position: " + position + " List size: " + expenses.size());
        }
    }


    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView amountTextView;
        TextView typeTextView;
        TextView frequencyTextView;
        ImageView iconImageView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views using the correct IDs
            titleTextView = itemView.findViewById(R.id.expense_title);
            amountTextView = itemView.findViewById(R.id.expense_amount);
            typeTextView = itemView.findViewById(R.id.expense_category);
            frequencyTextView = itemView.findViewById(R.id.billFrequency);
            iconImageView = itemView.findViewById(R.id.expense_icon);
        }
    }


}
