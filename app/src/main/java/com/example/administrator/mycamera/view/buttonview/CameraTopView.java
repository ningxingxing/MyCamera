package com.example.administrator.mycamera.view.buttonview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.port.ITopItem;

/**
 * Created by Administrator on 2018/6/14.
 */

public class CameraTopView extends LinearLayout implements View.OnClickListener {

    private ImageView ivFlash;
    private ImageView ivDelay;
    private ImageView ivWhite;
    private ImageView ivScene;
    private ImageView ivSwitch;

    private ITopItem mTopClickListener;

    public CameraTopView(Context context) {
        this(context, null);
    }

    public CameraTopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_camera_top, this);

        initView();
    }

    private void initView() {
        ivFlash = (ImageView) findViewById(R.id.iv_flash);
        ivFlash.setOnClickListener(this);

        ivDelay = (ImageView) findViewById(R.id.iv_delay);
        ivDelay.setOnClickListener(this);

        ivWhite = (ImageView) findViewById(R.id.iv_white);
        ivWhite.setOnClickListener(this);

        ivScene = (ImageView) findViewById(R.id.iv_scene);
        ivScene.setOnClickListener(this);

        ivSwitch = (ImageView) findViewById(R.id.iv_switch);
        ivSwitch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_flash:
                if (mTopClickListener != null) {
                    mTopClickListener.cameraFlash();
                }
                break;

            case R.id.iv_delay:
                if (mTopClickListener != null) {
                    mTopClickListener.cameraDelay();
                }
                break;

            case R.id.iv_white:
                if (mTopClickListener != null) {
                    mTopClickListener.cameraWhiteBalance();
                }
                break;

            case R.id.iv_scene:
                if (mTopClickListener != null) {
                    mTopClickListener.cameraScene();
                }
                break;

            case R.id.iv_switch:
                if (mTopClickListener != null) {
                    mTopClickListener.cameraSwitch();
                }
                break;
        }
    }

    public void setTopClickListener(ITopItem iTopItemListener) {
        this.mTopClickListener = iTopItemListener;
    }
}
