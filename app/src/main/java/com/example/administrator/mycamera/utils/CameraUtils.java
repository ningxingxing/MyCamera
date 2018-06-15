package com.example.administrator.mycamera.utils;

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
    public static void setBrightnessForCamera(Window window) {
        final WindowManager.LayoutParams layout = window.getAttributes();
        layout.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
        window.setAttributes(layout);
       // layout.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
    }
}
