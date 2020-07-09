package com.example.emojo.data.model;

public class User {

    private String email;

    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public String toString() {
        return this.getEmail();
    }
}
