package com.tdusnewbie.root.hakoln;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragmentsList = new ArrayList<>();
    ArrayList<String> fragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentsList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentTitleList.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return  fragmentTitleList.get(position);
    }

    public void addFragment( Fragment fm, String title)
    {
        fragmentTitleList.add(title);
        fragmentsList.add(fm);
    }

    @Override
    public void startUpdate(@NonNull ViewGroup container) {
        super.startUpdate(container);
    }
}
