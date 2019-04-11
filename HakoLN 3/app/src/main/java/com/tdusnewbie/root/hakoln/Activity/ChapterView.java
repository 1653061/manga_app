package com.tdusnewbie.root.hakoln.Activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tdusnewbie.root.hakoln.Fragment.ChapterContentFragment;
import com.tdusnewbie.root.hakoln.R;
import com.tdusnewbie.root.hakoln.ViewPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChapterView extends AppCompatActivity {

    private Toolbar barChangePage;
    private ImageButton nextChapter, previousChapter;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private int count = 0;
    double xDown, xUp;
    private ArrayList<Bitmap> arrayPage = new ArrayList<>();

    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();


        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(5);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Wait for second...");
        progressDialog.show();
        progressDialog.setCancelable(false);


        String id = getIntent().getStringExtra("ChapterID");
        String url = id;

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequestNovel = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                org.jsoup.nodes.Document document = Jsoup.parse(response);
                Document document1;
                Elements elements = document.select("div[class=reading-detail box_doc]");
                document = Jsoup.parse(elements.get(0).html());
                elements = document.select("div[class=page-chapter]");

                ArrayList<String> images = new ArrayList<>();
                for (int i=0; i<elements.size();i++){
                    count++;
                    document1 = Jsoup.parse(elements.get(i).html());
                    Elements elements1 = document1.select("img");
                    images.add(elements1.get(0).attr("src"));
                    Log.e("imagesx", images.get(i));
                }


                setViewPager();
                DownloadImageTask downloadImageTask = new DownloadImageTask();
                downloadImageTask.execute(images);

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

    private void setViewPager()
    {
        for(int i = 0; i <= count; i++)
        {
            ChapterContentFragment page = new ChapterContentFragment();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.default_image);
            page.setBitmap(bitmap);
            adapter.addFragment(page,"Page " + i);
        }
//        adapter.notifyDataSetChanged();

        viewPager.setAdapter(adapter);

    }

    public class DownloadImageTask extends AsyncTask<ArrayList<String>, Integer, Void> {
        private Bitmap image;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.dismiss();

        }


        @Override
        protected Void doInBackground(ArrayList<String>... arrayLists) {
            ArrayList<String> urldisplay = arrayLists[0];
            for (int i = 0; i < urldisplay.size(); i++) {
                try {
                    Log.e("ThreadDown",String.valueOf(i));
                    InputStream in = new java.net.URL(urldisplay.get(i)).openStream();
                    image = BitmapFactory.decodeStream(in);
                    arrayPage.add(image);

                    publishProgress(i);
                } catch (Exception e) {
                    image = null;
                }
            }

            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ChapterContentFragment chapterContentFragment;
            chapterContentFragment = (ChapterContentFragment) adapter.getItem(values[0]);
            chapterContentFragment.setBitmap(arrayPage.get(values[0]));

            //viewPager.setAdapter(adapter);
            //updateView(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

//    private void updateView(int index)
//    {
//
//        View v = listPageChapter.getChildAt(index -
//                listPageChapter.getFirstVisiblePosition()+ listPageChapter.getHeaderViewsCount());
//
//        if(v == null)
//            return;
//
//        Log.e("UpdateView",String.valueOf(index));
//
//        ImageView imageView =  v.findViewById(R.id.imagePage);
//        imageView.setImageBitmap(arrayPage.get(index));
//    }



    private void Mapping() {
        barChangePage = (Toolbar) findViewById(R.id.barChangePage);
        nextChapter = (ImageButton) findViewById(R.id.nextChapter);
        previousChapter = (ImageButton) findViewById(R.id.previosChapter);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

    }

    private void initialize() {
        setContentView(R.layout.activity_chapter_view);

        Mapping();

        setSupportActionBar(barChangePage);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    Log.d("Gesture","Down");
                    xDown = event.getX();
                }
                if(event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    Log.d("Gesture", "Move");
                }
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    xUp = event.getX();
                    double distance = Math.abs(xDown-xUp);
                    Log.d("Gesture","Distance: " + distance);
                    if(distance < 20) {
                        if (barChangePage.isShown())
                            barChangePage.setVisibility(View.GONE);
                        else
                            barChangePage.setVisibility(View.VISIBLE);
                    }
                    //return true;
                }

                return false;
            }
        });

    }

}
