package com.example.administrator.mycamera.presenter;

import com.example.administrator.mycamera.view.buttonview.CircleImageView;

/**
 * Created by Administrator on 2018/6/6.
 */

public interface IBottomClick {

    void videoClick();

    void shutterClick();

    void imageClick(CircleImageView id);
}
