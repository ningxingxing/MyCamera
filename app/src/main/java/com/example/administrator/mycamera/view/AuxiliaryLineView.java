package com.example.administrator.mycamera.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.mycamera.R;

/**
 * Created by Administrator on 2018/6/27.
 */

public class AuxiliaryLineView extends View{
    private Paint mPaint;
    private float mGuideLineWidth;
    private boolean mIsGuidesLineOn = false;

    public AuxiliaryLineView(Context context) {
        super(context);
        mGuideLineWidth = context.getResources().getDimension(R.dimen.guide_line_width);
    }

    public AuxiliaryLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void setAuxiliaryLine(boolean flag){
        this.mIsGuidesLineOn = flag;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mIsGuidesLineOn) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(getResources().getColor(R.color.shutter_selector_end));
            mPaint.setStrokeWidth(mGuideLineWidth);
            drawGuidesLines(canvas, mPaint);
        }
    }

    private void drawGuidesLines(Canvas canvas, Paint paint) {
        int width = getWidth();
        int height = getHeight();

        int verticalGap = height / 3;
        int horizonGap = width / 3;
        canvas.drawLine(0, verticalGap, width, verticalGap, paint);
        canvas.drawLine(0, verticalGap * 2, width, verticalGap * 2, paint);

        canvas.drawLine(horizonGap, 0, horizonGap, height, paint);
        canvas.drawLine(horizonGap * 2, 0, horizonGap * 2, height, paint);
    }
}
