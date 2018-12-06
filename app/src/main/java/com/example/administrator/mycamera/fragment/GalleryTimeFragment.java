package com.example.administrator.mycamera.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.mycamera.MyApplication;
import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.activity.GalleryActivity;
import com.example.administrator.mycamera.adapter.GalleryTimeAdapter;
import com.example.administrator.mycamera.model.FileInfo;
import com.example.administrator.mycamera.utils.CameraConstant;
import com.example.administrator.mycamera.utils.GalleryUtils;
import com.example.administrator.mycamera.utils.LogUtils;
import com.example.administrator.mycamera.utils.SortUtils;
import com.trustyapp.gridheaders.TrustyGridGridView;

import java.util.ArrayList;
import java.util.List;

public class GalleryTimeFragment extends Fragment implements GalleryTimeAdapter.IImageTimeClick {
    private String TAG = "Cam_GalleryTimeFragment";
    private ArrayList<FileInfo> mFileInfoList = new ArrayList<>();
    private GalleryTimeAdapter mGalleryTimeAdapter;
    private TrustyGridGridView gvImage;
    private MyApplication app;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery_time, container, false);

        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        gvImage = (TrustyGridGridView) view.findViewById(R.id.gv_image);

    }

    private void initData() {
        mGalleryTimeAdapter = new GalleryTimeAdapter(getActivity(), mFileInfoList);
        gvImage.setAdapter(mGalleryTimeAdapter);
        mGalleryTimeAdapter.setImageClickListener(this);
        new UpdateAsyncTask().execute(mFileInfoList);
    }

    @Override
    public void onItemClickListener(int position) {
       // LogUtils.e(TAG, "size=" + app.mImageTimeList);
        Intent intent = new Intent(getActivity(), GalleryActivity.class);
        intent.putExtra(GalleryUtils.KEY_TYPE,GalleryUtils.TIME_FRAGMENT);
        intent.putExtra(GalleryUtils.KEY_POSITION,position);
        startActivity(intent);

    }


    class UpdateAsyncTask extends AsyncTask<ArrayList<FileInfo>, String, String> {

        @Override
        protected String doInBackground(ArrayList<FileInfo>... lists) {

            List<FileInfo> videoList = GalleryUtils.getAllLocalVideos(getActivity());
            List<FileInfo> imageList = GalleryUtils.getAllLocalPhotos(getActivity());
            mFileInfoList.addAll(videoList);
            mFileInfoList.addAll(imageList);
            SortUtils sortUtils = new SortUtils();
            sortUtils.sort(mFileInfoList, CameraConstant.ID_SORT_TIME_DEC);
            app = MyApplication.getInstance();
            app.mImageTimeList = mFileInfoList;

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mGalleryTimeAdapter.setData(mFileInfoList);
        }
    }
}
