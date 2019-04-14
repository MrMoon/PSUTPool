package com.psut.pool.Models;

public class Offers {
    private String title;
    private int imageView;

    public Offers(String title, int imageView) {
        this.title = title;
        this.imageView = imageView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }
}
