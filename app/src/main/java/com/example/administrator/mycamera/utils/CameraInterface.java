package com.example.administrator.mycamera.utils;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Handler;

import com.example.administrator.mycamera.activity.CameraActivity;
import com.example.administrator.mycamera.manager.CameraManager;
import com.example.administrator.mycamera.manager.CameraManager.CameraProxy;
import com.example.administrator.mycamera.manager.CameraManagerFactory;
import com.example.administrator.mycamera.model.CameraPreference;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */

public class CameraInterface {
    private final String TAG = "Cam_CameraInterface";

    private static CameraInterface instance;
    private Parameters mParams;
    private Camera mCamera;
    private boolean isPreviewing = false;
    private float mPreviwRate = -1f;
    private static final int CAMERA_ID = 0; //后置摄像头
    //private static final int CAMERA_ID = 1; //前置摄像头

    private CameraProxy mCameraDevice;
    private int mCameraId = -1;  // current camera id
    private boolean mCameraOpened;  // true if camera is opened
    private static CameraProxy mMockCamera[];

    public static CameraInterface getInstance() {
        if (null == instance) {
            synchronized (CameraInterface.class) {
                if (null == instance) {
                    instance = new CameraInterface();
                }
            }
        }
        return instance;
    }

    /**
     * 使用Surfaceview开启预览
     *
     * @param holder
     */
//    public void doStartPreview(SurfaceHolder holder) {
//        Log.i(TAG, "doStartPreview...");
//        if (isPreviewing) {
//            mCamera.stopPreview();
//            return;
//        }
//        if (mCamera != null) {
//            try {
//                mCamera.setPreviewDisplay(holder);
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            initCamera(1.33f);
//        }
//    }

    /**
     * 使用TextureView预览Camera
     *
     * @param surface
     */
    public void doStartPreview(SurfaceTexture surface) {
        LogUtils.e(TAG, "doStartPreview...");
        if (isPreviewing) {
            mCameraDevice.stopPreview();
            mCameraDevice = null;
            return;
        }
        if (mCameraDevice != null) {
            try {
                mCameraDevice.setPreviewTexture(surface);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           // initCamera(1.33f);
        }

    }

    public void initCamera(CameraActivity activity, float previewRate,int cameraId) {
        CameraUtils cameraUtils = new CameraUtils();
        if (mCameraDevice != null) {
            mParams = mCameraDevice.getParameters();
            mParams.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式
            //LogUtils.e(TAG, "ev=" + mParams.getMaxExposureCompensation());
//          CamParaUtil.getInstance().printSupportPictureSize(mParams);
//          CamParaUtil.getInstance().printSupportPreviewSize(mParams);
            //设置PreviewSize和PictureSize
            Camera.Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
                    mParams.getSupportedPictureSizes(), previewRate, 1200);
            String picture = (String) CameraPreference.get(activity,CameraPreference.KEY_PICTURE_SIZE,pictureSize.width+"x"+pictureSize.height);

            String[] sp = picture.split("x");
            LogUtils.e(TAG,"width ="+sp[0] + " htight="+sp[1]);
            //mParams.setPictureSize(pictureSize.width, pictureSize.height);
//            Camera.Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
//                    mParams.getSupportedPreviewSizes(), previewRate, 120);
            mParams.setPictureSize(Integer.valueOf(sp[0]),Integer.valueOf(sp[1]));
            //mParams.setPreviewSize(Integer.valueOf(sp[0]),Integer.valueOf(sp[1]));
            mParams.setFlashMode("auto");
            mParams.setPreviewSize(mParams.getPreviewSize().width, mParams.getPreviewSize().height);

            int degree = cameraUtils.getCameraDisplayOrientation(activity, cameraId);
            mCameraDevice.setDisplayOrientation(degree);

            mParams.setPreviewFormat(PixelFormat.YCbCr_420_SP);
            // List<Camera.Size> supported = mParams.getSupportedPictureSizes();

//          CamParaUtil.getInstance().printSupportFocusMode(mParams);
            List<String> focusModes = mParams.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCameraDevice.setParameters(mParams);
            mCameraDevice.startPreview();//开启预览

            isPreviewing = true;
            mPreviwRate = previewRate;

            mParams = mCameraDevice.getParameters(); //重新get一次
            LogUtils.e(TAG, "最终设置:PreviewSize--With = " + mParams.getPictureSize().width
                    + "Height = " + mParams.getPictureSize().height);
        }
    }


    public CameraProxy openCamera(
            Activity activity, final int cameraId,
            Handler handler, final CameraManager.CameraOpenErrorCallback cb) {
        try {
            throwIfCameraDisabled(activity);
            return getInstance().open(handler, cameraId, cb);
        } catch (CameraDisabledException ex) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    cb.onCameraDisabled(cameraId);
                }
            });
        }
        return null;
    }

    public synchronized CameraManager.CameraProxy open(Handler handler, int cameraId,
                                                       CameraManager.CameraOpenErrorCallback cb) {
        if (mCameraDevice != null) {
            mCameraDevice.stopPreview();
            mCameraDevice.release();
            mCameraDevice = null;
        }
        LogUtils.e(TAG, "open CameraId:" + cameraId );
        if (mCameraDevice == null) {
            mCameraDevice = CameraManagerFactory.getAndroidCameraManager().cameraOpen(handler, cameraId, cb);
            if (mCameraDevice == null) {
                LogUtils.e(TAG, "fail to connect CameraId:" + mCameraId + ", aborting.");
                return null;
            }
            mParams = mCameraDevice.getCamera().getParameters();
        } else {
            if (!mCameraDevice.reconnect(handler, cb)) {
                LogUtils.e(TAG, "fail to reconnect Camera2:" + mCameraId + ", aborting.");
                return null;
            }
            mCameraDevice.setParameters(mParams);
        }
        mCameraOpened = true;
        return mCameraDevice;
    }

    /**
     * 释放相机
     */
    private void release() {
        if (mCameraDevice == null) return;
        mCameraOpened = false;
        mCameraDevice.release();
        mCameraDevice = null;
        mParams = null;
        mCameraId = -1;
    }


    private static void throwIfCameraDisabled(Activity activity) throws CameraDisabledException {
        // Check if device policy has disabled the camera.
        DevicePolicyManager dpm = (DevicePolicyManager) activity.getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        if (dpm.getCameraDisabled(null)) {
            throw new CameraDisabledException();
        }
    }

    // 控制图像的正确显示方向
//    private void setDispaly(Camera camera,Parameters parameters) {
//        if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
//            setDisplayOrientation(camera, -90);
//        } else {
//            parameters.setRotation(-90);
//        }
//
//    }

    // 实现的图像的正确显示
    private void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, new Object[] { i });
            }
        } catch (Exception e) {
            LogUtils.e("Came_e", "图像出错");
        }
    }
}
