package com.example.andappbydmitriipinzari.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.andappbydmitriipinzari.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;
    FirebaseAuth auth;
    private static final String USERS = "User";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, container, false);

        ImageView profilePicture = view.findViewById(R.id.profilePicture);
        TextView username = view.findViewById(R.id.usernameProfile);
        TextView email = view.findViewById(R.id.emailProfile);
        TextView favouriteAnimes = view.findViewById(R.id.FavouriteAnimes);
        TextView fullName = view.findViewById(R.id.fullNameProfile);


        firebaseDatabase = FirebaseDatabase.getInstance("https://andappbydmitriipinzari-default-rtdb.europe-west1.firebasedatabase.app/");

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            FirebaseUser firebaseUser = auth.getCurrentUser();

//ADD VALUE EVENT LISTENER NOT TRIGGERING

//ADD VALUE EVENT LISTENER NOT TRIGGERING

//ADD VALUE EVENT LISTENER NOT TRIGGERING
            userReference = firebaseDatabase.getReference("User").child(firebaseUser.getUid());
        }

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                username.setText(snapshot.child("username").getValue().toString());
                email.setText(snapshot.child("email").getValue().toString());
                fullName.setText(snapshot.child("fullName").getValue().toString());

//                lastname.setText(firstnameLastname[1]);
                Toast.makeText(getContext(), username.getText().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
