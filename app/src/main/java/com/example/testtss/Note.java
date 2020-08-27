package com.example.testtss;

import android.graphics.Bitmap;

public class Note {
    private int id;
    private String avatar;
    private String email;
    private String name;

    public Note(int id, String avatar, String email, String name) {
        this.id = id;
        this.avatar = avatar;
        this.email = email;
        this.name = name;
    }

    public Note(String avatar, String email, String name) {
        this.avatar = avatar;
        this.email = email;
        this.name = name;
    }

    public String getAvatar() {return avatar;}

    public String getEmail() {return email;}

    public String getName() {return name;}

    public void setAvatar(String avatar) {this.avatar = avatar;}
}
