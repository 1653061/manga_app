package com.tdusnewbie.root.hakoln.Model;

import java.util.ArrayList;

public class Chapter {
    private String nameChapter;
    private ArrayList<Integer> pageList;

    public Chapter() {}

    public String getNameChapter() {
        return nameChapter;
    }

    public void setNameChapter(String nameChapter) {
        this.nameChapter = nameChapter;
    }

    public ArrayList<Integer> getPageList() {
        return pageList;
    }

    public void setPageList(ArrayList<Integer> pageList) {
        this.pageList = pageList;
    }
}
