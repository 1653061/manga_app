package com.tdusnewbie.root.hakoln;

import android.app.ProgressDialog;
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
import com.tdusnewbie.root.hakoln.Activity.Home;
import com.tdusnewbie.root.hakoln.Model.ArrayBaseInfoModel;
import com.tdusnewbie.root.hakoln.Model.BaseInfoModel;

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
    private String url = "https://www.mangaeden.com/api/list/0";
    private String novel_url = "https://ln.hako.re";

    private String image_url = "https://cdn.mangaeden.com/mangasimg/";
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

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("VolleyResponse", "response: " + response);
                try {
                    JSONArray jsonArray = response.getJSONArray("manga");
                    InitialDataAsync initialDataAsync = new InitialDataAsync();
                    initialDataAsync.execute(jsonArray);
                    Log.e("Success", "success");
                } catch (JSONException e) {
                }
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

                for (int i=0; i<srcs.size(); i++){
                    if ((i+1) % 4 == 0) {
                        arrayList.add(srcs.get(i).attr("href"));
                        Log.d("VolleyResponse", "response: " + arrayList.get(arrayList.size()-1));
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


    private class InitialDataAsync extends AsyncTask<JSONArray, BaseInfoModel, Void> {
        int count = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "Download Data");
        }

        @Override
        protected Void doInBackground(JSONArray... jsonArrays) {
            JSONArray jsonArray = jsonArrays[0];
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    timeUnixs.add(jsonObject.getLong("ld"));
                    positions.put(jsonObject.getLong("ld"), i);
                } catch (JSONException e) {
                    count++;
                    Log.e(TAG, e.getMessage());
                }
            }

            Log.e(TAG, String.valueOf(count));

            Collections.sort(timeUnixs, Collections.<Long>reverseOrder());

            for (int i = 0; i < 15; i++) {
                if (positions.containsKey(timeUnixs.get(i))) {
                    int index = positions.get(timeUnixs.get(i));
                    BaseInfoModel baseInfoModel = new BaseInfoModel();
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(index);
                        baseInfoModel.setTitle(jsonObject.getString("t"));
                        JSONArray jsonCategories = jsonObject.getJSONArray("c");
                        ArrayList<String> categories = new ArrayList<>();
                        for (int k = 0; k < jsonCategories.length(); k++)
                            categories.add(jsonCategories.getString(k));
                        baseInfoModel.setCategories(categories);

                        String imageLink = image_url + jsonObject.getString("im");
                        baseInfoModel.setUrlImage(imageLink);

                        baseInfoModel.setId(jsonObject.getString("i"));
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());

                    }
                    publishProgress(baseInfoModel);
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
            if(threads == 0)
            {
                ArrayBaseInfoModel arrayBaseInfoModel = new ArrayBaseInfoModel(baseInfoModels);
                ArrayBaseInfoModel arrayBaseInfoNovelModel = new ArrayBaseInfoModel(baseInfoNovelModels);
                Log.e(TAG , String.valueOf(arrayBaseInfoModel.getBaseInfoModels().size()));
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
                    image = image.substring(regex.length(), image.length()-2);
                    Log.e("image link", image);
                    baseInfoModel.setUrlImage(image);

                    srcs = document.select("section[class=basic-section mobile-view mobile-toggle]");

                    document = Jsoup.parse(srcs.get(0).html());
                    srcs = document.select("a");

                    ArrayList<String> categories = new ArrayList<>();
                    for (int j = 3; j < srcs.size()-1; j++) {
                        categories.add(srcs.get(j).text());
                    }
                    baseInfoModel.setId(array.get(i));
                    baseInfoModel.setCategories(categories);
                    publishProgress(baseInfoModel);

                }catch (IOException e){}
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
            if(threads == 0)
            {
                ArrayBaseInfoModel arrayBaseInfoModel = new ArrayBaseInfoModel(baseInfoModels);
                ArrayBaseInfoModel arrayBaseInfoNovelModel = new ArrayBaseInfoModel(baseInfoNovelModels);
                Log.e(TAG , String.valueOf(arrayBaseInfoModel.getBaseInfoModels().size()));
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
