package com.example.administrator.mycamera.view;

import android.app.Dialog;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.adapter.PictureSizeAdapter;
import com.example.administrator.mycamera.manager.CameraManager;
import com.example.administrator.mycamera.model.CameraPreference;
import com.example.administrator.mycamera.model.PictureSizeData;
import com.example.administrator.mycamera.utils.CameraParameter;
import com.example.administrator.mycamera.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/20.
 */

public class PictureSizeDialog extends Dialog implements PictureSizeAdapter.IPictureClick{
    private final String TAG = "Cam_PictureSizeDialog";
    private RecyclerView mRecyclerView;
    private Context mContext;
    private PictureSizeAdapter mAdapter;
    private CameraParameter mCameraParameter;
    private CameraManager.CameraProxy mCameraDevice;
    private List<PictureSizeData> pictureSizeDataList = new ArrayList<>();
    private int mLastPosition = -1;

    private IPictureSizeClick iPictureSizeClick;
    public interface IPictureSizeClick{
        void onsettingPictureSize(int width ,int height);
    }

    public PictureSizeDialog(Context context, Parameters parameter,CameraManager.CameraProxy cameraDevice,IPictureSizeClick iPictureSizeClick) {
        super(context);
        this.mContext = context;
        this.mCameraDevice = cameraDevice;
        this.iPictureSizeClick = iPictureSizeClick;
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

        mAdapter.setPictureSizeClickListener(this);
    }


    public List<PictureSizeData> getData(Parameters parameter) {

        String picture = (String)CameraPreference.get(mContext,CameraPreference.KEY_PICTURE_SIZE,"720x1280");
        String[] p = picture.split("x");
        int pWidth = Integer.valueOf(p[0]);
        int pHeight = Integer.valueOf(p[1]);

        if (mCameraParameter != null) {
            List<Camera.Size> supportedSize = mCameraParameter.getSupportedPictureSizes(parameter);
            if (supportedSize != null && supportedSize.size() > 0) {
                for (int i = 0; i < supportedSize.size(); i++) {
                    PictureSizeData pictureData = new PictureSizeData();
                    pictureData.setPictureWidth(String.valueOf(supportedSize.get(i).width));
                    pictureData.setPictureHeight(String.valueOf(supportedSize.get(i).height));
                    //LogUtils.e(TAG,"supportedSize="+supportedSize.get(i).width + " "+supportedSize.get(i).height);
                    if (pWidth==supportedSize.get(i).width && pHeight==supportedSize.get(i).height){
                        pictureData.setSelect(true);
                    }else {
                        pictureData.setSelect(false);
                    }

                    pictureSizeDataList.add(pictureData);
                }

            }
        }
        return pictureSizeDataList;
    }

    @Override
    public void onItemClickListener(int position) {
        if (pictureSizeDataList==null)return;
        if (mLastPosition!=-1 && mLastPosition<pictureSizeDataList.size()){
            pictureSizeDataList.get(mLastPosition).setSelect(false);
        }
        pictureSizeDataList.get(position).setSelect(true);

        mAdapter.setData(pictureSizeDataList,position);
        mLastPosition = position;

        String width =pictureSizeDataList.get(position).getPictureWidth();
        String height = pictureSizeDataList.get(position).getPictureHeight();
        CameraPreference.put(mContext,CameraPreference.KEY_PICTURE_SIZE,(width + "x"+height));
        if (iPictureSizeClick!=null){
            iPictureSizeClick.onsettingPictureSize(Integer.valueOf(width),Integer.valueOf(height));
        }
        dismiss();
    }
}
