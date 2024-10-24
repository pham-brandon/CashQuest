package personal_finance_app_backend.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import personal_finance_app_backend.models.Goal;
import personal_finance_app_backend.services.DatabaseConnections;

public class GoalDAO {

    // Add a new goal
    public boolean addGoal(Goal goal) {
        String query = "INSERT INTO goals (user_id, name, target_amount, current_amount, deadline) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, goal.getUserId());
            stmt.setString(2, goal.getName());
            stmt.setDouble(3, goal.getTargetAmount());
            stmt.setDouble(4, goal.getCurrentAmount());
            stmt.setString(5, goal.getDeadline());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Retrieve goals for a user
    public ResultSet getGoalsByUser(int userId) {
        String query = "SELECT * FROM goals WHERE user_id = ?";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update an existing goal
    public boolean updateGoal(Goal goal) {
        String query = "UPDATE goals SET name = ?, target_amount = ?, current_amount = ?, deadline = ? WHERE goal_id = ?";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, goal.getName());
            stmt.setDouble(2, goal.getTargetAmount());
            stmt.setDouble(3, goal.getCurrentAmount());
            stmt.setString(4, goal.getDeadline());
            stmt.setInt(5, goal.getGoalId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a goal
    public boolean deleteGoal(int goalId) {
        String query = "DELETE FROM goals WHERE goal_id = ?";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, goalId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get a goal by ID
    public Goal getGoalById(int goalId) {
        String query = "SELECT * FROM goals WHERE goal_id = ?";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, goalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Goal(
                        rs.getInt("goal_id"),
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getDouble("target_amount"),
                        rs.getDouble("current_amount"),
                        rs.getString("deadline")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
