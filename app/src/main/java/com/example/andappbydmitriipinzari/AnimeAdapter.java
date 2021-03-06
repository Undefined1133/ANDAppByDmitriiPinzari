package com.example.andappbydmitriipinzari;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andappbydmitriipinzari.AnimeApiClasses.SearchedAnime;
import com.example.andappbydmitriipinzari.AnimeApiClasses.TopAnimeResult;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.ViewHolder> {
    private List<TopAnimeResult> topAnimes;

    public AnimeAdapter(List<TopAnimeResult> animes) {

        this.topAnimes = animes;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public AnimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.anime_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeAdapter.ViewHolder holder, int position) {

        TopAnimeResult anime = topAnimes.get(position);
        View view = holder.itemView;
        ImageView imageView = view.findViewById(R.id.image);
        TextView name = view.findViewById(R.id.name);
        name.setText(anime.title);


        Picasso.get().load(anime.imageUrl.jpg.getImage_url()).into(imageView);


        view.setOnClickListener(view1 -> {
            NavController navController = Navigation.findNavController(holder.itemView);

            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            String animeJson = gson.toJson(anime);

            bundle.putString("animeClicked", animeJson);
            navController.navigate(R.id.detailsFragment, bundle);
        });

    }

    public void setDataSet(List<TopAnimeResult> animeResults){
        topAnimes.clear();
        topAnimes.addAll(animeResults);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return topAnimes.size();
    }


}
