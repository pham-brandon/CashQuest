package personal_finance_app_backend.services;
import java.sql.ResultSet;
import personal_finance_app_backend.dao.TransactionDAO;
import personal_finance_app_backend.models.Transaction;

public class TransactionService {
    private TransactionDAO transactionDAO = new TransactionDAO();

    // Add a new transaction for a user (Need to look into OCR stuff)
    public boolean addTransaction(int userId, int categoryId, double amount, String transactionDate, String description) {
        Transaction newTransaction = new Transaction(0, userId, categoryId, amount, transactionDate, description, java.time.LocalDateTime.now().toString());
        return transactionDAO.addTransaction(newTransaction);
    }

    // Retrieve transactions for a user (Basic constructor for now)
    public ResultSet getTransactionsByUser(int userId) {
        return transactionDAO.getTransactionsByUser(userId);
    }

    // Update a transaction
    public boolean updateTransaction(Transaction transaction) {
        return transactionDAO.updateTransaction(transaction);
    }

    // Delete a transaction
    public boolean deleteTransaction(int transactionId) {
        return transactionDAO.deleteTransaction(transactionId);
    }
}
