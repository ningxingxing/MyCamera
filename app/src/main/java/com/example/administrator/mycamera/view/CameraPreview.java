package com.example.administrator.mycamera.view;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.administrator.mycamera.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/21.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
    private static final String TAG = "nsc_CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private MyFaceDetectionListener myFaceDetectionListener;


    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        myFaceDetectionListener = new MyFaceDetectionListener();
        mCamera.setFaceDetectionListener(myFaceDetectionListener);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

            myFaceDetectionListener.startFaceDetection(mCamera);

        } catch (IOException e) {
            LogUtils.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

            myFaceDetectionListener.startFaceDetection(mCamera); // re-start face detection feature

        } catch (Exception e){
            LogUtils.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void getCameraParemeters(){
        // get Camera parameters
        Camera.Parameters params = mCamera.getParameters();

        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            // Autofocus mode is supported
            // set the focus mode
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            // set Camera parameters
            mCamera.setParameters(params);
        }
    }

    public void setCameraParemeters(){
        // set Camera parameters
        Camera.Parameters params = mCamera.getParameters();

        if (params.getMaxNumMeteringAreas() > 0){ // check that metering areas are supported
            List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();

            Rect areaRect1 = new Rect(-100, -100, 100, 100);    // specify an area in center of image
            meteringAreas.add(new Camera.Area(areaRect1, 600)); // set weight to 60%
            Rect areaRect2 = new Rect(800, -1000, 1000, -800);  // specify an area in upper right of image
            meteringAreas.add(new Camera.Area(areaRect2, 400)); // set weight to 40%
            params.setMeteringAreas(meteringAreas);
        }

        mCamera.setParameters(params);
    }
}
