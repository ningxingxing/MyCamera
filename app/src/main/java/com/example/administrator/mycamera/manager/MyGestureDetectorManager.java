package com.example.administrator.mycamera.manager;


import android.content.Context;
import android.gesture.Gesture;
import android.util.FloatMath;
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
            mGestureDetectorManager.onePointerTouch(e);
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        if (mGestureDetectorManager!=null){
            if (e2.getPointerCount()==1){
               // mGestureDetectorManager.onePointerTouch(e2);
            }else if (e2.getPointerCount()==2){
                mGestureDetectorManager.zoomPreview(e2);
            }
        }

       // LogUtils.e(TAG,"nsc 1="+e2.getX(0)+ " 2= "+e2.getX(1) + " distanceX="+Math.sqrt(x*x + y*y));

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
