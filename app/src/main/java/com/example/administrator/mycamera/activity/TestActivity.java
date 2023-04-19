package com.example.administrator.mycamera.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.adapter.GalleryTimeAdapter;
import com.example.administrator.mycamera.fragment.GalleryTimeFragment;
import com.example.administrator.mycamera.model.FileInfo;
import com.example.administrator.mycamera.utils.GalleryUtils;
import com.trustyapp.gridheaders.TrustyGridGridView;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends Activity {

    private ArrayList<FileInfo> mFileInfoList = new ArrayList<>();
    private GalleryTimeAdapter mGalleryTimeAdapter;
    private TrustyGridGridView gvImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gallery_time);

        initView();

        initData();
    }

    private void initView() {
        //gvImage = (TrustyGridGridView)findViewById(R.id.gv_image);
    }

    private void initData(){
        mGalleryTimeAdapter = new GalleryTimeAdapter(this,mFileInfoList);
        gvImage.setAdapter(mGalleryTimeAdapter);


        // SortUtils sortUtils = new SortUtils();
        //sortUtils.sort(mFileInfoList, CameraConstant.ID_SORT_TIME_DEC);


        new UpdateAsyncTask().doInBackground();
    }


    class UpdateAsyncTask extends AsyncTask<ArrayList<FileInfo>,String,String> {

        @Override
        protected String doInBackground(ArrayList<FileInfo>... lists) {

            List<FileInfo> videoList = GalleryUtils.getAllLocalVideos(TestActivity.this);
            List<FileInfo> imageList = GalleryUtils.getAllLocalPhotos(TestActivity.this);
            //  mFileInfoList.addAll(videoList);
            mFileInfoList.addAll(imageList);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mGalleryTimeAdapter.setData(mFileInfoList);
        }
    }
}
