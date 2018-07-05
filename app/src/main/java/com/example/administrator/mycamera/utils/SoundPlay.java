package com.example.administrator.mycamera.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.example.administrator.mycamera.R;

public class SoundPlay {

    public static final int FOCUS_COMPLETE = 0;
    public static final int START_VIDEO_RECORDING = 1;
    public static final int STOP_VIDEO_RECORDING = 2;
    public static final int SHUTTER_CLICK = 3;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private int[] sourceId  = {R.raw.camera_focus,R.raw.video_record1,R.raw.video_stop,R.raw.camera_click};

    public SoundPlay(Context context) {
        this.mContext = context;
        initSound();
    }

    private void initSound() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }


    public synchronized void startPlay(int action) {
        if (mMediaPlayer == null) return;
        mMediaPlayer = MediaPlayer.create(mContext, sourceId[action]);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 装载完毕回调
                mMediaPlayer.start();
            }
        });
    }

    public void releasePlayer(){
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }



}
