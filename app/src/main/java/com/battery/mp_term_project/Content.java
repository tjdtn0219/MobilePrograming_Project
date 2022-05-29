package com.battery.mp_term_project;

import java.util.ArrayList;
import java.util.List;

public class Content {
    private String key;
    private User user;
    private String text;
    private List<String> images = new ArrayList<>();
    private long time;
    private int likes;

    public Content(){
    }

    public Content(String key, String text, List<String> images, long time, User user) {
        this.user = user;
        this.text = text;
        this.images = images;
        this.time = time;
        this.likes = 0;
    }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
