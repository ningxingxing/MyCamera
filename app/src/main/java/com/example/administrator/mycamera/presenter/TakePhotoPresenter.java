package com.example.administrator.mycamera.presenter;

import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;

import com.example.administrator.mycamera.activity.CameraActivity;
import com.example.administrator.mycamera.manager.CameraManager;
import com.example.administrator.mycamera.manager.CameraManager.CameraProxy;
import com.example.administrator.mycamera.model.CameraPreference;
import com.example.administrator.mycamera.utils.CameraConstant;
import com.example.administrator.mycamera.utils.CameraState;
import com.example.administrator.mycamera.utils.LogUtils;
import com.example.administrator.mycamera.utils.SaveImageUtils;
import com.example.administrator.mycamera.utils.SoundPlay;

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
    private boolean isLongClick = false;
    private SharedPreferences mSharedPreferences;
    //private MediaPlayer mMediaPlayer;
    private SoundPlay mSoundPool;

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
                        mTakePhoto.displayProgress(false);
                        LogUtils.e(TAG, "handleMessage mCameraState=" + mCameraState);
                        break;

                    case CameraConstant.SUCCESS_UPDATE_IMAGE_ToDb:
                        mTakePhoto.showThumbnail();
                        break;

                    case CameraConstant.FOCUS_SUCCESS:
                        if (isLongClick) {
                            takePicture();
                        }
                        break;
                }
            }
        }
    }

    public TakePhotoPresenter(CameraActivity context, ITakePhoto takePhoto) {
        this.mActivity = context;
        this.mTakePhoto = takePhoto;

        mHandler = new MainHandler(context);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
      //  mMediaPlayer = MediaPlayer.create(mActivity, R.raw.video_record1);
        mSoundPool = new SoundPlay(mActivity);
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
        takePicture();
    }

    @Override
    public void longClickTakePicture() {
        manuallyAutoFocus();
        isLongClick = true;
    }

    @Override
    public void onClickAutoFocus() {
        if (mCameraDevice == null) return;
        isLongClick = false;
        manuallyAutoFocus();

    }

    @Override
    public void onDestroySuper() {
        if (mSoundPool == null) return;
        mSoundPool.releasePlayer();
    }

    private void takePicture() {
        if (mPaused || mCameraDevice == null) return;
        LogUtils.e(TAG, "takePicture =" + mCameraState);

        if (mCameraState == CameraState.STATE_PREVIEW || mCameraState == CameraState.STATE_FOCUSED_FINISH) {
            mCameraState = CameraState.STATE_EDIT;
            mCameraDevice.takePicture(mHandler, new ShutterCallback(true),
                    new RawPictureCallback(), new PostViewPictureCallback(),
                    new JpegPictureCallback());

            mTakePhoto.showFlashOverlayAnimation();
            mTakePhoto.displayProgress(true);
            //拍照点击声音
            boolean shutterSound = mSharedPreferences.getBoolean(CameraPreference.KEY_VOLUME_SOUND, false);
            if (shutterSound){
                mSoundPool.startPlay(SoundPlay.SHUTTER_CLICK);
            }
            mCameraDevice.enableShutterSound(false);
        }
    }

    private class AutoFocusCallback implements CameraManager.CameraAFMoveCallback {

        @Override
        public void onAutoFocusMoving(boolean moving, CameraProxy camera) {
            if (mPaused) return;
            mCameraState = CameraState.STATE_FOCUSED_FINISH;

            LogUtils.e(TAG, "onAutoFocusMoving");
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
                    SaveImageUtils.saveImage(mHandler, mActivity, jpegData);
                    mHandler.sendEmptyMessage(FINISH_PICTURE);
                }
            }).start();
        }
    }

    /**
     * 手动对焦
     */
    private void manuallyAutoFocus() {
        if (mCameraDevice == null || mParameters == null) return;

        mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCameraDevice.setParameters(mParameters);
        mCameraDevice.autoFocus(mHandler, new CameraManager.CameraAFCallback() {
            @Override
            public void onAutoFocus(boolean success, CameraProxy camera) {
                if (success) {
                    mCameraDevice.cancelAutoFocus();
                    if (!Build.MODEL.equals("KORIDY H30")) {
                        mParameters = mCameraDevice.getParameters();
                        mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
                        camera.setParameters(mParameters);
                    } else {
                        mParameters = mCameraDevice.getParameters();
                        mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                        camera.setParameters(mParameters);
                    }
                    mCameraState = CameraState.STATE_FOCUSED_FINISH;
                    mTakePhoto.focusAnimationFinish();
                    mHandler.sendEmptyMessage(CameraConstant.FOCUS_SUCCESS);

                    //聚焦完成声音
                    boolean isFocusSound = mSharedPreferences.getBoolean(CameraPreference.KEY_FOCUSED_SOUND, false);
                    if (isFocusSound) {
                        // mMediaPlayer.start();
                        mSoundPool.startPlay(SoundPlay.START_VIDEO_RECORDING);

                    }
                }
                LogUtils.e(TAG, "manuallyAutoFocus success=" + success);
            }
        });
    }
}
