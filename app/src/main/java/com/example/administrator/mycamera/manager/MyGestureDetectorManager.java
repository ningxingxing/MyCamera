package com.example.administrator.mycamera.manager;


import android.content.Context;
import android.gesture.Gesture;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.administrator.mycamera.port.IGestureDetectorManager;
import com.example.administrator.mycamera.utils.LogUtils;

/**
 * Created by Administrator on 2018/6/28.
 */

public class MyGestureDetectorManager extends Gesture implements GestureDetector.OnGestureListener {
    private final String TAG = "GestureDetectorManager";
    private Context mContext;
    private IGestureDetectorManager mGestureDetectorManager;

    public MyGestureDetectorManager(Context context, IGestureDetectorManager gestureDetectorManager) {
        this.mContext = context;
        this.mGestureDetectorManager = gestureDetectorManager;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (mGestureDetectorManager != null) {
           // mGestureDetectorManager.onAutoFocus(e);
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (mGestureDetectorManager != null) {
            mGestureDetectorManager.onTakePhoto();
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
