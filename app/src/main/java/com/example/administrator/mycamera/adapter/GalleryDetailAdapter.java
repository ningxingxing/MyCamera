package com.example.administrator.mycamera.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.administrator.mycamera.activity.GalleryDetailActivity;
import com.example.administrator.mycamera.fragment.GalleryImageFragment;
import com.example.administrator.mycamera.fragment.GalleryTimeFragment;
import com.example.administrator.mycamera.fragment.GalleryVideoFragment;

public class GalleryDetailAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 3;
    private GalleryTimeFragment mGalleryTimeFragment = null;
    private GalleryImageFragment mGalleryImageFragment= null;
    private GalleryVideoFragment mGalleryVideoFragment = null;

    public GalleryDetailAdapter(FragmentManager fm) {
        super(fm);
        mGalleryTimeFragment = new GalleryTimeFragment();
        mGalleryImageFragment = new GalleryImageFragment();
        mGalleryVideoFragment = new GalleryVideoFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case GalleryDetailActivity.PAGE_ONE:
                fragment = mGalleryTimeFragment;
                break;
            case GalleryDetailActivity.PAGE_TWO:
                fragment = mGalleryImageFragment;
                break;
            case GalleryDetailActivity.PAGE_THREE:
                fragment = mGalleryVideoFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
