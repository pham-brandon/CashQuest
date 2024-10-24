package personal_finance_app_backend.services;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnections {
    private static final String URL = "jdbc:mysql://localhost:3306/personal_finance_app";
    private static final String USER = "root";
    private static final String PASSWORD = "password";
    // ^Talk to me about it I will help you set it up

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}