package com.example.administrator.mycamera.presenter;

import android.content.res.Configuration;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.KeyEvent;

import com.example.administrator.mycamera.activity.CameraActivity;
import com.example.administrator.mycamera.manager.CameraManager;
import com.example.administrator.mycamera.manager.CameraManager.CameraProxy;
import com.example.administrator.mycamera.utils.CameraUtils;
import com.example.administrator.mycamera.utils.LogUtils;

import java.io.File;
import java.io.IOException;


public class VideoPresenter implements IVideoCameraActivity {
    private final String TAG = "VideoPresenter";

    private CameraActivity mActivity;
    private IVideoPresenter mIVideoPresenter;
    private boolean mPaused = false;
    private CameraProxy mCameraDevice;
    private Parameters mParameters;
    private MediaRecorder mRecorder;

    public VideoPresenter(CameraActivity cameraActivity, IVideoPresenter iVideoPresenter) {
        this.mActivity = cameraActivity;
        this.mIVideoPresenter = iVideoPresenter;

    }

    @Override
    public void onPauseSuper() {
        mPaused = true;
        LogUtils.e(TAG, "onPauseSuper");
    }

    @Override
    public void onResumeSuper(CameraManager.CameraProxy cameraProxy) {
        this.mCameraDevice = cameraProxy;
        mParameters = mCameraDevice.getParameters();
        LogUtils.e(TAG, "nsc onResumeSuper=" + mCameraDevice);
    }

    @Override
    public void shutterClick() {

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
    public void switchCamera(CameraProxy cameraProxy) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {

    }

    @Override
    public void takePhotoDelay() {

    }

    @Override
    public void onSettingPictureSize(int width, int height) {

    }

    @Override
    public void videoStart() {
        LogUtils.e(TAG, "videoStart");
        startRecording();
    }

    @Override
    public void videoStop() {
        LogUtils.e(TAG, "videoStop");
        stopRecording();
    }

    private void startRecording() {
        LogUtils.e(TAG, "nsc startRecording=" + mCameraDevice);
        if (mCameraDevice == null) return;
        if (mRecorder == null) {
            mRecorder = new MediaRecorder(); // Create MediaRecorder
        }
        try {

            mCameraDevice.unlock();
            mRecorder.reset();
            mRecorder.setCamera(mCameraDevice.getCamera());
            // Set audio and video source and encoder
            // 这两项需要放在setOutputFormat之前
            mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
            //mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
            String path = CameraUtils.EXTERNAL_DIR;
            if (path != null) {

                File dir = new File(path + "/Camera");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                path = dir + "/" + System.currentTimeMillis() + ".mp4";
                mRecorder.setOutputFile(path);
                mRecorder.prepare();
                mRecorder.start();   // Recording is now started
                mIVideoPresenter.showThumbnail();
            }
            // mCameraDevice.unlock();
        } catch (Exception e) {
            LogUtils.e(TAG, "e=" + e.getMessage());
            mCameraDevice.lock();
        }
    }

    private void stopRecording() {
        if (mRecorder == null) return;

        try {
            mRecorder.stop();
            mRecorder.reset();
        } catch (Exception e) {

        }
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        }

        return null;
    }

}
