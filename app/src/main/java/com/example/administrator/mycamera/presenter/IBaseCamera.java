package com.example.administrator.mycamera.presenter;

/**
 * Created by Administrator on 2018/6/5.
 */

public interface IBaseCamera {
    void startPreview();

    void stopPreview();

    void showFlashOverlayAnimation();

    void displayProgress(boolean disable);

    void showThumbnail();
}
