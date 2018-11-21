package com.example.administrator.mycamera.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.mycamera.model.FileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryUtils {

    private static final String TAG = "Cam_GalleryUtils";

    public static final int IMAGE = 0;
    public static final int VIDEO = 4;
    public static final int MUSIC = 5;
    public static final int DOC = 6;
    public static final int ZIP = 7;
    public static final int APK = 8;
    public static final int OTHER = 9;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Activity activity, int color) {

        Window window = activity.getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(color);

        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static List<FileInfo> getImage(Context context) {
        List<FileInfo> fileInfoList = new ArrayList<>();
        String CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera";
        String CAMERA_IMAGE_BUCKET_ID = getBucketId(CAMERA_IMAGE_BUCKET_NAME);
        String[] projection = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_MODIFIED};
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";

        String[] selectionArgs = {CAMERA_IMAGE_BUCKET_ID};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC");
        if (cursor != null) {
            try {

                while (cursor.moveToNext()) {
                    int thumbPathIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    //  long modifyData = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
                    String path = cursor.getString(thumbPathIndex);
                    File file = new File(path);

                    // LogUtils.e(TAG,"nsc path="+path);
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setFilePath(path);
                    fileInfo.setFile(file.isFile());
                    fileInfo.setModifiedData(file.lastModified());
                    fileInfoList.add(fileInfo);
                }

            } finally {
                cursor.close();
            }

        }
        return fileInfoList;
    }

    public static List<FileInfo> getVideo(Context context) {

        List<FileInfo> fileInfoList = new ArrayList<>();
        String CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera";
        String CAMERA_IMAGE_BUCKET_ID = getBucketId(CAMERA_IMAGE_BUCKET_NAME);
        String[] projection = {MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION};
        String selection = MediaStore.Video.Media.BUCKET_ID + " = ?";

        String[] selectionArgs = {CAMERA_IMAGE_BUCKET_ID};
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC");
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int thumbPathIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
                    int durationIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION);
                    FileInfo fileInfo = new FileInfo();
                    String path = cursor.getString(thumbPathIndex);
                    long duration = cursor.getLong(durationIndex);
                    File file = new File(path);
                    fileInfo.setFilePath(path);
                    fileInfo.setFile(file.isFile());
                    fileInfo.setModifiedData(file.lastModified());
                    fileInfo.setFileS(duration);
                    fileInfoList.add(fileInfo);
                }

            } finally {
                cursor.close();
            }

        }
        LogUtils.e(TAG, "nsc path =" + fileInfoList.size());
        return fileInfoList;
    }

    private static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }


    public static int getFileType(File f) {
        int type = -1;
        String fileName = f.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".")
                + 1, fileName.length()).toLowerCase();
        if (ext.equals("mp3") || ext.equals("amr") || ext.equals("wma")
                || ext.equals("aac") || ext.equals("m4a") || ext.equals("mid")
                || ext.equals("xmf") || ext.equals("ogg") || ext.equals("wav")
                || ext.equals("qcp") || ext.equals("awb") || ext.equals("3gpp") || ext.equals("ape") || ext.equals("flac") || ext.equals("midi")) {
            type = MUSIC;
        } else if (ext.equals("3gp") || ext.equals("avi") || ext.equals("mp4")
                || ext.equals("3g2") || ext.equals("wmv") || ext.equals("divx")
                || ext.equals("mkv") || ext.equals("webm") || ext.equals("ts")
                || ext.equals("asf") || ext.equals("mov") || ext.equals("mpg") || ext.equals("flv")) {
            type = VIDEO;
        } else if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("gif")
                || ext.equals("png") || ext.equals("bmp") || ext.equals("wbmp")) {
            type = IMAGE;
        } else if (ext.equals("doc") || ext.equals("docx") || ext.equals("xls")
                || ext.equals("xlsx") || ext.equals("ppt") || ext.equals("pptx")
                || ext.equals("txt") || ext.equals("text") || ext.equals("pdf") || ext.equals("html")) {
            type = DOC;
        } else if (ext.equals("rar") || ext.equals("zip") || ext.equals("tar") || ext.equals("gz")
                || ext.equals("iso") || ext.equals("jar") || ext.equals("cab") || ext.equals("7z")
                || ext.equals("ace")) {
            type = ZIP;
        } else if (ext.equals("apk")) {
            type = APK;
        } else {
            type = OTHER;
        }
        return type;
    }

}
