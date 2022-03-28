package com.example.andappbydmitriipinzari.AnimeApiClasses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopAnime {
    @SerializedName("request_cache_expiry")
    int requestCacheExpiry;
    @SerializedName("request_cached")
    boolean requestCached;
    @SerializedName("request_hash")
    String requestHash;
    List<TopAnimeResult> data;

    public List<TopAnimeResult> getTop() {
        return data;
    }
}
