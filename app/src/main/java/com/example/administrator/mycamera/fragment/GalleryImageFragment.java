package com.example.administrator.mycamera.fragment;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.activity.GalleryActivity;
import com.example.administrator.mycamera.adapter.GalleryImageAdapter;
import com.example.administrator.mycamera.model.ImageFolder;
import com.example.administrator.mycamera.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GalleryImageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = "GalleryImageFragment";
    private RecyclerView mImageRecyclerView;

    private HashMap<String, ImageFolder> mBucketList = new HashMap<>();
    private List<ImageFolder> mImageList = new ArrayList<>();

    private GalleryImageAdapter mGalleryImageAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery_image, container, false);

        initView(view);
        initData();
        return view;

    }

    private void initView(View view) {

        mImageRecyclerView = (RecyclerView) view.findViewById(R.id.image_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mImageRecyclerView.setLayoutManager(layoutManager);
        mGalleryImageAdapter = new GalleryImageAdapter(getActivity(), mImageList);
        mImageRecyclerView.setAdapter(mGalleryImageAdapter);

    }


    private void initData() {

        getActivity().getLoaderManager().initLoader(1, null, GalleryImageFragment.this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        String[] columns = new String[]{
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_ID};


       return new CursorLoader(getActivity(),
                               MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                                       null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        updateData(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    private void updateData(Cursor cursor) {
        mImageList.clear();
        mBucketList.clear();

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
            mImageList.add(entry.getValue());
        }

        mGalleryImageAdapter.setData(mImageList);

    }
}
