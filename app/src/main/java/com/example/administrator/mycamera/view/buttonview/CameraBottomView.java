package com.example.administrator.mycamera.view.buttonview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.presenter.IBottomClick;
import com.example.administrator.mycamera.utils.LogUtils;

/**
 * 底部摄像拍照缩略图显示
 * Created by Administrator on 2018/6/4.
 */

public class CameraBottomView extends LinearLayout implements View.OnClickListener {
    private final String TAG = "CameraBottomView";

    private ImageButton ibVideo;
    private ImageButton ibShutter;
    private CircleImageView ibImage;

    private IBottomClick mBottomClickListener;

    public CameraBottomView(Context context) {
        this(context, null);
    }

    public CameraBottomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_camera_bottom, this);

        initView();
    }

    private void initView() {
        ibShutter = (ImageButton) findViewById(R.id.ib_shutter);
        ibShutter.setOnClickListener(this);

        ibVideo = (ImageButton) findViewById(R.id.ib_video);
        ibVideo.setOnClickListener(this);

        ibImage = (CircleImageView) findViewById(R.id.ib_image);
        ibImage.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_shutter:
                if (mBottomClickListener != null) {
                    mBottomClickListener.shutterClick(ibShutter);
                }
                break;

            case R.id.ib_video:
                if (mBottomClickListener != null) {
                    mBottomClickListener.videoClick();
                }
                break;

            case R.id.ib_image:
                if (mBottomClickListener != null) {
                    mBottomClickListener.imageClick(ibImage);
                }
                break;
        }
    }

    /**
     * 实例化bottomClickListener
     * @param iBottomClick
     */
    public void setBottomClickListener(IBottomClick iBottomClick) {
        this.mBottomClickListener = iBottomClick;
    }
}
