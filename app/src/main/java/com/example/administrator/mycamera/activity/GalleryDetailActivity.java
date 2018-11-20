package com.example.administrator.mycamera.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.adapter.GalleryDetailAdapter;

public class GalleryDetailActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;

    private GalleryDetailAdapter mGalleryDetailAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery_detail);

        initView();
        initData();
    }

    private void initView() {
        mViewPager = (ViewPager)findViewById(R.id.view_pager);
    }

    private void initData(){

        mGalleryDetailAdapter = new GalleryDetailAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mGalleryDetailAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        switch (checkedId) {
//
//            case R.id.rb_play_music:
//                mViewPager.setCurrentItem(PAGE_ONE);
//                break;
//
//            case R.id.rb_special_effects:
//                mViewPager.setCurrentItem(PAGE_TWO);
//                break;
//
//            case R.id.rb_lyric_show:
//                mViewPager.setCurrentItem(PAGE_THREE);
//                break;
//
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            switch (mViewPager.getCurrentItem()) {
                case PAGE_ONE:
                  //  rbPlayMusic.setChecked(true);
                    break;
                case PAGE_TWO:
                   // rbSpecialEffects.setChecked(true);
                    break;
                case PAGE_THREE:
                   // rbLyricShow.setChecked(true);
                    break;

            }
        }
    }
}
