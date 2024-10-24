package com.personal_finance_app_backend;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import personal_finance_app_backend.dao.UserDAO;
import personal_finance_app_backend.models.Users;
import personal_finance_app_backend.services.UserService;

public class UserServiceTest {

    private UserService userService;
    private UserDAO mockUserDAO;

    @Before
    public void setUp() {
        // Mock the UserDAO using Mockito
        mockUserDAO = mock(UserDAO.class);
        userService = new UserService(mockUserDAO);
    }

    // Test successful user registration
    @Test
    public void testRegisterUser_Success() {
        when(mockUserDAO.getUserByEmail("john@example.com")).thenReturn(null);
        when(mockUserDAO.addUser(any(Users.class))).thenReturn(true);
        boolean result = userService.registerUser("John Doe", "john@example.com", "password123");
        assertTrue(result);
        verify(mockUserDAO).addUser(any(Users.class));
    }

    // Test registration failure when email already exists
    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        when(mockUserDAO.getUserByEmail("john@example.com")).thenReturn(new Users(1, "John Doe", "john@example.com", "password123", null));
        boolean result = userService.registerUser("John Doe", "john@example.com", "password123");
        assertFalse(result);
        verify(mockUserDAO, never()).addUser(any(Users.class)); // Ensure addUser is never called
    }

    // Test successful login
    @Test
    public void testLoginUser_Success() {
        Users mockUser = new Users(1, "John Doe", "john@example.com", "password123", null);
        when(mockUserDAO.login("john@example.com", "password123")).thenReturn(mockUser);
        Users user = userService.loginUser("john@example.com", "password123");
        assertNotNull(user);
        assertEquals("john@example.com", user.getEmail());
        verify(mockUserDAO).login("john@example.com", "password123");
    }

    // Test login failure with incorrect credentials
    @Test
    public void testLoginUser_Fail() {
        when(mockUserDAO.login("john@example.com", "wrongpassword")).thenReturn(null);
        Users user = userService.loginUser("john@example.com", "wrongpassword");
        assertNull(user);
        verify(mockUserDAO).login("john@example.com", "wrongpassword");
    }

    // Test getUserById returns correct user
    @Test
    public void testGetUserById_Success() {
        Users mockUser = new Users(1, "John Doe", "john@example.com", "password123", null);
        when(mockUserDAO.getUserById(1)).thenReturn(mockUser);
        Users user = userService.getUserById(1);
        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        verify(mockUserDAO).getUserById(1);
    }

    // Test getUserById returns null for invalid ID
    @Test
    public void testGetUserById_Fail() {
        when(mockUserDAO.getUserById(999)).thenReturn(null);
        Users user = userService.getUserById(999);
        assertNull(user);
        verify(mockUserDAO).getUserById(999);
    }

    // Test updating a user
    @Test
    public void testUpdateUser_Success() {
        Users updatedUser = new Users(1, "John Smith", "john.smith@example.com", "newpassword123", null);
        when(mockUserDAO.updateUser(updatedUser)).thenReturn(true);
        boolean result = userService.updateUser(updatedUser);
        assertTrue(result);
        verify(mockUserDAO).updateUser(updatedUser);
    }

    // Test failing to update a user
    @Test
    public void testUpdateUser_Fail() {
        Users updatedUser = new Users(1, "John Smith", "john.smith@example.com", "newpassword123", null);
        when(mockUserDAO.updateUser(updatedUser)).thenReturn(false);
        boolean result = userService.updateUser(updatedUser);
        assertFalse(result);
        verify(mockUserDAO).updateUser(updatedUser);
    }

    // Test deleting a user successfully
    @Test
    public void testDeleteUser_Success() {
        when(mockUserDAO.deleteUser(1)).thenReturn(true);
        boolean result = userService.deleteUser(1);
        assertTrue(result);
        verify(mockUserDAO).deleteUser(1);
    }

    // Test failing to delete a user
    @Test
    public void testDeleteUser_Fail() {
        when(mockUserDAO.deleteUser(1)).thenReturn(false);
        boolean result = userService.deleteUser(1);
        assertFalse(result);
        verify(mockUserDAO).deleteUser(1);
    }
}
