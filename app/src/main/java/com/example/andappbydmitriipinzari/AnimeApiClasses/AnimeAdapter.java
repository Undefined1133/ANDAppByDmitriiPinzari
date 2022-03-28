package com.example.andappbydmitriipinzari.AnimeApiClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andappbydmitriipinzari.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.ViewHolder> {
    private List<TopAnimeResult> animes;
    private OnClickListener Listener;

    public AnimeAdapter(List<TopAnimeResult> animes) {
        this.animes = animes;
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
        TopAnimeResult anime = animes.get(position);
        View view = holder.itemView;
        ImageView imageView = view.findViewById(R.id.image);
        TextView name = view.findViewById(R.id.name);
        name.setText(anime.title);


        Picasso.get().load(anime.imageUrl.jpg.getImage_url()).into(imageView);

    }

    @Override
    public int getItemCount() {
        return animes.size();
    }

    public interface OnClickListener {
        void onClick(SearchedAnime anime);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.Listener = listener;

    }
}
