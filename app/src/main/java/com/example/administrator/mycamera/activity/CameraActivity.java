package com.example.administrator.mycamera.activity;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.SurfaceTexture;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.manager.CameraManager.CameraProxy;
import com.example.administrator.mycamera.model.CameraPreference;
import com.example.administrator.mycamera.port.IBottomClick;
import com.example.administrator.mycamera.port.ITopClick;
import com.example.administrator.mycamera.presenter.ITakePhoto;
import com.example.administrator.mycamera.presenter.TakePhotoPresenter;
import com.example.administrator.mycamera.utils.CameraInterface;
import com.example.administrator.mycamera.utils.CameraParameter;
import com.example.administrator.mycamera.utils.CameraUtils;
import com.example.administrator.mycamera.utils.FlashOverlayAnimation;
import com.example.administrator.mycamera.utils.LogUtils;
import com.example.administrator.mycamera.view.CameraGLSurfaceView;
import com.example.administrator.mycamera.view.buttonview.CameraBottomView;
import com.example.administrator.mycamera.view.buttonview.CircleImageView;

/**
 * Created by Administrator on 2018/5/21.
 */

public class CameraActivity extends Activity implements ITakePhoto, IBottomClick,ITopClick, LoaderManager.LoaderCallbacks<Cursor>, TextureView.SurfaceTextureListener {

    private final String TAG = "Cam_CameraActivity";
    private CameraGLSurfaceView mGlSurfaceView;
    private TakePhotoPresenter mTakePhotoPresenter;
    private CameraBottomView mCameraBottom;
    private DrawerLayout mDrawerLayout;

    private CameraProxy mCameraDevice;
    private Parameters mParameters;
    private int mCameraId = 0;
    private CircleImageView mImageButton;
    private View mFlashOverlay;
    private SeekBar mEvSeekBar;

    private FlashOverlayAnimation mFlashOverlayAnimation;
    private CameraParameter mCameraParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

        initView();

    }

    private void initView() {
        mTakePhotoPresenter = new TakePhotoPresenter(CameraActivity.this, this);
        mGlSurfaceView = (CameraGLSurfaceView) findViewById(R.id.gl_surfaceView);
        mGlSurfaceView.setSurfaceTextureListener(this);

        mCameraBottom = (CameraBottomView) findViewById(R.id.camera_bottom);
        mCameraBottom.setBottomClickListener(this);

        mFlashOverlay = (View)findViewById(R.id.flash_overlay);
        mFlashOverlayAnimation = new FlashOverlayAnimation();

        mEvSeekBar = (SeekBar)findViewById(R.id.evSeekBar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
       // mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    private void initData() {
        CameraUtils.setBrightnessForCamera(getWindow(),false);
        int MaxEV = CameraParameter.getCameraMaxExposureCompensation(mParameters);
        mEvSeekBar.setMax(MaxEV);
        CameraPreference.saveIntPreference(this,CameraPreference.KEY_EXPOSURE_COMPENSATION,MaxEV);
        CameraParameter.getCameraSupportedSceneMode(mParameters);
        mEvSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtils.e(TAG,"progress="+progress);
                CameraParameter.setCameraExposureCompensation(mCameraDevice,mParameters,progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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

        if (mCameraDevice==null) {
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
        if (mTakePhotoPresenter!=null) {
            mTakePhotoPresenter.shutterClick();
        }
    }

    @Override
    public void showFlashOverlayAnimation(){
        //启动闪光灯动画
        if (mFlashOverlayAnimation!=null) {
            mFlashOverlayAnimation.startFlashAnimation(mFlashOverlay);
        }
    }

    @Override
    public void displayProgress(boolean disable) {
        mCameraBottom.displayProgress(disable);
    }


    @Override
    public void imageClick(CircleImageView imageButton) {
        this.mImageButton = imageButton;
        getLoaderManager().initLoader(1, null, CameraActivity.this);
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
        LogUtils.e(TAG,"onSurfaceTextureAvailable");
        if (mCameraDevice != null) {
            mCameraDevice.setPreviewTexture(surface);
            startPreview();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        LogUtils.e(TAG,"onSurfaceTextureSizeChanged");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        LogUtils.e(TAG, "onSurfaceTextureDestroyed");
        if (mCameraDevice != null) {
            mCameraDevice.setPreviewTexture(null);
            stopPreview();
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        LogUtils.e(TAG,"onSurfaceTextureUpdated");
    }

    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID};

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader =
                new CursorLoader(this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
        return cursorLoader;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mImageButton == null || data == null) return;
        if (data != null) {
            int index = 0;
            int count = data.getCount();
            if (count > 0) {
                int thumbPathIndex = 0;
                if (data.moveToNext()) {
                    thumbPathIndex = data.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                }
                do {
                    index++;
                    if (index < 2) {
                        String path = data.getString(thumbPathIndex);
                        LogUtils.e("GetThumbnail", "onLoadFinished path=" + path);
                        Glide.with(this)  //可以是Context Activity Fragment FragmentActivy
                                .load(path)//图片加载路径可以支持多种路径
                                .placeholder(R.drawable.icon_thumb)//加载中显示的图片
                                .error(R.drawable.icon_thumb)//加载失败显示的图片
                                .into(mImageButton);//搭载的ImageView 用于显示加载图片
                        break;
                    }

                } while (data.moveToNext());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
}
