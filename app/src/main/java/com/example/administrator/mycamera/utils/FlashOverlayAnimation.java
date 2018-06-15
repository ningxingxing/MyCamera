package com.example.administrator.mycamera.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by Administrator on 2018/6/15.
 */

public class FlashOverlayAnimation {

    private int DELAY_TIME = 250;

    public void startFlashAnimation(final View view) {
        view.setVisibility(View.VISIBLE);
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 0.2f);
        valueAnimator.setDuration(DELAY_TIME);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setAlpha(value);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();

    }
}
