package com.example.administrator.mycamera.view.buttonview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.model.CameraPreference;
import com.example.administrator.mycamera.port.IScenesView;
import com.example.administrator.mycamera.utils.CameraParameter;
import com.example.administrator.mycamera.utils.LogUtils;


public class ScenesView extends LinearLayout implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private final String TAG = "Cam_ScenesView";
    private RadioGroup rgScenes;
    private RadioButton rbClose;
    private RadioButton rbSport;
    private RadioButton rbParty;
    private RadioButton rbSunset;
    private RadioButton rbNight;
    private IScenesView iScenesView;

    private Context mContext;
    private Camera.Parameters mParameter;
    private String[] sceneMode = {"auto", "sports", "party", "sunset", "night"};
    private int[] sceneDrawable = {R.drawable.sce_default, R.drawable.sce_sport_t, R.drawable.sce_party_t,
            R.drawable.sce_sunset_t, R.drawable.sce_night_t};

    private String mCurrentWhiteBalance;

    public ScenesView(Context context) {
        this(context, null);
    }

    public void setScenesClickListener(IScenesView iScenesView, Camera.Parameters parameter) {
        this.iScenesView = iScenesView;
        this.mParameter = parameter;
        LogUtils.e(TAG, "nsc mParameter=" + mParameter);
        initData();
    }

    public ScenesView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScenesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_sences, this);
        initView();
        //initData();
    }

    private void initView() {
        rgScenes = (RadioGroup) findViewById(R.id.rg_scenes);
        rbClose = (RadioButton) findViewById(R.id.rb_close);
        rbSport = (RadioButton) findViewById(R.id.rb_sport);
        rbParty = (RadioButton) findViewById(R.id.rb_party);
        rbSunset = (RadioButton) findViewById(R.id.rb_sunset);
        rbNight = (RadioButton) findViewById(R.id.rb_night);
        rgScenes.setOnCheckedChangeListener(this);

        rbClose.setOnClickListener(this);
        rbSport.setOnClickListener(this);
        rbParty.setOnClickListener(this);
        rbSunset.setOnClickListener(this);
        rbNight.setOnClickListener(this);
    }

    private void initData() {
        RadioButton[] sceneId = {rbClose, rbSport, rbParty, rbSunset, rbNight};

        for (int i = 0; i < sceneMode.length; i++) {
            if (CameraParameter.isSupportedSceneMode(mParameter, sceneMode[i])) {
                sceneId[i].setEnabled(true);
            } else {
                sceneId[i].setEnabled(false);
                sceneId[i].setVisibility(INVISIBLE);
              //  sceneId[i].setCompoundDrawables(null,null,getResources().getDrawable(sceneDrawable[i]),null);
            }
        }
        setSelectScene();
    }

    private void setSelectScene(){
        String scene = (String)CameraPreference.get(mContext,CameraPreference.KEY_SCENE_MODE,"auto");
        for (int i=0;i<sceneMode.length;i++){
            if (scene.equals(sceneMode[i])){
                switch (scene){
                    case "auto":
                        rgScenes.check(R.id.rb_close);
                        break;

                    case "sports":
                        rgScenes.check(R.id.rb_sport);
                        break;

                    case "party":
                        rgScenes.check(R.id.rb_party);
                        break;

                    case "sunset":
                        rgScenes.check(R.id.rb_sunset);
                        break;

                    case "night":
                        rgScenes.check(R.id.rb_night);
                        break;
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mCurrentWhiteBalance = CameraPreference.getStringPreference(mContext, CameraPreference.KEY_WHITE_BALANCE);
        if (!"auto".equals(mCurrentWhiteBalance)){
            return;
        }
        switch (checkedId) {
            case R.id.rb_close:
                if (iScenesView != null) {
                    iScenesView.onScenesClick(sceneMode[0]);
                }
                rgScenes.check(R.id.rb_close);
                break;

            case R.id.rb_sport:
                if (iScenesView != null) {
                    iScenesView.onScenesClick(sceneMode[1]);
                }
                rgScenes.check(R.id.rb_sport);
                break;

            case R.id.rb_party:
                if (iScenesView != null) {
                    iScenesView.onScenesClick(sceneMode[2]);
                }
                rgScenes.check(R.id.rb_party);
                break;

            case R.id.rb_sunset:
                if (iScenesView != null) {
                    iScenesView.onScenesClick(sceneMode[3]);
                }
                rgScenes.check(R.id.rb_sunset);
                break;

            case R.id.rb_night:
                if (iScenesView != null) {
                    iScenesView.onScenesClick(sceneMode[4]);
                }
                rgScenes.check(R.id.rb_night);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_close:
                if (iScenesView != null) {
                    iScenesView.onScenesClick(sceneMode[0]);
                }
                break;

            case R.id.rb_sport:
                if (iScenesView != null) {
                    iScenesView.onScenesClick(sceneMode[1]);
                }
                break;

            case R.id.rb_party:
                if (iScenesView != null) {
                    iScenesView.onScenesClick(sceneMode[2]);
                }
                break;

            case R.id.rb_sunset:
                if (iScenesView != null) {
                    iScenesView.onScenesClick(sceneMode[3]);
                }
                break;
            case R.id.rb_night:
                if (iScenesView != null) {
                    iScenesView.onScenesClick(sceneMode[4]);
                }
                break;

        }
    }
}
