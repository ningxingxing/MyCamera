package com.example.administrator.mycamera.utils;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by apple on 18/5/13.
 */

public class DeleteFileUtils {
    private final static String TAG = "DeleteFileUtils";

//    public void deleteFile(Context context, File file){
//        switch (Common.getFileType(file)){
//
//            case Common.IMAGE:
//                batchDeleteImage(context,file.getPath());
//                break;
//
//            case Common.VIDEO:
//                batchDeleteVideo(context,file.getPath());
//                break;
//
//            case Common.MUSIC:
//                batchDeleteAudio(context,file.getPath());
//                break;
//
//            case Common.DOC:
//            case Common.APK:
//            case Common.OTHER:
//            case Common.ZIP:
//                batchDeleteFile(context,file.getPath());
//                break;
//        }
//    }

    /**
     * delete file
     * @param context
     * @param path
     */
    public static void batchDeleteFile(Context context, String path){

        try {
            //Uri uri = Uri.parse("external");
            //Uri uri = MediaStore.Files.getContentUri("external");
            Uri uri = Uri.parse("content://com.android.contacts/data");
            ArrayList ops = new ArrayList();
            ops.add(ContentProviderOperation.newDelete(uri)
                    .withSelection(MediaStore.Files.FileColumns.DATA+"external=?",new String[]{path})
                    .build());
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY,ops);
        }catch (Exception e){
            Log.e(TAG,"Unknown authority="+e.getMessage());
        }

    }

    /**
     * delete image
     * @param context
     * @param path
     */
    public static void batchDeleteImage(Context context, String path){
        Uri uri = MediaStore.Files.getContentUri("external");
        ArrayList ops = new ArrayList();
        ops.add(ContentProviderOperation.newDelete(uri)
                .withSelection(MediaStore.Images.ImageColumns.DATA+"=?",new String[]{path})
                .build());

        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY,ops);
        }catch (Exception e){
            Log.e(TAG,"Unknown authority");
        }

    }

    /**
     * delete video
     * @param context
     * @param path
     */
    public static void batchDeleteVideo(Context context, String path){
        Uri uri = MediaStore.Files.getContentUri("external");
        ArrayList ops = new ArrayList();
        ops.add(ContentProviderOperation.newDelete(uri)
                .withSelection(MediaStore.Video.VideoColumns.DATA+"=?",new String[]{path})
                .build());

        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY,ops);
        }catch (Exception e){
            Log.e(TAG,"Unknown authority");
        }

    }

    /**
     * delete audio
     * @param context
     * @param path
     */
    public static void batchDeleteAudio(Context context, String path){
        Uri uri = MediaStore.Files.getContentUri("external");
        ArrayList ops = new ArrayList();
        ops.add(ContentProviderOperation.newDelete(uri)
                .withSelection(MediaStore.Audio.AudioColumns.DATA+"=?",new String[]{path})
                .build());

        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY,ops);
        }catch (Exception e){
            Log.e(TAG,"Unknown authority");
        }

    }


    public static void imageDeleteRecord(String filepath, Context mContext) {
        mContext.getContentResolver().delete(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Media.DATA + "=?", new String[]{filepath});
    }

    public static void videoDeleteRecord(String filepath, Context mContext) {
        mContext.getContentResolver().delete(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Video.Media.DATA + "=?", new String[]{filepath});
    }

    public static void deleteFileRecord(Context context, String filepath) {
        String volumeName = "external";
        Uri dbUri = MediaStore.Files.getContentUri(volumeName);
        context.getContentResolver().delete(dbUri, MediaStore.Files.FileColumns.DATA + "=?"
                , new String[]{filepath});
    }
}
