package com.example.jh.musicplayer.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinhui on 2017/6/8.
 * 邮箱: 1004260403@qq.com
 *
 * Fragment适配器
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }
    // 添加fragment
    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}

