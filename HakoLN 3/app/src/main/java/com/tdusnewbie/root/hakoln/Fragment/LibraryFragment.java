package com.tdusnewbie.root.hakoln.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tdusnewbie.root.hakoln.Activity.MangaView;
import com.tdusnewbie.root.hakoln.Model.Manga;
import com.tdusnewbie.root.hakoln.MangaListAdapter;
import com.tdusnewbie.root.hakoln.R;

import java.util.ArrayList;


public class LibraryFragment extends Fragment {
    View view;

    GridView gridManga;
    MangaListAdapter adapter;
    ArrayList<Manga> arrayManga;
    Intent libraryIntent;

    public LibraryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.library_fragment,container,false);

        gridManga = (GridView) view.findViewById(R.id.gridManga);
        arrayManga = new ArrayList<>();

        adapter = new MangaListAdapter(getContext(),R.layout.row_layout_library,arrayManga);

        gridManga.setAdapter(adapter);

        gridManga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Manga temp = arrayManga.get(position);
                libraryIntent = new Intent(getContext(),MangaView.class);

                libraryIntent.putExtra("idImage",temp);
                getContext().startActivity(libraryIntent);

            }
        });

        return  view;
    }
}
