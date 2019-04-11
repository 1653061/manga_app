package com.tdusnewbie.root.hakoln.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tdusnewbie.root.hakoln.R;

public class ChapterContentFragment extends Fragment {

    private ImageView imageContent;
    private Bitmap bitmap;
    private View view;

    public ChapterContentFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chapter_content,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Mapping();

        imageContent.setImageBitmap(bitmap);


    }

    private void Mapping()
    {
        imageContent = (ImageView) view.findViewById(R.id.imageContent);
    }


    public Bitmap getBitmap() {

        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
