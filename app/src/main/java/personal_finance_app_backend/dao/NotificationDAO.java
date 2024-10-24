package personal_finance_app_backend.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import personal_finance_app_backend.models.Notification;
import personal_finance_app_backend.services.DatabaseConnections;

public class NotificationDAO {

    // Add a new notification
    public boolean addNotification(Notification notification) {
        String query = "INSERT INTO notifications (user_id, message, is_read, created_at, type) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, notification.getUserId());
            stmt.setString(2, notification.getMessage());
            stmt.setBoolean(3, notification.getIsRead());
            stmt.setString(4, notification.getCreatedAt());
            stmt.setString(5, notification.getType());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Retrieve notifications for a user
    public ResultSet getNotificationsByUser(int userId) {
        String query = "SELECT * FROM notifications WHERE user_id = ?";
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

    // Mark a notification as read
    public boolean markAsRead(int notificationId) {
        String query = "UPDATE notifications SET is_read = TRUE WHERE notification_id = ?";
        try (Connection con = DatabaseConnections.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, notificationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
