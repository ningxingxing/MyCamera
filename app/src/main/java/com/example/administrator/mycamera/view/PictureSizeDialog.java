package com.example.administrator.mycamera.view;

import android.app.Dialog;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.adapter.PictureSizeAdapter;
import com.example.administrator.mycamera.model.PictureSizeData;
import com.example.administrator.mycamera.utils.CameraParameter;
import com.example.administrator.mycamera.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/20.
 */

public class PictureSizeDialog extends Dialog {
    private final String TAG = "Cam_PictureSizeDialog";
    private RecyclerView mRecyclerView;
    private PictureSizeAdapter mAdapter;
    private CameraParameter mCameraParameter;

    public PictureSizeDialog(Context context, Parameters parameter) {
        super(context);
        LogUtils.e(TAG, "PictureSizeDialog parameter=" + parameter);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_picture_size, null);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        mCameraParameter = new CameraParameter();
        mAdapter = new PictureSizeAdapter(context, getData(parameter));
        mRecyclerView.setAdapter(mAdapter);
        setContentView(contentView);
    }


    public List<PictureSizeData> getData(Parameters parameter) {
        List<PictureSizeData> pictureSize = new ArrayList<>();
        LogUtils.e(TAG,"getData="+mCameraParameter);
        if (mCameraParameter != null) {
            List<Camera.Size> supportedSize = mCameraParameter.getSupportedPictureSizes(parameter);
            if (supportedSize != null && supportedSize.size() > 0) {
                for (int i = 0; i < supportedSize.size(); i++) {
                    PictureSizeData pictureData = new PictureSizeData();
                    pictureData.setPictureWidth(String.valueOf(supportedSize.get(i).width));
                    pictureData.setPictureHeight(String.valueOf(supportedSize.get(i).height));
                    //LogUtils.e(TAG,"supportedSize="+supportedSize.get(i).width + " "+supportedSize.get(i).height);
                    pictureSize.add(pictureData);
                }

            }
        }
        return pictureSize;
    }
}
