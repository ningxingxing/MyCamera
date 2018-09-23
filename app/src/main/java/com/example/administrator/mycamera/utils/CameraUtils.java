package com.example.administrator.mycamera.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.view.Surface;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.activity.CameraActivity;
import com.example.administrator.mycamera.manager.CameraManager;
import com.example.administrator.mycamera.model.CameraPreference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2018/6/12.
 */

public class CameraUtils {


    public static String ms2Date(long ms) {
        Date date = new Date(ms);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }


    /**
     * 强制设置屏幕亮度为最大值
     */
    public static void setBrightnessForCamera(Window window, boolean isHdPreView) {
        final WindowManager.LayoutParams layout = window.getAttributes();
        if (isHdPreView) {
            layout.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
        } else {
            layout.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        }
        window.setAttributes(layout);
    }

    /**
     * get camera id
     * @param mActivity
     * @param cameraId
     * @return
     */
    public int getCameraId(CameraActivity mActivity,int cameraId) {
        if (mActivity.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FRONT)) {
            if (cameraId == CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = CameraInfo.CAMERA_FACING_BACK;
            } else {
                cameraId = CameraInfo.CAMERA_FACING_FRONT;
            }
        } else {
            Toast.makeText(mActivity, R.string.not_support_front_facing_camera,
                    Toast.LENGTH_SHORT).show();
            return cameraId;
        }
        //save camera id
        CameraPreference.setCameraId(mActivity, cameraId);
        return cameraId;
    }

    private static boolean checkCameraFacing(final int facing) {

        final int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (facing == info.facing) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasBackFacingCamera() {
        final int CAMERA_FACING_BACK = 0;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }

    public static boolean hasFrontFacingCamera() {
        final int CAMERA_FACING_BACK = 1;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }

    public int getCameraDisplayOrientation(Activity activity, int cameraId) {

        Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        //获取摄像头信息
        LogUtils.e("nsc","cameraId="+cameraId);
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        //获取摄像头当前的角度
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            //前置摄像头
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else {
            // back-facing  后置摄像头
            result = (info.orientation - degrees + 360) % 360;
        }
       return result;
    }

    public static int getDisplayOrientation(int degrees, int cameraId) {
        // See android.hardware.Camera.setDisplayOrientation for
        // documentation.
        Camera.CameraInfo info = new Camera.CameraInfo();
        //modify by nsc
        Camera.getCameraInfo(cameraId, info);
//        if (hasFrontFacingCamera() && cameraId==0) {
//            Camera.getCameraInfo(cameraId, info);
//        }else if (hasBackFacingCamera() && cameraId==1){
//            Camera.getCameraInfo(cameraId, info);
//        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    public int getDisplayRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0: return 0;
            case Surface.ROTATION_90: return 90;
            case Surface.ROTATION_180: return 180;
            case Surface.ROTATION_270: return 270;
        }
        return 0;
    }


}
