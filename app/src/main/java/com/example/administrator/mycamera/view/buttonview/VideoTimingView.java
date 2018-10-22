package com.example.administrator.mycamera.view.buttonview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.utils.CameraUtils;

public class VideoTimingView extends LinearLayout {
    private TextView tvFlicker;
    private View mView;
    private TextView tvTime;
    private final int START_VIDEO_TIMING = 1;
    private int mMinute = 0;//分钟
    private int mSecond = 0;//秒
    private Context mContext;

    public VideoTimingView(Context context) {
        this(context, null);
    }

    public VideoTimingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoTimingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.view_video_timing, this);
        initView();
    }

    private void initView() {
        tvFlicker = (TextView)findViewById(R.id.tv_flicker);
        tvTime = (TextView)findViewById(R.id.tv_time);
        mView.setVisibility(GONE);

    }

    private final Handler mHandler = new MainHandler();

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            if (message.what == START_VIDEO_TIMING) {
                showTime();
            }
        }
    }

    private void showTime(){
        mSecond ++;
        if (mSecond>=60){
            mMinute++;
            mSecond = 0;
        }

        tvTime.setText(CameraUtils.msToTime(mMinute,mSecond)+"");
        if (mHandler!=null) {
            mHandler.removeMessages(START_VIDEO_TIMING);
            mHandler.sendEmptyMessageDelayed(START_VIDEO_TIMING,1000);
        }
    }

    public void startVideoTime(){
        mView.setVisibility(VISIBLE);
        tvFlicker.setVisibility(GONE);
        if (mHandler!=null) {
            mHandler.sendEmptyMessageDelayed(START_VIDEO_TIMING, 1000);
        }

    }

    public void stopVideoTime(){
        mSecond = 0;
        mMinute = 0;
        mView.setVisibility(VISIBLE);
        tvFlicker.setVisibility(VISIBLE);
        tvTime.setText(mContext.getString(R.string.default_video_time));
        if (mHandler!=null) {
            mHandler.removeMessages(START_VIDEO_TIMING);
        }
    }

    public void hideVideoTime(){
        mSecond = 0;
        mMinute = 0;
        if (mHandler!=null) {
            mHandler.removeMessages(START_VIDEO_TIMING);
        }
        mView.setVisibility(GONE);
    }


}
