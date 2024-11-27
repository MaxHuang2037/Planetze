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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInFragment extends Fragment {
    private EditText email, password;
    private TextView signUpRedirect, forgotPasswordButton;

    private Button loginButton, backButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference userRef;
    private static final String TAG = "EmailPassword";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.log_in_fragment, container, false);

        email = view.findViewById(R.id.editTextTextEmailAddress);
        password = view.findViewById(R.id.editTextTextPassword);
        loginButton = view.findViewById(R.id.logInButton);
        signUpRedirect = view.findViewById(R.id.signUpRedirect);
        backButton = view.findViewById(R.id.backButton);
        forgotPasswordButton = view.findViewById(R.id.forgotPasswordButton);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        signUpRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SignUpFragment());
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HomeFragment());
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ForgotPasswordActivity());
            }
        });

        return view;
    }

    private void logIn(){
        String e = email.getText().toString().trim();
        String p = password.getText().toString().trim();
        if (e.isEmpty() || p.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        auth(e, p);
    }

    private void auth(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        if(user.isEmailVerified()){
                            Toast.makeText(getContext(), "LOGGED IN", Toast.LENGTH_SHORT).show();
                            checkFirstTime();
                        } else {
                            Toast.makeText(getContext(), "Account not verified. Please check your email", Toast.LENGTH_SHORT).show();
                            user.sendEmailVerification();
                        }

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Invalid email or password",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void checkFirstTime(){
        //Read from the database
        String UID = mAuth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance();
        userRef = db.getReference("users").child(mAuth.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getFirstTime()){
                    loadFragment(new AnnualCarbonFootprintFragment());
                } else {
                    loadFragment(new DashboardFragment());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
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
