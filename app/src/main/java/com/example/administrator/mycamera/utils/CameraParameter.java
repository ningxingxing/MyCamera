package com.example.administrator.mycamera.utils;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Size;

import com.example.administrator.mycamera.manager.CameraManager.CameraProxy;
import com.example.administrator.mycamera.model.CameraPreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/14.
 */

public class CameraParameter {
    private static final String TAG = "Cam_CameraParameter";

    public void initCameraParameter(Context context, CameraProxy cameraDevice, Parameters parameters) {

    }

    /**
     * set camera exposure compensation
     *
     * @param cameraDevice
     * @param parameters
     * @param iEV
     */
    public static void setCameraExposureCompensation(CameraProxy cameraDevice, Parameters parameters, int iEV) {
        try {
            if (parameters != null) {
                parameters.setExposureCompensation(iEV);
                cameraDevice.setParameters(parameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "setCameraExposureCompensation=" + e.getMessage());
        }
    }

    /**
     * get camera max exposure compensation
     *
     * @param parameters
     * @return
     */
    public static int getCameraMaxExposureCompensation(Parameters parameters) {
        if (parameters != null) {
            return parameters.getMaxExposureCompensation();
        }
        return 0;
    }

    /**
     * get camera min exposure compensation
     *
     * @param parameters
     * @return
     */
    public static int getCameraMinExposureCompensation(Parameters parameters) {
        if (parameters != null) {
            return parameters.getMinExposureCompensation();
        }
        return 0;
    }

    /**
     * set white balance
     *
     * @param cameraDevice
     * @param parameters
     * @param tag
     */
    public static void setCameraWhiteBalance(CameraProxy cameraDevice, Parameters parameters, String tag) {
        if (parameters != null) {
            parameters.setWhiteBalance(tag);
            cameraDevice.setParameters(parameters);
        }
    }

    public static String getCameraWhiteBalance(Parameters parameters) {
        if (parameters != null) {
            return parameters.getWhiteBalance();
        }
        return "";
    }

    /**
     * 获取支持的白平衡
     *
     * @param parameters
     * @return
     */
    public static List<String> getCameraSupportedWhiteBalance(Parameters parameters) {
        if (parameters != null) {
            return parameters.getSupportedWhiteBalance();
        }
        return null;
    }

    /**
     * set sceneMode
     *
     * @param parameters
     * @param sceneMoe
     */
    public static void setCameraSceneMode(Parameters parameters, String sceneMoe) {
        if (parameters != null) {
            parameters.setSceneMode(sceneMoe);
        }
    }

    public static String getCameraSceneMode(Parameters parameters) {
        if (parameters != null) {
            parameters.getSceneMode();
        }
        return "";
    }

    /**
     * 获取该相机支持的所有场景模式
     *
     * @param parameters
     * @return
     */
    public static List<String> getCameraSupportedSceneMode(Parameters parameters) {
        List<String> stringList =null;
        if (parameters != null) {
            stringList = parameters.getSupportedSceneModes();
            for (int i=0;i<stringList.size();i++){
                LogUtils.e(TAG,"stringList="+stringList.get(i).toString());
            }

        }
        return stringList;
    }

    /**
     * 设置图片大小
     *
     * @param parameters
     * @param width
     * @param height
     */
    public static void setCameraPictureSize(Parameters parameters, int width, int height) {
        if (parameters != null) {
            parameters.setPictureSize(width, height);
        }
    }

    /**
     * set camera picture size
     *
     * @param parameters
     * @return
     */
    public static Camera.Size getCameraPictureSize(Parameters parameters) {
        if (parameters != null) {
            return parameters.getPictureSize();
        }
        return null;
    }

    /**
     * 获取支持的图片大小
     * @param parameters
     * @return
     */
    public static List<Camera.Size> getSupportedPictureSizes(Parameters parameters) {
        List<Camera.Size> sizeList = null;
        if (parameters != null) {
            sizeList = parameters.getSupportedPictureSizes();
//            for (int i=0;i<sizeList.size();i++){
//                LogUtils.e(TAG,"sizeList width="+sizeList.get(i).width + " height="+sizeList.get(i).height);
//            }
        }
        return sizeList;
    }

    public static void setCameraFlashMode(Parameters parameters,String flashMode){
        if (parameters!=null){
            parameters.setFlashMode(flashMode);
        }
    }

    public static String getCameraFlashMode(Parameters parameters){
        if (parameters!=null){
            return parameters.getFlashMode();
        }
        return "";
    }

    public static List<String> getSupportedFlashModes(Parameters parameters){
        if (parameters!=null){
            return parameters.getSupportedFlashModes();
        }
        return null;
    }

//    public static void setHDR(Parameters parameters){
//        parameters.setH
//    }

    /**
     * set camera focus mode
     * @param parameters
     * @param focusMode
     */
    public static void setCameraFocusMode(Parameters parameters,String focusMode){
        if (parameters!=null){
            parameters.setFocusMode(focusMode);
        }
    }

    /**
     *
     * @param parameters
     * @return
     */
    public static String getCameraFocusMode(Parameters parameters){
        if (parameters!=null){
            return parameters.getFlashMode();
        }
        return null;
    }


    public static List<String> getSupportedFocusModes(Parameters parameters){
        if (parameters!=null){
            return parameters.getSupportedFocusModes();
        }
        return null;
    }

    /**
     * 打开或关闭hdr
     * @param parameters
     */
    public static void switchHDR(Parameters parameters){
        if (parameters!=null) {
            parameters.setSceneMode(parameters.SCENE_MODE_HDR);
        }
    }

    public static void setISO(Parameters parameters){

        if (parameters!=null){
            parameters.set(CameraPreference.KEY_ISO_MODE,"iso");
        }
    }
}
