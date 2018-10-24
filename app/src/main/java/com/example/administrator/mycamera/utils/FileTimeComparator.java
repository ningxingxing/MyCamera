package com.example.administrator.mycamera.utils;



import com.example.administrator.mycamera.model.FileInfo;

import java.util.Comparator;

public class FileTimeComparator implements Comparator<FileInfo> {

    private final boolean mIsAsc;

    public FileTimeComparator(boolean isAsc) {

        super();
        mIsAsc = isAsc;
    }

    @Override
    public int compare(FileInfo file1, FileInfo file2) {

        long retLong = comp(file1, file2);

        retLong = mIsAsc ? retLong : -retLong;

        if (retLong >= Integer.MAX_VALUE)
            retLong = Integer.MAX_VALUE;
        if (retLong <= Integer.MIN_VALUE)
            retLong = Integer.MIN_VALUE;

        int ret = (int) retLong;

        return ret;
    }

    public long comp(FileInfo file1, FileInfo file2) {

        if (file1.isDir() && file2.isFile())
            return -1;
        if (file1.isFile() && file2.isDir())
            return 1;
        return (file1.getModifiedData() - file2.getModifiedData());
    }
}
