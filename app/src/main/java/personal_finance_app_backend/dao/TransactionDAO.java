package personal_finance_app_backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import personal_finance_app_backend.models.Transaction;
import personal_finance_app_backend.services.DatabaseConnections;

public class TransactionDAO {

    // Add a new transaction
    public boolean addTransaction(Transaction transaction) {
        String query = "INSERT INTO transactions (user_id, category_id, amount, transaction_date, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, transaction.getUserId());
            stmt.setInt(2, transaction.getCategoryId());
            stmt.setDouble(3, transaction.getAmount());
            stmt.setString(4, transaction.getTransactionDate());
            stmt.setString(5, transaction.getDescription());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Retrieve transactions for a user
    public ResultSet getTransactionsByUser(int userId) {
        String query = "SELECT * FROM transactions WHERE user_id = ?";
        try {
            Connection con = DatabaseConnections.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update an existing transaction
    public boolean updateTransaction(Transaction transaction) {
        String query = "UPDATE transactions SET category_id = ?, amount = ?, transaction_date = ?, description = ? WHERE transaction_id = ?";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, transaction.getCategoryId());
            stmt.setDouble(2, transaction.getAmount());
            stmt.setString(3, transaction.getTransactionDate());
            stmt.setString(4, transaction.getDescription());
            stmt.setInt(5, transaction.getTransactionId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a transaction
    public boolean deleteTransaction(int transactionId) {
        String query = "DELETE FROM transactions WHERE transaction_id = ?";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, transactionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
