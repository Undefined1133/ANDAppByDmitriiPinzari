package com.example.andappbydmitriipinzari.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;
    FirebaseAuth auth;
    List<String> listOfFriendId = new ArrayList<String>();
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_layout, container, false);


        TextView fullNameFriend = view.findViewById(R.id.fullNameFriendsFragment);
        TextView emailFriend = view.findViewById(R.id.emailFriend);
        TextView usernameFriend = view.findViewById(R.id.usernameFriend);
        TextView animesWatched = view.findViewById(R.id.numberOfWatchedAnimes);

        firebaseDatabase = FirebaseDatabase.getInstance("https://andappbydmitriipinzari-default-rtdb.europe-west1.firebasedatabase.app/");
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            FirebaseUser firebaseUser = auth.getCurrentUser();

            userReference = firebaseDatabase.getReference("User").child(firebaseUser.getUid());
userReference.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

        if(snapshot.getKey().equals("Friends")){
            listOfFriendId = (List<String>) snapshot.getValue();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

        }

        return view;
}
}
