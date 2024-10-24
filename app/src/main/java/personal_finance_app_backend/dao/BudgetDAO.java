package personal_finance_app_backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import personal_finance_app_backend.models.Budget;
import personal_finance_app_backend.services.DatabaseConnections;

public class BudgetDAO {

    // Add a new budget
    public boolean addBudget(Budget budget) {
        String query = "INSERT INTO budgets (user_id, category_id, amount, period, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, budget.getUserId());
            stmt.setInt(2, budget.getCategoryId());
            stmt.setDouble(3, budget.getAmount());
            stmt.setString(4, budget.getPeriod());
            stmt.setString(5, budget.getStartDate());
            stmt.setString(6, budget.getEndDate());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Retrieve budgets for a user
    public ResultSet getBudgetsByUser(int userId) {
        String query = "SELECT * FROM budgets WHERE user_id = ?";
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

    // Update an existing budget
    public boolean updateBudget(Budget budget) {
        String query = "UPDATE budgets SET category_id = ?, amount = ?, period = ?, start_date = ?, end_date = ? WHERE budget_id = ?";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, budget.getCategoryId());
            stmt.setDouble(2, budget.getAmount());
            stmt.setString(3, budget.getPeriod());
            stmt.setString(4, budget.getStartDate());
            stmt.setString(5, budget.getEndDate());
            stmt.setInt(6, budget.getBudgetId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a budget
    public boolean deleteBudget(int budgetId) {
        String query = "DELETE FROM budgets WHERE budget_id = ?";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, budgetId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
