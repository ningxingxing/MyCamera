package com.example.administrator.mycamera.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private final String TAG = "Cam_MainActivity";
    private static final String[] permissionsArray = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int REQUEST_PERMISSION = 1;

    //还需申请的权限列表
    private List<String> permissionsList = new ArrayList<>();
    //申请权限后的返回码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkRequiredPermission(this);
        } else {
            goToCamera();
        }
    }


    private void checkRequiredPermission(final Activity activity) {
        for (String permission : permissionsArray) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }
        }
        if (permissionsList != null && permissionsList.size()>0) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_PERMISSION);
        }else {
            goToCamera();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        goToCamera();
                    } else {
                        Toast.makeText(MainActivity.this, "权限被拒绝： " + permissions[i], Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void goToCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.setAction(getIntent().getAction());
        startActivity(intent);
        finish();
    }
}
