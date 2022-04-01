package com.example.andappbydmitriipinzari.AnimeApiClasses;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SearchedAnime {

    @SerializedName("last_page")
    int lastPage;
    @SerializedName("request_cache_expiry")
    int requestCacheExpiry;
    @SerializedName("request_cached")
    boolean requestCached;
    @SerializedName("request_hash")
    String requestHash;
    @SerializedName("data")
    List<TopAnimeResult> data;

    public List<TopAnimeResult> getData() {
        return data;
    }
}
