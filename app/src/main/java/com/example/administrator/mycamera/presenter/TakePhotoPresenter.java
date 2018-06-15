package com.example.administrator.mycamera.presenter;

import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.administrator.mycamera.activity.CameraActivity;
import com.example.administrator.mycamera.manager.CameraManager;
import com.example.administrator.mycamera.manager.CameraManager.CameraProxy;
import com.example.administrator.mycamera.utils.CameraState;
import com.example.administrator.mycamera.utils.LogUtils;
import com.example.administrator.mycamera.utils.SaveImageUtils;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/5/21.
 */

public class TakePhotoPresenter implements ICameraActivity {

    private final String TAG = "Cam_TakePhotoPresenter";
    private ITakePhoto mTakePhoto;
    private CameraActivity mActivity;

    private CameraProxy mCameraDevice;
    private Parameters mParameters;
    private int mCameraId = 0;
    private int mCameraState = CameraState.STATE_PREVIEW;
    private boolean mPaused = false;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private MainHandler mHandler;
    private final int FINISH_PICTURE = 5;


    private class MainHandler extends Handler {
        private WeakReference weakReference;

        public MainHandler(CameraActivity parent) {
            super(Looper.getMainLooper());
            weakReference = new WeakReference(parent);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CameraActivity parent = (CameraActivity) weakReference.get();
            if (parent != null) {
                switch (msg.what) {

                    case FINISH_PICTURE:
                        mCameraState = CameraState.STATE_PREVIEW;
                        LogUtils.e(TAG, "handleMessage mCameraState=" + mCameraState);
                        break;
                }
            }
        }
    }

    public TakePhotoPresenter(CameraActivity context, ITakePhoto takePhoto) {
        this.mActivity = context;
        this.mTakePhoto = takePhoto;

        mHandler = new MainHandler(context);
    }


    @Override
    public void onPauseSuper() {
        LogUtils.e(TAG, "onPauseSuper");
        mPaused = true;
        //关闭相机
        // CameraInterface.getInstance().doStopCamera();
        mCameraState = CameraState.STATE_PREVIEW;
        mHandler.removeCallbacksAndMessages(null);
        mCameraDevice.setPreviewTexture(null);
        mTakePhoto.stopPreview();
    }

    @Override
    public void onResumeSuper(CameraProxy cameraProxy) {
        LogUtils.e(TAG, "onResumeSuper");
        this.mCameraDevice = cameraProxy;

        mPaused = false;
        //打开相机
        prepareCamera();
    }

    private void prepareCamera() {
        if (mCameraDevice == null || mTakePhoto == null) return;

        mParameters = mCameraDevice.getParameters();


    }


    @Override
    public void shutterClick() {

        if (mPaused || mCameraDevice == null) return;
        LogUtils.e(TAG, "shutterClick =" + mCameraState);

        if (mCameraState == CameraState.STATE_PREVIEW) {
            mCameraState = CameraState.STATE_EDIT;
            mCameraDevice.takePicture(mHandler, new ShutterCallback(true),
                    new RawPictureCallback(), new PostViewPictureCallback(),
                    new JpegPictureCallback());
        }

    }

    private final class ShutterCallback implements CameraManager.CameraShutterCallback {

        public ShutterCallback(boolean needsAnimation) {

        }

        @Override
        public void onShutter(CameraProxy camera) {

        }
    }

    private final class PostViewPictureCallback implements CameraManager.CameraPictureCallback {
        @Override
        public void onPictureTaken(byte[] data, CameraProxy camera) {

        }
    }

    private final class RawPictureCallback implements CameraManager.CameraPictureCallback {
        @Override
        public void onPictureTaken(byte[] rawData, CameraProxy camera) {

        }
    }


    /**
     * 拍照图片数据处理
     */
    private final class JpegPictureCallback implements CameraManager.CameraPictureCallback {

        public JpegPictureCallback() {

        }

        @Override
        public void onPictureTaken(final byte[] jpegData, CameraProxy camera) {
            if (mPaused || jpegData == null || mTakePhoto == null || mHandler == null) return;
            LogUtils.e(TAG, "onPictureTaken jpegData=" + jpegData);
            mTakePhoto.startPreview();
            // mCameraState = CameraState.STATE_EDIT;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SaveImageUtils.saveImage(mActivity, jpegData);
                    mHandler.sendEmptyMessage(FINISH_PICTURE);
                }
            }).start();
        }
    }
}
