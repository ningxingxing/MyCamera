package com.example.administrator.mycamera.utils

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.administrator.mycamera.model.Video
import java.util.concurrent.TimeUnit


private const val TAG:String = "VideoUtils"

 fun getVideoList(context:Context){

    val videoList = mutableListOf<Video>()
    val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Video.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }

    val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.SIZE
    )

    // Show only videos that are at least 5 minutes in duration.
    val selection = "${MediaStore.Video.Media.DURATION} >= ?"
    val selectionArgs = arrayOf(
        TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS).toString()
    )

    // Display videos in alphabetical order based on their display name.
    val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"

    val query = context.contentResolver.query(
        collection,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )
     LogUtils.e(TAG,"getVideoList count=${query?.count}")
    query?.use { cursor ->
        // Cache column indices.
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val nameColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
        val durationColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

        while (cursor.moveToNext()) {
            // Get values of columns for a given video.
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val duration = cursor.getInt(durationColumn)
            val size = cursor.getInt(sizeColumn)

            val contentUri: Uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

            LogUtils.e(TAG,"getVideoList contentUri=$contentUri")
            // Stores column values and the contentUri in a local object
            // that represents the media file.
            videoList += Video(contentUri, name, duration, size)
        }
    }
     LogUtils.e(TAG,"getVideoList size=${videoList.size}")

}


fun getVideoFileList(context:Context){
    val videoList = mutableListOf<Video>()
    val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Video.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATA,
        MediaStore.Files.FileColumns.MIME_TYPE,
        MediaStore.Files.FileColumns.SIZE,
        MediaStore.Files.FileColumns.TITLE
    )

    val selection = "mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ?"
    val whereArgs = arrayOf(
        "video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/flv",
    )

    val query = context.contentResolver.query(
        collection,
        projection,
        selection,
        whereArgs,
        MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
    )

    LogUtils.e(TAG,"getVideoList count=${query?.count}")
    query?.use { cursor ->
        // Cache column indices.
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val nameColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        val durationColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
        val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)
        while (cursor.moveToNext()) {
            // Get values of columns for a given video.
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val duration = cursor.getInt(durationColumn)
            val size = cursor.getInt(sizeColumn)

            val contentUri: Uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

            LogUtils.e(TAG,"getVideoList contentUri=$contentUri  titleColumn=$titleColumn")
            // Stores column values and the contentUri in a local object
            // that represents the media file.
            videoList += Video(contentUri, name, duration, size)
        }
    }
    LogUtils.e(TAG,"getVideoList size=${videoList.size}")

}