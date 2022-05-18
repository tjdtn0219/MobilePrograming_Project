package com.battery.mp_term_project;

import java.util.List;

public class Content {
    private int cid;
    private User user;
    private String text;
    private List<String> images;
    private long time;
//    private String writer_id;
    private int likes;

    public Content(){
    }

    public Content(String text, List<String> images, long time, User user) {
        this.user = user;
        this.text = text;
        this.images = images;
        this.time = time;
        this.likes = 0;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
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
