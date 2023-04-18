package com.example.administrator.mycamera.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.model.CameraPreferenceSettingData;
import com.example.administrator.mycamera.model.PictureSizeData;
import com.example.administrator.mycamera.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/20.
 */

public class CameraPreferenceSettingAdapter extends RecyclerView.Adapter<CameraPreferenceSettingAdapter.ViewHolder> {

    private final String TAG = "Cam_PictureSizeAdapter";
    private Context mContext;
    private ArrayList<CameraPreferenceSettingData> mPreferenceList;

    private IonItemClickListener mItemClickListener;
    public interface IonItemClickListener{
        void onItemClick(int position,String title);
    }

    public void setOnItemClickListener(IonItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener;
    }

    public CameraPreferenceSettingAdapter(Context context, ArrayList<CameraPreferenceSettingData> data) {
        this.mContext = context;
        this.mPreferenceList = data;
    }

    public void setData(ArrayList<CameraPreferenceSettingData> data,int position){
        this.mPreferenceList = data;
        notifyItemChanged(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_preference_setting, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tvPreference.setText(mPreferenceList.get(position).getTitle());
        holder.cbPreference.setChecked(mPreferenceList.get(position).isSelect());
        holder.llPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener!=null){
                    mItemClickListener.onItemClick(position,mPreferenceList.get(position).getTitle());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        int count =0;
        if (mPreferenceList!=null && mPreferenceList.size()>0){
            count = mPreferenceList.size();
        }
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPreference;
        RadioButton cbPreference;
        LinearLayout llPreference;

        public ViewHolder(View view) {
            super(view);
            tvPreference = (TextView) view.findViewById(R.id.tv_preference);
            cbPreference = (RadioButton) view.findViewById(R.id.cb_preference);
            llPreference = (LinearLayout)view.findViewById(R.id.ll_preference);
        }
    }
}
