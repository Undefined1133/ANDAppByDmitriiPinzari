package com.example.andappbydmitriipinzari.AnimeApiClasses;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import com.example.andappbydmitriipinzari.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class AnimeDetailsFragment extends Fragment {
    private TopAnimeResult animeResult;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference favouriteAnimeReference;
    FirebaseAuth auth;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.anime_details_layout, container, false);
        Bundle bundle = this.getArguments();
        String animeJson = bundle.getString("animeClicked");
        String usersUid = "";
        Gson gson = new Gson();

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            FirebaseUser firebaseUserCheckIfLogged = auth.getCurrentUser();
            usersUid = firebaseUserCheckIfLogged.getUid();
        }


        animeResult = gson.fromJson(bundle.getString("animeClicked"), TopAnimeResult.class);

        firebaseDatabase = FirebaseDatabase.getInstance("https://andappbydmitriipinzari-default-rtdb.europe-west1.firebasedatabase.app/");
        CheckBox watchedCheckBox = view.findViewById(R.id.favouriteAnimeCheckBox);
        ImageView image = view.findViewById(R.id.image);
        TextView dates = view.findViewById(R.id.dates);
        TextView name = view.findViewById(R.id.name);
        TextView rating = view.findViewById(R.id.rating);
        TextView pgRating = view.findViewById(R.id.pgRating);
        TextView episodes = view.findViewById(R.id.episodes);
        TextView synopsis = view.findViewById(R.id.synopsis);
        TextView findOutMore = view.findViewById(R.id.findOutMoreText);
        synopsis.setText(animeResult.synopsis);
        auth = FirebaseAuth.getInstance();


        if (auth.getCurrentUser() != null) {
            FirebaseUser firebaseUser = auth.getCurrentUser();
            favouriteAnimeReference = firebaseDatabase.getReference("FavouriteAnime").child(firebaseUser.getUid());

            favouriteAnimeReference.addListenerForSingleValueEvent(new ValueEventListener() {
                boolean isWatched = false;

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        TopAnimeResult animeResultFromSnapshot = snapshot1.getValue(TopAnimeResult.class);

                        assert animeResultFromSnapshot != null;

                        if (animeResultFromSnapshot.malId == animeResult.malId) {
                            isWatched = true;
                        }
                    }

                    if (isWatched) {
                        watchedCheckBox.setChecked(true);
                    } else {

                        watchedCheckBox.setChecked(false);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to get favourite animes", Toast.LENGTH_SHORT).show();
                }
            });


            watchedCheckBox.setOnClickListener(view1 ->

            {

                boolean isChecked = watchedCheckBox.isChecked();

                if (isChecked) {
                    DatabaseReference userReference;

                    userReference = firebaseDatabase.getReference("User").child(firebaseUser.getUid()).child("watchedAnime");


                    DatabaseReference newChildReferenceFavouriteAnime = favouriteAnimeReference.push();
                    String keyAnime = newChildReferenceFavouriteAnime.getKey();


                    favouriteAnimeReference.child(keyAnime).setValue(animeResult);

                    DatabaseReference newChildReferenceUser = userReference.push();
                    String key = newChildReferenceUser.getKey();
                    userReference.child(key).setValue(Integer.toString(animeResult.malId));


                }
            });

        } else {

            auth = FirebaseAuth.getInstance();
            DatabaseReference userReference1;
            DatabaseReference favouriteAnimeReference;


            FirebaseUser firebaseUser = auth.getCurrentUser();
            favouriteAnimeReference = firebaseDatabase.getReference("FavouriteAnime").child(firebaseUser.getUid());
            if (firebaseUser != null) {
                userReference1 = firebaseDatabase.getReference("User").child(firebaseUser.getUid()).child("watchedAnime");

                userReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot watchedAnimeSnapshot : snapshot.getChildren()) {

                            String key = watchedAnimeSnapshot.getKey();
                            String value = watchedAnimeSnapshot.getValue().toString();
                            Log.e("Key is: ", key);

                            if (value.equals(Integer.toString(animeResult.malId))) {
                                userReference1.child(key).removeValue();
                                Log.e("Removed ", "Is it tho?");
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.v("onCancelled", "idk something is wrong");
                    }
                });
                favouriteAnimeReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot favouriteAnimeSnapshot : snapshot.getChildren()) {

                            String key = favouriteAnimeSnapshot.getKey();
                            TopAnimeResult value = favouriteAnimeSnapshot.getValue(TopAnimeResult.class);
                            Log.e("Key is: ", key);


                            if (Integer.toString(value.malId).equals(Integer.toString(animeResult.malId))) {
                                favouriteAnimeReference.child(key).removeValue();
                                Log.e("Removed ", "Is it tho?");
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }


//        DateFormat dateFormat= new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
//        String strDateFrom = dateFormat.format( animeResult.date.from);
//        String strDateTo = dateFormat.format(animeResult.date.to);

        if ((!animeResult.airing)  && (animeResult.aired.prop.to != null)  && (animeResult.aired.prop.to.year != null)) {
            dates.setText(new StringBuilder().append(animeResult.aired.prop.from.year).append("-").
                    append(animeResult.aired.prop.from.month).append("-").
                    append(animeResult.aired.prop.from.day).append("/").
                    append(animeResult.aired.prop.to.year).append("-").
                    append(animeResult.aired.prop.to.month).append("-").
                    append(animeResult.aired.prop.to.day).toString());
        } else if (animeResult.type.equals("Movie") || animeResult.type.equals("Special")) {
            dates.setText(new StringBuilder().append(animeResult.aired.prop.from.year).append("-").
                    append(animeResult.aired.prop.from.month).append("-").
                    append(animeResult.aired.prop.from.day).toString());
        } else {
            dates.setText(new StringBuilder().append(animeResult.aired.prop.from.year).append("-").
                    append(animeResult.aired.prop.from.month).append("-").
                    append(animeResult.aired.prop.from.day).append("/").
                    append("Still airing").toString());
        }
        pgRating.setText(animeResult.rating);
        episodes.setText("Nr of episodes : " + Integer.valueOf(animeResult.episodes));
        Picasso.get().load(animeResult.imageUrl.jpg.getImage_url()).into(image);
        name.setText(animeResult.title);
        rating.setText("Rating :" + animeResult.score.toString());

        findOutMore.setOnClickListener(view1 -> {
            openCustomTab(getActivity(), animeResult.url);
        });


        return view;

    }

    private void openCustomTab(FragmentActivity appCompatActivity, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(true);
        builder.build().launchUrl(appCompatActivity, Uri.parse(url));
    }
}
