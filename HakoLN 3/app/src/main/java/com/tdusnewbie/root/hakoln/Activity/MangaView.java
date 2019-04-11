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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.tdusnewbie.root.hakoln.Model.BaseInfoModel;
import com.tdusnewbie.root.hakoln.Model.Manga;
import com.tdusnewbie.root.hakoln.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MangaView extends AppCompatActivity {

    TextView txtmangaView_Title, txtmangaView_LastUpdate, txtmangaView_Category, txtmangaView_Author;
    ImageView imageMangaView_Poster;
    ReadMoreTextView rmDescription;
    ListView listChapter;
    Manga manga = new Manga();
    ArrayList<String> chap;
    ProgressDialog progressDialog;
    BaseInfoModel baseInfoModel;


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


        baseInfoModel = (BaseInfoModel) getIntent().getSerializableExtra("MangaInfo");

        String url = baseInfoModel.getId();

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequestNovel = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                org.jsoup.nodes.Document document = Jsoup.parse(response);
                Document document1;
                Elements elements = document.select("time");
                String time = elements.get(0).text();
                String split = "[Cập nhật lúc: ";
                time = time.substring(split.length(), time.length()-1);
                manga.setLast_chapter_date(time);

                manga.setTitle(baseInfoModel.getTitle());

                elements = document.select("li[class=author row]");
                document1 = Jsoup.parse(elements.get(0).html());
                elements = document1.select("p[class=col-xs-8]");

                manga.setAuthor(elements.get(0).text());

                elements = document.select("div[class=detail-content]");
                document1 = Jsoup.parse(elements.get(0).html());
                elements = document1.select("p");

                Log.e("contetn", elements.get(0).text());
                String description = elements.get(0).text();
                description.replace("<br>", " ");
                manga.setDescription(description);

                elements = document.select("div[class=list-chapter]");
                document1 = Jsoup.parse(elements.get(0).html());
                elements = document1.select("a");

                ArrayList<String> chapterList = new ArrayList<>();
                for (int i=0; i<elements.size()-1;i++){
                    chapterList.add(elements.get(i).attr("href"));
                }
                manga.setChapterList(chapterList);

                manga.setCategories(baseInfoModel.getCategories());


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

    private void LoadData(Manga data) {


        txtmangaView_Title.setText(data.getTitle());
        txtmangaView_LastUpdate.setText(data.getLast_chapter_date());
        txtmangaView_Category.setText("");
        txtmangaView_Author.setText(manga.getAuthor());
        if (data.getImage() == null) {
            imageMangaView_Poster.setImageResource(R.drawable.default_image);
        } else
            imageMangaView_Poster.setImageBitmap(data.getImage());
        for (int i = 0; i < data.getCategories().size(); i++) {
            if (i + 1 == data.getCategories().size())
                txtmangaView_Category.append(data.getCategories().get(i));
            else
                txtmangaView_Category.append(data.getCategories().get(i) + ", ");
        }

        rmDescription.setText(data.getDescription());

        chap = new ArrayList<>();

        int a = data.getChapterList().size();
        for (int i = 0; i < data.getChapterList().size(); i++) {
            chap.add("Chapter " + String.valueOf(a--));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MangaView.this, android.R.layout.simple_list_item_1, chap);
        listChapter.setAdapter(adapter);
    }

    private void Mapping() {
        txtmangaView_Title = (TextView) findViewById(R.id.txtmangaView_Title);
        txtmangaView_LastUpdate = (TextView) findViewById(R.id.txtmangaView_LastUpdate);
        txtmangaView_Category = (TextView) findViewById(R.id.txtmangaView_Category);
        txtmangaView_Author = (TextView) findViewById(R.id.txtmangaView_Author);

        imageMangaView_Poster = (ImageView) findViewById(R.id.imageMangaView_Poster);
        rmDescription = (ReadMoreTextView) findViewById(R.id.rmDescription);
        listChapter = (ListView) findViewById(R.id.listChapter);
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
                manga.setImage(image);
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

    private void initialize() {
        setContentView(R.layout.activity_manga_view);
        Mapping();


        LoadData(manga);

        listChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idChap = manga.getChapterList().get(position);

                Intent intent = new Intent(MangaView.this, ChapterView.class);
                intent.putExtra("ChapterID",idChap);
                startActivity(intent);
            }
        });

    }

}
