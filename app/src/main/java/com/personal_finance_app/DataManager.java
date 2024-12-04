package com.personal_finance_app;

import java.util.ArrayList;

public class DataManager {
    private static DataManager instance;
    private ArrayList<Expense> expenseList;

    private DataManager() {
        expenseList = new ArrayList<>();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public ArrayList<Expense> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(ArrayList<Expense> list) {
        expenseList = list;
    }
}
