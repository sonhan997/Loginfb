package com.example.connectfb;

public class User {
    public String username;
    public String email;
    public String password, cfpassword;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String password, String cfpassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.cfpassword = cfpassword;
    }
}
