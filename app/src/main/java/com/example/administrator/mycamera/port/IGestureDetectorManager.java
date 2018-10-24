package com.example.administrator.mycamera.port;

import android.view.MotionEvent;

/**
 * Created by Administrator on 2018/6/28.
 */

public interface IGestureDetectorManager {

    void onAutoFocus(MotionEvent event);

    void onTakePhoto();

    void zoomPreview(MotionEvent event);

    void onePointerTouch(MotionEvent event);
}
