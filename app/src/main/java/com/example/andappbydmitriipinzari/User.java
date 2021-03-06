package com.example.andappbydmitriipinzari;

import java.util.List;

public class User {
    public String fullName, password, email, username, profilePictureUrl;

    public List<String> watchedAnime;
    public List<String> friends;

    public User() {

    }

    public User(String fullName, String password, String email, String username, String profilePictureUrl, List<String> favouriteAnimeList,List<String> friends) {
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.username = username;
        this.watchedAnime = favouriteAnimeList;
        this.profilePictureUrl = profilePictureUrl;
        this.friends =friends;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
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

    public List<String> getWatchedAnime() {
        return watchedAnime;
    }

    public void setWatchedAnime(List<String> watchedAnime) {
        this.watchedAnime = watchedAnime;
    }
}
