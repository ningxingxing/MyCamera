package com.example.administrator.mycamera.activity;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.model.FileInfo;
import com.example.administrator.mycamera.model.ImageTimeFolder;
import com.example.administrator.mycamera.utils.GalleryUtils;
import com.example.administrator.mycamera.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryDetailActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String mBucketId;
    private List<ImageTimeFolder> mImageList = new ArrayList<>();
    private ArrayList<FileInfo> mFileInfo = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GalleryUtils.setWindowStatusBarColor(this,R.color.blue_dark);
        setContentView(R.layout.activity_gallery_detail);

        getData();
        initData();
    }

    private void getData(){
        mBucketId = getIntent().getStringExtra("bucketId");
    }

    private void initData(){
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
        if (mImageList != null && mImageList.size() > 0) {
            mImageList.clear();
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
        LogUtils.e("nsc","size="+mFileInfo.size());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
