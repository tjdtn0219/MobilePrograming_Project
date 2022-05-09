package com.battery.mp_term_project;

import android.net.Uri;
import android.widget.ImageView;

public class ContentRecyclerViewItem {
    private Uri user_img;
    private String user_name;
    private String user_text;
    private Uri img1;
    private Uri img2;
    private Uri img3;

    public ContentRecyclerViewItem(Uri user_img, String user_name, String user_text, Uri img1, Uri img2, Uri img3){
        this.user_img = user_img;
        this.user_name = user_name;
        this.user_text = user_text;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;

    }


    public Uri getUser_img() {
        return user_img;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_text() {
        return user_text;
    }

    public Uri getImg1() {
        return img1;
    }

    public Uri getImg2() {
        return img2;
    }

    public Uri getImg3() {
        return img3;
    }
}
