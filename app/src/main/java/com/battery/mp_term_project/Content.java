package com.battery.mp_term_project;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Content {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String text;

    private String writer_id;

    private int likes;

    private List<Integer> comments_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getWriter_id() {
        return writer_id;
    }

    public void setWriter_id(String writer_id) {
        this.writer_id = writer_id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<Integer> getComments_id() {
        return comments_id;
    }

    public void setComments_id(List<Integer> comments_id) {
        this.comments_id = comments_id;
    }
}
