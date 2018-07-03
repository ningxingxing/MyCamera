package com.example.administrator.mycamera.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/6/7.
 */

public class CameraPreference {

    public CameraPreference() {
    }

    //camera id
    public static final String CAMERA_ID = "cameraId";
    //曝光补偿
    public static final String KEY_EXPOSURE_COMPENSATION = "key_exposure_compensation";
    //白平衡
    public static final String KEY_WHITE_BALANCE = "key_white_balance";
    //场景模式
    public static final String KEY_SCENE_MODE = "key_scene_mode";
    //闪光灯
    public static final String KEY_FLASH_MODE = "key_flash_mode";
    //hdr
    public static final String KEY_SCENE_MODE_HDR = "key_scene_mode_hdr";

    public static final String KEY_ISO_MODE = "iso";

    public static final String KEY_PICTURE_SIZE="key_picture_size";
    public static final String KEY_AUXILIARY_LINE = "key_auxiliary_line";
    public static final String KEY_LOCATION = "key_location";
    public static final String KEY_TAKE_PICTURE_SOUND = "key_take_picture_sound";
    public static final String KEY_DELAY_SOUND = "key_delay_sound";
    public static final String KEY_FOCUSED_SOUND = "key_focused_sound";
    public static final String KEY_VOLUME_SOUND= "key_volume_sound";
    public static final String KEY_HD_PREVIEW = "key_hd_preview";

    public static final String FOCUS_MODE_CONTINUOUS_PICTURE = "continuous-picture";


    public static void setCameraId(Context context, int cameraId) {
        SharedPreferences pref = context.getSharedPreferences(CAMERA_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(CAMERA_ID, cameraId);
        editor.commit();
    }

    public static int getCameraId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(CAMERA_ID, Context.MODE_PRIVATE);
        return pref.getInt(CAMERA_ID, 0);
    }


    /**
     * 保存字符串数据到sharedPreference
     * @param key
     * @param tag
     */
    public static void saveStringPreference(Context context,String key, String tag) {
        SharedPreferences pref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, tag);
        editor.commit();
    }

    /**
     * 获取保存到sharedPreference的数据
     * @param key
     * @return
     */
    public static String getStringPreference(Context context,String key) {
        SharedPreferences pref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    /**
     * 保存整形数据
     * @param context
     * @param key
     * @param value
     */
    public static void saveIntPreference(Context context,String key,int value){
        SharedPreferences pref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntPreference(Context context,String key){
        SharedPreferences pref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }


    /**
     * 保存boolean数据
     * @param context
     * @param key
     * @param flag
     */
    public static void saveBooleanPreference(Context context,String key,boolean flag){
        SharedPreferences pref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, flag);
        editor.commit();
    }

    public static boolean getBooleanPreference(Context context,String key){
        SharedPreferences pref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }
}
