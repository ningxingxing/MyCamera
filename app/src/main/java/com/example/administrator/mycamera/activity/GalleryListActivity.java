package com.example.administrator.mycamera.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.adapter.GalleryPagerAdapter;
import com.example.administrator.mycamera.utils.GalleryUtils;

public class GalleryListActivity extends FragmentActivity implements ViewPager.OnPageChangeListener,
        RadioGroup.OnCheckedChangeListener {
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
  //  public static final int PAGE_THREE = 2;

    private RadioGroup rgList;
    private RadioButton rbTime;
    private RadioButton rbImage;
   // private RadioButton rbVideo;
    private LinearLayout llList;

    private GalleryPagerAdapter mGalleryPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GalleryUtils.setWindowStatusBarColor(this,R.color.blue_dark);
        setContentView(R.layout.activity_gallery_list);

        initView();
        initData();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        rgList = (RadioGroup) findViewById(R.id.rg_list);
        rgList.setOnCheckedChangeListener(this);
        rbTime = (RadioButton) findViewById(R.id.rb_time);
        rbImage = (RadioButton) findViewById(R.id.rb_image);
       // rbVideo = (RadioButton) findViewById(R.id.rb_video);

        llList = (LinearLayout)findViewById(R.id.ll_list);


    }

    private void initData() {

        mGalleryPagerAdapter = new GalleryPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mGalleryPagerAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(this);

        rgList.check(R.id.rb_time);

        //setTopHeight();
    }

    private void setTopHeight(){

        int height = GalleryUtils.getStatusBarHeight(this);
        if (height>0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(0, height, 0, 0);
            llList.setLayoutParams(params);
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
//                case PAGE_THREE:
//                    rbVideo.setChecked(true);
//                    break;

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

//            case R.id.rb_video:
//                mViewPager.setCurrentItem(PAGE_THREE);
//                break;

        }
    }
}
