package com.example.administrator.mycamera.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.example.administrator.mycamera.model.CameraPreference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2018/6/12.
 */

public class SaveImageUtils {
    private static final String TAG = "Cam_SaveImageUtils";

    /**
     * 保存图片
     * @param handler
     * @param context
     * @param data
     */
    public static void saveImage(Handler handler, Context context, byte[] data) {
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        int cameraId =   CameraPreference.getCameraId(context);
        if (cameraId==0) {
            bmp = rotateBitmapByDegree(bmp, 90);
        }else {
            bmp = rotateBitmapByDegree(bmp, -90);
        }
        FileOutputStream fOut = null;
        String path = getSaveImagePath(context);
        try {
            fOut = new FileOutputStream(path);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        } catch (IOException e) {
            LogUtils.e(TAG, "e=" + e.getMessage());
        } finally {
            try {
                if (fOut != null) {
                    fOut.flush();
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateImageToDb(handler, context, path);
        }
    }

    /**
     * 获取保存图片的路径
     *
     * @return
     */
    public static String getSaveImagePath(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String fileName = ms2Date(System.currentTimeMillis()) + ".jpg";
            String folder_path = PreferenceManager.getDefaultSharedPreferences(context).getString(CameraPreference.KEY_STORAGE_PATH, CameraUtils.DEFAULT_SAVE_PATH);
           // String filePath = Environment.getExternalStorageDirectory() + "/DCIM/Camera";
            File fp = new File(folder_path);
            if (!fp.exists()) {
                fp.mkdir();
            }
            File f = new File(folder_path, fileName);
            return f.getPath();
        }
        return System.currentTimeMillis() + ".jpg";
    }


    /**
     * 将图片更新到数据库
     *
     * @param context
     * @param filename
     */
    private static void updateImageToDb(final Handler handler, Context context, String filename) {
        MediaScannerConnection.scanFile(context,
                new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        LogUtils.e("updateImageToDb success", "path " + path + ":" + "uri=" + uri);
                        //getBitmapDegree(path);
                        handler.sendEmptyMessage(CameraConstant.SUCCESS_UPDATE_IMAGE_ToDb);
                    }
                });
    }

    private static int getBitmapDegree(String path) {

        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtils.e(TAG, "getBitmapDegree=" + degree);
        return degree;
    }

    /**
     * 旋转图片角度
     *
     * @param bm
     * @param degree
     * @return
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * ms to date
     * @param ms
     * @return
     */
    public static String ms2Date(long ms) {
        Date date = new Date(ms);
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
        return format.format(date);
    }
}
