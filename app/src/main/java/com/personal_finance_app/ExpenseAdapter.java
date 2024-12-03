package com.personal_finance_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        } else {
            // Handle null case or log for debugging
            Log.e("ExpenseAdapter", "Expense at position " + position + " is null.");
        }
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView amountTextView;
        TextView typeTextView;
        TextView frequencyTextView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views using the correct IDs
            titleTextView = itemView.findViewById(R.id.expense_title);
            amountTextView = itemView.findViewById(R.id.expense_amount);
            typeTextView = itemView.findViewById(R.id.expense_category);
            frequencyTextView = itemView.findViewById(R.id.billFrequency);
        }
    }
}
