package com.tdusnewbie.root.hakoln.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    public final String TAG = SplashScreen.class.getSimpleName();

    //full manga
    private String url = "http://www.nettruyen.com/";
    private String novel_url = "https://ln.hako.re";

    private Map<Long, Integer> positions = new HashMap<>();
    private ArrayList<Long> timeUnixs = new ArrayList<>();
    private ArrayList<BaseInfoModel> baseInfoModels = new ArrayList<>();
    private ArrayList<BaseInfoModel> baseInfoNovelModels = new ArrayList<>();
    int threads = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("VolleyResponse", "response manga: " + response);
                ArrayList<String> arrayList = new ArrayList<>();
                org.jsoup.nodes.Document document = Jsoup.parse(response);
                Elements srcs = document.select("a[class=jtip]");

                for (int i = 0; i < 12; i++) {

                    arrayList.add(srcs.get(i).attr("href"));
                    Log.d("VolleyResponse", "href: " + arrayList.get(i));

                }

                InitialDataAsync initialDataAsync = new InitialDataAsync();
                initialDataAsync.execute(arrayList);
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

        StringRequest stringRequestNovel = new StringRequest(Request.Method.GET,
                novel_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<String> arrayList = new ArrayList<>();
                org.jsoup.nodes.Document document = Jsoup.parse(response);
                Elements srcs = document.select("div[class=landscape at-index]");
                document = Jsoup.parse(srcs.get(0).html());
                srcs = document.select("a");

                for (int i = 0; i < srcs.size(); i++) {
                    if ((i + 1) % 4 == 0) {
                        arrayList.add(srcs.get(i).attr("href"));
                        Log.d("VolleyResponse", "response: " + arrayList.get(arrayList.size() - 1));
                    }
                }

                InitialDataNovelAsync initialDataNovelAsync = new InitialDataNovelAsync();
                initialDataNovelAsync.execute(arrayList);
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

        requestQueue.add(stringRequest);
        requestQueue.add(stringRequestNovel);
    }


    private class InitialDataAsync extends AsyncTask<ArrayList<String>, BaseInfoModel, Void> {
        int count = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "Download Data");
        }

        @Override
        protected Void doInBackground(ArrayList<String>... arrayLists) {
            ArrayList<String> array = arrayLists[0];
            for (int i = 0; i < array.size(); i++) {

                try {
                    BaseInfoModel baseInfoModel = new BaseInfoModel();
                    org.jsoup.nodes.Document document = Jsoup.connect(array.get(i)).get();

                    Elements srcs = document.select("h1[class=title-detail]");
                    baseInfoModel.setTitle(srcs.get(0).text());

                    Log.e("a", baseInfoModel.getTitle());

                    srcs = document.select("img[alt=" + baseInfoModel.getTitle() + "]");

                    Log.e("image link", srcs.toString());

                    String image = srcs.get(0).attr("src");
                    baseInfoModel.setUrlImage(image);

                    srcs = document.select("li[class=kind row]");

                    document = Jsoup.parse(srcs.get(0).html());
                    srcs = document.select("p[class=col-xs-8]");
                    document = Jsoup.parse(srcs.get(0).html());
                    srcs = document.select("a");

                    ArrayList<String> categories = new ArrayList<>();
                    for (int j = 3; j < srcs.size() - 1; j++) {
                        categories.add(srcs.get(j).text());
                    }

                    baseInfoModel.setId(array.get(i));
                    baseInfoModel.setCategories(categories);
                    publishProgress(baseInfoModel);

                } catch (IOException e) {
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(BaseInfoModel... values) {
            super.onProgressUpdate(values);
            baseInfoModels.add(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e(TAG, "Download success");
            threads--;
            if (threads == 0) {
                ArrayBaseInfoModel arrayBaseInfoModel = new ArrayBaseInfoModel(baseInfoModels);
                ArrayBaseInfoModel arrayBaseInfoNovelModel = new ArrayBaseInfoModel(baseInfoNovelModels);
                Log.e(TAG, String.valueOf(arrayBaseInfoModel.getBaseInfoModels().size()));
                Intent intent = new Intent(SplashScreen.this, Home.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("BASEINFOMODELS", arrayBaseInfoModel);
                bundle.putSerializable("BASEINFONOVELMODELS", arrayBaseInfoNovelModel);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }
    }

    private class InitialDataNovelAsync extends AsyncTask<ArrayList<String>, BaseInfoModel, Void> {
        int count = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "Download Data");

        }

        @Override
        protected Void doInBackground(ArrayList<String>... arrayLists) {
            ArrayList<String> array = arrayLists[0];
            for (int i = 0; i < array.size(); i++) {

                try {
                    BaseInfoModel baseInfoModel = new BaseInfoModel();
                    org.jsoup.nodes.Document document = Jsoup.connect(array.get(i)).get();

                    Elements srcs = document.select("title");

                    baseInfoModel.setTitle(srcs.get(0).text());
                    Log.e("a", baseInfoModel.getTitle());

                    srcs = document.select("div[class=content img-in-ratio]");

                    Log.e("image link", srcs.toString());

                    String image = srcs.get(0).attr("style");
                    String regex = "background-image: url('";
                    image = image.substring(regex.length(), image.length() - 2);
                    Log.e("image link", image);
                    baseInfoModel.setUrlImage(image);

                    srcs = document.select("section[class=basic-section mobile-view mobile-toggle]");

                    document = Jsoup.parse(srcs.get(0).html());
                    srcs = document.select("a");

                    ArrayList<String> categories = new ArrayList<>();
                    for (int j = 3; j < srcs.size() - 1; j++) {
                        categories.add(srcs.get(j).text());
                    }
                    baseInfoModel.setId(array.get(i));
                    baseInfoModel.setCategories(categories);
                    publishProgress(baseInfoModel);

                } catch (IOException e) {
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(BaseInfoModel... values) {
            super.onProgressUpdate(values);
            baseInfoNovelModels.add(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e(TAG, "Download success");
            Log.e(TAG, baseInfoNovelModels.size() + "");

            threads--;
            if (threads == 0) {
                ArrayBaseInfoModel arrayBaseInfoModel = new ArrayBaseInfoModel(baseInfoModels);
                ArrayBaseInfoModel arrayBaseInfoNovelModel = new ArrayBaseInfoModel(baseInfoNovelModels);
                Log.e(TAG, String.valueOf(arrayBaseInfoModel.getBaseInfoModels().size()));
                Intent intent = new Intent(SplashScreen.this, Home.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("BASEINFOMODELS", arrayBaseInfoModel);
                bundle.putSerializable("BASEINFONOVELMODELS", arrayBaseInfoNovelModel);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }
    }
}
