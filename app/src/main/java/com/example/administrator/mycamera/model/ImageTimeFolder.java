package com.example.administrator.mycamera.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 550211 on 2017/7/4.
 */

public class ImageTimeFolder {
    private long time;
    private String thumbPath;

    private String filePath;

    public String getDate() {
        return new SimpleDateFormat("yyyy年MM月dd日")
                .format(new Date(time*1000L));
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
