package com.example.administrator.mycamera.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.activity.CameraActivity;
import com.example.administrator.mycamera.manager.CameraManager;
import com.example.administrator.mycamera.model.CameraPreference;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2018/6/12.
 */

public class CameraUtils {
    private static final String TAG = "Cam_CameraUtils";
    // Orientation hysteresis amount used in rounding, in degrees
    public static final int ORIENTATION_HYSTERESIS = 5;
    public static final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String EXTERNAL_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
    public static final String DEFAULT_CAMERA_DIR = EXTERNAL_DIR + "/Camera";
    public static final String DEFAULT_SAVE_PATH = DEFAULT_CAMERA_DIR;

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
     *
     * @param mActivity
     * @param cameraId
     * @return
     */
    public int getCameraId(CameraActivity mActivity, int cameraId) {
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
        LogUtils.e("nsc", "cameraId=" + cameraId);
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

    public int getDisplayOrientation(int degrees, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    public int getCameraOrientation(int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        return info.orientation;
    }

    public int roundOrientation(int orientation, int orientationHistory) {
        boolean changeOrientation = false;
        if (orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
            changeOrientation = true;
        } else {
            int dist = Math.abs(orientation - orientationHistory);
            dist = Math.min(dist, 360 - dist);
            changeOrientation = (dist >= 45 + ORIENTATION_HYSTERESIS);
        }
        if (changeOrientation) {
            return ((orientation + 45) / 90 * 90) % 360;
        }
        return orientationHistory;
    }

    public int getDisplayRotation(Activity activity) {
        int i = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (i) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    /**
     * 获取倒计时时间
     * @param context
     * @return
     */
    public int getCountDownTime(Context context){
        String countDownDuration = (String)CameraPreference.get(context,CameraPreference.KEY_COUNT_DOWN,context.getString(R.string.count_down_top_close));
        if (context.getString(R.string.count_down_top_two).equals(countDownDuration)){
            return 2;
        }else if (context.getString(R.string.count_down_top_five).equals(countDownDuration)){
            return 5;
        }else if (context.getString(R.string.count_down_top_ten).equals(countDownDuration)){
            return 10;
        } else{
            return 0;
        }

    }

    public static void prepareMatrix(Matrix matrix, boolean mirror, int displayOrientation,
                                     int viewWidth, int viewHeight) {
        // Need mirror for front camera.
        matrix.setScale(mirror ? -1 : 1, 1);
        // This is the value for android.hardware.Camera.setDisplayOrientation.
        matrix.postRotate(displayOrientation);
        // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
        // UI coordinates range from (0, 0) to (width, height).
        matrix.postScale(viewWidth / 2000f, viewHeight / 2000f);
        matrix.postTranslate(viewWidth / 2f, viewHeight / 2f);
    }

    public static void dumpRect(RectF rect, String msg) {
        LogUtils.v(TAG, msg + "=(" + rect.left + "," + rect.top
                + "," + rect.right + "," + rect.bottom + ")");
    }

    public static String doubleToString(double num){
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }

    public static String longToSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.0");
        String fileSizeString = "";
        String wrongSize = "(0B)";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1000) {
            fileSizeString = df.format((double) fileS) + "B)";
        } else if (fileS < 1000000) {
            fileSizeString = df.format((double) fileS / 1024) + "K)";
        } else if (fileS < 1000000000) {
            fileSizeString = df.format((double) fileS / 1048576) + "M)";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G)";
        }
        return fileSizeString;
    }




    /**
     * 获取屏幕宽度
     * @return
     */
    public int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     * @return
     */
    public int getScreenHeight(Activity activity){
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
}
