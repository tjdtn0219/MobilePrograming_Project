package com.battery.mp_term_project;

import android.app.Application;

public class GlobalVar extends Application {
    private String uid;

    @Override
    public void onCreate(){
        super.onCreate();
        uid = "";
    }
    @Override
    public void onTerminate(){
        super.onTerminate();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
