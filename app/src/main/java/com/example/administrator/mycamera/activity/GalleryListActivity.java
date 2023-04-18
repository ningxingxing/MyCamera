package com.example.administrator.mycamera.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

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

        mViewPager.setPageTransformer(true,new MyPageTransformer3());
    }



    class MyPageTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(@NonNull View page, float position) {
            //3D旋转
            int width = page.getWidth();
            int pivotX = 0;
            if (position <= 1 && position > 0) {// right scrolling
                pivotX = 0;
            } else if (position == 0) {

            } else if (position < 0 && position >= -1) {// left scrolling
                pivotX = width;
            }
            //设置x轴的锚点
            page.setPivotX(pivotX);
            //设置绕Y轴旋转的角度
            page.setRotationY(15f * position);
        }
    }

    class MyPageTransformer1 implements ViewPager.PageTransformer {
        private final float MIN_SCALE = 0.75f;

        @Override
        public void transformPage(@NonNull View page, float position) {

            if (position <= 0f) {
                page.setTranslationX(0f);
                page.setScaleX(1f);
                page.setScaleY(1f);
            } else if (position <= 1f) {
                final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                page.setAlpha(1 - position);
                page.setPivotY(0.5f * page.getHeight());
                page.setTranslationX(page.getWidth() * -position);
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
            }
            page.setRotation(180 * position);

        }
    }


    class MyPageTransformer2 implements ViewPager.PageTransformer {
        private final float MIN_SCALE = 0.5f;
        private final float MIN_ALPHA = 0.5f;

        @Override
        public void transformPage(@NonNull View page, float position) {
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            float alphaFactor = MIN_ALPHA + (1 - MIN_ALPHA) * (1 - Math.abs(position));
            page.setScaleY(scaleFactor);
            page.setAlpha(alphaFactor);
        }

    }

    /**
     * 参数page：这个是你即将把动画赋予的子页面
     参数position：一看是个float值，就要和作为int值的它区分开，float是个相对位置，
     它是一个-1到1的值，相对位置提供给开发者，可以控制动画进度和方式。
     具体的：0的时候是要执行动画的页面处于页面前端并居中的位置，
     1是要执行动画的页面向右移动到看不见（宽度为一整个页面），
     -1是要执行的动画向左移动到看不见的位置（宽度为一整个页面），
     正好对应一个进入动画，一个退出动画。
     *
     * setAlpha(@FloatRange(from=0.0, to=1.0) float alpha) 透明度
     setTranslationX(float translationX) X轴平移
     setTranslationY(float translationY) Y轴平移
     setTranslationZ(float translationZ) Z轴平移
     setRotation(float rotation) 设置相对中心点的旋转角度，正值按照顺时针转动
     setRotationX(float rotationX) 设置相对X轴（水平轴）旋转，正值为从X轴向下看顺时针旋转
     setRotationY(float rotationY)设置相对Y轴（竖直轴）旋转，正值为从Y轴向下看顺时针旋转
     setPivotX(float pivotX) 设置X轴附近的轴心点的X坐标
     setPivotY(float pivotY) 设置Y轴附近的轴心点的Y坐标
     setScaleX(float scaleX) 设置X轴方向的缩放比例
     setScaleY(float scaleY) 设置Y轴方向的缩放比例

     */
    class MyPageTransformer3 implements ViewPager.PageTransformer {
        private final float MIN_SCALE = 0.5f;
        private final float MIN_ALPHA = 0.5f;

        @Override
        public void transformPage(@NonNull View page, float position) {
            if (position <= 0f) {
               // page.setTranslationX(page.getWidth());
               // page.setAlpha(position);
               // page.setScaleX(1f);
              //  page.setScaleY(1f);
            } else if (position <= 1f) {
               // final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                page.setAlpha(1 - position);
               // page.setTranslationY(0);
                // page.setTranslationX(page.getWidth() * -position);
               // page.setScaleX(scaleFactor);
                //page.setPivotX(page.getWidth());
            }
            page.setPivotX(page.getWidth());
            page.setRotationY(15f * position);
        }

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
