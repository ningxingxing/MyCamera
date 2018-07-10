package com.example.administrator.mycamera.view.buttonview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.port.IWhiteBalanceView;

public class WhiteBalanceView extends LinearLayout implements View.OnClickListener {

    private ImageView mLightAutomatic;
    private ImageView mLightDaylight;
    private ImageView mLightFluores;
    private ImageView mLightIncandescent;
    private ImageView mLightOvercast;
    private Context mContext;

    private IWhiteBalanceView mIWhiteBalanceView;

    public WhiteBalanceView(Context context) {
        this(context, null);
    }

    public WhiteBalanceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WhiteBalanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.camera_top_light, this);
        initView();
    }

    public void setWhiteBalanceViewClickListener(IWhiteBalanceView iWhiteBalanceView) {
        this.mIWhiteBalanceView = iWhiteBalanceView;
    }

    private void initView() {
        mLightAutomatic = (ImageView) findViewById(R.id.light_automatic);
        mLightDaylight = (ImageView) findViewById(R.id.light_daylight);
        mLightFluores = (ImageView) findViewById(R.id.light_fluores);
        mLightIncandescent = (ImageView) findViewById(R.id.light_incandescent);
        mLightOvercast = (ImageView) findViewById(R.id.light_overcast);

        mLightAutomatic.setOnClickListener(this);
        mLightDaylight.setOnClickListener(this);
        mLightFluores.setOnClickListener(this);
        mLightIncandescent.setOnClickListener(this);
        mLightOvercast.setOnClickListener(this);
    }

    public void updateSelectIcon(int index) {
        mLightAutomatic.setSelected(index==0);
        mLightDaylight.setSelected(index==1);
        mLightFluores.setSelected(index==2);
        mLightIncandescent.setSelected(index==3);
        mLightOvercast.setSelected(index==4);
    }

    @Override
    public void onClick(View v) {
        int toast = 0;
        switch (v.getId()) {

            case R.id.light_automatic:
                if (mIWhiteBalanceView != null) {
                    mIWhiteBalanceView.onWhiteBalanceClick("auto", 0);
                    toast = R.string.pref_camera_whitebalance_entry_auto;
                }
                break;

            case R.id.light_daylight:
                if (mIWhiteBalanceView != null) {
                    mIWhiteBalanceView.onWhiteBalanceClick("daylight", 1);
                    toast = R.string.pref_camera_whitebalance_entry_daylight;
                }
                break;

            case R.id.light_fluores:
                if (mIWhiteBalanceView != null) {
                    mIWhiteBalanceView.onWhiteBalanceClick("fluorescent", 2);
                    toast = R.string.pref_camera_whitebalance_entry_fluorescent;
                }
                break;

            case R.id.light_incandescent:
                if (mIWhiteBalanceView != null) {
                    mIWhiteBalanceView.onWhiteBalanceClick("incandescent", 3);
                    toast = R.string.pref_camera_whitebalance_entry_incandescent;
                }
                break;

            case R.id.light_overcast:
                if (mIWhiteBalanceView != null) {
                    mIWhiteBalanceView.onWhiteBalanceClick("cloudy-daylight", 4);
                    toast = R.string.pref_camera_whitebalance_entry_cloudy;
                }
                break;
        }
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}

