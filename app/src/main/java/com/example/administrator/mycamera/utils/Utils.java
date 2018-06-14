package com.example.administrator.mycamera.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2018/6/12.
 */

public class Utils {


    public static String ms2Date(long ms) {
        Date date = new Date(ms);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }
}
