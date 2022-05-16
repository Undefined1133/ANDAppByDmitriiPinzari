package com.example.andappbydmitriipinzari.AnimeApiClasses;

import com.google.gson.annotations.SerializedName;

public class TopAnimeResult {
    @SerializedName("airing")
   public Boolean airing;
    @SerializedName("episodes")
    public int episodes;
    @SerializedName("images")
    public Images imageUrl;
    @SerializedName("mal_id")
    public int malId;
    @SerializedName("members")
    public int members;
    @SerializedName("rating")
    public  String rating;
    @SerializedName("score")
    public  Double score;
    @SerializedName("aired")
    public  Aired aired;
    @SerializedName("synopsis")
    public String synopsis;
    @SerializedName("title")
    public  String title;
    @SerializedName("type")
    public  String type;
    @SerializedName("url")
    public  String url;


    public TopAnimeResult(){

    }

    public Boolean getAiring() {
        return airing;
    }

    public void setAiring(Boolean airing) {
        this.airing = airing;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public Images getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Images imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getMalId() {
        return malId;
    }

    public void setMalId(int malId) {
        this.malId = malId;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Aired getAired() {
        return aired;
    }

    public void setAired(Aired aired) {
        this.aired = aired;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
