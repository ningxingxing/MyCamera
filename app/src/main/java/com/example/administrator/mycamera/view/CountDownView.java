package com.example.administrator.mycamera.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.utils.LogUtils;

public class CountDownView extends FrameLayout {
    private final String TAG = "Cam_CountDownView";

    private TextView tvCountDown;
    private static final long ANIMATION_DURATION_MS = 1000;
    private int mCount = 0;
    private View mView;
    private static final int SET_TIMER_TEXT = 1;
    private final Handler mHandler = new MainHandler();

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            if (message.what == SET_TIMER_TEXT) {
                remainingSecondsChanged();
            }
        }
    }

    public CountDownView(@NonNull Context context) {
        this(context, null);


    }

    public CountDownView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);


    }

    public CountDownView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mView = LayoutInflater.from(context).inflate(R.layout.view_count_down, this);
        initView();
    }

    private void initView() {
        tvCountDown = (TextView) findViewById(R.id.remaining_seconds);

    }


    private void remainingSecondsChanged() {
        if (mHandler==null)return;
        mCount--;

        tvCountDown.setText(mCount + "");
        if (mCount > 0) {
            mHandler.sendEmptyMessageDelayed(SET_TIMER_TEXT, ANIMATION_DURATION_MS);
        } else {
            mView.setVisibility(GONE);
            mHandler.removeMessages(SET_TIMER_TEXT);
        }
        LogUtils.e(TAG, "mCount=" + mCount);
    }


    public void setCountDownTime(int time) {
        this.mCount = time;
         tvCountDown.setText(time + "");
    }

    public int getCountDownCurrentTime(){
        return mCount;
    }

    public void startCountDown() {
        if (mHandler!=null) {
            mView.setVisibility(VISIBLE);
            mHandler.sendEmptyMessageDelayed(SET_TIMER_TEXT, ANIMATION_DURATION_MS);
        }
    }

    public void cancelCountDown() {
        if (mHandler!=null){
            mHandler.removeMessages(SET_TIMER_TEXT);
            mView.setVisibility(GONE);
        }

    }

//    private void startFadeOutAnimation() {
//        int textWidth = tvCountDown.getMeasuredWidth();
//        int textHeight = tvCountDown.getMeasuredHeight();
//        tvCountDown.setScaleX(1f);
//        tvCountDown.setScaleY(1f);
//        tvCountDown.setTranslationX(mPreviewArea.centerX() - textWidth / 2);
//        tvCountDown.setTranslationY(mPreviewArea.centerY() - textHeight / 2);
//        tvCountDown.setPivotX(textWidth / 2);
//        tvCountDown.setPivotY(textHeight / 2);
//        tvCountDown.setAlpha(1f);
//        float endScale = 2.5f;
//        tvCountDown.animate().scaleX(endScale).scaleY(endScale)
//                .alpha(0f).setDuration(ANIMATION_DURATION_MS).start();
//    }

}
