package com.example.administrator.mycamera.utils;

import android.hardware.Camera;
import android.view.Window;
import android.view.WindowManager;

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
}
