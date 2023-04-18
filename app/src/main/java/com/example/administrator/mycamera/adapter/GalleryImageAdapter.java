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
import com.example.administrator.mycamera.model.ImageFolder;
import com.example.administrator.mycamera.model.PictureSizeData;
import com.example.administrator.mycamera.utils.CameraUtils;
import com.example.administrator.mycamera.utils.LogUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/6/20.
 */

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.ViewHolder> {

    private final String TAG = "Cam_PictureSizeAdapter";
    private Context mContext;
    private List<ImageFolder> mImageList;

    private IImageClick mImageClick;
    public interface IImageClick{
        void onItemClickListener(int position);
    }

    public void setImageClickListener(IImageClick iPictureClick){
        this.mImageClick = iPictureClick;
    }


    public GalleryImageAdapter(Context context, List<ImageFolder> imageList) {
        this.mContext = context;
        this.mImageList = imageList;
    }

    public void setData(List<ImageFolder> imageList){
        this.mImageList = imageList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_gallery_image, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Glide.with(mContext)
                .load(mImageList.get(position).getFirstImagePath())
                .centerCrop()
                .into(holder.ivImageIcon);

        holder.tvImageTitle.setText(""+mImageList.get(position).getFolderName());
        holder.tvImageNum.setText(""+mImageList.get(position).imageCount);

        holder.llImage.setOnClickListener(new View.OnClickListener() {
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

        LinearLayout llImage;
        ImageView  ivImageIcon;
        TextView tvImageTitle;
        TextView tvImageNum;

        public ViewHolder(View view) {
            super(view);
            llImage = (LinearLayout) view.findViewById(R.id.ll_image);
            ivImageIcon = (ImageView) view.findViewById(R.id.iv_image_icon);
            tvImageTitle = (TextView) view.findViewById(R.id.tv_image_title);
            tvImageNum = (TextView)view.findViewById(R.id.tv_image_num);
        }
    }
}
