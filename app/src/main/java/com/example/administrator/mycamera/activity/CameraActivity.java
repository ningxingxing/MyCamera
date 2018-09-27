package com.example.administrator.mycamera.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.Camera.Parameters;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
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
import com.example.administrator.mycamera.view.CountDownView;
import com.example.administrator.mycamera.view.PictureSizeDialog;
import com.example.administrator.mycamera.view.buttonview.AuxiliaryLineView;
import com.example.administrator.mycamera.view.buttonview.CameraBottomView;
import com.example.administrator.mycamera.view.buttonview.CameraTopView;
import com.example.administrator.mycamera.view.buttonview.CircleImageView;
import com.example.administrator.mycamera.view.buttonview.CountDownTopView;
import com.example.administrator.mycamera.view.buttonview.FocusAnimationView;
import com.example.administrator.mycamera.view.buttonview.WhiteBalanceView;

/**
 * Created by Administrator on 2018/5/21.
 */

public class CameraActivity extends Activity implements ITakePhoto, IVideoPresenter, IBottomItem, ITopItem,
        ISettingFragment, TextureView.SurfaceTextureListener, IGestureDetectorManager, IWhiteBalanceView
     ,CameraGLSurfaceView.OnTouchListener,CountDownTopView.ICountDownTop,DrawerLayout.DrawerListener{

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
    private CountDownView mCountDownView;
    private CountDownTopView mCountDownTopView;

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
    private int mWhiteBalanceIcons[] = {R.drawable.wb_automatic, R.drawable.wb_daylight,
            R.drawable.wb_fluorescence, R.drawable.wb_incandescent, R.drawable.wb_overcast};

    private String mWhiteBalanceMode[] = {"auto", "daylight", "fluorescent", "incandescent", "cloudy-daylight"};
    private CameraUtils mCameraUtils;

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

        mCountDownView = (CountDownView)findViewById(R.id.count_down_to_capture);
//        mCountDownView.setVisibility(View.VISIBLE);
//        mCountDownView.setCountDownTime(10);
//        mCountDownView.startCountDown();

        mCountDownTopView = (CountDownTopView)findViewById(R.id.count_down_top_view);
        mCountDownTopView.setCountDownTopClick(this);

        mFocusAnimationView = (FocusAnimationView) findViewById(R.id.focus_animation_view);
        mGestureDetector = new GestureDetectorCompat(CameraActivity.this, new MyGestureDetectorManager(CameraActivity.this, this));

    }

    private void initViewData() {
        mWindowManager = this.getWindowManager();
        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();

        mCameraParameter = new CameraParameter();
        mCameraUtils = new CameraUtils();
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

        showThumbnail();
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

    }

    @Override
    protected void onPause() {
        super.onPause();
        mTakePhotoPresenter.onPauseSuper();
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
        initData();
        LogUtils.e(TAG, "onResume");
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
       // float downY = ev.getY();
//        if (downY < mScreenHeight - mCameraBottom.getHeight() && downY > mCameraTop.getHeight()) {
            mGestureDetector.onTouchEvent(ev);
//        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void videoClick() {
        if (mCurrentModel == CameraConstant.PHOTO_MODEL) {
            mCurrentModel = CameraConstant.VIDEO_MODEL;
        } else if (mCurrentModel == CameraConstant.VIDEO_MODEL) {
            mCurrentModel = CameraConstant.PHOTO_MODEL;
        }
    }

    @Override
    public void shutterClick(ImageButton id) {
        if (mTakePhotoPresenter != null && mCurrentModel == CameraConstant.PHOTO_MODEL) {
            mTakePhotoPresenter.shutterClick();
        }
        if (mVideoPresenter != null && mCurrentModel == CameraConstant.VIDEO_MODEL) {
            mVideoPresenter.shutterClick();
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
    public void showThumbnail() {
        //Bitmap bitmap = Thumbnail.getImageThumbnail(CameraActivity.this);
        String path = Thumbnail.getImageThumbnail(CameraActivity.this);
        if (path != null) {
            mCameraBottom.showThumbnailPath(path);
        }
    }

    @Override
    public void focusAnimationFinish() {
        mFocusAnimationView.setFocusAnimationCancel();
        mFocusAnimationView.setVisibility(View.GONE);
    }


    @Override
    public void imageClick(CircleImageView imageButton) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CameraConstant.OPEN_CAMERA_PHOTO);
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
        if (mCountDownTopView.getVisibility()==View.GONE) {
            mCountDownTopView.setVisibility(View.VISIBLE);
            mCameraTop.setVisibility(View.GONE);
            mCountDownTopView.setCurrentSelect();
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
        mCameraTop.setBackgroundColor(0);
    }

    @Override
    public void cameraSwitch() {
        if (mTakePhotoPresenter != null) {
            mTakePhotoPresenter.switchCamera(mCameraDevice);
        }
    }

    private void openCamera(){
        if (mCameraDevice==null) {
            mCameraDevice = CameraInterface.getInstance().openCamera(CameraActivity.this, mCameraId, null, mCameraOpenErrorCallback);
            mParameters = mCameraDevice.getParameters();
        }else {
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
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            boolean isHdPreview = mSharedPreferences.getBoolean(CameraPreference.KEY_HD_PREVIEW, false);
            CameraUtils.setBrightnessForCamera(getWindow(), isHdPreview);
        }
    }

    @Override
    public void closeSettingFragment() {
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void showPictureSizeSelect() {
        PictureSizeDialog pictureSizeDialog = new PictureSizeDialog(CameraActivity.this, mParameters);
        pictureSizeDialog.show();
    }

    /**
     * setting auxiliary line
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
            mTakePhotoPresenter.onConfigurationChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mTakePhotoPresenter!=null){
            mTakePhotoPresenter.onKeyUp(keyCode,event);
        }
       // return super.onKeyDown(keyCode, event);
        return true;//不设置声音
    }

    public CameraManager.CameraOpenErrorCallback mCameraOpenErrorCallback =
            new CameraManager.CameraOpenErrorCallback() {
                @Override
                public void onCameraDisabled(int cameraId) {
                    LogUtils.e(TAG,"CameraOpenErrorCallback onCameraDisabled="+cameraId);
                }

                @Override
                public void onDeviceOpenFailure(int cameraId) {
                    LogUtils.e(TAG,"CameraOpenErrorCallback onDeviceOpenFailure="+cameraId);

                }

                @Override
                public void onReconnectionFailure(CameraManager mgr) {
                    LogUtils.e(TAG,"CameraOpenErrorCallback onReconnectionFailure="+mgr.toString());
                }
            };


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mTakePhotoPresenter != null) {
            mTakePhotoPresenter.onClickAutoFocus();
            mFocusAnimationView.setVisibility(View.VISIBLE);
            mFocusAnimationView.startFocusAnimation(event.getX(), event.getY());
        }
        return true;
    }

    /**
     * setting count down time
     */
    @Override
    public void countDownTopTime() {
        if (mCameraTop.getVisibility()==View.GONE){
            mCameraTop.setVisibility(View.VISIBLE);
            mCountDownTopView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        LogUtils.e(TAG,"nsc onDrawerOpened="+drawerView.getTag());
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        LogUtils.e(TAG,"nsc onDrawerClosed="+drawerView.getTag());
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        LogUtils.e(TAG,"nsc onDrawerStateChanged="+newState);
    }

    /**
     * 启动拍照倒计时
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
     * @return
     */
    @Override
    public int getCurrentCountDownTime() {
        return mCountDownView.getCountDownCurrentTime();
    }
    //count down end
}
