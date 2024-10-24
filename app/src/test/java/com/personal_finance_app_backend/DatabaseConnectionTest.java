package com.personal_finance_app_backend;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    private static final String URL = "jdbc:mysql://localhost:3306/personal_finance_app";
    private static final String USER = "root";
    private static final String PASSWORD = "password"; // Update with your actual password

    @Test
    public void testDatabaseConnection() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            assertNotNull("Connection should be established", conn);
            System.out.println("Connected to the database successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to connect to the database: " + e.getMessage());
        }
    }
}
