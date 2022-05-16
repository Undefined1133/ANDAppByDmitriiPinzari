package com.example.andappbydmitriipinzari.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andappbydmitriipinzari.AnimeAdapter;
import com.example.andappbydmitriipinzari.AnimeApiClasses.TopAnimeResult;
import com.example.andappbydmitriipinzari.R;
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
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;
    DatabaseReference userReferenceForFollowing;
    DatabaseReference userReferenceForFollowers;

    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageRef;
    Uri imageUri;

    List<TopAnimeResult> listOfResultsOfSearchedAnimes = new ArrayList<TopAnimeResult>();
    List<TopAnimeResult> listOfResultsOfSearchedAnimesForAdapterSet = new ArrayList<TopAnimeResult>();
    private final long ONE_MEGABYTE = 1024 * 1024;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, container, false);


        AnimeAdapter animeAdapter = new AnimeAdapter(listOfResultsOfSearchedAnimes);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        ImageView profilePicture = view.findViewById(R.id.profilePicture);
        TextView username = view.findViewById(R.id.usernameProfile);
        TextView email = view.findViewById(R.id.emailProfile);

        TextView fullName = view.findViewById(R.id.fullNameProfile);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerProfile);
        TextView numberOfFollowing = view.findViewById(R.id.numberOfFollowing);
        TextView numberOfFollowers = view.findViewById(R.id.numberOfFollowers);
        firebaseDatabase = FirebaseDatabase.getInstance("https://andappbydmitriipinzari-default-rtdb.europe-west1.firebasedatabase.app/");

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseUser firebaseUser = auth.getCurrentUser();

            userReference = firebaseDatabase.getReference("User").child(firebaseUser.getUid());
            userReferenceForFollowing = firebaseDatabase.getReference("User").child(firebaseUser.getUid()).child("friends");
            userReferenceForFollowers = firebaseDatabase.getReference("User");


            if (userReferenceForFollowers != null) {
                userReferenceForFollowers.addListenerForSingleValueEvent(new ValueEventListener() {
                    int count = 0;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                if (snapshot2.getKey().equals("friends")) {
                                    for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                        if (snapshot3.getValue().equals(firebaseUser.getEmail())) {
                                            count++;
                                        }
                                    }
                                }
                            }

                        }
                        numberOfFollowers.setText(String.valueOf(count));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
        if (userReferenceForFollowing != null) {

            userReferenceForFollowing.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long number = snapshot.getChildrenCount();
                    numberOfFollowing.setText(String.valueOf(number));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if (userReference != null) {
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    StorageReference mountainsRef = storageRef.child("images/" + auth.getCurrentUser().getUid());


                    mountainsRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            profilePicture.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e("Getting Bytes", "Something went wrong");
                        }
                    });
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    username.setText(snapshot.child("username").getValue().toString());
                    email.setText(snapshot.child("email").getValue().toString());
                    fullName.setText(snapshot.child("fullName").getValue().toString());
                    DatabaseReference favouriteAnimeReference;


                    favouriteAnimeReference = firebaseDatabase.getReference("FavouriteAnime").child(firebaseUser.getUid());

                    favouriteAnimeReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                String key = postSnapshot.getKey();
                                TopAnimeResult value = postSnapshot.getValue(TopAnimeResult.class);

                                listOfResultsOfSearchedAnimesForAdapterSet.add(value);
                                Log.e("Key is: ", key);
                            }
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                            recyclerView.hasFixedSize();
                            animeAdapter.setDataSet(listOfResultsOfSearchedAnimesForAdapterSet);
                            recyclerView.setAdapter(animeAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });

            ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getData() != null) {
                                imageUri = result.getData().getData();
                                profilePicture.setImageURI(imageUri);
                                uploadPicture();
                            } else {
                                Toast.makeText(getActivity(), "Image not uploaded", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
            profilePicture.setOnClickListener(view1 -> {
                choosePicture(activityResultLaunch);
            });


        }

        return view;
    }

    private void uploadPicture() {
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseUser firebaseUser = auth.getCurrentUser();
            String uid = firebaseUser.getUid();
            StorageReference mountainsRef = storageRef.child("images/" + uid);
            userReference = firebaseDatabase.getReference("User").child(uid).child("profilePictureUrl");

            mountainsRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Image uploaded.", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void choosePicture(ActivityResultLauncher<Intent> activityResultLauncher) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(intent);
    }

}
