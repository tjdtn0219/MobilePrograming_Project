package com.battery.mp_term_project;

import android.widget.ImageView;

public class ContentRecyclerViewItem {
    private int user_img;
    private String user_name;
    private String user_text;
    private int img1;
    private int img2;
    private int img3;

    public ContentRecyclerViewItem(int user_img, String user_name, String user_text, int img1, int img2, int img3){
        this.user_img = user_img;
        this.user_name = user_name;
        this.user_text = user_text;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;

    }


    public int getUser_img() {
        return user_img;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_text() {
        return user_text;
    }

    public int getImg1() {
        return img1;
    }

    public int getImg2() {
        return img2;
    }

    public int getImg3() {
        return img3;
    }
}
