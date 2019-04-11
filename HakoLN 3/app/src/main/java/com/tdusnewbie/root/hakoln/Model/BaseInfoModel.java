package com.tdusnewbie.root.hakoln.Model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;


public class BaseInfoModel implements Serializable {
    private String id;
    private String urlImage;
    private transient Bitmap bitmap;
    private String title;
    private ArrayList<String> categories;

    public BaseInfoModel() {
        categories = new ArrayList<>();
    }

    public BaseInfoModel(String id, String urlImage, Bitmap bitmap, String title, ArrayList<String> categories) {
        this.id = id;
        this.urlImage = urlImage;
        this.bitmap = bitmap;
        this.title = title;
        this.categories = categories;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
