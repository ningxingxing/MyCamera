package com.example.administrator.mycamera.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/6/7.
 */

public class CameraPreference {

    public CameraPreference() {
    }

    //camera id
    public static final String CAMERA_ID = "cameraId";
    //曝光补偿
    public static final String KEY_EXPOSURE_COMPENSATION = "key_exposure_compensation";
    //白平衡
    public static final String KEY_WHITE_BALANCE = "key_white_balance";
    //场景模式
    public static final String KEY_SCENE_MODE = "key_scene_mode";
    //闪光灯
    public static final String KEY_FLASH_MODE = "key_flash_mode";
    //hdr
    public static final String KEY_SCENE_MODE_HDR = "key_scene_mode_hdr";

    public static final String KEY_ISO_MODE = "iso";

    public static final String KEY_STORAGE_PATH = "key_storage_path";
    public static final String KEY_PICTURE_SIZE="key_picture_size";
    public static final String KEY_AUXILIARY_LINE = "key_auxiliary_line";
    public static final String KEY_LOCATION = "key_location";
    public static final String KEY_TAKE_PICTURE_SOUND = "key_take_picture_sound";
    public static final String KEY_DELAY_SOUND = "key_delay_sound";
    public static final String KEY_FOCUSED_SOUND = "key_focused_sound";
    public static final String KEY_VOLUME_SOUND= "key_volume_sound";
    public static final String KEY_HD_PREVIEW = "key_hd_preview";
    public static final String KEY_COUNT_DOWN = "key_count_down";
    public static final String KEY_PREVIEW_SCALE = "key_preview_scale";

    public static final String FOCUS_MODE_CONTINUOUS_PICTURE = "continuous-picture";

    /**
     * 保存在手机里面的文件名
     */
//    public static String FILE_NAME = "hs_eba_sharedata";
    public static String SPLASH_KEY = "splash_key";
    public static String EXCHANGESTATE ="exchangeState";


    public static void setCameraId(Context context, int cameraId) {
        SharedPreferences pref = context.getSharedPreferences(CAMERA_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(CAMERA_ID, cameraId);
        editor.commit();
    }

    public static int getCameraId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(CAMERA_ID, Context.MODE_PRIVATE);
        return pref.getInt(CAMERA_ID, 0);
    }


    /**
     * 保存字符串数据到sharedPreference
     * @param key
     * @param tag
     */
    public static void saveStringPreference(Context context,String key, String tag) {
        SharedPreferences pref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, tag);
        editor.commit();
    }

    /**
     * 获取保存到sharedPreference的数据
     * @param key
     * @return
     */
    public static String getStringPreference(Context context,String key) {
        SharedPreferences pref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    /**
     * 保存整形数据
     * @param context
     * @param key
     * @param value
     */
    public static void saveIntPreference(Context context,String key,int value){
        SharedPreferences pref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntPreference(Context context,String key){
        SharedPreferences pref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

//    /**
//     * 清除所有数据
//     *
//     * @param context
//     */
//    public static void clear(Context context) {
//        SharedPreferences sp = MyApplication.sp;
//        SharedPreferences.Editor editor = sp.edit();
//        editor.clear();
//        SharedPreferencesCompat.apply(editor);
//    }
//
//    /**
//     * 查询某个key是否已经存在
//     *
//     * @param context
//     * @param key
//     * @return
//     */
//    public static boolean contains(Context context, String key) {
//        SharedPreferences sp = MyApplication.sp;
//        return sp.contains(key);
//    }
//
//    /**
//     * 返回所有的键值对
//     *
//     * @param context
//     * @return
//     */
//    public static Map<String, ?> getAll(Context context) {
//        SharedPreferences sp = MyApplication.sp;
//        return sp.getAll();
//    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

}
