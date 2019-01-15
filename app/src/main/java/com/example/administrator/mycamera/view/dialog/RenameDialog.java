package com.example.administrator.mycamera.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.mycamera.R;

import java.io.File;

public class RenameDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private String mPath = "";

    private EditText etRename;
    private Button btnCancel;
    private Button btnRenameOk;

    private IRenameListener iRenameListener;

    public interface IRenameListener {
        void okClick(String name);
    }

    public void setRenameListener(IRenameListener listener) {
        this.iRenameListener = listener;
    }


    public RenameDialog(@NonNull Context context, String path) {
        super(context);
        this.mContext = context;
        this.mPath = path;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_rename, null);
        setContentView(view);

        initView(view);
        initData();
    }

    private void initView(View view) {

        etRename = (EditText) view.findViewById(R.id.et_rename);
        btnCancel = (Button) view.findViewById(R.id.btn_rename_cancel);
        btnRenameOk = (Button) view.findViewById(R.id.btn_rename_ok);

        btnCancel.setOnClickListener(this);
        btnRenameOk.setOnClickListener(this);
    }

    private void initData(){
        File file = new File(mPath);
        etRename.setText(file.getName());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.btn_rename_cancel:
                dismiss();
                break;


            case R.id.btn_rename_ok:

                String name = etRename.getText().toString().trim();
                File file = new File(mPath);
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(mContext, mContext.getString(R.string.gallery_rename_not_null), Toast.LENGTH_SHORT).show();
                } else if (name.equals(mPath)) {
                    Toast.makeText(mContext, mContext.getString(R.string.gallery_name_exist), Toast.LENGTH_SHORT).show();
                } else {

                    String newName = file.getParent() + "/" + etRename.getText().toString().trim();
                    if (file.renameTo(new File(newName))) {
                        mContext.getContentResolver()
                                .delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA
                                                + "=?",
                                        new String[]{mPath});
                        MediaScannerConnection.scanFile(mContext, new String[]{newName}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        if (iRenameListener != null) {
                                            iRenameListener.okClick(path);
                                        }
                                    }
                                });

                        Toast.makeText(mContext, mContext.getString(R.string.gallery_rename_success), Toast.LENGTH_SHORT).show();
                    }
                }


                dismiss();
                break;
        }
    }
}
