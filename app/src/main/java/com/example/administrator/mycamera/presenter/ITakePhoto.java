package com.example.administrator.mycamera.presenter;

/**
 * Created by Administrator on 2018/5/21.
 */

public interface ITakePhoto extends IBaseCamera{
    void startCountDown(int time);

    void cancelCountDown();

    int getCurrentCountDownTime();

}
