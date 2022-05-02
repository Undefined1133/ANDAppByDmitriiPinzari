package com.example.andappbydmitriipinzari;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private List<User> friendList;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;
    DatabaseReference userReferenceForGettingFollowers;
    DatabaseReference userReferenceForAddingFollowers;
    Context context;
    private final long ONE_MEGABYTE = 1024 * 1024;

    public FriendsAdapter(List<User> friendList, Context context) {

        this.friendList = friendList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = friendList.get(position);
        View view = holder.itemView;
        TextView fullNameFriend = view.findViewById(R.id.fullNameFriendsFragment);
        TextView emailFriend = view.findViewById(R.id.emailFriend);
        TextView usernameFriend = view.findViewById(R.id.usernameFriend);
        TextView animesWatched = view.findViewById(R.id.numberOfWatchedAnimes);
        ImageView profilePictureFriend = view.findViewById(R.id.profilePictureFriend);
        Button followButton = view.findViewById(R.id.followButton);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://andappbydmitriipinzari-default-rtdb.europe-west1.firebasedatabase.app/");
        userReference = firebaseDatabase.getReference("User");

        if (auth != null) {
            FirebaseUser currentUser = auth.getCurrentUser();
            userReferenceForGettingFollowers = firebaseDatabase.getReference("User").child(currentUser.getUid());
            userReferenceForAddingFollowers = firebaseDatabase.getReference("User").child(currentUser.getUid()).child("friends");
            userReferenceForGettingFollowers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> friendMails = new ArrayList<>();
                    if(snapshot.getKey().equals("friends")){
                        friendMails = (List<String>) snapshot.getValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
//            SharedPreferences preferences = context.getSharedPreferences(currentUser.getUid(), Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            boolean followedBoolean = preferences.getBoolean(user.email, false);
//            if (!followedBoolean) {
//                followButton.setText("FOLLOW");
//            } else {
//                followButton.setText("UNFOLLOW");
//            }

            followButton.setOnClickListener(view1 -> {
                userReferenceForAddingFollowers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DatabaseReference newChildReferenceUser = userReferenceForAddingFollowers.push();
                        String key = newChildReferenceUser.getKey();
                        userReferenceForAddingFollowers.child(key).setValue(user.email);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "ADDING FOLLOWER SOMETHING WRONG", Toast.LENGTH_SHORT).show();
                    }
                });


//                if (currentText.equals("FOLLOW")) {
//                    followButton.setText("UNFOLLOW");
//                    editor.putBoolean(user.email, true);
//
//
//                }
            });
        }


        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                        String key = snapshot2.getKey();
                        String value = snapshot2.getValue().toString();
                        if (key.equals("email") && user.email.equals(value)) {

                            StorageReference mountainsRef = storageRef.child("images/" + snapshot1.getKey());
                            mountainsRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    profilePictureFriend.setImageBitmap(bitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Getting Bytes", "Something went wrong");
                                }
                            });
                        } else {
                            Log.e("Not email", "Current value and key are not email");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database in friends adapter", "Something went wrong : " + error);
            }
        });
        fullNameFriend.setText(user.fullName);
        emailFriend.setText(user.email);
        usernameFriend.setText(user.username);
        int count = 0;
        if (user.watchedAnime != null) {
            for (int i = 0; i < user.watchedAnime.size(); i++) {
                count++;
            }
            animesWatched.setText(count);
        }
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setDataSet(List<User> friendListNew) {
        friendList.clear();
        friendList.addAll(friendListNew);
        notifyDataSetChanged();
    }

}
