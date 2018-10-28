package com.example.administrator.mycamera.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.model.FileInfo;
import com.example.administrator.mycamera.utils.CameraConstant;
import com.example.administrator.mycamera.utils.DeleteFileUtils;
import com.example.administrator.mycamera.utils.GalleryUtils;
import com.example.administrator.mycamera.utils.LogUtils;
import com.example.administrator.mycamera.utils.SortUtils;
import com.example.administrator.mycamera.view.ZoomImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends Activity implements View.OnClickListener{

    private final String TAG = "Cam_GalleryActivity";

    private ImageView ivAllFile;
    private ImageView ivDetail;
    private ZoomImageView ivImage;
    private ViewPager mViewpager;

    private RadioGroup rgGallery;
    private RadioButton rbShare;
    private RadioButton rbDelete;
    private RadioButton rbEdit;
    private RadioButton rbMore;

    private List<FileInfo> mFileInfoList = new ArrayList<>();
    private float mDownX =0;
    private float mDownY = 0;
    private int mCurrentPosition = 0;
    private int mLastPosition = 0;
    private ImageView[] mImageViews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initView();
        initData();
    }

    private void initView() {
        ivAllFile = (ImageView)findViewById(R.id.iv_all_file);
        ivDetail = (ImageView)findViewById(R.id.iv_detail);
        ivImage = (ZoomImageView)findViewById(R.id.iv_image);
        mViewpager = (ViewPager)findViewById(R.id.view_pager);

        rgGallery = (RadioGroup)findViewById(R.id.rg_gallery);
        rbShare = (RadioButton)findViewById(R.id.rb_share);
        rbShare.setOnClickListener(this);
        rbDelete = (RadioButton)findViewById(R.id.rb_delete);
        rbDelete.setOnClickListener(this);
        rbEdit = (RadioButton)findViewById(R.id.rb_edit);
        rbEdit.setOnClickListener(this);
        rbMore = (RadioButton)findViewById(R.id.rb_more);
        rbMore.setOnClickListener(this);

    }

    private void initData() {
        if (mFileInfoList!=null){
            mFileInfoList.clear();
        }
       List<FileInfo> videoList = GalleryUtils.getVideo(GalleryActivity.this);
       List<FileInfo> imageList = GalleryUtils.getImage(GalleryActivity.this);
        mFileInfoList.addAll(videoList);
        mFileInfoList.addAll(imageList);

        Glide.with(GalleryActivity.this)
                .load(mFileInfoList.get(0).getFilePath())
                .into(ivImage);

        SortUtils sortUtils = new SortUtils();
        sortUtils.sort(mFileInfoList, CameraConstant.ID_SORT_TIME_DEC);

        showImage();

    }

    private void showImage(){
        mImageViews = new ImageView[mFileInfoList.size()];
        mViewpager.setAdapter(new PagerAdapter() {


            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ZoomImageView zoomImageView = new ZoomImageView(getApplication());
                File file = new File(mFileInfoList.get(position).getFilePath());
                Glide.with(getApplication())
                        .load(file)
                        .into(zoomImageView);

                mImageViews[position] = zoomImageView;

                container.addView(zoomImageView);
                return zoomImageView;
            }

            @Override
            public int getCount() {
                return mFileInfoList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mImageViews[position]);
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                mCurrentPosition = position;
                super.setPrimaryItem(container, position, object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void startUpdate(ViewGroup container) {
                super.startUpdate(container);
            }
        });

    }

    private void showDeleteDialog() {

        final Dialog dialogDelete = new Dialog(this, R.style.DialogTheme);
        dialogDelete.setCanceledOnTouchOutside(true);
        dialogDelete.show();
        Window window = dialogDelete.getWindow();
        window.setContentView(R.layout.dialog_message);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.5f;
        //window.setGravity(Gravity.BOTTOM);
        window.setAttributes(params);

        TextView tvDialogTitle = (TextView) dialogDelete.findViewById(R.id.tv_dialog_title);
        tvDialogTitle.setText(R.string.gallery_delete_ok);
        TextView tvOk = (TextView) dialogDelete.findViewById(R.id.tv_ok);
        tvOk.setText(R.string.gallery_delete);
        TextView tvCancel = (TextView) dialogDelete.findViewById(R.id.tv_cancel);
        tvCancel.setText(R.string.cancel);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(mFileInfoList.get(mCurrentPosition).getFilePath());
               // DeleteFileUtils.batchDeleteFile(GalleryActivity.this,mFileInfoList.get(mCurrentPosition).getFilePath());

                if (GalleryUtils.getFileType(file) == GalleryUtils.IMAGE){

                    DeleteFileUtils.imageDeleteRecord(mFileInfoList.get(mCurrentPosition).getFilePath(),GalleryActivity.this);

                }else if (GalleryUtils.getFileType(file) ==GalleryUtils.VIDEO){

                    DeleteFileUtils.videoDeleteRecord(mFileInfoList.get(mCurrentPosition).getFilePath(),GalleryActivity.this);

                }
                mFileInfoList.remove(mCurrentPosition);
                mLastPosition = mCurrentPosition;
                showImage();
                if (mLastPosition>=mFileInfoList.size()){
                    mLastPosition--;
                }
                LogUtils.e(TAG," nsc instantiateItem="+mCurrentPosition);
                mViewpager.setCurrentItem(mLastPosition);
                Toast.makeText(GalleryActivity.this,getString(R.string.gallery_delete_success),Toast.LENGTH_SHORT).show();

                dialogDelete.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDelete.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.rb_delete:
                showDeleteDialog();
                break;
        }

    }

}
