package com.example.administrator.mycamera.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.administrator.mycamera.activity.GalleryListActivity;
import com.example.administrator.mycamera.fragment.GalleryTimeFragment;
import com.example.administrator.mycamera.fragment.GalleryListFragment;

public class GalleryPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 2;
    private GalleryTimeFragment mGalleryTimeFragment = null;
    //private GalleryListFragment mGalleryImageFragment= null;
    private GalleryListFragment mGalleryListFragment = null;

    public GalleryPagerAdapter(FragmentManager fm) {
        super(fm);
        mGalleryTimeFragment = new GalleryTimeFragment();
       // mGalleryImageFragment = new GalleryListFragment();
        mGalleryListFragment = new GalleryListFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (position){
            case GalleryListActivity.PAGE_ONE:
                fragment = mGalleryTimeFragment;
                break;
//            case GalleryListActivity.PAGE_TWO:
//                fragment = mGalleryImageFragment ;
//                bundle.putString("fileType","image");
//                fragment.setArguments(bundle);
//                break;
            case GalleryListActivity.PAGE_TWO:
                fragment = mGalleryListFragment;
                //bundle.putString("fileType","video");
               // fragment.setArguments(bundle);
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
