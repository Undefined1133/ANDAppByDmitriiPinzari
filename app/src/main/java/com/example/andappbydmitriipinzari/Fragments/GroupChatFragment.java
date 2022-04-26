package com.example.andappbydmitriipinzari.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andappbydmitriipinzari.AnimeApiClasses.MessageAdapter;
import com.example.andappbydmitriipinzari.Message;
import com.example.andappbydmitriipinzari.R;
import com.example.andappbydmitriipinzari.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GroupChatFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    MessageAdapter messageAdapter;
    User user;
    List<Message> messageList;
    RecyclerView recyclerView;
    ImageButton imageButton;
    EditText enteredMessage;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_anime_layout, container, false);
auth = FirebaseAuth.getInstance();
databaseReference = FirebaseDatabase.getInstance().getReference();
user = new User();
recyclerView=view.findViewById(R.id.recViewMessages);
enteredMessage= view.findViewById(R.id.enterMessage);
imageButton=view.findViewById(R.id.sendButton);
imageButton.setOnClickListener(view1 -> {

});
messageList = new ArrayList<>();
final FirebaseUser currentUser = auth.getCurrentUser();
user.setEmail(currentUser.getEmail());











        return view;
    }
}
