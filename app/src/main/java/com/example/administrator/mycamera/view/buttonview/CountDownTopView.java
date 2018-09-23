package com.example.administrator.mycamera.view.buttonview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.utils.LogUtils;

public class CountDownTopView extends LinearLayout implements RadioGroup.OnCheckedChangeListener{
    private final String TAG = "CountDownTopView";
    private View mView;
    private RadioGroup rgCountDown;

    public CountDownTopView(Context context) {
        this(context,null);
    }

    public CountDownTopView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CountDownTopView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mView = LayoutInflater.from(context).inflate(R.layout.view_count_down_top, this);
        initView();
    }

    private void initView() {
        rgCountDown = (RadioGroup)findViewById(R.id.rg_count_down);
        rgCountDown.check(R.id.rb_five);
        rgCountDown.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId){

            case R.id.rb_close:
                LogUtils.e(TAG,"nsc =rb_close");
                rgCountDown.check(R.id.rb_close);
                break;


            case R.id.rb_two:
                LogUtils.e(TAG,"nsc =rb_two");
                rgCountDown.check(R.id.rb_two);
                break;


            case R.id.rb_five:

                LogUtils.e(TAG,"nsc =rb_five");
                rgCountDown.check(R.id.rb_five);
                break;


            case R.id.rb_ten:
                rgCountDown.check(R.id.rb_ten);
                LogUtils.e(TAG,"nsc =rb_ten");
                break;


        }


    }
}
