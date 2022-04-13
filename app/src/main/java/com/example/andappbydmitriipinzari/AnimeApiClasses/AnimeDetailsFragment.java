package com.example.andappbydmitriipinzari.AnimeApiClasses;

import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
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
        animeResult = gson.fromJson(bundle.getString("animeClicked"), TopAnimeResult.class);

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
