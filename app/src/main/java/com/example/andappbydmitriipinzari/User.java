package com.example.andappbydmitriipinzari;

public class User {
    public String fullName,password,email,username;

    public User(){

    }

    public User(String fullName, String password, String email, String username) {
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.username = username;
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
}