package com.example.administrator.mycamera.port;

import android.widget.ImageButton;

import com.example.administrator.mycamera.view.buttonview.CircleImageView;

/**
 * Created by Administrator on 2018/6/6.
 */

public interface IBottomItem {

    void videoClick();

    void shutterClick(ImageButton id);

    void imageClick(CircleImageView id);
}
