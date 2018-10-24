package com.example.administrator.mycamera.utils;


import com.example.administrator.mycamera.model.FileInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by 550211 on 2017/6/2.
 */

public class SortUtils {

    private FileTimeComparator mFileTimeAscComp = new FileTimeComparator(true);//升序
    private FileTimeComparator mFileTimeDecComp = new FileTimeComparator(false);//降序
    private Object mLock = new Object();

    public void sort(ArrayList<FileInfo> mFileList, int sortType) {

        Comparator<? super FileInfo> comparator = mFileTimeAscComp;
        switch (sortType) {

            case CameraConstant.ID_SORT_TIME_ASC:
                comparator = mFileTimeAscComp;

                break;
            case CameraConstant.ID_SORT_TIME_DEC:
                comparator = mFileTimeDecComp;

                break;
            default:
                comparator = mFileTimeAscComp;

                break;
        }

        synchronized (mLock) {
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            Collections.sort(mFileList, comparator);
        }
    }
}
