package com.example.administrator.mycamera.fragment;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.administrator.mycamera.adapter.GalleryListAdapter;
import com.example.administrator.mycamera.model.AlbumCollection;
import com.example.administrator.mycamera.model.FileInfo;
import com.example.administrator.mycamera.model.ImageFolder;
import com.example.administrator.mycamera.utils.GalleryUtils;
import com.example.administrator.mycamera.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GalleryListFragment extends Fragment implements AlbumCollection.AlbumCallbacks,GalleryListAdapter.IImageClick {

    private final String TAG = "GalleryListFragment";
    private HashMap<String, ImageFolder> mBucketList = new HashMap<>();
    private List<ImageFolder> mVideoList = new ArrayList<>();

    private RecyclerView mVideoRecyclerView;
    private GalleryListAdapter mGalleryListAdapter;

    private AlbumCollection mCollection;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery_video, container, false);

        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mVideoRecyclerView = (RecyclerView) view.findViewById(R.id.video_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mVideoRecyclerView.setLayoutManager(layoutManager);
        mGalleryListAdapter = new GalleryListAdapter(getActivity(), mVideoList);
        mVideoRecyclerView.setAdapter(mGalleryListAdapter);

        mGalleryListAdapter.setImageClickListener(this);


    }

    private void initData() {
        mCollection = new AlbumCollection();
        mCollection.onCreate(getActivity(), this);
        mCollection.loadAlbums();
       // getActivity().getLoaderManager().initLoader(1, null, this);

        //new LoadDataAsync().execute();
    }

    class LoadDataAsync extends AsyncTask<Cursor, String, String> {

        @Override
        protected String doInBackground(Cursor... strings) {
           // getVideo(getActivity());
            mVideoList.clear();
            Cursor cursor = strings[0];
            while (cursor.moveToNext()) {
                if (Build.VERSION.SDK_INT>Build.VERSION_CODES.Q){
                    int dataIndex = cursor.getColumnIndex("_data");
                    int bucketNameIndex = cursor.getColumnIndex("bucket_display_name");
                    int bucketIdIndex = cursor.getColumnIndex("bucket_id");
                    String path = cursor.getString(dataIndex);
                    String bucketName = cursor.getString(bucketNameIndex);
                    String bucketId = cursor.getString(bucketIdIndex);
                    ImageFolder bucket = mBucketList.get(bucketId);
                    if (bucket == null) {
                        bucket = new ImageFolder();
                        mBucketList.put(bucketId, bucket);
                        bucket.folderName = bucketName;
                        bucket.setFirstImagePath(path);
                        bucket.setBucketId(bucketId);
                    }
                }else {
                    int dataIndex = cursor.getColumnIndex("_data");
                    int bucketNameIndex = cursor.getColumnIndex("bucket_display_name");
                    int countIndex = cursor.getColumnIndex("count");
                    int bucketIdIndex = cursor.getColumnIndex("bucket_id");
                    String path = cursor.getString(dataIndex);
                    String bucketName = cursor.getString(bucketNameIndex);
                    String amount = cursor.getString(countIndex);
                    String bucketId = cursor.getString(bucketIdIndex);

                    ImageFolder bucket = mBucketList.get(bucketId);
                    if (bucket == null) {
                        bucket = new ImageFolder();
                        mBucketList.put(bucketId, bucket);
                        bucket.folderName = bucketName;
                        bucket.setFirstImagePath(path);
                        bucket.setBucketId(bucketId);
                        bucket.setImageCount(Integer.valueOf(amount));
                    }
                }
            }
            LogUtils.e(TAG,"LoadDataAsync ="+mBucketList.size());
            Iterator<Map.Entry<String, ImageFolder>> itr = mBucketList.entrySet()
                    .iterator();
            while (itr.hasNext()) {
                Map.Entry<String, ImageFolder> entry = (Map.Entry<String, ImageFolder>) itr
                        .next();
                mVideoList.add(entry.getValue());
            }
            for (int i=0;i<mVideoList.size();i++) {
                List<FileInfo> imageList = GalleryUtils.getImage(getActivity(),mVideoList.get(i).getFolderPath());
                //List<FileInfo> videoList = GalleryUtils.getVideo(getActivity(),mVideoList.get(i).getFolderPath());
                mVideoList.get(i).setImageNum(imageList.size());
                int videoNum = mVideoList.get(i).getImageCount()-imageList.size();
                mVideoList.get(i).setVideoNum(videoNum);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mGalleryListAdapter.setData(mVideoList);
        }
    }


    @Override
    public void onAlbumLoad(Cursor cursor) {

       new LoadDataAsync().execute(cursor);
    }

    @Override
    public void onAlbumReset() {

    }

    @Override
    public void onItemClickListener(int position) {
        //LogUtils.e(TAG,"path="+mVideoList.get(position).getFirstImagePath());
        String bucketId = mVideoList.get(position).getBucketId();
        Intent intent = new Intent(getActivity(), GalleryDetailActivity.class);
        intent.putExtra("bucketId", bucketId);
        intent.putExtra("folderPath", mVideoList.get(position).getFolderPath());
        intent.putExtra("folderName", mVideoList.get(position).getFolderName());
        startActivity(intent);
    }

    private int getImageSize(List<FileInfo> fileInfoList) {
        int count = 0;
        for (int i = 0; i < fileInfoList.size(); i++) {
            File file = new File(fileInfoList.get(i).getFilePath());
            if (GalleryUtils.IMAGE == GalleryUtils.getFileType(file)) {
                count++;
            }
        }
        return count;
    }

    public void getVideo(Context context) {
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();

        String[] projVideo = {MediaStore.Video.Thumbnails._ID
                , MediaStore.Video.Thumbnails.DATA
                , MediaStore.Video.Media.DURATION
                , MediaStore.Video.Media.SIZE
                , MediaStore.Video.Media.DISPLAY_NAME
                , MediaStore.Video.Media.DATE_MODIFIED};

        Cursor mCursor = mContentResolver.query(videoUri, projVideo,
                MediaStore.Video.Media.MIME_TYPE + " in(?, ?, ?, ?)",
                new String[]{"video/mp4", "video/3gp", "video/avi", "video/rmvb"},
                MediaStore.Video.Media.DATE_MODIFIED + " desc");

        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                // 获取视频的路径
                @SuppressLint("Range") String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DATA));
                // 获取该视频的父路径名
                String dirPath = new File(path).getParentFile().getAbsolutePath();
                LogUtils.e(TAG, "dirPath ="+dirPath);

            }
            mCursor.close();
        }
    }
}
