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

import com.example.andappbydmitriipinzari.AnimeApiClasses.AnimeAdapter;
import com.example.andappbydmitriipinzari.AnimeApiClasses.AnimeClient;
import com.example.andappbydmitriipinzari.AnimeApiClasses.SearchedAnime;
import com.example.andappbydmitriipinzari.AnimeApiClasses.SearchedAnimeById;
import com.example.andappbydmitriipinzari.AnimeApiClasses.TopAnime;
import com.example.andappbydmitriipinzari.AnimeApiClasses.TopAnimeResult;
import com.example.andappbydmitriipinzari.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageRef;
    Uri imageUri;
    ArrayList<String> animeIdList = new ArrayList<>();
    List<SearchedAnimeById> listOfSearchedAnimes = new ArrayList<SearchedAnimeById>();
    List<TopAnimeResult> listOfResultsOfSearchedAnimes = new ArrayList<TopAnimeResult>();
    List<TopAnimeResult> listOfResultsOfSearchedAnimesForAdapterSet = new ArrayList<TopAnimeResult>();
    private static final String USERS = "User";
    private final long ONE_MEGABYTE = 1024 * 1024;
    private List<SearchedAnime> searchedAnimeList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, container, false);

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://api.jikan.moe/v4/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        AnimeAdapter animeAdapter = new AnimeAdapter(listOfResultsOfSearchedAnimes);
        AnimeClient client = retrofit.create(AnimeClient.class);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        ImageView profilePicture = view.findViewById(R.id.profilePicture);
        TextView username = view.findViewById(R.id.usernameProfile);
        TextView email = view.findViewById(R.id.emailProfile);
        TextView favouriteAnimes = view.findViewById(R.id.FavouriteAnimes);
        TextView fullName = view.findViewById(R.id.fullNameProfile);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerProfile);

        firebaseDatabase = FirebaseDatabase.getInstance("https://andappbydmitriipinzari-default-rtdb.europe-west1.firebasedatabase.app/");

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseUser firebaseUser = auth.getCurrentUser();

            userReference = firebaseDatabase.getReference("User").child(firebaseUser.getUid());


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
                            if (result.getData().getData() != null) {
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
//            StorageReference mountainsRef = storageRef.child("images/" + uid + " "+imageUriForUpload);
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
// Create a reference to 'images/mountains.jpg'


// While the file names are the same, the references point to different files

    }

    private void choosePicture(ActivityResultLauncher<Intent> activityResultLauncher) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(intent);
    }

}
