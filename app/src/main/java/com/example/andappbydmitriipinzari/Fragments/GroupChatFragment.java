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
import com.google.firebase.database.Query;
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
                if (auth.getCurrentUser() != null) {
                    Message message = new Message(enteredMessage.getText().toString(), user.email);
                    enteredMessage.setText("");
                    databaseReferenceMessages.push().setValue(message);
                } else {
                    Toast.makeText(getContext(), "You have to be logged in", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "You have to type something first", Toast.LENGTH_SHORT).show();
            }
        });

        messageList = new ArrayList<>();
        final FirebaseUser currentUser = auth.getCurrentUser();

//        user.setEmail(currentUser.getEmail());

        if (auth.getCurrentUser() != null) {

            databaseReferenceUser = firebaseDatabase.getReference("User").child(auth.getCurrentUser().getUid());
            databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key = ds.getKey();
                        String value = ds.getValue().toString();
                        if (key.equals("email")) {
                            user.setEmail(value.toString());
                        } else if (key.equals("fullName")) {
                            user.setFullName(value.toString());
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            databaseReferenceMessages = firebaseDatabase.getReference("Messages");
            Query query = databaseReferenceMessages.orderByKey();


            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Message message = postSnapshot.getValue(Message.class);
                        if (messageList.isEmpty()) {
                            messageList.add(message);
                        } else {
                            for (int i = 0; i < messageList.size(); i++) {
                                String messageForCheck = message.getMessage();
                                boolean flag = false;
                                for (int j = 0; j < messageList.size(); j++) {
                                    if (messageList.get(j).getMessage().equals(messageForCheck)) {
                                        flag = true;
                                    }
                                }
                                if (!flag) {
                                    messageList.add(message);
                                }
                            }
                        }
                    }
                    displayMessages(messageList);
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


