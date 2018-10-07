package com.example.administrator.mycamera.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.adapter.CameraPreferenceSettingAdapter;
import com.example.administrator.mycamera.model.CameraPreference;
import com.example.administrator.mycamera.model.CameraPreferenceSettingData;
import com.example.administrator.mycamera.utils.CameraConstant;
import com.example.administrator.mycamera.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class CameraPreferenceSettingActivity extends Activity implements CameraPreferenceSettingAdapter.IonItemClickListener{
    private final String TAG = "Cam_CameraPreferenceSettingActivity";
    private RecyclerView mRecyclerPreference;
    private CameraPreferenceSettingAdapter mCameraPreferenceSettingAdapter;
    private ArrayList<CameraPreferenceSettingData> mSettingDataList = new ArrayList<>();

    private String mPreferenceKey = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera_preference_setting);

        initView();
        initGetData();
        initData();
    }

    private void initView() {

        mRecyclerPreference = (RecyclerView)findViewById(R.id.recycler_preference);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CameraPreferenceSettingActivity.this);
        mRecyclerPreference.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        //mRecyclerPreference.setHasFixedSize(true);

    }

    private void initGetData(){

        mPreferenceKey = getIntent().getStringExtra(CameraConstant.SETTING_FRAGMENT);
        if (mPreferenceKey.equals(CameraConstant.COUNT_DOWN_DATA)){
            getCountDownData();
        }else if (mPreferenceKey.equals(CameraPreference.KEY_PREVIEW_SCALE)) {
            getPreviewScale();
        }

    }

    private void initData(){
        mCameraPreferenceSettingAdapter = new CameraPreferenceSettingAdapter(CameraPreferenceSettingActivity.this,mSettingDataList);
        mRecyclerPreference.setAdapter(mCameraPreferenceSettingAdapter);
        mCameraPreferenceSettingAdapter.setOnItemClickListener(this);

    }


    private void getCountDownData() {
        mSettingDataList.clear();
        String[] items = getResources().getStringArray(R.array.count_down_time);
        String time = (String) CameraPreference.get(CameraPreferenceSettingActivity.this, CameraPreference.KEY_COUNT_DOWN, "关闭");
        for (int i = 0; i < items.length; i++) {
            CameraPreferenceSettingData preferenceSettingData = new CameraPreferenceSettingData();
            preferenceSettingData.setTitle(items[i]);
            if (time.equals(items[i])) {
                preferenceSettingData.setSelect(true);
            } else {
                preferenceSettingData.setSelect(false);
            }
            mSettingDataList.add(preferenceSettingData);
        }

    }

    private void getPreviewScale(){
        mSettingDataList.clear();
        String previewScale = (String)CameraPreference.get(CameraPreferenceSettingActivity.this,
                CameraPreference.KEY_PREVIEW_SCALE,"4:3");
        String[] items = getResources().getStringArray(R.array.preview_scale);
        for (int i=0;i<items.length;i++){
            CameraPreferenceSettingData preferenceSettingData = new CameraPreferenceSettingData();
            preferenceSettingData.setTitle(items[i]);
            if (previewScale.equals(items[i])){
                preferenceSettingData.setSelect(true);
            }else {
                preferenceSettingData.setSelect(false);
            }
            mSettingDataList.add(preferenceSettingData);
        }

    }

    @Override
    public void onItemClick(int position, String title) {

        if (mSettingDataList.get(position).isSelect()) {
            mSettingDataList.get(position).setSelect(false);
        }else {
            mSettingDataList.get(position).setSelect(true);
        }
        mCameraPreferenceSettingAdapter.setData(mSettingDataList,position);
       // LogUtils.e(TAG," position="+position + " title="+title);
        Intent intent = new Intent();
        if (mPreferenceKey.equals(CameraConstant.COUNT_DOWN_DATA)) {
            intent.putExtra("countDownTime", title);
            CameraPreference.put(CameraPreferenceSettingActivity.this,CameraPreference.KEY_COUNT_DOWN,title);
        }else if (mPreferenceKey.equals(CameraPreference.KEY_PREVIEW_SCALE)){
            intent.putExtra("previewScale",title);
            CameraPreference.put(CameraPreferenceSettingActivity.this,CameraPreference.KEY_PREVIEW_SCALE,title);
        }
        setResult(RESULT_OK,intent);
        finish();
    }
}
