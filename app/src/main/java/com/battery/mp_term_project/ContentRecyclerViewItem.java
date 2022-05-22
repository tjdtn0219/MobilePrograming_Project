package com.battery.mp_term_project;

import android.net.Uri;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ContentRecyclerViewItem {
    private Uri user_img;
    private String user_name;
    private String user_text;
    private List<Uri> image_list;
    private int likes;

    public ContentRecyclerViewItem(Uri user_img, String user_name, String user_text, List<String> image_list, int likes) {
        this.user_img = user_img;
        this.user_name = user_name;
        this.user_text = user_text;
        this.image_list = new ArrayList<>();
        if (image_list != null) {
            for(String uri : image_list) {
                this.image_list.add(Uri.parse(uri));
            }
        }

        this.likes = likes;
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

    public List<Uri> getImage_list() {
        return image_list;
    }

    public void setImage_list(List<Uri> image_list) {
        this.image_list = image_list;
    }

    public int getLikes() { return likes; }

    public void setLikes(int likes) { this.likes = likes; }
}
