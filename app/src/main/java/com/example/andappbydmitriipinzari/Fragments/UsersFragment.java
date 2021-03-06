package com.example.andappbydmitriipinzari.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andappbydmitriipinzari.UsersAdapter;
import com.example.andappbydmitriipinzari.R;
import com.example.andappbydmitriipinzari.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersReference;
    private RecyclerView recyclerView;
    private UsersAdapter FriendsAdapter;
    private FirebaseAuth auth;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users_layout, container, false);

        List<User> userList = new ArrayList<User>();
        recyclerView = view.findViewById(R.id.recyclerViewForUsers);
        firebaseDatabase = FirebaseDatabase.getInstance("https://andappbydmitriipinzari-default-rtdb.europe-west1.firebasedatabase.app/");
        auth = FirebaseAuth.getInstance();

        usersReference = firebaseDatabase.getReference("User");

        if (auth.getCurrentUser() != null) {
            usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        User user = new User();
                        if (!snapshot1.getKey().equals(auth.getCurrentUser().getUid())) {
                            for (DataSnapshot snapshot2 : snapshot1.getChildren())
                                if (snapshot2.getKey().equals("fullName")) {
                                    user.fullName = snapshot2.getValue().toString();
                                } else if (snapshot2.getKey().equals("username")) {
                                    user.username = snapshot2.getValue().toString();
                                } else if (snapshot2.getKey().equals("email")) {
                                    user.email = snapshot2.getValue().toString();
                                } else if (snapshot2.getKey().equals("")) {

                                }

                            userList.add(user);
                            displayUsers(userList);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            Toast.makeText(getContext(), "You have to be logged in!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void displayUsers(List<User> userList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FriendsAdapter = new UsersAdapter(userList, getContext());
        recyclerView.setAdapter(FriendsAdapter);
    }
}