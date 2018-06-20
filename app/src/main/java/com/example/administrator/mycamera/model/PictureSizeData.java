package com.example.administrator.mycamera.model;

/**
 * Created by Administrator on 2018/6/20.
 */

public class PictureSizeData {
    private String pictureWidth;
    private String pictureHeight;
    private int currentPictureWidth;
    private int currentPictureHeight;

    public PictureSizeData() {
    }

    public String getPictureWidth() {
        return pictureWidth;
    }

    public void setPictureWidth(String pictureWidth) {
        this.pictureWidth = pictureWidth;
    }

    public String getPictureHeight() {
        return pictureHeight;
    }

    public void setPictureHeight(String pictureHeight) {
        this.pictureHeight = pictureHeight;
    }

    public int getCurrentPictureWidth() {
        return currentPictureWidth;
    }

    public void setCurrentPictureWidth(int currentPictureWidth) {
        this.currentPictureWidth = currentPictureWidth;
    }

    public int getCurrentPictureHeight() {
        return currentPictureHeight;
    }

    public void setCurrentPictureHeight(int currentPictureHeight) {
        this.currentPictureHeight = currentPictureHeight;
    }
}
