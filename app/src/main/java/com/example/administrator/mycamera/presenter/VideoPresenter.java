package com.example.administrator.mycamera.presenter;

import android.hardware.Camera.Parameters;

import com.example.administrator.mycamera.activity.CameraActivity;
import com.example.administrator.mycamera.manager.CameraManager;
import com.example.administrator.mycamera.manager.CameraManager.CameraProxy;
import com.example.administrator.mycamera.utils.LogUtils;


public class VideoPresenter implements ICameraActivity {
    private final String TAG = "VideoPresenter";

    private CameraActivity mActivity;
    private IVideoPresenter mIVideoPresenter;
    private boolean mPaused = false;
    private CameraProxy mCameraDevice;
    private Parameters mParameters;

    public VideoPresenter(CameraActivity cameraActivity, IVideoPresenter iVideoPresenter) {
        this.mActivity = cameraActivity;
        this.mIVideoPresenter = iVideoPresenter;

    }

    @Override
    public void onPauseSuper() {
        mPaused = true;
        LogUtils.e(TAG,"onPauseSuper");
    }

    @Override
    public void onResumeSuper(CameraManager.CameraProxy cameraProxy) {
        this.mCameraDevice = cameraProxy;
        mParameters = mCameraDevice.getParameters();
    }

    @Override
    public void shutterClick() {
        LogUtils.e(TAG,"shutterClick");
    }

    @Override
    public void longClickTakePicture() {

    }

    @Override
    public void onClickAutoFocus() {

    }

    @Override
    public void onDestroySuper() {

    }

    @Override
    public void switchCamera() {

    }
}
