package com.example.andappbydmitriipinzari.AnimeApiClasses;

import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import com.example.andappbydmitriipinzari.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.http.Url;

public class AnimeDetailsFragment extends Fragment {
    private TopAnimeResult animeResult;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.anime_details_layout, container, false);
    Bundle bundle = this.getArguments();
String animeJson = bundle.getString("animeClicked");
    Gson gson = new Gson();
    animeResult = gson.fromJson(bundle.getString("animeClicked"),TopAnimeResult.class);
        ImageView image = view.findViewById(R.id.image);
        TextView name = view.findViewById(R.id.name);
        TextView rating = view.findViewById(R.id.rating);
        TextView pgRating = view.findViewById(R.id.pgRating);
        TextView episodes = view.findViewById(R.id.episodes);
        TextView synopsis = view.findViewById(R.id.synopsis);
        TextView findOutMore = view.findViewById(R.id.findOutMoreText);
        synopsis.setText(animeResult.synopsis);
        pgRating.setText(animeResult.rated);
        episodes.setText("Nr of episodes : " + Integer.valueOf(animeResult.episodes));
        Picasso.get().load(animeResult.imageUrl.jpg.getImage_url()).into(image);
        name.setText(animeResult.title);
        rating.setText(animeResult.score.toString());

findOutMore.setOnClickListener(view1 -> {
    openCustomTab(getActivity(),animeResult.url);
});





        return view;

    }
private void openCustomTab(FragmentActivity appCompatActivity, String url){
     CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
     builder.setShowTitle(true);
     builder.build().launchUrl(appCompatActivity, Uri.parse(url));
}
}
