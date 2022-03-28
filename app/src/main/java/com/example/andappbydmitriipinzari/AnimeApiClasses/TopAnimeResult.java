package com.example.andappbydmitriipinzari.AnimeApiClasses;

import com.example.andappbydmitriipinzari.AnimeApiClasses.Images;
import com.google.gson.annotations.SerializedName;

public class TopAnimeResult {
    @SerializedName("airing")
    Boolean airing;
    @SerializedName("end_date")
    String endDate;
    @SerializedName("episodes")
    int episodes;
    @SerializedName("images")
    Images imageUrl;
    @SerializedName("mal_id")
    int malId;
    @SerializedName("members")
    int members;
    @SerializedName("rated")
    String rated;
    @SerializedName("score")
    Double score;
    @SerializedName("start_date")
    String startDate;
    @SerializedName("synopsis")
    String synopsis;
    @SerializedName("title")
    String title;
    @SerializedName("type")
    String type;
    @SerializedName("url")
    String url;

}
