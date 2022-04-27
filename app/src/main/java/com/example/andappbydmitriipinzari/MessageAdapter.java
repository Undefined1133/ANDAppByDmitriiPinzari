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
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {
Context context;
List<Message> messageList;
DatabaseReference messageDatabase;
FirebaseAuth auth;


public MessageAdapter(Context context, List<Message> messageList, DatabaseReference messageDatabase){
    this.context = context;
    this.messageDatabase = messageDatabase;
    this.messageList = messageList;
}


    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageButton delete;
        LinearLayout linearLayout;



        public MessageAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.title);
            delete= itemView.findViewById(R.id.delete);
            linearLayout = itemView.findViewById(R.id.message);

            delete.setOnClickListener(view -> {
                messageDatabase.child(messageList.get(getBindingAdapterPosition()).getKey()).removeValue();
            });
        }
    }


    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
View view = LayoutInflater.from(context).inflate(R.layout.item_message,parent,false);


        return new MessageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {
Message message = messageList.get(position);
auth = FirebaseAuth.getInstance();

if(message.getName().equals(auth.getCurrentUser().getEmail())){
    holder.title.setText("You: "  + message.getMessage());
    holder.title.setGravity(Gravity.START);
    holder.linearLayout.setBackgroundColor(Color.parseColor("#EF973"));
}
else
{
    holder.title.setText(message.getName() + ":" + message.getMessage());
    holder.delete.setVisibility(View.GONE);
}
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
