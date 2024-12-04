package com.example.Planetzecarbontracker;


public class LoginPresenter {
    private final LogInView view;
    private final LoginModel model;

    public LoginPresenter(LogInView view, LoginModel model) {
        this.view = view;
        this.model = model;
    }

    // Handle login logic
    public void logIn(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            view.showErrorMessage("Please fill out all fields.");
            return;
        }

        model.authenticate(email, password, new LoginModel.LoginCallback() {
            @Override
            public void onSuccess(String uid) {
                model.checkFirstTime(uid, new LoginModel.UserCallback() {
                    @Override
                    public void onUserLoaded(User user) {
                        view.showWelcomeMessage("Welcome, " + user.getName());
                        if (user.getFirstTime()) {
                            view.navigateToAnnualCarbonFootprint();
                        } else {
                            view.navigateToDashboard();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        view.showErrorMessage(message);
                    }
                });
            }

            @Override
            public void onError(String message) {
                view.showErrorMessage(message);
            }
        });
    }
}
