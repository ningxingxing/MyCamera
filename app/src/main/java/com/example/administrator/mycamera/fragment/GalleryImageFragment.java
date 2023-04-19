package com.example.administrator.mycamera.fragment;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.activity.GalleryDetailActivity;
import com.example.administrator.mycamera.adapter.GalleryImageAdapter;
import com.example.administrator.mycamera.adapter.GalleryListAdapter;
import com.example.administrator.mycamera.model.ImageFolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GalleryImageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, GalleryImageAdapter.IImageClick {

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
        mGalleryImageAdapter.setImageClickListener(this);
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
        new LoadDataAsync().execute(cursor);
    }

    @Override
    public void onItemClickListener(int position) {
        if (mImageList != null) {
            String bucketId = mImageList.get(position).getBucketId();
            Intent intent = new Intent(getActivity(), GalleryDetailActivity.class);
            intent.putExtra("bucketId", bucketId);
            intent.putExtra("folderPath", mImageList.get(position).getFolderPath());
            intent.putExtra("folderName", mImageList.get(position).getFolderName());
            startActivity(intent);
        }
    }

    class LoadDataAsync extends AsyncTask<Cursor, String, String> {

        @Override
        protected String doInBackground(Cursor... strings) {
            mImageList.clear();
            mBucketList.clear();

            Cursor cursor = strings[0];

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

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mGalleryImageAdapter.setData(mImageList);
        }
    }
}
