package com.example.andappbydmitriipinzari.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andappbydmitriipinzari.AnimeApiClasses.AnimeAdapter;
import com.example.andappbydmitriipinzari.AnimeApiClasses.AnimeClient;
import com.example.andappbydmitriipinzari.R;
import com.example.andappbydmitriipinzari.AnimeApiClasses.TopAnime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnimeSearchFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_anime_layout, container, false);

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://api.jikan.moe/v4/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        AnimeClient client = retrofit.create(AnimeClient.class);

        Call<TopAnime> call = client.getTopAnime();
        call.enqueue(new Callback<TopAnime>() {

            @Override
            public void onResponse(Call<TopAnime> call, Response<TopAnime> response) {
                if (response.body() != null) {

                     TopAnime listOfAnime =  response.body();

                    RecyclerView recyclerView = view.findViewById(R.id.animeList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                    recyclerView.hasFixedSize();

                    AnimeAdapter animeAdapter = new AnimeAdapter(listOfAnime.getTop());
                    recyclerView.setAdapter(animeAdapter);
                }

            }

            @Override
            public void onFailure(Call<TopAnime> call, Throwable t) {
                Toast.makeText(getActivity(), "error: (", Toast.LENGTH_SHORT).show();
            }
        });

        return view;


    }
}
