package com.example.administrator.mycamera.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.model.FileInfo;
import com.example.administrator.mycamera.model.ImageFolder;
import com.example.administrator.mycamera.model.PictureSizeData;
import com.example.administrator.mycamera.utils.CameraUtils;
import com.example.administrator.mycamera.utils.GalleryUtils;
import com.example.administrator.mycamera.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/20.
 */

public class GalleryDetailAdapter extends RecyclerView.Adapter<GalleryDetailAdapter.ViewHolder> {

    private final String TAG = "Cam_GalleryDetailAdapter";
    private Context mContext;
    private List<FileInfo> mImageList;

    private IImageClick mImageClick;
    public interface IImageClick{
        void onItemClickListener(int position);
    }

    public void setImageClickListener(IImageClick iPictureClick){
        this.mImageClick = iPictureClick;
    }


    public GalleryDetailAdapter(Context context, List<FileInfo> imageList) {
        this.mContext = context;
        this.mImageList = imageList;
    }

    public void setData(List<FileInfo> imageList){
        this.mImageList = imageList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_gallery_detail, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Glide.with(mContext)
                .load(mImageList.get(position).getFilePath())
                .centerCrop()
                .into(holder.ivImageDetail);
        File file = new File(mImageList.get(position).getFilePath());

        if (GalleryUtils.getFileType(file) == GalleryUtils.VIDEO){
           // LogUtils.e(TAG,"nsc ="+file.getPath());
            holder.ivMark.setVisibility(View.VISIBLE);
        }else {
            holder.ivMark.setVisibility(View.GONE);
        }

        holder.rlDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageClick!=null){
                    mImageClick.onItemClickListener(position);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int count =0;
        if (mImageList!=null && mImageList.size()>0){
            count = mImageList.size();
        }
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlDetail;
        ImageView  ivMark;
        ImageView ivImageDetail;

        public ViewHolder(View view) {
            super(view);
            rlDetail = (RelativeLayout) view.findViewById(R.id.rl_detail);
            ivMark = (ImageView) view.findViewById(R.id.iv_mark);
            ivImageDetail = (ImageView) view.findViewById(R.id.iv_image_detail);
        }
    }
}
