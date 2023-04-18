package com.example.administrator.mycamera.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.activity.CameraPreferenceSettingActivity;
import com.example.administrator.mycamera.model.CameraPreference;
import com.example.administrator.mycamera.port.ISettingFragment;
import com.example.administrator.mycamera.utils.CameraConstant;
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
    private Preference mCountDown;
    private Preference mPreviewScale;
    private int COUNT_DOWN_RESULT = 1;
    private int PREVIEW_SCALE = 2;


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
                    if (mISettingFragment != null) {
                        mISettingFragment.showPictureSizeSelect();
                        //mISettingFragment.closeSettingFragment();
                    }
                    return true;
                }
            });

        }
        //倒计时
        mCountDown = findPreference(CameraPreference.KEY_COUNT_DOWN);
        findPreference(CameraPreference.KEY_COUNT_DOWN).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                //getCountDownData();
                Intent intent = new Intent(getActivity(), CameraPreferenceSettingActivity.class);
                intent.putExtra(CameraConstant.SETTING_FRAGMENT,CameraConstant.COUNT_DOWN_DATA);
                startActivityForResult(intent,COUNT_DOWN_RESULT);
                return true;
            }
        });

        //拍照画幅
        mPreviewScale = findPreference(CameraPreference.KEY_PREVIEW_SCALE);
        findPreference(CameraPreference.KEY_PREVIEW_SCALE).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent intent = new Intent(getActivity(), CameraPreferenceSettingActivity.class);
                intent.putExtra(CameraConstant.SETTING_FRAGMENT,CameraPreference.KEY_PREVIEW_SCALE);
                startActivityForResult(intent,PREVIEW_SCALE);
                return false;
            }
        });

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
     *
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
     *
     * @param sharedPreferences
     * @param key               item key
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
                if (mISettingFragment != null) {
                    boolean isAuxiliaryLine = mSharedPreferences.getBoolean(CameraPreference.KEY_AUXILIARY_LINE, false);
                    mISettingFragment.setAuxiliaryLine(isAuxiliaryLine);
                }
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // LogUtils.e(TAG,"nsc ="+requestCode);
        if (resultCode==RESULT_OK){

            if (requestCode==COUNT_DOWN_RESULT){

                if (data!=null){
                    String countDown = data.getStringExtra("countDownTime");
                    mCountDown.setSummary(countDown+"");
                }

            }else if (requestCode ==PREVIEW_SCALE){
                if (data!=null){
                    String previewScale = data.getStringExtra("previewScale");
                    mPreviewScale.setSummary(previewScale);
                }

            }

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //LogUtils.e(TAG,"nsc setUserVisibleHint ="+isVisibleToUser);
        if (isVisibleToUser){
            String countDownTime = (String) CameraPreference.get(getActivity(),CameraPreference.KEY_COUNT_DOWN,"0");
            mCountDown.setSummary(countDownTime+"");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //LogUtils.e(TAG,"nsc onHiddenChanged ="+hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        //LogUtils.e(TAG,"nsc onResume =");
        String countDownTime = (String) CameraPreference.get(getActivity(),CameraPreference.KEY_COUNT_DOWN,"0");
        mCountDown.setSummary(countDownTime+"");
    }
}
