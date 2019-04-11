package com.tdusnewbie.root.hakoln;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tdusnewbie.root.hakoln.Model.Manga;

import java.util.ArrayList;

public class MangaListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Manga> mangaList;

    public MangaListAdapter(Context context, int layout, ArrayList<Manga> mangaList) {
        this.context = context;
        this.layout = layout;
        this.mangaList = mangaList;
    }

    @Override
    public int getCount() {
        return mangaList.size();
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
        ImageView imagePoster;
        TextView txtMangaTitle, txtLastUpdate, txtCurrentChapter;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);

            viewHolder.imagePoster = (ImageView) convertView.findViewById(R.id.imagePoster);
            viewHolder.txtMangaTitle = (TextView) convertView.findViewById(R.id.txtMangaTitle);
            viewHolder.txtLastUpdate = (TextView) convertView.findViewById(R.id.txtLastUpdate);
            viewHolder.txtCurrentChapter = (TextView) convertView.findViewById(R.id.txtCurrentChapter);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Manga mangaInfo = mangaList.get(position);

        viewHolder.imagePoster.setImageBitmap(mangaInfo.getImage());
        viewHolder.txtMangaTitle.setText(mangaInfo.getTitle());
        viewHolder.txtLastUpdate.setText(mangaInfo.getLast_chapter_date());

        return convertView;
    }
}
