package com.example.administrator.mycamera.view;

import android.hardware.Camera;

import com.example.administrator.mycamera.utils.LogUtils;

/**
 * Created by Administrator on 2018/5/21.
 */

public class MyFaceDetectionListener implements Camera.FaceDetectionListener {

    /**
     * mCamera.setFaceDetectionListener(new MyFaceDetectionListener());
     * @param faces
     * @param camera
     */
    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        if (faces.length > 0){
            LogUtils.d("FaceDetection", "face detected: "+ faces.length +
                    " Face 1 Location X: " + faces[0].rect.centerX() +
                    "Y: " + faces[0].rect.centerY() );
        }
    }

    public void startFaceDetection(Camera camera){
        // Try starting Face Detection
        Camera.Parameters params = camera.getParameters();

        // start face detection only *after* preview has started
        if (params.getMaxNumDetectedFaces() > 0){
            // camera supports face detection, so can start it:
            camera.startFaceDetection();
        }
    }
}
