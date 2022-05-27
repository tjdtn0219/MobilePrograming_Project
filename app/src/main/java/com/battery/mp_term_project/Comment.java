package com.battery.mp_term_project;

public class Comment {
    private User user;
    private String text;
    private long time;
    private String content_id;

    public Comment() {}

    public Comment(User user, String text, long time, String content_id)
    {
        this.user = user;
        this.text = text;
        this.time = time;
        this.content_id = content_id;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public long getTime() {
        return time;
    }

    public String getContent_id() {
        return content_id;
    }
}
