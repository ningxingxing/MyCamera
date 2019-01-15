package com.example.administrator.mycamera;

import android.app.Application;

import com.example.administrator.mycamera.model.FileInfo;
import com.github.anrwatchdog.ANRWatchDog;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/13.
 */

public class MyApplication extends Application {

    public ArrayList<FileInfo> mImageTimeList;
    public ArrayList<FileInfo> mImageDetailList;

    private static MyApplication instance = null;
    public static synchronized MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        new ANRWatchDog().start();
    }
}
