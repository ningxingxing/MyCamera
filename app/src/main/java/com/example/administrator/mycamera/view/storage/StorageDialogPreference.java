package com.example.administrator.mycamera.view.storage;

import android.app.Dialog;
import android.content.Context;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.mycamera.utils.CameraUtils;
import com.example.administrator.mycamera.utils.LogUtils;
import com.example.administrator.mycamera.view.storage.StorageDialog;

/**
 * Created by fz on 2017/5/16.
 */

public class StorageDialogPreference extends Preference implements Preference.OnPreferenceClickListener,Preference.OnPreferenceChangeListener,StorageDialog.OnStoragePathChangedListener {
    private final String TAG = "Cam_StorageDialogPreference";
    private Context mContext;
    public StorageDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;

        this.setOnPreferenceClickListener(this);
        this.setOnPreferenceChangeListener(this);
    }

    @Override
    protected void onBindView(View view) {
        setSummary(getPersistedString(CameraUtils.DEFAULT_SAVE_PATH));
        super.onBindView(view);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String path= (String) newValue;
        if (preference == this) {
            setSummary(path);
            LogUtils.e(TAG,"onPreferenceChange path="+path);
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        showStorageDialog();
        return true;
    }

    private void showStorageDialog() {
        final Dialog dialog = new StorageDialog(mContext, this);
        dialog.show();
    }


    @Override
    public void OnStoragePathChanged(String path) {
        final boolean changed = !TextUtils.equals(
                getPersistedString(CameraUtils.DEFAULT_SAVE_PATH), path);
        if (changed) {
           // CCGUtils.sendHomeEvent("path_change");
            persistString(path);
            callChangeListener(path);
            notifyChanged();
        }
    }
}
