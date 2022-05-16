package com.example.andappbydmitriipinzari;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {
    Context context;
    List<Message> messageList;
    DatabaseReference messageDatabase;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersReference;

    public MessageAdapter(Context context, List<Message> messageList, DatabaseReference messageDatabase) {
        this.context = context;
        this.messageDatabase = messageDatabase;
        this.messageList = messageList;
    }


    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        LinearLayout linearLayout;


        public MessageAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            linearLayout = itemView.findViewById(R.id.message);
        }
    }


    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {
        Message message = messageList.get(position);
        auth = FirebaseAuth.getInstance();
        if (message.getName().equals(auth.getCurrentUser().getEmail())) {
            holder.title.setText("You: " + message.getMessage());
            holder.title.setGravity(Gravity.START);
            holder.linearLayout.setBackgroundColor(Color.parseColor("#fabaff"));
        } else {
            firebaseDatabase = FirebaseDatabase.getInstance("https://andappbydmitriipinzari-default-rtdb.europe-west1.firebasedatabase.app/");
            usersReference = firebaseDatabase.getReference("User");
            usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = new User();
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        for (DataSnapshot snapshot2 :snapshot1.getChildren()) {
                            if (snapshot2.getKey().equals("fullName")) {
                                user.fullName = snapshot2.getValue().toString();
                            } else if (snapshot2.getKey().equals("username")) {
                                user.username = snapshot2.getValue().toString();
                            } else if (snapshot2.getKey().equals("email")) {
                                user.email = snapshot2.getValue().toString();
                            }
                        }
                        if(user.email.equals(message.getName())){
                            holder.title.setText(user.fullName +": " + message.getMessage());
                            holder.linearLayout.setBackgroundColor(Color.parseColor("#2c96c7"));
                    }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            holder.title.setText(message.getName() + ":" + message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
