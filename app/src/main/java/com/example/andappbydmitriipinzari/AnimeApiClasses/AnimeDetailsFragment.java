package com.example.andappbydmitriipinzari.AnimeApiClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Rating;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.http.Url;

public class AnimeDetailsFragment extends Fragment {
    private TopAnimeResult animeResult;
    FirebaseDatabase firebaseDatabase;

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

        SharedPreferences preferences = getActivity().getSharedPreferences(usersUid, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
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

        boolean isWatched = preferences.getBoolean(Integer.toString(animeResult.malId), false);

        Log.v("boolean isWatched: ", String.valueOf(isWatched));

        if (isWatched) {
            watchedCheckBox.setChecked(true);
        } else {

            watchedCheckBox.setChecked(false);
        }

        watchedCheckBox.setOnClickListener(view1 -> {

            boolean isChecked = watchedCheckBox.isChecked();

            if (isChecked) {
                DatabaseReference userReference;
                auth = FirebaseAuth.getInstance();


                editor.putBoolean(Integer.toString(animeResult.malId), true).apply();

                boolean check = preferences.getBoolean(Integer.toString(animeResult.malId), false);
                Log.v("boolean check: ", String.valueOf(check));

                if (auth.getCurrentUser() != null) {

                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    userReference = firebaseDatabase.getReference("User").child(firebaseUser.getUid()).child("watchedAnime");
                    DatabaseReference newChildReference = userReference.push();
                    String key = newChildReference.getKey();
                    userReference.child(key).setValue(Integer.toString(animeResult.malId));

                }


            } else {
                Log.e("Its not else", "NotElseWTF");
                editor.remove(Integer.toString(animeResult.malId)).apply();
                auth = FirebaseAuth.getInstance();
                DatabaseReference userReference1;
                FirebaseUser firebaseUser = auth.getCurrentUser();

                if(firebaseUser!=null) {
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
                }
            }
        });


//        DateFormat dateFormat= new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
//        String strDateFrom = dateFormat.format( animeResult.date.from);
//        String strDateTo = dateFormat.format(animeResult.date.to);
        dates.setText(new StringBuilder().append(animeResult.aired.prop.from.year).append("-").
                append(animeResult.aired.prop.from.month).append("-").
                append(animeResult.aired.prop.from.day).append("/").
                append(animeResult.aired.prop.to.year).append("-").
                append(animeResult.aired.prop.to.month).append("-").
                append(animeResult.aired.prop.to.day).toString());

        pgRating.setText(animeResult.rated);
        episodes.setText("Nr of episodes : " + Integer.valueOf(animeResult.episodes));
        Picasso.get().load(animeResult.imageUrl.jpg.getImage_url()).into(image);
        name.setText(animeResult.title);
        rating.setText(animeResult.score.toString());

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
