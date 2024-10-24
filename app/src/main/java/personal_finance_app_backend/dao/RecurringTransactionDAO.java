package personal_finance_app_backend.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import personal_finance_app_backend.models.RecurringTransaction;
import personal_finance_app_backend.services.DatabaseConnections;

public class RecurringTransactionDAO {

    // Add a recurring transaction
    public boolean addRecurringTransaction(RecurringTransaction recurringTransaction) {
        String query = "INSERT INTO recurring_transactions (user_id, category_id, amount, description, start_date, frequency, next_due_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, recurringTransaction.getUserId());
            stmt.setInt(2, recurringTransaction.getCategoryId());
            stmt.setDouble(3, recurringTransaction.getAmount());
            stmt.setString(4, recurringTransaction.getDescription());
            stmt.setString(5, recurringTransaction.getStartDate());
            stmt.setString(6, recurringTransaction.getFrequency());
            stmt.setString(7, recurringTransaction.getNextDueDate());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Retrieve recurring transactions for a user
    public ResultSet getRecurringTransactionsByUser(int userId) {
        String query = "SELECT * FROM recurring_transactions WHERE user_id = ?";
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

    // Update a recurring transaction
    public boolean updateRecurringTransaction(RecurringTransaction recurringTransaction) {
        String query = "UPDATE recurring_transactions SET category_id = ?, amount = ?, description = ?, start_date = ?, frequency = ?, next_due_date = ? WHERE recurring_id = ?";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, recurringTransaction.getCategoryId());
            stmt.setDouble(2, recurringTransaction.getAmount());
            stmt.setString(3, recurringTransaction.getDescription());
            stmt.setString(4, recurringTransaction.getStartDate());
            stmt.setString(5, recurringTransaction.getFrequency());
            stmt.setString(6, recurringTransaction.getNextDueDate());
            stmt.setInt(7, recurringTransaction.getRecurringId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a recurring transaction
    public boolean deleteRecurringTransaction(int recurringTransactionId) {
        String query = "DELETE FROM recurring_transactions WHERE recurring_id = ?";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, recurringTransactionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
