package com.example.administrator.mycamera.view.buttonview;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.model.CameraPreference;
import com.example.administrator.mycamera.utils.CameraConstant;
import com.example.administrator.mycamera.utils.LogUtils;

public class CountDownTopView extends LinearLayout implements RadioGroup.OnCheckedChangeListener,View.OnClickListener{
    private final String TAG = "CountDownTopView";
    private View mView;
    private RadioGroup rgCountDown;
    private RadioButton rbClose;
    private RadioButton rbTwo;
    private RadioButton rbFive;
    private RadioButton rbTen;
    private LinearLayout llCountDownTop;

    private ICountDownTop mCountDownTopClickListener;
    private Context mContext;

    public interface ICountDownTop{
//        void countDownTopClose();
//        void countDownTopTwo();
//        void countDownTopFive();
//        void countDownTopTen();
        void countDownTopTime();
    }

    public void setCountDownTopClick(ICountDownTop iCountDownTop){
        this.mCountDownTopClickListener = iCountDownTop;
    }

    public CountDownTopView(Context context) {
        this(context,null);
    }

    public CountDownTopView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CountDownTopView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.view_count_down_top, this);
        initView();
    }

    private void initView() {
        rgCountDown = (RadioGroup)findViewById(R.id.rg_count_down);
        rbClose = (RadioButton)findViewById(R.id.rb_close);
        rbTwo = (RadioButton)findViewById(R.id.rb_two);
        rbFive = (RadioButton)findViewById(R.id.rb_five);
        rbTen = (RadioButton)findViewById(R.id.rb_ten);
        llCountDownTop = (LinearLayout)findViewById(R.id.ll_count_down_top);

        llCountDownTop.setOnClickListener(this);
        rgCountDown.setOnCheckedChangeListener(this);
        rgCountDown.setOnClickListener(this);
        rbClose.setOnClickListener(this);
        rbTwo.setOnClickListener(this);
        rbFive.setOnClickListener(this);
        setCurrentSelect();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_two:
                if (mCountDownTopClickListener!=null){
                    mCountDownTopClickListener.countDownTopTime();
                }
                break;

            case R.id.rb_close:
                if (mCountDownTopClickListener!=null){
                    mCountDownTopClickListener.countDownTopTime();
                }
                break;

            case R.id.rb_five:
                if (mCountDownTopClickListener!=null){
                    mCountDownTopClickListener.countDownTopTime();
                }
                break;

            case R.id.rb_ten:
                if (mCountDownTopClickListener!=null){
                    mCountDownTopClickListener.countDownTopTime();
                }
                break;

        }
    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        String countTime = null;
        switch (checkedId){

            case R.id.rb_close:
                if (mCountDownTopClickListener!=null){
                    mCountDownTopClickListener.countDownTopTime();
                }
                countTime = rbClose.getText().toString();
                rgCountDown.check(R.id.rb_close);
                break;


            case R.id.rb_two:
                if (mCountDownTopClickListener!=null){
                    mCountDownTopClickListener.countDownTopTime();
                }
                countTime = rbTwo.getText().toString();
                rgCountDown.check(R.id.rb_two);
                break;


            case R.id.rb_five:
                if (mCountDownTopClickListener!=null){
                    mCountDownTopClickListener.countDownTopTime();
                }
                countTime = rbFive.getText().toString();
                rgCountDown.check(R.id.rb_five);
                break;


            case R.id.rb_ten:
                if (mCountDownTopClickListener!=null){
                    mCountDownTopClickListener.countDownTopTime();
                }
                countTime = rbTen.getText().toString();
                rgCountDown.check(R.id.rb_ten);
                break;


        }
        CameraPreference.put(mContext,CameraPreference.KEY_COUNT_DOWN, countTime);

    }

    public void setCurrentSelect(){
        if (mContext==null || rgCountDown==null)return;
        String def = mContext.getString(R.string.count_down_top_close);
        String time = (String)CameraPreference.get(mContext,CameraPreference.KEY_COUNT_DOWN,def);
        if (time.equals(rbClose.getText().toString())){
            rgCountDown.check(R.id.rb_close);
        }else if (time.equals(rbTwo.getText().toString())){
            rgCountDown.check(R.id.rb_two);
        }else if (time.equals(rbFive.getText().toString())){
            rgCountDown.check(R.id.rb_five);
        }else if (time.equals(rbTen.getText().toString())){
            rgCountDown.check(R.id.rb_ten);
        }

    }
}
