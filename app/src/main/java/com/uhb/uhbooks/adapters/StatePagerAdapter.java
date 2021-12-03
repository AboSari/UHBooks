package com.uhb.uhbooks.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class StatePagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList;

    public StatePagerAdapter(@NonNull FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragmentList = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
