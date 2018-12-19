package com.example.administrator.mycamera.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.administrator.mycamera.R;

public class VideoActivity extends Activity {

    private VideoView mVideoView;
    private String mPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        initView();
        initData();
    }

    private void initView() {
        mVideoView = (VideoView) findViewById(R.id.video_view);
    }

    private void initData() {

        mPath = getIntent().getStringExtra("videoPath");
        MediaController  controller = new MediaController(VideoActivity.this);//实例化控制器
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaybackVideo();
                finish();
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                stopPlaybackVideo();
                return true;
            }
        });

        try {



            Uri uri = Uri.parse(mPath);
            mVideoView.setVideoURI(uri);

            /**
             * 将控制器和播放器进行互相关联
             */
            controller.setMediaPlayer(mVideoView);
            mVideoView.setMediaController(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void stopPlaybackVideo() {
        try {
            mVideoView.stopPlayback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
