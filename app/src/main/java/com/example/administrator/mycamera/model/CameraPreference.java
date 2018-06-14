package com.example.administrator.mycamera.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/6/7.
 */

public class CameraPreference {

    private Context mContext;

    public CameraPreference() {
    }

    public CameraPreference(Context context) {
        this.mContext = context;
    }

    public void setCameraId(Context context, int cameraId) {
        SharedPreferences pref = context.getSharedPreferences("cameraId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("cameraId", cameraId);
        editor.commit();
    }

    public int getCameraId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("cameraId", Context.MODE_PRIVATE);
        return pref.getInt("cameraId", 0);
    }


    /**
     * 保存字符串数据到sharedPreference
     * @param key
     * @param tag
     */
    public void savePreference(String key, String tag) {
        SharedPreferences pref = mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, tag);
        editor.commit();
    }

    /**
     * 获取保存到sharedPreference的数据
     * @param key
     * @return
     */
    public String getPreference(String key) {
        SharedPreferences pref = mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }
}
