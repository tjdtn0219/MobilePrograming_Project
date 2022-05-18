package com.battery.mp_term_project;

import android.app.Application;

public class GlobalVar extends Application {
    private User current_user;

    @Override
    public void onCreate(){
        super.onCreate();
        current_user = null;
    }
    @Override
    public void onTerminate(){
        super.onTerminate();
    }

    public User getCurrent_user() {
        return current_user;
    }

    public void setCurrent_user(User current_user) {
        this.current_user = current_user;
    }
}
