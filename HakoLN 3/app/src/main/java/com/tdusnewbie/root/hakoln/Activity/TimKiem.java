package com.tdusnewbie.root.hakoln.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.tdusnewbie.root.hakoln.R;

import java.util.ArrayList;

public class TimKiem extends AppCompatActivity {
    ListView listTimKiem;
    ArrayAdapter<String> adaper;
    ArrayList<String> arrayTimKiem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem);

        listTimKiem = findViewById(R.id.listTimKiem);

        arrayTimKiem = new ArrayList<>();
        arrayTimKiem.add("Ba ria");
        arrayTimKiem.add("Vung tau");
        arrayTimKiem.add("Long Dien");
        arrayTimKiem.add("Chau Duc");
        arrayTimKiem.add("Xuyen Moc");
        arrayTimKiem.add("Dat Do");
        arrayTimKiem.add("Tan Thanh");

        adaper = new ArrayAdapter<String>(TimKiem.this,android.R.layout.simple_list_item_1,arrayTimKiem);
        listTimKiem.setAdapter(adaper);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);

        MenuItem menuItem = menu.findItem(R.id.menuTimkiem);

        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adaper.getFilter().filter(newText);
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

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
