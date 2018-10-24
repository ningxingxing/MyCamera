package com.example.administrator.mycamera.presenter;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;

import com.example.administrator.mycamera.activity.CameraActivity;
import com.example.administrator.mycamera.manager.CameraManager;
import com.example.administrator.mycamera.manager.CameraManager.CameraProxy;
import com.example.administrator.mycamera.utils.CameraConstant;
import com.example.administrator.mycamera.utils.CameraUtils;
import com.example.administrator.mycamera.utils.LogUtils;
import com.example.administrator.mycamera.utils.SaveImageUtils;

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
    private String mPath = null;

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
            //mRecorder.reset();
            mRecorder.setCamera(mCameraDevice.getCamera());
            // Set audio and video source and encoder
            // 这两项需要放在setOutputFormat之前
            mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            //3.设置音频的编码格式
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            //设置图像的编码格式
            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

           // mRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
            //mRecorder.setMaxFileSize(1024*1024*100);//100M
           // mRecorder.setMaxDuration(1000*60*60);
           // mRecorder.setOrientationHint(90);
            //设置录像的分辨率
            //mRecorder.setVideoSize(352, 288);

            String path = CameraUtils.EXTERNAL_DIR;
            if (path != null) {

                File dir = new File(path + "/Camera");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                mPath = path = dir + "/" + SaveImageUtils.ms2Date(System.currentTimeMillis()) + ".mp4";
               LogUtils.e(TAG,"startRecording path="+path);
                mRecorder.setOutputFile(path);
                mRecorder.prepare();
                mRecorder.start();   // Recording is now started
               // mIVideoPresenter.showThumbnail();
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
            mRecorder.release();
            mRecorder = null;
            updateVideo(mActivity, mPath);

        } catch (Exception e) {

        }
    }

    private void updateVideo(Context context, String filename) {
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(filename, MediaStore.Video.Thumbnails.MINI_KIND);
        mIVideoPresenter.showThumbnail(SaveImageUtils.rotateBitmapByDegree(thumbnail,90));
        MediaScannerConnection.scanFile(context,
                new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        File file = new File(path);

                        LogUtils.e("updateImageToDb success", "path " + path + ":" + "uri=" + file.length());

                    }
                });
    }

}
