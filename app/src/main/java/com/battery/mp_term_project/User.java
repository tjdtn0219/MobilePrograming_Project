package com.battery.mp_term_project;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    private String uid;
    private String name;
    private List<Integer> contents;
    private String profileText;
    private String profileImage;

    public User(@NonNull String uid, String name, List<Integer> contents, String profileText, String profileImage) {
        this.uid = uid;
        this.name = name;
        this.contents = contents;
        this.profileText = profileText;
        this.profileImage = profileImage;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getContents() {
        return contents;
    }

    public void setContents(List<Integer> contents) {
        this.contents = contents;
    }

    public String getProfileText() {
        return profileText;
    }

    public void setProfileText(String profileText) {
        this.profileText = profileText;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
