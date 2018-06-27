package com.example.administrator.mycamera.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.widget.DrawerLayout;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.fragment.ModelFragment;
import com.example.administrator.mycamera.fragment.SettingFragment;
import com.example.administrator.mycamera.manager.CameraManager.CameraProxy;
import com.example.administrator.mycamera.model.CameraPreference;
import com.example.administrator.mycamera.port.IBottomItem;
import com.example.administrator.mycamera.port.ISettingFragment;
import com.example.administrator.mycamera.port.ITopItem;
import com.example.administrator.mycamera.presenter.ITakePhoto;
import com.example.administrator.mycamera.presenter.TakePhotoPresenter;
import com.example.administrator.mycamera.utils.CameraConstant;
import com.example.administrator.mycamera.utils.CameraInterface;
import com.example.administrator.mycamera.utils.CameraParameter;
import com.example.administrator.mycamera.utils.CameraUtils;
import com.example.administrator.mycamera.utils.FlashOverlayAnimation;
import com.example.administrator.mycamera.utils.LogUtils;
import com.example.administrator.mycamera.utils.Thumbnail;
import com.example.administrator.mycamera.view.AuxiliaryLineView;
import com.example.administrator.mycamera.view.CameraGLSurfaceView;
import com.example.administrator.mycamera.view.PictureSizeDialog;
import com.example.administrator.mycamera.view.buttonview.CameraBottomView;
import com.example.administrator.mycamera.view.buttonview.CircleImageView;

/**
 * Created by Administrator on 2018/5/21.
 */

public class CameraActivity extends Activity implements ITakePhoto, IBottomItem, ITopItem,
        ISettingFragment, TextureView.SurfaceTextureListener {

    private final String TAG = "Cam_CameraActivity";
    private CameraGLSurfaceView mGlSurfaceView;
    private TakePhotoPresenter mTakePhotoPresenter;
    private CameraBottomView mCameraBottom;
    private DrawerLayout mDrawerLayout;
    private AuxiliaryLineView mAuxiliaryLine;
    private View mFlashOverlay;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

        initView();
        initFragment();
    }

    private void initView() {
        mTakePhotoPresenter = new TakePhotoPresenter(CameraActivity.this, this);
        mGlSurfaceView = (CameraGLSurfaceView) findViewById(R.id.gl_surfaceView);
        mGlSurfaceView.setSurfaceTextureListener(this);

        mCameraBottom = (CameraBottomView) findViewById(R.id.camera_bottom);
        mCameraBottom.setBottomClickListener(this);

        mFlashOverlay = (View) findViewById(R.id.flash_overlay);
        mFlashOverlayAnimation = new FlashOverlayAnimation();

        mEvSeekBar = (SeekBar) findViewById(R.id.evSeekBar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mDrawerLayout.addDrawerListener(this);
        mAuxiliaryLine = (AuxiliaryLineView) findViewById(R.id.auxiliary_line);
    }

    private void initData() {
        CameraUtils.setBrightnessForCamera(getWindow(), false);
        int MaxEV = CameraParameter.getCameraMaxExposureCompensation(mParameters);
        mEvSeekBar.setMax(MaxEV);
        CameraPreference.saveIntPreference(this, CameraPreference.KEY_EXPOSURE_COMPENSATION, MaxEV);
        CameraParameter.getCameraSupportedSceneMode(mParameters);
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCameraDevice == null) {
            mCameraDevice = CameraInterface.getInstance().openCamera(CameraActivity.this, mCameraId, null, null);
            mParameters = mCameraDevice.getParameters();
        }
        mGlSurfaceView.onResume();
        mGlSurfaceView.bringToFront();
        mTakePhotoPresenter.onResumeSuper(mCameraDevice);
        initData();
    }

    @Override
    public void videoClick() {

    }

    @Override
    public void shutterClick(ImageButton id) {
        if (mTakePhotoPresenter != null) {
            mTakePhotoPresenter.shutterClick();
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
        Bitmap bitmap = Thumbnail.getImageThumbnail(CameraActivity.this);
        if (bitmap != null) {
            mCameraBottom.showThumbnail(bitmap);
        }
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
            CameraInterface.getInstance().initCamera(1.33f);
        }
    }

    @Override
    public void stopPreview() {
        if (mCameraDevice != null) {
            mCameraDevice.setErrorCallback(null);
            mCameraDevice.setZoomChangeListener(null);
            mCameraDevice.setFaceDetectionCallback(null, null);
            mCameraDevice.stopPreview();
            mCameraDevice.release();
            mCameraDevice = null;
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (mCameraDevice != null) {
            mCameraDevice.setPreviewTexture(surface);
            startPreview();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

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

    }

    @Override
    public void cameraFlash() {
        // CameraParameter.setCameraFlashMode(mParameters,);
    }

    @Override
    public void cameraDelay() {

    }

    @Override
    public void cameraWhiteBalance() {

    }

    @Override
    public void cameraScene() {

    }

    @Override
    public void cameraSwitch() {

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
        beginTransaction.add(containerViewId, fragment, tag);
        //add current fragment to backStack
        beginTransaction.addToBackStack(tag);
        beginTransaction.commit();
    }

    /**
     * remove fragment
     */
    private void removeFragment() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (ft!=null) {
            if (mSettingFragment != null && mSettingFragment.isVisible()) {
                ft.remove(mSettingFragment);
            }
            if (mModelFragment != null && mModelFragment.isVisible()) {
                ft.remove(mModelFragment);
            }
            ft.commit();
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
            LogUtils.e(TAG, "initFragment isHdPreview=" + isHdPreview);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG,"onDestroy");
        removeFragment();
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

    @Override
    public void setAuxiliaryLine(boolean flag) {
        mAuxiliaryLine.setAuxiliaryLine(flag);
    }
}
