package com.tdusnewbie.root.hakoln.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.tdusnewbie.root.hakoln.Fragment.HomeFragment;
import com.tdusnewbie.root.hakoln.Fragment.InfoFragment;
import com.tdusnewbie.root.hakoln.Fragment.LibraryFragment;
import com.tdusnewbie.root.hakoln.Model.ArrayBaseInfoModel;
import com.tdusnewbie.root.hakoln.Model.BaseInfoModel;
import com.tdusnewbie.root.hakoln.R;
import com.tdusnewbie.root.hakoln.ViewPagerAdapter;

import java.io.InputStream;
import java.util.ArrayList;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //TextView txtState;
    private final String TAG = Home.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private HomeFragment homeFragment = new HomeFragment();
    private LibraryFragment libraryFragment = new LibraryFragment();
    private InfoFragment infoFragment = new InfoFragment();

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private ArrayList<BaseInfoModel> baseInfoModels;
    private ArrayList<BaseInfoModel> baseInfoNovelModels;
    private ProgressDialog progressDialog;
    private int threads = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(5);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Wait for second...");

        progressDialog.show();

        progressDialog.setCancelable(false);

        Intent intent = getIntent();

        ArrayBaseInfoModel arrayBaseInfoModel = (ArrayBaseInfoModel) intent.getSerializableExtra(("BASEINFOMODELS"));
        ArrayBaseInfoModel arrayBaseInfoNovelModel = (ArrayBaseInfoModel) intent.getSerializableExtra(("BASEINFONOVELMODELS"));
        Log.e(TAG, String.valueOf(arrayBaseInfoNovelModel.getBaseInfoModels().size()));

        baseInfoModels = arrayBaseInfoModel.getBaseInfoModels();

        ArrayList<String> listLink = new ArrayList<>();
        for (int i=0; i<baseInfoModels.size();i++){
            listLink.add(baseInfoModels.get(i).getUrlImage());
        }

        baseInfoNovelModels = arrayBaseInfoNovelModel.getBaseInfoModels();

        ArrayList<String> listLinkNovel = new ArrayList<>();
        for (int i=0; i<baseInfoNovelModels.size();i++){
            listLinkNovel.add(baseInfoNovelModels.get(i).getUrlImage());
        }

        DownloadImageNovelTask downloadImageNovelTask = new DownloadImageNovelTask();
        downloadImageNovelTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,listLinkNovel);

        DownloadImageTask downloadImageTask = new DownloadImageTask();
        downloadImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,listLink);




    }
    private void Mapping()
    {

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationMenu);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId())
        {
            case R.id.menuTrangChu:
                int position = tabLayout.getSelectedTabPosition();
                if(position !=0)
                    tabLayout.getTabAt(0).select();
                else
                    Toast.makeText(Home.this,"Bạn đang ở trang chủ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuXemNhieu:
                Toast.makeText(Home.this, "Xem nhiều Menu", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuTruyenTranh:
                Toast.makeText(Home.this, "Truyện tranh Menu", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuTruyenChu:
                Toast.makeText(Home.this, "Truyện chữ Menu", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuSearchTruyenTranh:
                Toast.makeText(Home.this, "Tìm kiếm Menu", Toast.LENGTH_SHORT).show();
                Intent intentManga = new Intent(Home.this, Search.class);
                startActivity(intentManga);
                break;
            case R.id.menuSearchTruyenChu:
                Toast.makeText(Home.this, "Tìm kiếm Menu", Toast.LENGTH_SHORT).show();
                Intent intentNovel = new Intent(Home.this, Search.class);
                startActivity(intentNovel);
                break;

        }

        return true;
    }

    public class DownloadImageTask extends AsyncTask<ArrayList<String>, Void, Void> {
        private Bitmap image;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(ArrayList<String>... urls) {
            ArrayList<String> urldisplay = urls[0];
            for (int i=0; i<urldisplay.size();i++) {
                try {
                    InputStream in = new java.net.URL(urldisplay.get(i)).openStream();
                    image = BitmapFactory.decodeStream(in);
                    baseInfoModels.get(i).setBitmap(image);
                } catch (Exception e) {
                    image = null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            threads--;
            Log.e("Thread","Ngoai");
            if (threads==0)
            {
                Log.e("Thread","Trong");
                progressDialog.dismiss();
                initialize();
            }
        }
    }

    public class DownloadImageNovelTask extends AsyncTask<ArrayList<String>, Void, Void> {
        private Bitmap image;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(ArrayList<String>... urls) {
            ArrayList<String> urldisplay = urls[0];
            for (int i=0; i<urldisplay.size();i++) {
                try {
                    InputStream in = new java.net.URL(urldisplay.get(i)).openStream();
                    image = BitmapFactory.decodeStream(in);
                    baseInfoNovelModels.get(i).setBitmap(image);
                    Log.e("Image",urldisplay.get(i));
                } catch (Exception e) {
                    image = null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            threads--;
            Log.e("Thread","Ngoai");
            if (threads==0)
            {
                Log.e("Thread","Trong");
                progressDialog.dismiss();
                initialize();
            }
        }
    }


    private synchronized void initialize() {
        homeFragment.setBaseInfoModels(baseInfoModels);
        homeFragment.setBaseInfoNovelModels(baseInfoNovelModels);
        Mapping();


        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);


        //receiveIntentMess();

        //infoFragment.setUserInfoPage(user);


        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(homeFragment, "Gần đây");
        adapter.addFragment(libraryFragment, "Đã lưu");
        adapter.addFragment(infoFragment,"Danh sách đọc");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}
