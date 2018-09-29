package com.example.administrator.mycamera.view.buttonview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.administrator.mycamera.R;

public class ScenesView extends LinearLayout {


    public ScenesView(Context context) {
        this(context,null);
    }

    public ScenesView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScenesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_sences, this);

    }
}
