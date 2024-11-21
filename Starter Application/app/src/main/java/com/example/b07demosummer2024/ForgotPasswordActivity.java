package com.example.b07demosummer2024;

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

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends Fragment {

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forgot_password_fragment, container, false);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Reference UI elements
        Button buttonResendLink = view.findViewById(R.id.btnSendResetLink);
        EditText editTextEmail = view.findViewById(R.id.editTextEmail);
        TextView tvForgotPasswordTitle = view.findViewById(R.id.tvForgotPasswordTitle);

        // Set OnClickListener for the "Send Reset Link" button
        buttonResendLink.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(getContext(), "Please enter an email address", Toast.LENGTH_SHORT).show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            } else {
                sendResetLink(email);
            }
        });

        return view;
    }

    private void sendResetLink(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Password reset email sent to: " + email, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to send reset email: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
