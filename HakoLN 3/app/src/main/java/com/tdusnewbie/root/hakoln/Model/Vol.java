package com.tdusnewbie.root.hakoln.Model;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Vol {
    private String title;
    private String novelName;
    private ArrayList<String> chapterList;
    private ArrayList<String> chapterTitle;
    private Bitmap image;
    private String novelLink;

    public Vol() {
    }

    public Vol(String title, String novelName, ArrayList<String> chapterList, ArrayList<String> chapterTitle, Bitmap image) {
        this.title = title;
        this.novelName = novelName;
        this.chapterList = chapterList;
        this.chapterTitle = chapterTitle;
        this.image = image;
    }

    public ArrayList<String> getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(ArrayList<String> chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNovelName() {
        return novelName;
    }

    public void setNovelName(String novelName) {
        this.novelName = novelName;
    }

    public ArrayList<String> getChapterList() {
        return chapterList;
    }

    public void setChapterList(ArrayList<String> chapterList) {
        this.chapterList = chapterList;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getNovelLink() {
        return novelLink;
    }

    public void setNovelLink(String novelLink) {
        this.novelLink = novelLink;
    }
}
