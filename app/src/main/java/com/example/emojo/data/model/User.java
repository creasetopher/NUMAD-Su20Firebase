package com.example.emojo.data.model;

public class User {

    private String email;
    private String username;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public String toString() {
        return this.getUsername();
    }
}
