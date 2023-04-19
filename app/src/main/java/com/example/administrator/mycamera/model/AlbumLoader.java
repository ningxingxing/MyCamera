package com.example.administrator.mycamera.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.MediaStore;

import androidx.loader.content.CursorLoader;

/**
 * @author nsc
 * @date 2017/11/22.
 */

public class AlbumLoader extends CursorLoader {

    public static final String COLUMN_COUNT = "count";

    /**
     * content://media/external/file
     */
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");

    public static Uri getCurrentUri() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
    }

    private static final String[] COLUMNS = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.DATA,
            COLUMN_COUNT};

    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.DATA,
            "COUNT(*) AS " + COLUMN_COUNT
    };//"COUNT(*) AS " + COLUMN_COUNT


    private static final String[] PROJECTION_Q = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    };

    public static String[] getCurrentProjection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return PROJECTION_Q;
        } else {
            return PROJECTION;
        }
    }


    /**
     * (media_type=? OR media_type =?) AND _size>0) GROUP BY (bucket_id
     */
    private static final String SELECTION =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                    + ") GROUP BY (bucket_id";

    private static final String SELECTION_Q = "mime_type = ? or mime_type = ?";
    private static final String[] SELECTION_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
    };

    public static String getCurrentSelection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return SELECTION_Q;
        } else {
            return SELECTION;
        }
    }

    private static final String BUCKET_ORDER_BY = "datetaken DESC";

    private AlbumLoader(Context context, String selection, String[] selectionArgs) {
        super(context, getCurrentUri(), getCurrentProjection(), getCurrentSelection(), SELECTION_ARGS, BUCKET_ORDER_BY);
    }

    public static CursorLoader newInstance(Context context) {
        String selection = SELECTION;
        String[] selectionArgs = SELECTION_ARGS;
        return new AlbumLoader(context, selection, selectionArgs);
    }

    @Override
    public Cursor loadInBackground() {
        return super.loadInBackground();
    }
}
