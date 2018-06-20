package com.example.administrator.mycamera.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.model.PictureSizeData;
import com.example.administrator.mycamera.utils.LogUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/6/20.
 */

public class PictureSizeAdapter extends RecyclerView.Adapter<PictureSizeAdapter.ViewHolder> {

    private final String TAG = "Cam_PictureSizeAdapter";
    private Context mContext;
    private List<PictureSizeData> mPictureSizeList;

    public PictureSizeAdapter(Context context, List<PictureSizeData> data) {
        this.mContext = context;
        this.mPictureSizeList = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_picture_size, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvPictureWidth.setText(mPictureSizeList.get(position).getPictureWidth());
        holder.tvPictureHeight.setText(mPictureSizeList.get(position).getPictureHeight());
        holder.rlPictureSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        int count =0;
        if (mPictureSizeList!=null && mPictureSizeList.size()>0){
            count = mPictureSizeList.size();
        }
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlPictureSize;
        TextView tvPictureWidth;
        TextView tvPictureHeight;

        public ViewHolder(View view) {
            super(view);
            rlPictureSize = (RelativeLayout) view.findViewById(R.id.rl_picture_size);
            tvPictureWidth = (TextView) view.findViewById(R.id.tv_picture_width);
            tvPictureHeight = (TextView) view.findViewById(R.id.tv_picture_height);
        }
    }
}
