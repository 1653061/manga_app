package com.tdusnewbie.root.hakoln.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tdusnewbie.root.hakoln.Model.ArrayBaseInfoModel;
import com.tdusnewbie.root.hakoln.Model.BaseInfoModel;
import com.tdusnewbie.root.hakoln.R;
import com.tdusnewbie.root.hakoln.UpdatedListAdaper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Search extends AppCompatActivity {
    public final String TAG = Search.class.getSimpleName();

    private int threads = 5;
    private Boolean flag = false;
    private String url = "https://ln.hako.re/tim-kiem";
    private String manga_url = "";

    private ProgressDialog progressDialog;

    private ListView listSearch;
    private UpdatedListAdaper adaper;

    private ArrayList<BaseInfoModel> arrayManga = new ArrayList<>();
    private ArrayList<BaseInfoModel> arrayNovel;

    ArrayList<String> images1 = new ArrayList<>();
    ArrayList<String> images2 = new ArrayList<>();
    ArrayList<String> images3 = new ArrayList<>();
    ArrayList<String> images4 = new ArrayList<>();
    ArrayList<String> images5 = new ArrayList<>();
    ArrayList<Bitmap> bitmap1 = new ArrayList<>();
    ArrayList<Bitmap> bitmap2 = new ArrayList<>();
    ArrayList<Bitmap> bitmap3 = new ArrayList<>();
    ArrayList<Bitmap> bitmap4 = new ArrayList<>();
    ArrayList<Bitmap> bitmap5 = new ArrayList<>();


//    private RequestQueue requestQueue;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

//        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(5);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Wait for second...");

        initialize();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        MenuItem menuItem = menu.findItem(R.id.menuSearch);

        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() < 2) {
                    Toast.makeText(getApplicationContext(), "Input need larger than 2 character", Toast.LENGTH_SHORT).show();
                    return false;
                }

                flag = false;
                progressDialog.show();
                progressDialog.setCancelable(false);
                showSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });


        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (requestQueue != null){
//            requestQueue.cancelAll(this);
//        }
    }

    private void initialize() {
        setContentView(R.layout.activity_search);

        listSearch = findViewById(R.id.listSearch);

        Log.e("size_tmp", arrayManga.size() + "");
        adaper = new UpdatedListAdaper(Search.this, R.layout.row_list_update, arrayManga);
        listSearch.setAdapter(adaper);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    class LoadListener {
        private String html;

        @android.webkit.JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            Log.e("result", html.length() + "");

            // initialize();
            Document document = Jsoup.parse(html);
            Elements elements = document.select("a[class=tooltip tooltipstered]");
            for (int i = 0; i < elements.size(); i++) {
                Log.e("elements", elements.get(i).toString());
                BaseInfoModel baseInfoModel = new BaseInfoModel();
                String link = elements.get(i).attr("href");
                baseInfoModel.setId(link);
                String name = elements.get(i).text();
                baseInfoModel.setTitle(name);
                arrayManga.add(baseInfoModel);
            }

            elements = document.select("td[class=none table-cell-l]");
            Log.e("size", elements.size() + "");
            for (int i = 0; i < elements.size(); i++) {
                ArrayList<String> categories = new ArrayList<>();
                categories.add(elements.get(i).text());
                Log.e("value", elements.get(i).text());

                arrayManga.get(i).setCategories(categories);
            }

            Log.e("size", elements.size() + "");
            elements = document.select("img");

            ArrayList<String> image = new ArrayList<>();
            for (int i = 0; i < elements.size(); i++) {
                image.add(elements.get(i).attr("src"));
            }

            DownloadImageNovelTask downloadImageNovelTask = new DownloadImageNovelTask(-1);
            downloadImageNovelTask.execute(image);

        }

        public String getHtml() {
            return html;
        }
    }

    public class DownloadImageNovelTask extends AsyncTask<ArrayList<String>, Void, Void> {
        private int idThread = 0;
        private Bitmap image;

        DownloadImageNovelTask(int id) {
            idThread = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            threads = 5;
        }

        protected Void doInBackground(ArrayList<String>... urls) {
            ArrayList<String> urldisplay = urls[0];
            for (int i = 0; i < urldisplay.size(); i++) {
                try {
                    InputStream in = new java.net.URL(urldisplay.get(i)).openStream();
                    image = BitmapFactory.decodeStream(in);
                    switch (idThread) {
                        case -1:
                            arrayManga.get(i).setBitmap(image);
                            Log.e("Image", urldisplay.get(i));
                            break;
                        case 1:
                            bitmap1.add(image);
                            break;
                        case 2:
                            bitmap2.add(image);
                            break;
                        case 3:
                            bitmap3.add(image);
                            break;
                        case 4:
                            bitmap4.add(image);
                            break;
                        case 5:
                            bitmap5.add(image);
                            break;

                    }
                } catch (Exception e) {
                    image = null;
                }
                Log.e("download", "Threads+ " + String.valueOf(idThread));

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("result", "Threads+ " + String.valueOf(idThread));
            if (idThread == -1){
                progressDialog.dismiss();
                adaper = new UpdatedListAdaper(Search.this, R.layout.row_list_update, arrayManga);
                listSearch.setAdapter(adaper);
            } else {
                threads--;
                if (threads == 0) {
                    progressDialog.dismiss();
                    int index = 0;
                    Log.e("Size image", bitmap1.size() + "");
                    for (int i=0; i<bitmap1.size();i++){
                        arrayManga.get(index).setBitmap(bitmap1.get(i));
                        index++;
                    }
                    Log.e("Size image", bitmap2.size() + "");

                    for (int i=0; i<bitmap2.size();i++){
                        arrayManga.get(index).setBitmap(bitmap2.get(i));
                        index++;
                    }
                    Log.e("Size image", bitmap3.size() + "");

                    for (int i=0; i<bitmap3.size();i++){
                        arrayManga.get(index).setBitmap(bitmap3.get(i));
                        index++;
                    }
                    Log.e("Size image", bitmap4.size() + "");

                    for (int i=0; i<bitmap4.size();i++){
                        arrayManga.get(index).setBitmap(bitmap4.get(i));
                        index++;
                    }
                    Log.e("Size image", bitmap5.size() + "");

                    for (int i=0; i<bitmap5.size();i++){
                        arrayManga.get(index).setBitmap(bitmap5.get(i));
                        index++;
                    }
                    adaper = new UpdatedListAdaper(Search.this, R.layout.row_list_update, arrayManga);
                    listSearch.setAdapter(adaper);
                }
            }
        }
    }

    public class DownloadInfoData extends AsyncTask<String, Void, Void> {
        private Bitmap image;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            int page = 1;
            String url = "http://www.nettruyen.com/tim-truyen?keyword=" + strings[0] + "&page=" + String.valueOf(page);
            Log.e("URL_search", url);
            while (true) {
                org.jsoup.nodes.Document document;
                try {
                    document = Jsoup.connect(url).get();
                    Elements srcs = document.select("div[class=item]");

                    for (int i = 0; i < srcs.size(); i++) {
                        BaseInfoModel baseInfoModel = new BaseInfoModel();
                        Document document1 = Jsoup.parse(srcs.get(i).html());
                        Elements srcs1 = document1.select("a[class=jtip]");
                        baseInfoModel.setTitle(srcs1.get(0).text());
                        baseInfoModel.setId(srcs1.get(0).attr("href"));
                        srcs1 = document1.select("li[class=chapter clearfix]");
                        Document document2 = Jsoup.parse(srcs1.get(0).html());
                        srcs1 = document2.select("a");
                        String categorie = srcs1.get(0).text();
                        srcs1 = document2.select("i");
                        categorie += "\n" + srcs1.get(0).text();
                        Log.e("aaxxx", categorie);
                        ArrayList<String> categories = new ArrayList<>();
                        categories.add(categorie);
                        baseInfoModel.setCategories(categories);
                        srcs1 = document1.select("div[class=image]");
                        document1 = Jsoup.parse(srcs1.get(0).html());
                        srcs1 = document1.select("img");
                        baseInfoModel.setUrlImage(srcs1.get(0).attr("data-original"));

                        arrayManga.add(baseInfoModel);
                    }
                    page += 1;
                    url = "http://www.nettruyen.com/tim-truyen?keyword=" + strings[0] + "&page=" + String.valueOf(page);
                } catch (IOException e) {
                    Log.e("Finish", "Pagination ");
                    break;
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            images1 = new ArrayList<>();
            images2 = new ArrayList<>();
            images3 = new ArrayList<>();
            images4 = new ArrayList<>();
            images5 = new ArrayList<>();
            bitmap1 = new ArrayList<>();
            bitmap2 = new ArrayList<>();
            bitmap3 = new ArrayList<>();
            bitmap4 = new ArrayList<>();
            bitmap5 = new ArrayList<>();



            for (int i = 0; i < arrayManga.size(); i++) {
                if (i >= 0 && i < 8) {
                    images1.add(arrayManga.get(i).getUrlImage());
                }
                if (i >= 8 && i < 16) {
                    images2.add(arrayManga.get(i).getUrlImage());
                }
                if (i >= 16 && i < 24) {
                    images3.add(arrayManga.get(i).getUrlImage());
                }
                if (i >= 24 && i < 32) {
                    images4.add(arrayManga.get(i).getUrlImage());
                }
                if (i >= 32 && i < arrayManga.size()) {
                    images5.add(arrayManga.get(i).getUrlImage());
                }
            }
            DownloadImageNovelTask downloadImageNovelTask1 = new DownloadImageNovelTask(1);
            downloadImageNovelTask1.executeOnExecutor(THREAD_POOL_EXECUTOR, images1);
            DownloadImageNovelTask downloadImageNovelTask2 = new DownloadImageNovelTask(2);
            downloadImageNovelTask2.executeOnExecutor(THREAD_POOL_EXECUTOR, images2);
            DownloadImageNovelTask downloadImageNovelTask3 = new DownloadImageNovelTask(3);
            downloadImageNovelTask3.executeOnExecutor(THREAD_POOL_EXECUTOR, images3);
            DownloadImageNovelTask downloadImageNovelTask4 = new DownloadImageNovelTask(4);
            downloadImageNovelTask4.executeOnExecutor(THREAD_POOL_EXECUTOR, images4);
            DownloadImageNovelTask downloadImageNovelTask5 = new DownloadImageNovelTask(5);
            downloadImageNovelTask5.executeOnExecutor(THREAD_POOL_EXECUTOR, images5);

        }
    }

    private void showSearch(final String query) {
        arrayManga.clear();
        DownloadInfoData downloadInfoData = new DownloadInfoData();
        downloadInfoData.execute(query);
    }
}
