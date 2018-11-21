package com.example.administrator.mycamera.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.adapter.GalleryDetailAdapter;
import com.example.administrator.mycamera.utils.GalleryUtils;

public class GalleryDetailActivity extends FragmentActivity implements ViewPager.OnPageChangeListener,
        RadioGroup.OnCheckedChangeListener {
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;

    private RadioGroup rgDetail;
    private RadioButton rbTime;
    private RadioButton rbImage;
    private RadioButton rbVideo;
    private LinearLayout llDetail;

    private GalleryDetailAdapter mGalleryDetailAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GalleryUtils.setWindowStatusBarColor(this,R.color.blue_dark);
        setContentView(R.layout.activity_gallery_detail);

        initView();
        initData();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        rgDetail = (RadioGroup) findViewById(R.id.rg_detail);
        rgDetail.setOnCheckedChangeListener(this);
        rbTime = (RadioButton) findViewById(R.id.rb_time);
        rbImage = (RadioButton) findViewById(R.id.rb_image);
        rbVideo = (RadioButton) findViewById(R.id.rb_video);

        llDetail = (LinearLayout)findViewById(R.id.ll_detail);


    }

    private void initData() {

        mGalleryDetailAdapter = new GalleryDetailAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mGalleryDetailAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(this);

        rgDetail.check(R.id.rb_time);

        //setTopHeight();
    }

    private void setTopHeight(){

        int height = GalleryUtils.getStatusBarHeight(this);
        if (height>0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(0, height, 0, 0);
            llDetail.setLayoutParams(params);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            switch (mViewPager.getCurrentItem()) {
                case PAGE_ONE:
                    rbTime.setChecked(true);
                    break;
                case PAGE_TWO:
                    rbImage.setChecked(true);
                    break;
                case PAGE_THREE:
                    rbVideo.setChecked(true);
                    break;

            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {

            case R.id.rb_time:
                mViewPager.setCurrentItem(PAGE_ONE);
                break;

            case R.id.rb_image:
                mViewPager.setCurrentItem(PAGE_TWO);
                break;

            case R.id.rb_video:
                mViewPager.setCurrentItem(PAGE_THREE);
                break;

        }
    }
}
