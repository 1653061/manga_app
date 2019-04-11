package com.tdusnewbie.root.hakoln.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tdusnewbie.root.hakoln.Model.BaseInfoModel;
import com.tdusnewbie.root.hakoln.Model.Vol;
import com.tdusnewbie.root.hakoln.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChapterNovelView extends AppCompatActivity {

    private TextView txtTitleVolume, txtTitleParent;
    private ImageView imagePoster;
    private ListView listChapter;
    private String novel_url;

    private Vol vol = new Vol();
    private BaseInfoModel baseInfoModel;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(5);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Wait for second...");
        progressDialog.show();
        progressDialog.setCancelable(false);


        baseInfoModel = (BaseInfoModel) getIntent().getSerializableExtra("VolumeInfo");
        novel_url = baseInfoModel.getId();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequestNovel = new StringRequest(Request.Method.GET,
                novel_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                org.jsoup.nodes.Document document = Jsoup.parse(response);
                Elements srcs = document.select("h2[class=series-name]");
                Log.e("title", srcs.get(0).toString());

                Document document1 = Jsoup.parse(srcs.get(0).html());
                srcs = document1.select("a");


                vol.setTitle(srcs.get(0).text());
                vol.setNovelName(srcs.get(1).text());
                vol.setNovelLink(srcs.get(1).attr("href"));

                srcs = document.select("div[class=chapter-name]");

                ArrayList<String> chapterTitle = new ArrayList<>();
                ArrayList<String> chapterList = new ArrayList<>();

                for (int i=0; i<srcs.size(); i++){
                    document1 = Jsoup.parse(srcs.get(i).html());
                    Elements srcs_tmp = document1.select("a");
                    if(!srcs_tmp.get(0).text().matches("Minh họa"))
                    {
                        chapterTitle.add(srcs_tmp.get(0).text());
                        chapterList.add(srcs_tmp.get(0).attr("href"));
                        Log.e("linkzzzzxzx", srcs_tmp.get(0).attr("href"));
                    }

                }
                vol.setChapterList(chapterList);
                vol.setChapterTitle(chapterTitle);

                DownloadImageTask downloadImageTask = new DownloadImageTask();
                downloadImageTask.execute(baseInfoModel.getUrlImage());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR TAG", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("X-RapidAPI-Key", "ec4e50cc6fmsh5c33f530421de1fp1ea9eajsn55fb23bc71d2");
                params.put("Content-Type", "application/json");
                return params;
            }


        };

        requestQueue.add(stringRequestNovel);


    }

    private void Mapping()
    {
        txtTitleVolume = findViewById(R.id.txtTitleVolume);
        txtTitleParent = findViewById(R.id.txtTitleParent);

        imagePoster = findViewById(R.id.imagePoster);
        listChapter = findViewById(R.id.listChapter);

    }

    private void LoadData()
    {
        imagePoster.setImageBitmap(vol.getImage());
        txtTitleVolume.setText(vol.getTitle());
        txtTitleParent.setText(vol.getNovelName());


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vol.getChapterTitle());
        listChapter.setAdapter(adapter);
    }

    private void initialize()
    {
        setContentView(R.layout.activity_chapter_novel_view);

        Mapping();

        LoadData();

        listChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChapterNovelView.this,ReadNovelView.class);
                startActivity(intent);
            }
        });
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Void> {
        private Bitmap image;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String urldisplay = strings[0];
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                image = BitmapFactory.decodeStream(in);
                vol.setImage(image);
            } catch (Exception e) {
                image = null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            initialize();
        }
    }
}
