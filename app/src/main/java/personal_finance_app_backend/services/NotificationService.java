package personal_finance_app_backend.services;

import personal_finance_app_backend.dao.NotificationDAO;
import personal_finance_app_backend.models.Notification;
import java.sql.ResultSet;

public class NotificationService {

    private NotificationDAO notificationDAO = new NotificationDAO();

    // Send a new notification to the user
    public boolean sendNotification(int userId, String message, String type) {
        Notification newNotification = new Notification(0, userId, message, false, java.time.LocalDateTime.now().toString(), type);
        return notificationDAO.addNotification(newNotification);
    }

    // Retrieve notifications for a user
    public ResultSet getNotificationsByUser(int userId) {
        return notificationDAO.getNotificationsByUser(userId);
    }

    // Mark a notification as read
    public boolean markNotificationAsRead(int notificationId) {
        return notificationDAO.markAsRead(notificationId);
    }
}
