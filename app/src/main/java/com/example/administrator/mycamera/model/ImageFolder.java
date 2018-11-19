package com.example.administrator.mycamera.model;

import java.io.File;

/**
 * Created by 550211 on 2017/6/3.
 */

public class ImageFolder {

    /**
     * image folder path
     */
    public String folderPath;

    /**
     * first image path
     */
    public String firstImagePath;

    /**
     * folder name
     */
    public String folderName;

    /**
     * picture count
     */
    public int imageCount;

    public String bucketId;

    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
//this.firstImagePath = firstImagePath;
        if (firstImagePath != null) {
            this.firstImagePath = firstImagePath;
            File f = new File(firstImagePath);
            this.folderPath = f.getParentFile().getAbsolutePath();
        }
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }
}
