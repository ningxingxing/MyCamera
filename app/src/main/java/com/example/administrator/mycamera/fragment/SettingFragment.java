package com.example.administrator.mycamera.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.model.CameraPreference;
import com.example.administrator.mycamera.port.ISettingFragment;
import com.example.administrator.mycamera.utils.CameraUtils;
import com.example.administrator.mycamera.utils.LogUtils;

/**
 * Created by Administrator on 2018/6/20.
 */

public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final String TAG = "Cam_SettingFragment";
    private SharedPreferences mSharedPreferences;
    private ISettingFragment mISettingFragment;
    private Preference mPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setting);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        mPreference = findPreference(CameraPreference.KEY_PICTURE_SIZE);
        if (mPreference != null) {
            findPreference(CameraPreference.KEY_PICTURE_SIZE).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (mISettingFragment!=null){
                        mISettingFragment.showPictureSizeSelect();
                    }
                    return true;
                }
            });

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, null);
        initToolbar(view);
        return view;
    }

    /**
     * close setting Fragment
     * @param view
     */
    private void initToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.settings));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.icon_setting);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mISettingFragment != null) {
                    mISettingFragment.closeSettingFragment();
                }
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mISettingFragment != null) {
                    mISettingFragment.closeSettingFragment();
                }
            }
        });
    }

    public void setISettingFragment(ISettingFragment iSettingFragment) {
        this.mISettingFragment = iSettingFragment;
    }

    /**
     * Which option to listen for
     * @param sharedPreferences
     * @param key    item key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        LogUtils.e(TAG, "onSharedPreferenceChanged key=" + key);
        switch (key) {
            case CameraPreference.KEY_HD_PREVIEW:
                if (getActivity() != null) {
                    boolean isHdPreview = mSharedPreferences.getBoolean(CameraPreference.KEY_HD_PREVIEW, false);
                    CameraUtils.setBrightnessForCamera(getActivity().getWindow(), isHdPreview);
                }
                break;

            case CameraPreference.KEY_AUXILIARY_LINE:
                if (mISettingFragment!=null) {
                    boolean isAuxiliaryLine = mSharedPreferences.getBoolean(CameraPreference.KEY_AUXILIARY_LINE, false);
                    mISettingFragment.setAuxiliaryLine(isAuxiliaryLine);
                }
                break;
        }
    }

}
