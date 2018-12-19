package com.example.administrator.mycamera.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    }


    @Override
    public void onAlbumLoad(Cursor cursor) {
        mVideoList.clear();
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex("_data"));
            String bucketName = cursor.getString(cursor.getColumnIndex("bucket_display_name"));
            String amount = cursor.getString(cursor.getColumnIndex("count"));
            String bucketId = cursor.getString(cursor.getColumnIndex("bucket_id"));

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

        mGalleryListAdapter.setData(mVideoList);
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
        intent.putExtra("folderPath",mVideoList.get(position).getFolderPath());
        intent.putExtra("folderName",mVideoList.get(position).getFolderName());
        startActivity(intent);
    }

    private int getImageSize(List<FileInfo> fileInfoList){
        int count=0;
        for (int i=0;i<fileInfoList.size();i++){
            File file = new File(fileInfoList.get(i).getFilePath());
            if (GalleryUtils.IMAGE ==GalleryUtils.getFileType(file)){
                count++;
            }
        }
        return count;
    }
}
