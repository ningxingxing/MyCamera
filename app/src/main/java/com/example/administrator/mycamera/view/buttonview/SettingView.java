package com.example.administrator.mycamera.view.buttonview;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.administrator.mycamera.R;

/**
 * Created by Administrator on 2018/6/19.
 */

public class SettingView extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setting);
    }
}
