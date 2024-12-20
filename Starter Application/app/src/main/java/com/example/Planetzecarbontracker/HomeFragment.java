package com.example.Planetzecarbontracker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {
    private FirebaseAuth mAuth;
    private User user;
    private FirebaseDatabase db;
    private DatabaseReference userRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);

        Button loginButton = view.findViewById(R.id.loginButton);
        Button signupButton = view.findViewById(R.id.signupButton);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        db = FirebaseDatabase.getInstance();

        if(currentUser != null && currentUser.isEmailVerified()){
            userRef = db.getReference("users").child(mAuth.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                    if(user == null){
                        mAuth.signOut();
                        return;
                    }
                    Toast.makeText(getContext(), "Welcome, " + user.getName(), Toast.LENGTH_SHORT).show();
                    if(user.getFirstTime()){
                        loadFragment(new AnnualCarbonFootprintFragment());
                    } else {
                        Log.v("Redirect", "Redreiction to dashboard");
                        loadFragment(new DashboardFragment());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                }
            });
        } else {
            mAuth.signOut();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new LogInView());
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SignUpFragment());
            }
        });

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
