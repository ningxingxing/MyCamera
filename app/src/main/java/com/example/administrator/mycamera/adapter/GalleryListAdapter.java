package com.example.administrator.mycamera.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.model.ImageFolder;


import java.util.List;

/**
 * Created by Administrator on 2018/6/20.
 */

public class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.ViewHolder> {

    private final String TAG = "Cam_PictureSizeAdapter";
    private Context mContext;
    private List<ImageFolder> mVideoList;

    private IImageClick mImageClick;

    public interface IImageClick {
        void onItemClickListener(int position);
    }

    public void setImageClickListener(IImageClick iPictureClick) {
        this.mImageClick = iPictureClick;
    }


    public GalleryListAdapter(Context context, List<ImageFolder> videoList) {
        this.mContext = context;
        this.mVideoList = videoList;
    }

    public void setData(List<ImageFolder> videoList) {
        this.mVideoList = videoList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_gallery_video, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Glide.with(mContext)
                .load(mVideoList.get(position).getFirstImagePath())
                .centerCrop()
                .into(holder.ivVideoIcon);

        holder.tvVideoTitle.setText("" + mVideoList.get(position).getFolderName());

        holder.tvVideoNum.setText(mVideoList.get(position).getVideoNum()+"");
        holder.tvImageNum.setText(mVideoList.get(position).getImageNum()+"");

        holder.llVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageClick != null) {
                    mImageClick.onItemClickListener(position);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mVideoList != null && mVideoList.size() > 0) {
            count = mVideoList.size();
        }
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llVideo;
        ImageView ivVideoIcon;
        TextView tvVideoTitle;
        TextView tvVideoNum;
        TextView tvImageNum;

        public ViewHolder(View view) {
            super(view);
            llVideo = (LinearLayout) view.findViewById(R.id.ll_video);
            ivVideoIcon = (ImageView) view.findViewById(R.id.iv_video_icon);
            tvVideoTitle = (TextView) view.findViewById(R.id.tv_video_title);
            tvVideoNum = (TextView)view.findViewById(R.id.tv_video_num);
            tvImageNum = (TextView)view.findViewById(R.id.tv_image_num);
        }
    }
}
