package com.example.administrator.mycamera.model;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 550211 on 2017/4/24.
 * use to save file data
 */

public class FileInfo {
    private int fileImage;
    private String fileSize;
    private long fileS;
    private String fileTime;
    private String fileName;
    private String filePath;
    private String lastPath;
    private int FileNum;
    private boolean canRead;
    private boolean canWrite;
    private boolean selected;
    private boolean isHidden;
    private long ModifiedData;
    private boolean isDir;
    private boolean isFile;
    private boolean isFavorite;
    private String pinyin;
    private String firstLetter;

    private String thumbPath;
    private long time;
    private long duration;

    public FileInfo() {
    }
//
//    public FileInfo(int fileImage, String fileSize, long fileS, String fileTime, String fileName, String filePath, String lastPath, int fileNum, boolean canRead,
//                    boolean canWrite, boolean selected, boolean isHidden, long modifiedData, boolean isDir, boolean isFile, boolean isFavorite, String pinyin,
//                    String firstLetter) {
//        this.fileImage = fileImage;
//        this.fileSize = fileSize;
//        this.fileS = fileS;
//        this.fileTime = fileTime;
//        this.fileName = fileName;
//        this.filePath = filePath;
//        this.lastPath = lastPath;
//        FileNum = fileNum;
//        this.canRead = canRead;
//        this.canWrite = canWrite;
//        this.selected = selected;
//        this.isHidden = isHidden;
//        ModifiedData = modifiedData;
//        this.isDir = isDir;
//        this.isFile = isFile;
//        this.isFavorite = isFavorite;
//        this.pinyin = pinyin;
//        this.firstLetter = firstLetter;
//    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getFileImage() {
        return fileImage;
    }

    public void setFileImage(int fileImage) {
        this.fileImage = fileImage;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public long getFileS() {
        return fileS;
    }

    public void setFileS(long fileS) {
        this.fileS = fileS;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLastPath() {
        return lastPath;
    }

    public void setLastPath(String lastPath) {
        this.lastPath = lastPath;
    }

    public int getFileNum() {
        return FileNum;
    }

    public void setFileNum(int fileNum) {
        FileNum = fileNum;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public long getModifiedData() {
        return ModifiedData;
    }

    public void setModifiedData(long modifiedData) {
        ModifiedData = modifiedData;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

//    @Override
//    public String toString() {
//        return "FileInfo{" +
//                "fileSize='" + fileSize + '\'' +
//                ", fileS=" + fileS +
//                ", fileTime='" + fileTime + '\'' +
//                ", fileName='" + fileName + '\'' +
//                ", filePath='" + filePath + '\'' +
//                ", lastPath='" + lastPath + '\'' +
//                ", FileNum=" + FileNum +
//                ", canRead=" + canRead +
//                ", canWrite=" + canWrite +
//                ", selected=" + selected +
//                ", isHidden=" + isHidden +
//                ", ModifiedData=" + ModifiedData +
//                ", isDir=" + isDir +
//                ", isFile=" + isFile +
//                '}';
//    }

    public String getDate() {
        return new SimpleDateFormat("yyyy年MM月dd日")
                .format(new Date(time*1000L));
    }


}
