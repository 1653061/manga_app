package com.tdusnewbie.root.hakoln;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tdusnewbie.root.hakoln.Model.Chapter;

import java.util.ArrayList;

public class PageChapterAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Bitmap> pageList;


    public PageChapterAdapter(Context context, int layout, ArrayList<Bitmap> pageList) {
        this.context = context;
        this.layout = layout;
        this.pageList = pageList;
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder
    {
        ImageView imagePage;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            viewHolder.imagePage = (ImageView) convertView.findViewById(R.id.imagePage);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Bitmap page = pageList.get(position);

        if(page == null)
            viewHolder.imagePage.setImageResource(R.drawable.default_image);
        else
            viewHolder.imagePage.setImageBitmap(page);

        return convertView;
    }
}
