package com.example.administrator.mycamera.view.storage;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.mycamera.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */

public class StoragePathAdapter extends RecyclerView.Adapter<StoragePathAdapter.PathHolder> {
    private PathOnClickListener pathOnClickListener;
    private List<String> mPath_list;
    private String mCurrentPath;

    public void setmPath_list(String path) {
        String[] mPath_list=path.split("/");
        String[] pathlist=new String[mPath_list.length-1];
        for(int i=0;i<mPath_list.length-1;i++){
            pathlist[i]=mPath_list[i+1];
        }
        this.mPath_list = Arrays.asList(pathlist);
        this.mCurrentPath = path;
        notifyDataSetChanged();
    }


    @Override
    public PathHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_path, null);
        PathHolder holder=new PathHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(PathHolder holder, int position) {
        String path=mPath_list.get(position);
        holder.path_tv.setText(path);
        holder.path_tv.setTextColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return mPath_list==null?0:mPath_list.size();
    }

    public class PathHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView path_tv;
        public PathHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
            path_tv= (TextView) view.findViewById(R.id.path);
            path_tv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            List<String> newlist=  mPath_list.subList(0,getAdapterPosition()+1);
            String newpath=getNewPath(newlist);
            if(!newpath.equals(mCurrentPath)){
                    setmPath_list(newpath);
                    if(pathOnClickListener!=null ){
                        pathOnClickListener.onPathClick(newpath);
                    }
            }
        }

        private String getNewPath(List<String> newlist) {
            StringBuilder builder=new StringBuilder();
            for(int i=0;i<newlist.size();i++) {
                builder.append("/").append(newlist.get(i));
            }

            return builder.toString();
        }
    }

    public void setPathOnClickListener(PathOnClickListener pathOnClickListener) {
        this.pathOnClickListener = pathOnClickListener;
    }

    public interface PathOnClickListener{
        void onPathClick(String path);
    }
}
