package com.battery.mp_term_project;

import android.net.Uri;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ContentRecyclerViewItem {
    private Uri user_img;
    private String user_id;
    private String user_name;
    private String user_text;
    private long time;
    private int likes;
    private List<String> imageList = new ArrayList<>();

    public ContentRecyclerViewItem(Uri user_img, String user_id, String user_name, String user_text, long time, int likes) {
        this.user_img = user_img;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_text = user_text;
        this.time = time;
        this.likes = likes;
    }

    public ContentRecyclerViewItem(Uri user_img, String user_id, String user_name, String user_text, long time, int likes, List<String> imageList)
    {
        this(user_img, user_id, user_name, user_text, time, likes);
        this.imageList = imageList;
    }

    public Uri getUser_img() {
        return user_img;
    }

    public void setUser_img(Uri user_img) {
        this.user_img = user_img;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_text() {
        return user_text;
    }

    public void setUser_text(String user_text) {
        this.user_text = user_text;
    }

    public int getLikes() { return likes; }

    public void setLikes(int likes) { this.likes = likes; }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<String> getImage_list()
    {
        return imageList;
    }
}
