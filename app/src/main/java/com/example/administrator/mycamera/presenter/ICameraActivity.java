package com.example.administrator.mycamera.presenter;

import android.content.res.Configuration;
import android.view.KeyEvent;

import com.example.administrator.mycamera.manager.CameraManager.CameraProxy;

/**
 * Created by Administrator on 2018/6/5.
 * 将CameraActivity的方法给实现此接口的类使用
 */

public interface ICameraActivity {
    void onPauseSuper();

    void onResumeSuper(CameraProxy cameraProxy);

    void shutterClick();

    void longClickTakePicture();

    void onClickAutoFocus();

    void onDestroySuper();

    void switchCamera(CameraProxy cameraProxy);

    void onConfigurationChanged(Configuration newConfig);

    void onKeyDown(int keyCode, KeyEvent event);

    void takePhotoDelay();

    void onSettingPictureSize(int width,int height);


}
