package com.example.Planetzecarbontracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class LogInFragment extends Fragment implements LoginView {
    private EditText email, password;
    private Button loginButton;
    private TextView signUpRedirect, forgotPasswordButton;

    private LoginPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.log_in_fragment, container, false);

        // Bind UI elements
        email = view.findViewById(R.id.editTextTextEmailAddress);
        password = view.findViewById(R.id.editTextTextPassword);
        loginButton = view.findViewById(R.id.logInButton);
        signUpRedirect = view.findViewById(R.id.signUpRedirect);
        forgotPasswordButton = view.findViewById(R.id.forgotPasswordButton);

        // Initialize the Presenter
        presenter = new LoginPresenter(this, new LoginModel());

        // Set button listeners
        loginButton.setOnClickListener(v -> presenter.logIn(
                email.getText().toString().trim(),
                password.getText().toString().trim()
        ));

        signUpRedirect.setOnClickListener(v -> loadFragment(new SignUpFragment()));
        forgotPasswordButton.setOnClickListener(v -> loadFragment(new ForgotPasswordActivity()));

        return view;
    }

    @Override
    public void showLoading() {
        // Show loading indicator (e.g., a ProgressBar)
    }

    @Override
    public void hideLoading() {
        // Hide loading indicator
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWelcomeMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToDashboard() {
        loadFragment(new DashboardFragment());
    }

    @Override
    public void navigateToAnnualCarbonFootprint() {
        loadFragment(new AnnualCarbonFootprintFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
