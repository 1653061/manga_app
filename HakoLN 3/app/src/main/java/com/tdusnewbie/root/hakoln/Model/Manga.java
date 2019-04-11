package com.tdusnewbie.root.hakoln.Model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class Manga implements Serializable {
    private String title;
    private String last_chapter_date;
    private String author;
    private String description;
    private ArrayList<String> categories;
    private ArrayList<String> chapterList;
    private Bitmap image;

    public Manga(){
    }


    public ArrayList<String> getChapterList() {
        return chapterList;
    }

    public void setChapterList(ArrayList<String> chapterList) {
        this.chapterList = chapterList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLast_chapter_date() {
        return last_chapter_date;
    }

    public void setLast_chapter_date(String last_chapter_date) {
        this.last_chapter_date = last_chapter_date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
