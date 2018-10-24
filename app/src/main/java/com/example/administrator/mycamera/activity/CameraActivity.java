package com.example.administrator.mycamera.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.fragment.ModelFragment;
import com.example.administrator.mycamera.fragment.SettingFragment;
import com.example.administrator.mycamera.manager.CameraManager;
import com.example.administrator.mycamera.manager.CameraManager.CameraProxy;
import com.example.administrator.mycamera.manager.MyGestureDetectorManager;
import com.example.administrator.mycamera.model.CameraPreference;
import com.example.administrator.mycamera.port.IBottomItem;
import com.example.administrator.mycamera.port.IGestureDetectorManager;
import com.example.administrator.mycamera.port.IModeFragment;
import com.example.administrator.mycamera.port.IScenesView;
import com.example.administrator.mycamera.port.ISettingFragment;
import com.example.administrator.mycamera.port.ITopItem;
import com.example.administrator.mycamera.port.IWhiteBalanceView;
import com.example.administrator.mycamera.presenter.ITakePhoto;
import com.example.administrator.mycamera.presenter.IVideoPresenter;
import com.example.administrator.mycamera.presenter.TakePhotoPresenter;
import com.example.administrator.mycamera.presenter.VideoPresenter;
import com.example.administrator.mycamera.utils.CameraConstant;
import com.example.administrator.mycamera.utils.CameraInterface;
import com.example.administrator.mycamera.utils.CameraParameter;
import com.example.administrator.mycamera.utils.CameraUtils;
import com.example.administrator.mycamera.utils.FlashOverlayAnimation;
import com.example.administrator.mycamera.utils.LogUtils;
import com.example.administrator.mycamera.utils.Thumbnail;
import com.example.administrator.mycamera.view.CameraGLSurfaceView;
import com.example.administrator.mycamera.view.FaceView;
import com.example.administrator.mycamera.view.CountDownView;
import com.example.administrator.mycamera.view.PictureSizeDialog;
import com.example.administrator.mycamera.view.buttonview.AuxiliaryLineView;
import com.example.administrator.mycamera.view.buttonview.CameraBottomView;
import com.example.administrator.mycamera.view.buttonview.CameraTopView;
import com.example.administrator.mycamera.view.buttonview.CircleImageView;
import com.example.administrator.mycamera.view.buttonview.CountDownTopView;
import com.example.administrator.mycamera.view.buttonview.FocusAnimationView;
import com.example.administrator.mycamera.view.buttonview.ScenesView;
import com.example.administrator.mycamera.view.buttonview.VideoTimingView;
import com.example.administrator.mycamera.view.buttonview.WhiteBalanceView;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/5/21.
 */

public class CameraActivity extends Activity implements ITakePhoto, IVideoPresenter, IBottomItem, ITopItem,
        ISettingFragment, TextureView.SurfaceTextureListener, IGestureDetectorManager, IWhiteBalanceView
        , CameraGLSurfaceView.OnTouchListener, CountDownTopView.ICountDownTop, DrawerLayout.DrawerListener,
        IScenesView, PictureSizeDialog.IPictureSizeClick ,IModeFragment{

    private final String TAG = "Cam_CameraActivity";
    private CameraGLSurfaceView mGlSurfaceView;
    private TakePhotoPresenter mTakePhotoPresenter;
    private VideoPresenter mVideoPresenter;

    private CameraBottomView mCameraBottom;
    private DrawerLayout mDrawerLayout;
    private AuxiliaryLineView mAuxiliaryLine;
    private View mFlashOverlay;
    private FocusAnimationView mFocusAnimationView;
    private CameraTopView mCameraTop;
    private WhiteBalanceView mWhiteBalance;
    private FaceView mFaceView;
    private CountDownView mCountDownView;
    private CountDownTopView mCountDownTopView;
    private ScenesView mScenesView;
    private FrameLayout mFlPreview;
    private VideoTimingView mVideoTime;

    private CameraProxy mCameraDevice;
    private Parameters mParameters;
    private int mCameraId = 0;
    private SeekBar mEvSeekBar;

    private FlashOverlayAnimation mFlashOverlayAnimation;
    private CameraParameter mCameraParameter;

    private FragmentManager mFragmentManager;
    private SettingFragment mSettingFragment;
    private ModelFragment mModelFragment;
    private SharedPreferences mSharedPreferences;

    private GestureDetectorCompat mGestureDetector;

    private WindowManager mWindowManager;
    private int mScreenHeight = 0;

    private int mCurrentModel = CameraConstant.PHOTO_MODEL;
    private int mVideoModel = CameraConstant.STOP_RECORDING;
    private int mWhiteBalanceIcons[] = {R.drawable.wb_automatic, R.drawable.wb_daylight,
            R.drawable.wb_fluorescence, R.drawable.wb_incandescent, R.drawable.wb_overcast};

    private String mWhiteBalanceMode[] = {"auto", "daylight", "fluorescent", "incandescent", "cloudy-daylight"};
    private CameraUtils mCameraUtils;

    private final int CHANGE_PREVIEW = 1;
    private CameraHandler mCameraHandler = null;
    private int mZoomRate = 0;//0-support max
    private float mOldDistance = 0;

    private class CameraHandler extends Handler {
        private WeakReference weakReference;

        public CameraHandler(CameraActivity parent) {
            super(Looper.getMainLooper());
            weakReference = new WeakReference(parent);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CameraActivity parent = (CameraActivity) weakReference.get();
            if (parent != null) {

                switch (msg.what) {

                    case CHANGE_PREVIEW:
                        setPreviewScale();
                        break;

                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

        initView();
        initViewData();
        initFragment();
    }

    private void initView() {
        mTakePhotoPresenter = new TakePhotoPresenter(CameraActivity.this, this);
        mVideoPresenter = new VideoPresenter(CameraActivity.this, this);
        mGlSurfaceView = (CameraGLSurfaceView) findViewById(R.id.gl_surfaceView);
        mGlSurfaceView.setSurfaceTextureListener(this);
        mGlSurfaceView.setOnTouchListener(this);

        mCameraBottom = (CameraBottomView) findViewById(R.id.camera_bottom);
        mCameraBottom.setBottomClickListener(this);

        mCameraTop = (CameraTopView) findViewById(R.id.camera_top);
        mCameraTop.setTopClickListener(this);

        mFlashOverlay = (View) findViewById(R.id.flash_overlay);
        mFlashOverlayAnimation = new FlashOverlayAnimation();

        mEvSeekBar = (SeekBar) findViewById(R.id.evSeekBar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(this);
        mAuxiliaryLine = (AuxiliaryLineView) findViewById(R.id.auxiliary_line);
        mWhiteBalance = (WhiteBalanceView) findViewById(R.id.white_balance);
        mWhiteBalance.setWhiteBalanceViewClickListener(this);
        mFaceView = (FaceView) findViewById(R.id.face_view);
        mCountDownView = (CountDownView) findViewById(R.id.count_down_to_capture);

        mCountDownTopView = (CountDownTopView) findViewById(R.id.count_down_top_view);
        mCountDownTopView.setCountDownTopClick(this);

        mFocusAnimationView = (FocusAnimationView) findViewById(R.id.focus_animation_view);
        mGestureDetector = new GestureDetectorCompat(CameraActivity.this, new MyGestureDetectorManager(CameraActivity.this, this));

        mScenesView = (ScenesView) findViewById(R.id.scenes_view);
        mFlPreview = (FrameLayout) findViewById(R.id.fl_preview);

        mVideoTime = (VideoTimingView) findViewById(R.id.video_time);

    }

    private void initViewData() {
        mWindowManager = this.getWindowManager();
        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();

        mCameraParameter = new CameraParameter();
        mCameraUtils = new CameraUtils();
        mCameraHandler = new CameraHandler(CameraActivity.this);
    }

    private void initData() {
        CameraUtils.setBrightnessForCamera(getWindow(), false);
        int MaxEV = CameraParameter.getCameraMaxExposureCompensation(mParameters);
        mEvSeekBar.setMax(MaxEV);
        // CameraPreference.saveIntPreference(this, CameraPreference.KEY_EXPOSURE_COMPENSATION, MaxEV);
        // CameraParameter.getCameraSupportedSceneMode(mParameters);
        mCameraParameter.getCameraSupportedWhiteBalance(mParameters);
        mEvSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtils.e(TAG, "progress=" + progress);
                CameraParameter.setCameraExposureCompensation(mCameraDevice, mParameters, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mScenesView.setScenesClickListener(this, mParameters);
        showThumbnail(null);
    }

    private void initFragment() {
        LogUtils.e(TAG, "initFragment mSettingFragment=" + mSettingFragment);
        mFragmentManager = getFragmentManager();
        mSettingFragment = new SettingFragment();
        mModelFragment = new ModelFragment();
        addFragment(mModelFragment, R.id.content_model, "model");
        addFragment(mSettingFragment, R.id.content_setting, "setting");

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSettingFragment.setISettingFragment(this);

        //初始化是否显示辅助线
        boolean isAuxiliaryLine = mSharedPreferences.getBoolean(CameraPreference.KEY_AUXILIARY_LINE, false);
        setAuxiliaryLine(isAuxiliaryLine);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mTakePhotoPresenter.onPauseSuper();
        mVideoPresenter.onPauseSuper();
        mGlSurfaceView.onPause();
        LogUtils.e(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCameraDevice == null) {
            mCameraId = CameraPreference.getCameraId(CameraActivity.this);
            openCamera();
        }
        mGlSurfaceView.onResume();
        mGlSurfaceView.bringToFront();
        mTakePhotoPresenter.onResumeSuper(mCameraDevice);
        mVideoPresenter.onResumeSuper(mCameraDevice);
        initData();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG, "onDestroy");
        // removeFragment();
        mTakePhotoPresenter.onDestroySuper();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float downY = ev.getY();
        if (downY < mScreenHeight - mCameraBottom.getHeight() && downY > mCameraTop.getHeight()) {
            mGestureDetector.onTouchEvent(ev);
        }

//        if (mGestureDetector!=null && mGestureDetector.onTouchEvent(ev)){
//            return true;
//        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void videoClick() {
        if (mCurrentModel == CameraConstant.PHOTO_MODEL) {
            mCurrentModel = CameraConstant.VIDEO_MODEL;
            mVideoTime.stopVideoTime();
        } else if (mCurrentModel == CameraConstant.VIDEO_MODEL) {
            mCurrentModel = CameraConstant.PHOTO_MODEL;
            mVideoTime.hideVideoTime();
        }

        mCameraBottom.updateIcon(mCurrentModel);
    }

    @Override
    public void shutterClick(ImageButton id) {
        if (mTakePhotoPresenter != null && mCurrentModel == CameraConstant.PHOTO_MODEL) {
            mTakePhotoPresenter.shutterClick();
            mVideoTime.hideVideoTime();
        }
        if (mVideoPresenter != null && mCurrentModel == CameraConstant.VIDEO_MODEL) {

            if (mVideoModel == CameraConstant.STOP_RECORDING) {
                mVideoModel = CameraConstant.START_RECORDING;
                mVideoPresenter.videoStart();
                mVideoTime.startVideoTime();
            } else if (mVideoModel == CameraConstant.START_RECORDING) {
                mVideoModel = CameraConstant.STOP_RECORDING;
                mVideoPresenter.videoStop();
                mVideoTime.stopVideoTime();
            }
            mCameraBottom.updateIcon(mVideoModel);
        }
    }

    @Override
    public void showFlashOverlayAnimation() {
        //启动闪光灯动画
        if (mFlashOverlayAnimation != null) {
            mFlashOverlayAnimation.startFlashAnimation(mFlashOverlay);
        }
    }

    @Override
    public void displayProgress(boolean disable) {
        mCameraBottom.displayProgress(disable);
    }

    @Override
    public void showThumbnail(Bitmap bitmap) {
        //Bitmap bitmap = Thumbnail.getImageThumbnail(CameraActivity.this);
        // String path = Thumbnail.getImageThumbnail(CameraActivity.this);
        if (bitmap != null) {
            mCameraBottom.showThumbnail(bitmap);
        } else {
            String path = Thumbnail.comparedThumbPath(CameraActivity.this);
            if (path != null) {
                mCameraBottom.showThumbnailPath(path);
                LogUtils.e(TAG, "showThumbnail path =" + path);
            }
        }
    }

    @Override
    public void focusAnimationFinish() {
        mFocusAnimationView.setFocusAnimationCancel();
        mFocusAnimationView.setVisibility(View.GONE);
    }


    @Override
    public void imageClick(CircleImageView imageButton) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, CameraConstant.OPEN_CAMERA_PHOTO);

        Intent intent = new Intent(CameraActivity.this,GalleryActivity.class);
        startActivity(intent);
    }

    @Override
    public void startPreview() {
        if (mCameraDevice != null) {
            CameraInterface.getInstance().initCamera(CameraActivity.this, 1.33f, mCameraId);
        }
    }

    @Override
    public void stopPreview() {
        if (mCameraDevice != null) {
            mCameraDevice.setErrorCallback(null);
            mCameraDevice.setZoomChangeListener(null);
            mCameraDevice.setFaceDetectionCallback(null, null);
            mCameraDevice.setPreviewDataCallbackWithBuffer(null, null);
            mCameraDevice.stopPreview();
            mCameraDevice.release();
            // mCamera = Camera.open(0);
            mCameraDevice.lock();
            mCameraDevice.unlock();
            mCameraDevice = null;
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        LogUtils.e(TAG, "onSurfaceTextureAvailable width=" + width + " height=" + height);
        if (mCameraDevice != null) {
            mCameraDevice.setPreviewTexture(surface);
            startPreview();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        LogUtils.e(TAG, "onSurfaceTextureSizeChanged");
        if (mCameraHandler != null) {
            mCameraHandler.sendEmptyMessage(CHANGE_PREVIEW);
        }

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mCameraDevice != null) {
            mCameraDevice.setPreviewTexture(null);
            stopPreview();
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        LogUtils.e(TAG, "onSurfaceTextureUpdated");

    }

    @Override
    public void cameraFlash() {
        if (mCameraParameter != null && mParameters != null && mCameraDevice != null) {
            int flashMode = mCameraTop.setFlashIcon();
            mParameters.setFlashMode(mCameraTop.getFlashMode(flashMode));
            mCameraDevice.setParameters(mParameters);
        }
    }

    @Override
    public void cameraDelay() {
        if (mTakePhotoPresenter != null) {
            mTakePhotoPresenter.takePhotoDelay();
            if (mCountDownTopView.getVisibility() == View.GONE) {
                mCountDownTopView.setVisibility(View.VISIBLE);
                mCameraTop.setVisibility(View.GONE);
                mCountDownTopView.setCurrentSelect();
            } else {
                mCountDownTopView.setVisibility(View.GONE);
                mCameraTop.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void cameraWhiteBalance() {
        visibleWhiteTopView();
        String currentWhiteBalance = CameraPreference.getStringPreference(CameraActivity.this, CameraPreference.KEY_WHITE_BALANCE);
        for (int i = 0; i < mWhiteBalanceMode.length; i++) {
            if (mWhiteBalanceMode[i].equals(currentWhiteBalance)) {
                mWhiteBalance.updateSelectIcon(i);
            }
        }

    }

    @Override
    public void cameraScene() {
        //mCameraTop.setBackgroundColor(0);
        if (mScenesView.getVisibility() == View.GONE) {
            mScenesView.setVisibility(View.VISIBLE);
            mCameraTop.setVisibility(View.GONE);
        }
    }

    @Override
    public void cameraSwitch() {
        if (mTakePhotoPresenter != null) {
            mTakePhotoPresenter.switchCamera(mCameraDevice);
        }
    }

    private void openCamera() {
        if (mCameraDevice == null) {
            mCameraDevice = CameraInterface.getInstance().openCamera(CameraActivity.this, mCameraId, null, mCameraOpenErrorCallback);
            mParameters = mCameraDevice.getParameters();
        } else {
            mParameters = mCameraDevice.getParameters();
        }
    }

    public SurfaceTexture getSurfaceTexture() {
        return mGlSurfaceView.getSurfaceTexture();
    }

    public void visibleWhiteTopView() {
        if (mWhiteBalance.getVisibility() == View.GONE) {
            mWhiteBalance.setVisibility(View.VISIBLE);
            mCameraTop.setVisibility(View.GONE);
        }
    }

    /**
     * add fragment
     *
     * @param fragment
     * @param containerViewId
     * @param tag
     */
    private void addFragment(Fragment fragment, int containerViewId, String tag) {
        //获取事务
        FragmentTransaction beginTransaction = mFragmentManager.beginTransaction();
        beginTransaction.replace(containerViewId, fragment, tag);
        //add current fragment to backStack
        beginTransaction.addToBackStack(tag);
        beginTransaction.commit();
    }

    /**
     * remove fragment
     */
    private void removeFragment() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (ft != null) {
            if (mSettingFragment != null) {
                ft.remove(mSettingFragment);
                mSettingFragment = null;
            }
            if (mModelFragment != null) {
                ft.remove(mModelFragment);
                mSettingFragment = null;
            }
            ft.commitAllowingStateLoss();
        }
    }

    /**
     * when window focus changed change window brightness
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // LogUtils.e(TAG, "nsc onWindowFocusChanged=" + hasFocus);
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            boolean isHdPreview = mSharedPreferences.getBoolean(CameraPreference.KEY_HD_PREVIEW, false);
            CameraUtils.setBrightnessForCamera(getWindow(), isHdPreview);
        }
    }

    @Override
    public void openSettingFragment() {
        addFragment(mSettingFragment, R.id.content_setting, "setting");
    }

    @Override
    public void closeSettingFragment() {
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void showPictureSizeSelect() {
        PictureSizeDialog pictureSizeDialog = new PictureSizeDialog(CameraActivity.this, mParameters, mCameraDevice, this);
        pictureSizeDialog.show();
    }

    /**
     * setting auxiliary line
     *
     * @param flag
     */
    @Override
    public void setAuxiliaryLine(boolean flag) {
        mAuxiliaryLine.setAuxiliaryLine(flag);
    }

    @Override
    public void onAutoFocus(MotionEvent event) {
        if (mTakePhotoPresenter != null) {
            mTakePhotoPresenter.onClickAutoFocus();
            mFocusAnimationView.setVisibility(View.VISIBLE);
            mFocusAnimationView.startFocusAnimation(event.getX(), event.getY());
        }
    }

    /**
     * 长按拍照
     */
    @Override
    public void onTakePhoto() {
        if (mTakePhotoPresenter != null) {
            mTakePhotoPresenter.longClickTakePicture();
        }
    }


    /**
     * 缩放界面
     *
     * @param event
     */
    @Override
    public void zoomPreview(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                mOldDistance = CameraUtils.getDistance(event);
                break;
            case MotionEvent.ACTION_MOVE:
                float newDist = CameraUtils.getDistance(event);
                if (newDist > mOldDistance) {
                    handleZoom(true);
                } else if (newDist < mOldDistance) {
                    handleZoom(false);
                }
                mOldDistance = newDist;
                break;
        }

    }

    /**
     * 点击聚焦
     * @param event
     */
    @Override
    public void onePointerTouch(MotionEvent event) {
        if (mTakePhotoPresenter != null) {
            mTakePhotoPresenter.onClickAutoFocus();
            mFocusAnimationView.setVisibility(View.VISIBLE);
            mFocusAnimationView.startFocusAnimation(event.getX(), event.getY());
        }
    }

    private void handleZoom(boolean isZoomIn) {
        if (mParameters == null || mCameraDevice == null) return;
        if (mParameters.isZoomSupported()) {
            int maxZoom = mParameters.getMaxZoom();
            int zoom = mParameters.getZoom();
            if (isZoomIn && zoom < maxZoom) {
                zoom =zoom+2;
            } else if (zoom > 0) {
                zoom =zoom-2;
            }
            mParameters.setZoom(zoom);
            mCameraDevice.setParameters(mParameters);
        } else {
            Log.i(TAG, "zoom not supported");
        }
    }

    @Override
    public void onWhiteBalanceClick(String tag, int index) {
        if (mCameraDevice != null && mParameters != null) {
            if (mCameraParameter.isSupportedWhiteBalance(mParameters, tag)) {
                mCameraParameter.setCameraWhiteBalance(mCameraDevice, mParameters, tag);
                CameraPreference.saveStringPreference(CameraActivity.this, CameraPreference.KEY_WHITE_BALANCE, tag);
            } else {
                Toast.makeText(getApplication(), "不支持该模式", Toast.LENGTH_SHORT).show();
            }
            mWhiteBalance.setVisibility(View.GONE);
            mCameraTop.setVisibility(View.VISIBLE);
            mCameraTop.setWhiteBalanceIcon(mWhiteBalanceIcons[index]);
        }
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        DealScreenSwitching screenSwitching = new DealScreenSwitching();
//        screenSwitching.invokeFragmentManagerNoteStateNotSaved();
//    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mTakePhotoPresenter != null) {
            mTakePhotoPresenter.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mTakePhotoPresenter != null) {
            mTakePhotoPresenter.onKeyDown(keyCode, event);
        }
        // return super.onKeyDown(keyCode, event);
        return true;//不设置声音
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        return super.onKeyUp(keyCode, event);
    }

    public CameraManager.CameraOpenErrorCallback mCameraOpenErrorCallback =
            new CameraManager.CameraOpenErrorCallback() {
                @Override
                public void onCameraDisabled(int cameraId) {
                    LogUtils.e(TAG, "CameraOpenErrorCallback onCameraDisabled=" + cameraId);
                }

                @Override
                public void onDeviceOpenFailure(int cameraId) {
                    LogUtils.e(TAG, "CameraOpenErrorCallback onDeviceOpenFailure=" + cameraId);

                }

                @Override
                public void onReconnectionFailure(CameraManager mgr) {
                    LogUtils.e(TAG, "CameraOpenErrorCallback onReconnectionFailure=" + mgr.toString());
                }
            };


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mCountDownTopView.getVisibility() == View.VISIBLE) {
            mCountDownTopView.setVisibility(View.GONE);
        }
        if (mScenesView.getVisibility() == View.VISIBLE) {
            mScenesView.setVisibility(View.GONE);
        }
        mCameraTop.setVisibility(View.VISIBLE);

//        if (mTakePhotoPresenter != null) {
//            mTakePhotoPresenter.onClickAutoFocus();
//            mFocusAnimationView.setVisibility(View.VISIBLE);
//            mFocusAnimationView.startFocusAnimation(event.getX(), event.getY());
//        }
        LogUtils.e(TAG,"nsc touch");
        return true;
    }

    /**
     * setting count down time
     */
    @Override
    public void countDownTopTime() {
        if (mCameraTop.getVisibility() == View.GONE) {
            mCameraTop.setVisibility(View.VISIBLE);
            mCountDownTopView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        LogUtils.e(TAG, "nsc onDrawerOpened=" + drawerView.getTag());
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        LogUtils.e(TAG, "nsc onDrawerClosed=" + drawerView.getTag());
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        LogUtils.e(TAG, "nsc onDrawerStateChanged=" + newState);
    }

    /**
     * 启动拍照倒计时
     *
     * @param time
     */
    @Override
    public void startCountDown(int time) {
        mCountDownView.setCountDownTime(time);
        mCountDownView.startCountDown();
    }

    /**
     * 取消拍照倒计时
     */
    @Override
    public void cancelCountDown() {
        mCountDownView.cancelCountDown();
    }

    /**
     * 获取当前倒计时剩余时间
     *
     * @return
     */
    @Override
    public int getCurrentCountDownTime() {
        return mCountDownView.getCountDownCurrentTime();
    }

    @Override
    public void onScenesClick(String sceneMode) {
        if (mScenesView.getVisibility() == View.VISIBLE) {
            mScenesView.setVisibility(View.GONE);
            mCameraTop.setVisibility(View.VISIBLE);
        }
        String mCurrentWhiteBalance = mCameraParameter.getCameraWhiteBalance(mParameters);
        if (mCurrentWhiteBalance.equals("auto")) {
            mCameraParameter.setCameraSceneMode(mParameters, sceneMode);
            CameraPreference.put(CameraActivity.this, CameraPreference.KEY_SCENE_MODE, sceneMode);
            Toast.makeText(CameraActivity.this, sceneMode, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onsettingPictureSize(int width, int height) {
        if (mTakePhotoPresenter != null) {
            mTakePhotoPresenter.onSettingPictureSize(width, height);
        }
    }

    //count down end

    /**
     * 设置预览界面大小
     */
    private void setPreviewScale() {
        int top = 0;
        int height = mCameraUtils.getScreenHeight(CameraActivity.this);
        int width = mCameraUtils.getScreenWidth(CameraActivity.this);
        String previewSize = (String) CameraPreference.get(CameraActivity.this, CameraPreference.KEY_PREVIEW_SCALE, "4:3");

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mGlSurfaceView.getLayoutParams();
        if (getString(R.string.preview_scale_43).equals(previewSize)) {
            height = width * 4 / 3;
            top = mCameraTop.getHeight();

        } else if (getString(R.string.preview_scale_11).equals(previewSize)) {
            top = (height - width) / 3;
            height = width;

        } else {
            top = 0;
        }

        params.width = mGlSurfaceView.getWidth();
        params.height = height;
        params.setMargins(0, top, 0, 0);
        mGlSurfaceView.setLayoutParams(params);
    }

    @Override
    public void openHdr(boolean isOpen) {
        if (mCameraDevice!=null && mCameraParameter!=null){
            mCameraParameter.switchHDR(mParameters);
            mCameraDevice.setParameters(mParameters);
        }
    }
}
