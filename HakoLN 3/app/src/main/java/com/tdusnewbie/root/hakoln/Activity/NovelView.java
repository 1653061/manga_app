package com.tdusnewbie.root.hakoln.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.tdusnewbie.root.hakoln.R;

public class NovelView extends AppCompatActivity {

    private TextView txtTitle, txtAuthor, txtCategory, txtStatus;
    private ReadMoreTextView rmDescription;
    private ImageView imagePoster;
    private ExpandableListView listVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_view);


    }

    private void Mapping()
    {
        txtTitle = findViewById(R.id.txtTitle);
        txtAuthor = findViewById(R.id.txtAuthor);
        txtCategory = findViewById(R.id.txtCategory);
        txtStatus = findViewById(R.id.txtStatus);

        rmDescription = findViewById(R.id.rmDescription);
        imagePoster = findViewById(R.id.imagePoster);
        listVolume = findViewById(R.id.listVolume);
    }


}
