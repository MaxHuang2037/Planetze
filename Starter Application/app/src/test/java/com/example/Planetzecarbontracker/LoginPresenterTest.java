package com.example.Planetzecarbontracker;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for the LoginPresenter class.
 * These tests validate the behavior of the LoginPresenter
 * in different scenarios, such as valid and invalid login attempts.
 */
public class LoginPresenterTest {

    // Mocked instances of the View and Model to simulate their behavior
    @Mock
    private LogInView mockView;

    @Mock
    private LoginModel mockModel;

    // The LoginPresenter instance being tested
    private LoginPresenter presenter;

    /**
     * Initializes the mocks and sets up the LoginPresenter instance
     * before running each test.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes all of the @Mock instances
        presenter = new LoginPresenter(mockView, mockModel); // Presenter under test
    }

    /**
     * Test case 1: The user submits empty email and password fields.
     * Expected behavior: The View should show an error message prompting the user to fill out all fields.
     */
    @Test
    public void testEmptyFields_showsErrorMessage() {
        // case where the user submits empty fields
        presenter.logIn("", "");

        // Verify that the View displays the appropriate error message
        Mockito.verify(mockView).showErrorMessage("Please fill out all fields.");
    }

    /**
     * Test case 2: A first-time user successfully logs in.
     * Expected behavior: The View should welcome the user
     * and navigate to the Annual Carbon Footprint screen.
     */
    @Test
    public void testValidCredentials_firstTimeUser() {
        // case of successful authentication
        Mockito.doAnswer(invocation -> {
            LoginModel.LoginCallback callback = invocation.getArgument(2);
            callback.onSuccess("testUid"); // case of success with a test UID
            return null;
        }).when(mockModel).authenticate(Mockito.anyString(), Mockito.anyString(), Mockito.any());

        // case where the user being a first-time user
        Mockito.doAnswer(invocation -> {
            LoginModel.UserCallback callback = invocation.getArgument(1);
            User user = new User("testUid", "Test User", "test@example.com", true);
            callback.onUserLoaded(user); // case where user data being loaded
            return null;
        }).when(mockModel).checkFirstTime(Mockito.anyString(), Mockito.any());

        // Call the logIn method with valid credentials
        presenter.logIn("test@example.com", "password123");

        // Verify that the View welcomes the user and navigates appropriately
        Mockito.verify(mockView).showWelcomeMessage("Welcome, Test User");
        Mockito.verify(mockView).navigateToAnnualCarbonFootprint();
    }

    /**
     * Test case 3: A returning user successfully logs in.
     * Expected behavior: The View should welcome the user and navigate
     * to the Dashboard.
     */
    @Test
    public void testValidCredentials_returningUser() {
        // case of successful authentication
        Mockito.doAnswer(invocation -> {
            LoginModel.LoginCallback callback = invocation.getArgument(2);
            callback.onSuccess("testUid"); // case of success with a test UID
            return null;
        }).when(mockModel).authenticate(Mockito.anyString(), Mockito.anyString(), Mockito.any());

        // case that the user being a returning user
        Mockito.doAnswer(invocation -> {
            LoginModel.UserCallback callback = invocation.getArgument(1);
            User user = new User("testUid", "Test User", "test@example.com", false);
            callback.onUserLoaded(user); // case where user data being loaded
            return null;
        }).when(mockModel).checkFirstTime(Mockito.anyString(), Mockito.any());

        // Call the logIn method with valid credentials
        presenter.logIn("test@example.com", "password123");

        // Verify that the View welcomes the user and navigates appropriately
        Mockito.verify(mockView).showWelcomeMessage("Welcome, Test User");
        Mockito.verify(mockView).navigateToDashboard();
    }

    /**
     * Test case 4: The user is successfully authenticated, but the Model fails to find the user's data.
     * Expected behavior: The View should show an error message stating that the user was not found.
     */
    @Test
    public void testValidCredentials_userNotFound() {
        // case of successful authentication
        Mockito.doAnswer(invocation -> {
            LoginModel.LoginCallback callback = invocation.getArgument(2);
            callback.onSuccess("testUid"); // case of success with a test UID
            return null;
        }).when(mockModel).authenticate(Mockito.anyString(), Mockito.anyString(), Mockito.any());

        // case where a user not being found in the database
        Mockito.doAnswer(invocation -> {
            LoginModel.UserCallback callback = invocation.getArgument(1);
            callback.onError("User not found."); // case of an error
            return null;
        }).when(mockModel).checkFirstTime(Mockito.anyString(), Mockito.any());

        // Call the logIn method with valid credentials
        presenter.logIn("test@example.com", "password123");

        // Verify that the View displays the appropriate error message
        Mockito.verify(mockView).showErrorMessage("User not found.");
    }

    /**
     * Test case 5: The user submits invalid credentials.
     * Expected behavior: The View should show an error message stating
     * that the email or password is invalid.
     */
    @Test
    public void testInvalidCredentials_showsErrorMessage() {
        // case of authentication failure
        Mockito.doAnswer(invocation -> {
            LoginModel.LoginCallback callback = invocation.getArgument(2);
            callback.onError("Invalid email or password."); // case of an error
            return null;
        }).when(mockModel).authenticate(Mockito.anyString(), Mockito.anyString(), Mockito.any());

        // Call the logIn method with invalid credentials
        presenter.logIn("test@example.com", "wrongpassword");

        // Verify that the View displays the appropriate error message and not the other one
        Mockito.verify(mockView).showErrorMessage("Invalid email or password.");
    }
}
