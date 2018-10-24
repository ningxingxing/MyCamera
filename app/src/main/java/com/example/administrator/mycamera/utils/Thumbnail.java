package com.example.administrator.mycamera.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Administrator on 2018/6/26.
 */

public class Thumbnail {
    private static final String TAG = "Thumbnail";

    /**
     * 获取缩略图显示
     *
     * @param context
     * @return
     */
    public static String getImageThumbnail(Context context) {
        String path = null;
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
                if (cursor.moveToFirst()) {
                    int thumbPathIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    //  long modifyData = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
                    path = cursor.getString(thumbPathIndex);
                    LogUtils.e(TAG, "getImageThumbnail path=" + path);
                }

            } finally {
                cursor.close();
            }

        }

        //return getImageThumbnail(path,500,500);
        return path;
    }

    private static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }

    public static String getVideoThumbnail(Context context) {
        String path = null;
        String CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera";
        String CAMERA_IMAGE_BUCKET_ID = getBucketId(CAMERA_IMAGE_BUCKET_NAME);
        String[] projection = {MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DATE_MODIFIED};
        String selection = MediaStore.Video.Media.BUCKET_ID + " = ?";

        String[] selectionArgs = {CAMERA_IMAGE_BUCKET_ID};
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);
        if (cursor.moveToLast()) {
            int thumbPathIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
            path = cursor.getString(thumbPathIndex);
            LogUtils.e(TAG, "getVideoThumbnail path=" + path);
        }
        return path;
        //return getImageThumbnail(path, 300, 300);
    }


    public static String comparedThumbPath(Context context) {

        String imagePath = getImageThumbnail(context);
        String videoPath = getVideoThumbnail(context);


       // Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MICRO_KIND);
        // 图片Bitmap转file
       // File file = CommonUtils.compressImage(bitmap);

        LogUtils.e(TAG,"nsc image="+imagePath + " video="+videoPath);
        if (new File(imagePath).lastModified() > new File(videoPath).lastModified()) {//时间短的最新
            return imagePath;
        } else {
            return videoPath;
        }
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图
     * 此方法有两点好处：
     * 1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
     * 2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
     * 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    private static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                     int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        System.out.println("w" + bitmap.getWidth());
        System.out.println("h" + bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
