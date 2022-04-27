package com.example.andappbydmitriipinzari.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andappbydmitriipinzari.AnimeApiClasses.TopAnimeResult;
import com.example.andappbydmitriipinzari.MessageAdapter;
import com.example.andappbydmitriipinzari.Message;
import com.example.andappbydmitriipinzari.R;
import com.example.andappbydmitriipinzari.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupChatFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceMessages;
    DatabaseReference databaseReferenceUser;
    MessageAdapter messageAdapter;
    User user;
    List<Message> messageList;
    RecyclerView recyclerView;
    ImageButton imageButton;
    EditText enteredMessage;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_chat_layout, container, false);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://andappbydmitriipinzari-default-rtdb.europe-west1.firebasedatabase.app/");

        user = new User();
        recyclerView = view.findViewById(R.id.recViewMessages);
        enteredMessage = view.findViewById(R.id.enterMessage);
        imageButton = view.findViewById(R.id.sendButton);
        imageButton.setOnClickListener(view1 -> {
            if (!TextUtils.isEmpty(enteredMessage.getText().toString())) {
                Message message = new Message(enteredMessage.getText().toString(), user.fullName);
                enteredMessage.setText("");
                databaseReferenceMessages.push().setValue(message);
            } else {
                Toast.makeText(getContext(), "You have to type something first", Toast.LENGTH_SHORT).show();
            }
        });
        messageList = new ArrayList<>();
        final FirebaseUser currentUser = auth.getCurrentUser();
        user.setEmail(currentUser.getEmail());

        if (auth.getCurrentUser() != null) {

            databaseReferenceUser = firebaseDatabase.getReference("User").child(auth.getCurrentUser().getUid());
            databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> favouriteAnimes = new ArrayList<String>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key = ds.getKey();
                        String value = ds.getValue().toString();
                        if (key.equals("email")) {
                            user.setEmail(value.toString());
                        } else if (key.equals("fullName")) {
                            user.setFullName(value.toString());
                        }
                        if (key.equals("watchedAnime")) {
                            for (DataSnapshot ds1 : ds.getChildren()) {
                                String key1 = ds1.getKey();
                                favouriteAnimes.add(ds1.getValue().toString());
                            }
                        }

                    }
                    user.setWatchedAnime(favouriteAnimes);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            databaseReferenceMessages = firebaseDatabase.getReference("Messages").child(auth.getCurrentUser().getUid());

            databaseReferenceMessages.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Message message = snapshot.getValue(Message.class);
                    message.setKey(snapshot.getKey());
                    messageList.add(message);
                    displayMessages(messageList);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Message message = snapshot.getValue(Message.class);
                    message.setKey(snapshot.getKey());
                    List<Message> newMessages = new ArrayList<Message>();

                    for (Message m : messageList) {
                        if (m.getKey().equals(message.getKey())) {
                            newMessages.add(message);
                        } else {
                            newMessages.add(m);
                        }
                    }
                    messageList = newMessages;
                    displayMessages(messageList);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Message message = snapshot.getValue(Message.class);
                    message.setKey(snapshot.getKey());
                    List<Message> newMessages = new ArrayList<Message>();
                    for (Message m : messageList) {
                        if (!m.getKey().equals(message.getKey()))
                            newMessages.add(m);
                    }
                    messageList = newMessages;
                    displayMessages(messageList);
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


        return view;
    }

    private void displayMessages(List<Message> messageList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messageAdapter = new MessageAdapter(getContext(), messageList, databaseReferenceMessages);
        recyclerView.setAdapter(messageAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
//        messageList = new ArrayList<>();

    }
}
