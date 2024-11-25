package com.example.Planetzecarbontracker;

// EmailVerificationReminderActivity.java
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class EmailVerificationReminderActivity extends Fragment {

    private TextView verificationMessage;
    private Button resendVerificationButton, backButton;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register, container, false);

        backButton = view.findViewById(R.id.backButton);
        verificationMessage = view.findViewById(R.id.verificationMessage);
        resendVerificationButton = view.findViewById(R.id.resendVerificationButton);
        mAuth = FirebaseAuth.getInstance();

        resendVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationEmail();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SignUpFragment());
            }
        });
        return view;
    }

    private void resendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(),
                                    "Verification email sent. Please check your inbox.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(),
                                    "Failed to send verification email. Please try again later.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

