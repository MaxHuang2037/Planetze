package com.example.Planetzecarbontracker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginModel {
    private final FirebaseAuth mAuth;
    private final FirebaseDatabase db;

    public LoginModel() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
    }

    // Authenticate user with email and password
    public void authenticate(String email, String password, LoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            callback.onSuccess(user.getUid());
                        } else if (user != null) {
                            user.sendEmailVerification();
                            callback.onError("Account not verified. Please check your email.");
                        }
                    } else {
                        callback.onError("Invalid email or password.");
                    }
                });
    }

    // Check if the user is logging in for the first time
    public void checkFirstTime(String uid, UserCallback callback) {
        DatabaseReference userRef = db.getReference("users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    callback.onUserLoaded(user);
                } else {
                    callback.onError("User not found.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Database error: " + error.getMessage());
            }
        });
    }

    // Callback interfaces
    public interface LoginCallback {
        void onSuccess(String uid);
        void onError(String message);
    }

    public interface UserCallback {
        void onUserLoaded(User user);
        void onError(String message);
    }
}
