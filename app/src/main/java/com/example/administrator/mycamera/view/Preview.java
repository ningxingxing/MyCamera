package com.example.administrator.mycamera.view;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.hardware.Camera.Size;
import android.hardware.Camera.Parameters;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2018/5/21.
 */

public class Preview extends ViewGroup implements SurfaceHolder.Callback {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int mPreviewSize;
    private Parameters mParameters;
    private  List<Size> mSupportedPreviewSizes;

    public Preview(Context context) {
        this(context,null);
    }

    public Preview(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Preview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mSurfaceView = new SurfaceView(context);
        addView(mSurfaceView);

        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    public void initCameraParameters(){
        List<Size> supported = mParameters.getSupportedPictureSizes();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        Camera.Parameters parameters = mCamera.getParameters();
       // parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        requestLayout();
        mCamera.setParameters(parameters);

        // Important: Call startPreview() to start updating the preview surface.
        // Preview must be started before you can take a picture.
        mCamera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    public void setCamera(Camera camera) {
        if (mCamera == camera) return;

       // stopPreviewAndFreeCamera();

        mCamera = camera;

        if (mCamera != null) {
            List<Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
            mSupportedPreviewSizes = localSizes;
            requestLayout();

            try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Important: Call startPreview() to start updating the preview
            // surface. Preview must be started before you can take a picture.
            mCamera.startPreview();
        }
    }
}
