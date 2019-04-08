package com.psut.pool.Models;

import com.psut.pool.R;

public class Notifications {

    //Global Variables and Objects:
    private int imgDrawable = R.drawable.ic_notifications_black_24dp;
    private String txt;

    public Notifications(String txt) {
        this.txt = txt;
    }

    public int getImgDrawable() {
        return imgDrawable;
    }

    public void setImgDrawable(int imgDrawable) {
        this.imgDrawable = imgDrawable;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
