package personal_finance_app_backend.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import personal_finance_app_backend.models.Users;
import personal_finance_app_backend.services.DatabaseConnections;

public class UserDAO {

    // Add a new user to the database
    public boolean addUser(Users user) {
        if (getUserByEmail(user.getEmail()) != null) {
            System.out.println("User with this email already exists.");
            return false; // User with the email already exists
        }

        String query = "INSERT INTO users (name, email, password, created_at) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getCreatedAt());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Retrieve a user by ID
    public Users getUserById(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Users(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("created_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieve a user by Email (useful for login/registration)
    public Users getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Users(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("created_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update user information
    public boolean updateUser(Users user) {
        String query = "UPDATE users SET name = ?, email = ?, password = ? WHERE user_id = ?";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setInt(4, user.getUserId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a user by ID
    public boolean deleteUser(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Login function to verify user credentials
    public Users login(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Users(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("created_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if login fails (Or maybe we should return some kind of message leave it as NULL for now
    }
}
