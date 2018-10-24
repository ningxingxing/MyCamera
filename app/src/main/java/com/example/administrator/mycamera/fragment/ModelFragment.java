package com.example.administrator.mycamera.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.port.IModeFragment;

/**
 * Created by Administrator on 2018/6/20.
 */

public class ModelFragment extends Fragment implements View.OnClickListener{

    private RadioButton rbHdr;
    private IModeFragment iModeFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_model,null);


        initView(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initView(View v) {

        rbHdr =(RadioButton)v.findViewById(R.id.rb_hdr);


    }

    public void setIModeFragment(IModeFragment iModeFragment) {
        this.iModeFragment = iModeFragment;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.rb_hdr:
                if (iModeFragment!=null){
                    iModeFragment.openHdr(true);
                }

                break;


        }
    }
}
