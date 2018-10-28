package com.example.administrator.mycamera.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

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
    public static final int ZIP=7;
    public static final int APK = 8;
    public static final int OTHER = 9;

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

               while ( cursor.moveToNext()) {
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

    public static List<FileInfo> getVideo(Context context){

        List<FileInfo> fileInfoList = new ArrayList<>();
        String CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera";
        String CAMERA_IMAGE_BUCKET_ID = getBucketId(CAMERA_IMAGE_BUCKET_NAME);
        String[] projection = {MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DATE_MODIFIED};
        String selection = MediaStore.Video.Media.BUCKET_ID + " = ?";

        String[] selectionArgs = {CAMERA_IMAGE_BUCKET_ID};
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC");
        if (cursor != null) {
            try {
                FileInfo fileInfo = new FileInfo();
                while ( cursor.moveToNext()) {
                    int thumbPathIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
                    //  long modifyData = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
                    String path = cursor.getString(thumbPathIndex);
                    File file = new File(path);
                    //LogUtils.e(TAG,"nsc path="+path);
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
                || ext.equals("qcp") || ext.equals("awb")|| ext.equals("3gpp")|| ext.equals("ape")|| ext.equals("flac")||ext.equals("midi")) {
            type = MUSIC;
        }
        else if (ext.equals("3gp") || ext.equals("avi") || ext.equals("mp4")
                || ext.equals("3g2") || ext.equals("wmv") || ext.equals("divx")
                || ext.equals("mkv") || ext.equals("webm") || ext.equals("ts")
                || ext.equals("asf")|| ext.equals("mov")||ext.equals("mpg")||ext.equals("flv") ) {
            type = VIDEO;
        }
        else if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("gif")
                || ext.equals("png") || ext.equals("bmp")||ext.equals("wbmp")) {
            type = IMAGE;
        }
        else if (ext.equals("doc") || ext.equals("docx") || ext.equals("xls")
                || ext.equals("xlsx") || ext.equals("ppt") || ext.equals("pptx")
                || ext.equals("txt") || ext.equals("text") || ext.equals("pdf")|| ext.equals("html")) {
            type = DOC;
        }
        else if (ext.equals("rar") || ext.equals("zip") || ext.equals("tar") || ext.equals("gz")
                || ext.equals("iso") || ext.equals("jar") || ext.equals("cab") || ext.equals("7z")
                || ext.equals("ace")) {
            type = ZIP;
        }
        else if (ext.equals("apk")) {
            type = APK;
        }
        else {
            type = OTHER;
        }
        return type;
    }
}
