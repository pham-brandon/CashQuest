package personal_finance_app_backend.services;

import java.sql.ResultSet;

import personal_finance_app_backend.dao.RecurringTransactionDAO;
import personal_finance_app_backend.models.RecurringTransaction;

public class RecurringTransactionService {

    private RecurringTransactionDAO recurringTransactionDAO = new RecurringTransactionDAO();

    // Add a new recurring transaction
    public boolean addRecurringTransaction(int userId, int categoryId, double amount, String description, String startDate, String frequency, String nextDueDate) {
        RecurringTransaction newRecurringTransaction = new RecurringTransaction(0, userId, categoryId, amount, description, startDate, frequency, nextDueDate);
        return recurringTransactionDAO.addRecurringTransaction(newRecurringTransaction);
    }

    // Retrieve recurring transactions for a user
    public ResultSet getUserRecurringTransactions(int userId) {
        return recurringTransactionDAO.getRecurringTransactionsByUser(userId);
    }

    // Update a recurring transaction
    public boolean updateRecurringTransaction(RecurringTransaction recurringTransaction) {
        return recurringTransactionDAO.updateRecurringTransaction(recurringTransaction);
    }

    // Delete a recurring transaction
    public boolean deleteRecurringTransaction(int recurringTransactionId) {
        return recurringTransactionDAO.deleteRecurringTransaction(recurringTransactionId);
    }
}
