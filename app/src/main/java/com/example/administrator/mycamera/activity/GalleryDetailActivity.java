package com.example.administrator.mycamera.activity;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.mycamera.MyApplication;
import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.adapter.GalleryDetailAdapter;
import com.example.administrator.mycamera.model.FileInfo;
import com.example.administrator.mycamera.model.ImageTimeFolder;
import com.example.administrator.mycamera.utils.GalleryUtils;
import com.example.administrator.mycamera.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryDetailActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, GalleryDetailAdapter.IImageClick {

    private String mBucketId;
    private String mFolderName;
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
    }

    private void initData() {
        tvDetailTitle.setText(mFolderName + "");
        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String columns[] = new String[]{
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Thumbnails.DATA};
        return new CursorLoader(this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                MediaStore.Images.Media.BUCKET_ID + "=? ",
                new String[]{mBucketId}, MediaStore.Images.Media.DATE_ADDED + " desc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (mFileInfo != null && mFileInfo.size() > 0) {
            mFileInfo.clear();
        }

        if (cursor.moveToNext()) {
            int thumbPathIndex = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
            int timeIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
            int pathIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                FileInfo fi = new FileInfo();
                String thumbPath = cursor.getString(thumbPathIndex);
                Long date = cursor.getLong(timeIndex);
                String filepath = cursor.getString(pathIndex);

                File f = new File(filepath);
                fi.setTime(date);
                fi.setThumbPath(thumbPath);
                fi.setFilePath(filepath);
                fi.setFileName(f.getName());
                mFileInfo.add(fi);

            } while (cursor.moveToNext());
        }
        mGalleryDetailAdapter.setData(mFileInfo);


        MyApplication app = (MyApplication) getApplication();
        app.mImageDetailList = new ArrayList<>();
        app.mImageDetailList.addAll(mFileInfo);

        LogUtils.e("nsc", "size=" + mFileInfo.size());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
