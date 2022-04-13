package com.example.andappbydmitriipinzari;

import com.example.andappbydmitriipinzari.AnimeApiClasses.TopAnimeResult;

import java.util.List;

public class User {
    public String fullName,password,email,username,profilePictureUrl;

    public List<TopAnimeResult> favouriteAnimeList;

    public User(){

    }

    public User(String fullName, String password, String email, String username, String profilePictureUrl, List<TopAnimeResult> favouriteAnimeList) {
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.username = username;
        this.favouriteAnimeList = favouriteAnimeList;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public List<TopAnimeResult> getFavouriteAnimeList() {
        return favouriteAnimeList;
    }

    public void setFavouriteAnimeList(List<TopAnimeResult> favouriteAnimeList) {
        this.favouriteAnimeList = favouriteAnimeList;
    }
}
