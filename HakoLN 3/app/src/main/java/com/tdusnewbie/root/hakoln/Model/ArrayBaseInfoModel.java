package com.tdusnewbie.root.hakoln.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ArrayBaseInfoModel implements Serializable {
    private ArrayList<BaseInfoModel> baseInfoModels;

    public ArrayBaseInfoModel(ArrayList<BaseInfoModel> baseInfoModels){
        this.baseInfoModels = new ArrayList<>(baseInfoModels);
    }

    public ArrayList<BaseInfoModel> getBaseInfoModels() {
        return this.baseInfoModels;
    }
}
