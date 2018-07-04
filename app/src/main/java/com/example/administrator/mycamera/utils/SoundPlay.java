package com.example.administrator.mycamera.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.example.administrator.mycamera.R;

import java.util.HashMap;

public class SoundPlay implements SoundPool.OnLoadCompleteListener {

    public static final int FOCUS_COMPLETE = 1;
    public static final int START_VIDEO_RECORDING = 2;
    public static final int STOP_VIDEO_RECORDING = 3;
    public static final int SHUTTER_CLICK = 4;
    private Context mContext;
    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();

    public SoundPlay(Context context) {
        this.mContext = context;
        initSound();
    }

    private void initSound() {
        mSoundPool = new SoundPool(10, AudioManager.STREAM_ALARM, 5);
        soundID.put(FOCUS_COMPLETE, mSoundPool.load(mContext, R.raw.camera_focus, 1));
        soundID.put(START_VIDEO_RECORDING, mSoundPool.load(mContext, R.raw.video_record1, 1));
        soundID.put(STOP_VIDEO_RECORDING, mSoundPool.load(mContext, R.raw.video_stop, 1));
        soundID.put(SHUTTER_CLICK, mSoundPool.load(mContext, R.raw.camera_click, 1));
    }


    public synchronized void startPlay(int action) {
        if (mSoundPool == null) return;
        switch (action) {
            case FOCUS_COMPLETE:
                mSoundPool.play(soundID.get(FOCUS_COMPLETE), 1f, 1f, 0, 0, 1f);
                LogUtils.e("nsc","startPlay ="+soundID.get(FOCUS_COMPLETE));
                break;

            case START_VIDEO_RECORDING:
                mSoundPool.play(soundID.get(START_VIDEO_RECORDING), 1f, 1f, 0, 0, 1f);
                break;

            case STOP_VIDEO_RECORDING:
                mSoundPool.play(soundID.get(STOP_VIDEO_RECORDING), 1f, 1f, 0, 0, 1f);
                break;

            case SHUTTER_CLICK:
                mSoundPool.play(soundID.get(SHUTTER_CLICK), 1f, 1f, 0, 0, 1f);
                break;
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        LogUtils.e("nsc","onLoadComplete");
    }

    public void releaseSound() {
        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }
    }

}
