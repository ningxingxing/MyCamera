package com.example.administrator.mycamera.view.buttonview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.mycamera.R;

/**
 * Created by Administrator on 2018/7/2.
 */

public class FocusAnimationView extends View {
    private final String TAG = "FocusAnimationView";

    private ValueAnimator mValueAnimator;
    private Paint mPaint;
    private float mDownX;
    private float mDownY;
    private float mCircleRadius = 50;

    public FocusAnimationView(Context context) {
        this(context, null);

    }

    public FocusAnimationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.shutter_selector_end));
        mPaint.setStrokeWidth(2);
    }

    public void setFocusAnimation() {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawCircle(mDownX,mDownY, mCircleRadius, mPaint);
        //LogUtils.e(TAG,"dispatchDraw mDownX="+mDownX  + "mDownY="+mDownY );
    }

    public void startFocusAnimation( float downX,  float downY) {
        this.mDownX = downX;
        this.mDownY = downY;
        mPaint.setColor(getResources().getColor(R.color.shutter_selector_end));
        mValueAnimator = ValueAnimator.ofFloat(70f, 50f);
        mValueAnimator.setDuration(1000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mCircleRadius = value;
                setFocusAnimation();
            }
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mPaint.setColor(getResources().getColor(R.color.yellow));
                setFocusAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mValueAnimator.start();

    }

    /**
     *
     */
    public void setFocusAnimationCancel(){
        if (mValueAnimator!=null && !mValueAnimator.isRunning()){
            mValueAnimator.cancel();
        }
    }

}
