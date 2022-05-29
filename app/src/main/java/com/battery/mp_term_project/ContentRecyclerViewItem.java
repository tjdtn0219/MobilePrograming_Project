package com.battery.mp_term_project;

import android.net.Uri;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ContentRecyclerViewItem {
    private final Content content;

    public ContentRecyclerViewItem(Content content)
    {
        this.content = content;
    }

    public String getKey() {
        return content.getKey();
    }

    public Uri getUser_img() {
        //#todo : 프로필 이미지 설정
        //return content.getUser().getProfileImage();
        return null;
    }

    public String getUser_name() {
        return content.getUser().getName();
    }

    public String getContent_text() {
        return content.getText();
    }

    public int getLikes() { return content.getLikes(); }

    public String getUser_id() {
        return content.getUser().getUid();
    }

    public long getTime() {
        return content.getTime();
    }

    public List<String> getImage_list()
    {
        return content.getImages();
    }
}
