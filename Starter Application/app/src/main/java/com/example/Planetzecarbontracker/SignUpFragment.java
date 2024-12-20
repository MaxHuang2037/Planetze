package com.example.Planetzecarbontracker;

import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpFragment extends Fragment {
    private EditText name, email, password, confirmPassword;
    private TextView logInRedirect;

    private Button signUpButton, backButton;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference userRef;
    private static final String TAG = "EmailPassword";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();

        name = view.findViewById((R.id.editTextTextName));
        email = view.findViewById(R.id.editTextTextEmailAddress);
        password = view.findViewById(R.id.editTextTextPassword);
        confirmPassword = view.findViewById(R.id.editTextTextConfirmPassword);
        signUpButton = view.findViewById(R.id.signUpButton);
        logInRedirect = view.findViewById(R.id.logInRedirect);
        backButton = view.findViewById(R.id.backButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        logInRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new LogInView());
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HomeFragment());
            }
        });

        return view;
    }

    private void signUp(){
        String n = name.getText().toString().trim();
        String e = email.getText().toString().trim();
        String p = password.getText().toString().trim();
        String cp = confirmPassword.getText().toString().trim();

        if (n.isEmpty() || e.isEmpty() || p.isEmpty() || cp.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!p.equals(cp)){
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        createAccount(n, e, p);
    }
    private void createAccount(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            // adding to the database
                            db = FirebaseDatabase.getInstance();
                            String UID = mAuth.getUid();
                            userRef = db.getReference("users");

                            // user schema
                            User user = new User(UID, name, email, true);

                            userRef.child(UID).setValue(user);

                            Toast.makeText(getContext(), "Account created.",
                                    Toast.LENGTH_SHORT).show();
                            loadFragment(new EmailVerificationReminderActivity());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Something went wrong. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
