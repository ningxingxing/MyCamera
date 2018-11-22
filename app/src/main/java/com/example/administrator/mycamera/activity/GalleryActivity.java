package com.example.administrator.mycamera.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.model.FileInfo;
import com.example.administrator.mycamera.utils.CameraConstant;
import com.example.administrator.mycamera.utils.CameraUtils;
import com.example.administrator.mycamera.utils.DeleteFileUtils;
import com.example.administrator.mycamera.utils.GalleryUtils;
import com.example.administrator.mycamera.utils.LogUtils;
import com.example.administrator.mycamera.utils.SortUtils;
import com.example.administrator.mycamera.view.ZoomImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends Activity implements View.OnClickListener {

    private final String TAG = "Cam_GalleryActivity";

    private ImageView ivAllFile;
    private ImageView ivDetail;
    private ZoomImageView ivImage;
    private ViewPager mViewpager;
    private ImageView ivVideo;

    private RadioGroup rgGallery;
    private RadioButton rbShare;
    private RadioButton rbDelete;
    private RadioButton rbEdit;
    private RadioButton rbMore;

    private List<FileInfo> mFileInfoList = new ArrayList<>();
    private float mDownX = 0;
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
        ivAllFile = (ImageView) findViewById(R.id.iv_all_file);
        ivAllFile.setOnClickListener(this);
        ivDetail = (ImageView) findViewById(R.id.iv_detail);
        ivDetail.setOnClickListener(this);
        ivImage = (ZoomImageView) findViewById(R.id.iv_image);
        mViewpager = (ViewPager) findViewById(R.id.view_pager);
        ivVideo = (ImageView)findViewById(R.id.iv_video);

        rgGallery = (RadioGroup) findViewById(R.id.rg_gallery);
        rbShare = (RadioButton) findViewById(R.id.rb_share);
        rbShare.setOnClickListener(this);
        rbDelete = (RadioButton) findViewById(R.id.rb_delete);
        rbDelete.setOnClickListener(this);
        rbEdit = (RadioButton) findViewById(R.id.rb_edit);
        rbEdit.setOnClickListener(this);
        rbMore = (RadioButton) findViewById(R.id.rb_more);
        rbMore.setOnClickListener(this);

    }

    private void initData() {
        if (mFileInfoList != null) {
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

    private void showImage() {
        mImageViews = new ImageView[mFileInfoList.size()];
        mViewpager.setAdapter(new PagerAdapter() {


            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ZoomImageView zoomImageView = new ZoomImageView(getApplication());
                File file = new File(mFileInfoList.get(position).getFilePath());
                if (GalleryUtils.getFileType(file) ==GalleryUtils.VIDEO){
                    ivVideo.setAlpha(255);
                }else {
                    ivVideo.setAlpha(0);
                }
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

                if (GalleryUtils.getFileType(file) == GalleryUtils.IMAGE) {

                    DeleteFileUtils.imageDeleteRecord(mFileInfoList.get(mCurrentPosition).getFilePath(), GalleryActivity.this);

                } else if (GalleryUtils.getFileType(file) == GalleryUtils.VIDEO) {

                    DeleteFileUtils.videoDeleteRecord(mFileInfoList.get(mCurrentPosition).getFilePath(), GalleryActivity.this);

                }
                mFileInfoList.remove(mCurrentPosition);
                mLastPosition = mCurrentPosition;
                showImage();
                if (mLastPosition >= mFileInfoList.size()) {
                    mLastPosition--;
                }
                LogUtils.e(TAG, " nsc instantiateItem=" + mCurrentPosition);
                mViewpager.setCurrentItem(mLastPosition);
                Toast.makeText(GalleryActivity.this, getString(R.string.gallery_delete_success), Toast.LENGTH_SHORT).show();

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

        switch (v.getId()) {

            case R.id.rb_delete:
                showDeleteDialog();
                break;

            case R.id.rb_share:
                sharedFile();
                break;

            case R.id.iv_detail:
                showDetailDialog();
                break;

            case R.id.iv_all_file:
                Intent intent = new Intent(this,GalleryListActivity.class);
                startActivity(intent);
                break;
        }

    }

    /**
     * 显示详情
     */
    private void showDetailDialog() {
        final Dialog detailDialog = new Dialog(this, R.style.DialogTheme);
        detailDialog.setCanceledOnTouchOutside(true);
        detailDialog.show();
        Window window = detailDialog.getWindow();
        window.setContentView(R.layout.dialog_detail);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.5f;
        //window.setGravity(Gravity.BOTTOM);
        window.setAttributes(params);

        TextView tvFileName = (TextView) detailDialog.findViewById(R.id.tv_file_name);
        TextView tvFileType = (TextView) detailDialog.findViewById(R.id.tv_file_type);
        TextView tvFileDuration = (TextView) detailDialog.findViewById(R.id.tv_file_duration);
        TextView tvFileTime = (TextView) detailDialog.findViewById(R.id.tv_file_time);
        TextView tvFime = (TextView) detailDialog.findViewById(R.id.tv_time);
        TextView tvFileModify = (TextView) detailDialog.findViewById(R.id.tv_file_modify);
        TextView tvFilePath = (TextView) detailDialog.findViewById(R.id.tv_file_path);

        File file = new File(mFileInfoList.get(mCurrentPosition).getFilePath());
        tvFileName.setText(file.getName() + "");

        String ext = file.getName().substring(file.getName().lastIndexOf(".")
                + 1, file.getName().length()).toLowerCase();
        if (GalleryUtils.getFileType(file) == GalleryUtils.VIDEO) {
            tvFileType.setText("Video/" + ext);
            tvFime.setText(getResources().getString(R.string.gallery_file_time));
            tvFileTime.setText(CameraUtils.msToTime(mFileInfoList.get(mCurrentPosition).getFileS()));
        } else if (GalleryUtils.getFileType(file) == GalleryUtils.IMAGE) {
            tvFileType.setText("Image/" + ext);
            tvFime.setText(getResources().getString(R.string.gallery_file_resolution));

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            if (bitmap != null) {
                tvFileTime.setText(bitmap.getWidth() + "x" + bitmap.getHeight());
            }
        }

        tvFileDuration.setText(CameraUtils.longToSize(file.length()));

        tvFileModify.setText(CameraUtils.ms2Date(file.lastModified()) + "");
        tvFilePath.setText(file.getPath() + " ");


    }

    /**
     * 实现文件分享
     */
    private void sharedFile() {
        File file = new File(mFileInfoList.get(mCurrentPosition).getFilePath());
        Intent mShareIntent = new Intent();
        mShareIntent.setAction(Intent.ACTION_SEND);
        mShareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getPath()));
        mShareIntent.setType("*/*");
        startActivity(Intent.createChooser(mShareIntent, ""));
    }

}

