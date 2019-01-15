package com.example.administrator.mycamera.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.administrator.mycamera.R;

public class MorePopupWindow extends PopupWindow implements View.OnClickListener{

    private View contentView;
    private Button btnMoreEdit;
    private Button btnPlay;
    private Button btnRename;
    private Button btnShare;
    private Button btnRotate;

    protected OnClickListener mOnClickListener;
    public MorePopupWindow(Activity context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        contentView = inflater.inflate(R.layout.popup_more, null);

        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(contentView);
        this.setWidth(w / 3 + 50);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.AnimationPreview);


        btnMoreEdit = (Button) contentView.findViewById(R.id.btn_more_edit);
        btnPlay = (Button) contentView.findViewById(R.id.btn_play);
        btnRename = (Button) contentView.findViewById(R.id.btn_rename);
        btnShare = (Button) contentView.findViewById(R.id.btn_share);
        btnRotate = (Button) contentView.findViewById(R.id.btn_rotate);
        btnMoreEdit.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnRename.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnRotate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_more_edit:

                if (mOnClickListener!=null){
                    mOnClickListener.moreEditClick(btnMoreEdit);
                }
                MorePopupWindow.this.dismiss();
                break;

            case R.id.btn_play:
                if (mOnClickListener!=null){
                    mOnClickListener.playClick(btnPlay);
                }

                this.dismiss();
                break;


            case R.id.btn_rename:
                if (mOnClickListener!=null){
                    mOnClickListener.renameClick(btnRename);
                }
                this.dismiss();
                break;

            case R.id.btn_share:
                if (mOnClickListener!=null){
                    mOnClickListener.shareClick(btnShare);
                }

                this.dismiss();
                break;


            case R.id.btn_rotate:
                if (mOnClickListener!=null){
                    mOnClickListener.rotateClick(btnRotate);
                }

                this.dismiss();
                break;

        }

    }


    public interface OnClickListener {
        void moreEditClick(Button b);

        void playClick(Button b);

        void renameClick(Button button);

        void shareClick(Button button);

        void rotateClick(Button button);
    }


    public void setMoreOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }


    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
             this.showAtLocation(parent, Gravity.RIGHT | Gravity.BOTTOM, 10, parent.getHeight()*2);
        } else {
            this.dismiss();
        }
    }

    /**
     * 控件不可操作
     */
    public void setEnableTrue() {
        btnRename.setEnabled(true);
    }

    /**
     * 设置控件可操作
     */
    public void setEnableFalse() {
        btnRename.setEnabled(false);
    }


    public void popupDismiss() {
        MorePopupWindow.this.dismiss();
    }
}
