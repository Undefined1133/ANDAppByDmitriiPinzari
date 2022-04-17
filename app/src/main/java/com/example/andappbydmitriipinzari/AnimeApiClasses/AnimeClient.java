package com.example.andappbydmitriipinzari.AnimeApiClasses;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AnimeClient {
@GET("anime")
Call<SearchedAnime> searchAnime(@Query("q") String queryString);
@GET("top/anime")
    Call<TopAnime>getTopAnime();
@GET("anime/{id}")
    Call<SearchedAnimeById>getAnimeById(@Path("id") String id);

}
