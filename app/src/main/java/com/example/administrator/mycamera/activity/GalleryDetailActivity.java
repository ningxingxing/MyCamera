package com.example.administrator.mycamera.activity;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.administrator.mycamera.MyApplication;
import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.adapter.GalleryDetailAdapter;
import com.example.administrator.mycamera.model.FileInfo;
import com.example.administrator.mycamera.model.ImageTimeFolder;
import com.example.administrator.mycamera.utils.CameraConstant;
import com.example.administrator.mycamera.utils.GalleryUtils;
import com.example.administrator.mycamera.utils.LogUtils;
import com.example.administrator.mycamera.utils.SortUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryDetailActivity extends Activity implements  View.OnClickListener, GalleryDetailAdapter.IImageClick {

    private String mBucketId;
    private String mFolderName;
    private String mFolderPath;
    private List<ImageTimeFolder> mImageList = new ArrayList<>();
    private ArrayList<FileInfo> mFileInfo = new ArrayList<>();
    private GalleryDetailAdapter mGalleryDetailAdapter;

    private RecyclerView mDetailRecycleView;
    private ImageView ivBack;
    private TextView tvDetailTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GalleryUtils.setWindowStatusBarColor(this, R.color.blue_dark);
        setContentView(R.layout.activity_gallery_detail);

        initView();
        getData();
        initData();
    }

    private void initView() {
        mDetailRecycleView = (RecyclerView) findViewById(R.id.detail_recycleView);
        mDetailRecycleView.setLayoutManager(new GridLayoutManager(this, 3));
        mGalleryDetailAdapter = new GalleryDetailAdapter(this, mFileInfo);
        mDetailRecycleView.setAdapter(mGalleryDetailAdapter);
        mGalleryDetailAdapter.setImageClickListener(this);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        tvDetailTitle = (TextView) findViewById(R.id.tv_detail_title);
    }

    private void getData() {
        mBucketId = getIntent().getStringExtra("bucketId");
        mFolderName = getIntent().getStringExtra("folderName");
        mFolderPath = getIntent().getStringExtra("folderPath");
    }

    private void initData() {
        tvDetailTitle.setText(mFolderName + "");


        new UpdateAsyncTask().execute();

    }

    class UpdateAsyncTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {

            List<FileInfo> videoList = GalleryUtils.getVideo(GalleryDetailActivity.this,mFolderPath);
            List<FileInfo> imageList = GalleryUtils.getImage(GalleryDetailActivity.this,mFolderPath);

            mFileInfo.addAll(videoList);
            mFileInfo.addAll(imageList);

            SortUtils sortUtils = new SortUtils();
            sortUtils.sort(mFileInfo, CameraConstant.ID_SORT_TIME_DEC);

            MyApplication app = (MyApplication) getApplication();
            app.mImageDetailList = new ArrayList<>();
            app.mImageDetailList.addAll(mFileInfo);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mGalleryDetailAdapter.setData(mFileInfo);

        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClickListener(int position) {
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra(GalleryUtils.KEY_TYPE,GalleryUtils.DETAIL_ACTIVITY);
        intent.putExtra(GalleryUtils.KEY_POSITION,position);
        startActivity(intent);
    }
}
