package com.example.administrator.mycamera.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.model.FileInfo;
import com.example.administrator.mycamera.utils.LogUtils;
import com.trustyapp.gridheaders.TrustyGridSimpleAdapter;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xing on 2017/7/11.
 */

public class GalleryTimeAdapter extends BaseAdapter implements TrustyGridSimpleAdapter {
    private Context mContext;

    private ArrayList<FileInfo> fileInfo;

    private IImageTimeClick mImageTimeClick;
    public interface IImageTimeClick{
        void onItemClickListener(int position);
    }

    public void setImageClickListener(IImageTimeClick iPictureClick){
        this.mImageTimeClick = iPictureClick;
    }


    public GalleryTimeAdapter(Context mContext, ArrayList<FileInfo> fileInfo) {
        this.mContext = mContext;
        this.fileInfo = fileInfo;
    }

    public void setData(ArrayList<FileInfo> fileInfo) {
        this.fileInfo = fileInfo;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = 0;
        if (fileInfo != null && fileInfo.size() > 0) {
            count = fileInfo.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class ViewHolder {
        ImageView ivImage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.adapter_gallery_time, null);

            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (fileInfo != null && fileInfo.size() > 0) {

            File file = new File(fileInfo.get(position).getFilePath());
            Glide.with(mContext).load(file).asBitmap().dontAnimate().centerCrop()
                    .signature(new MediaStoreSignature("image/jpeg", file.lastModified(), 0))
                    .into(viewHolder.ivImage);
        }

        viewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageTimeClick!=null){
                    mImageTimeClick.onItemClickListener(position);
                }
            }
        });
        return convertView;
    }

    class HeaderViewHolder {
        public TextView tvTimeHeader;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup viewGroup) {

        HeaderViewHolder mHeadViewHolder = null;
        if (convertView==null){
            mHeadViewHolder = new HeaderViewHolder();
            convertView = View.inflate(mContext,R.layout.item_time_header,null);

            mHeadViewHolder.tvTimeHeader = (TextView) convertView.findViewById(R.id.tv_time_header);

            convertView.setTag(mHeadViewHolder);
        }else {
            mHeadViewHolder = (HeaderViewHolder)convertView.getTag();
        }
        mHeadViewHolder.tvTimeHeader.setText(fileInfo.get(position).getDate());

        return convertView;
    }

    @Override
    public long getHeaderId(int i) {
        // File file = new File(fileInfo.get(i).getFilePath());
        return getTimeId(fileInfo.get(i).getDate());
        //  return getTimeId(strToDateLong(file.lastModified()));
    }

    public long getTimeId(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date mDate = null;

        try {
            mDate = sdf.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mDate.getTime();
    }

    public String strToDateLong(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(date);
    }

}