package com.example.administrator.mycamera.fragment;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.administrator.mycamera.model.ImageFolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GalleryVideoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private HashMap<String, ImageFolder> mBucketList = new HashMap<>();
    private List<ImageFolder> mVideoList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getLoaderManager().initLoader(1, null, GalleryVideoFragment.this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] columns = new String[]{
                MediaStore.Video.Media.DATA,//视屏的绝对地址
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,//视屏文件在sdcard的名称
                MediaStore.Video.Media.SIZE,//视屏的文件大小
                MediaStore.Video.Media.ARTIST,//歌曲的演唱者
                MediaStore.Video.Media.BUCKET_ID};
        return new CursorLoader(getActivity(),
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToNext()) {

            Integer photoPathIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int bucketDisplayNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);

            do {
                String path = cursor.getString(photoPathIndex);
                String bucketName = cursor.getString(bucketDisplayNameIndex);
                String bucketId = cursor.getString(bucketIdIndex);

                ImageFolder bucket = mBucketList.get(bucketId);
                if (bucket == null) {
                    bucket = new ImageFolder();
                    mBucketList.put(bucketId, bucket);
                    bucket.folderName = bucketName;
                    bucket.setFirstImagePath(path);
                    bucket.setBucketId(bucketId);
                }
                bucket.imageCount++;

            } while (cursor.moveToNext());

        }
        Iterator<Map.Entry<String, ImageFolder>> itr = mBucketList.entrySet()
                .iterator();
        while (itr.hasNext()) {
            Map.Entry<String, ImageFolder> entry = (Map.Entry<String, ImageFolder>) itr
                    .next();
            mVideoList.add(entry.getValue());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
