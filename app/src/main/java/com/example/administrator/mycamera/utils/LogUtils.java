package com.example.administrator.mycamera.utils;

import android.util.Log;

/**
 * Created by Administrator on 2018/5/21.
 */

public class LogUtils {
    //close or open debug
    private static boolean isDebug = true;

    public static void d(String tag,String msg){
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag,String msg){
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag,String msg){
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void i(String tag,String msg){
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag,String msg){
        if (isDebug) {
            Log.w(tag,msg);
        }
    }


}
