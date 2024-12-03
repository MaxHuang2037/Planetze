package com.example.Planetzecarbontracker;

public interface LoginView {
    void showLoading();  // Show a loading indicator
    void hideLoading();  // Hide the loading indicator
    void showErrorMessage(String message);  // Display an error message
    void showWelcomeMessage(String message);  // Display a welcome message
    void navigateToDashboard();  // Navigate to the dashboard
    void navigateToAnnualCarbonFootprint();  // Navigate to the annual carbon footprint screen
}
