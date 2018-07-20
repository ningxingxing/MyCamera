package com.example.administrator.mycamera.filters;

public class FiltersUtil {
    public static final String FILTER_DEFAULT = "default";
    public static final String FILTER_LIGHT = "filter_light_key";
    public static final String FILTER_LIGHT_YELLOW = "yellow";
    public static final String FILTER_LIGHT_YELLOWING = "yellowing";
    public static final String FILTER_LIGHT_SUNSET_GLOW = "sunset_glow";
    public static final String FILTER_LIGHT_FOG = "fog";
    public static final String FILTER_LIGHT_RICH = "rich";
    public static final String FILTER_LIGHT_HIGHTLIGHT = "hightlight";
    public static final String FILTER_LIGHT_LOWLIGHT = "lowlight";
    
    public static final String FILTER_SPECIAL = "filter_special_key";
    public static final String FILTER_SPECIAL_INVERT = "invert";
    public static final String FILTER_SPECIAL_REFIEF = "relief";
    public static final String FILTER_SPECIAL_SKETCH = "sketch";
    public static final String FILTER_SPECIAL_BW = "bw";
    public static final String FILTER_SPECIAL_INK = "ink";
    
    public static final String FILTER_FUNNY = "filter_funny_key";
    public static final String FILTER_FUNNY_PINCH = "pinch";
    public static final String FILTER_FUNNY_WAVE = "wave";
    public static final String FILTER_FUNNY_MIRROR = "mirror";
    public static final String FILTER_FUNNY_SPHERE = "sphere";
    public static final String FILTER_FUNNY_MAGIC = "magic_mirror";
    
    public static final String FILTER_LOMO = "filter_lomo_key";
    public static final String Filter_LOMO_WRAM = "wram";
    public static final String Filter_LOMO_FILM = "film";
    public static final String Filter_LOMO_FALL = "fall";
    public static final String Filter_LOMO_COLD = "cold";
    public static final String Filter_LOMO_OLD = "old";
    public static final String Filter_LOMO_CYAN = "cyan";
    
    public static final int FILTER_TYPE_DEFAULT = 0;
    public static final int FILTER_TYPE_FUNNY_PINCH = 1;
    public static final int FILTER_TYPE_FUNNY_WAVE = 2;
    public static final int FILTER_TYPE_FUNNY_MIRROR = 3;
    public static final int FILTER_TYPE_FUNNY_SPHERE = 4;
    public static final int FILTER_TYPE_FUNNY_MAGIC = 5;
    
    public static final int FILTER_TYPE_LOMO_WARM = 8;
    public static final int FILTER_TYPE_LOMO_FILM = 9;
    public static final int FILTER_TYPE_LOMO_FALL = 10;
    public static final int FILTER_TYPE_LOMO_COLD = 11;
    public static final int FILTER_TYPE_LOMO_OLD = 12;
    public static final int FILTER_TYPE_LOMO_CYAN = 13;
    
    public static final int FILTER_TYPE_SPECIAL_INVERT = 15;
    public static final int FILTER_TYPE_SPECIAL_REFIEF = 16;
    public static final int FILTER_TYPE_SPECIAL_SKETCH = 17;
    public static final int FILTER_TYPE_SPECIAL_BW = 18;
    public static final int FILTER_TYPE_SPECIAL_INK = 19;
    
    public static final int FILTER_TYPE_LIGHT_YELLOW = 21;
    public static final int FILTER_TYPE_LIGHT_YELLOWING = 22;
    public static final int FILTER_TYPE_LIGHT_SUNSET_GLOW = 23;
    public static final int FILTER_TYPE_LIGHT_FOG = 24;
    public static final int FILTER_TYPE_LIGHT_RICH = 25;
    public static final int FILTER_TYPE_LIGHT_HIGHTLIGHT = 26;
    public static final int FILTER_TYPE_LIGHT_LOWLIGHT = 27;
    
    
    public static int getEffectTypeId(String type) {
        int typeId = FILTER_TYPE_DEFAULT;
        if (null == type || FILTER_DEFAULT.equals(type)) {
            typeId = FILTER_TYPE_DEFAULT;
        } else if (FILTER_SPECIAL_INVERT.equals(type)) {
            typeId = FILTER_TYPE_SPECIAL_INVERT;
        } else if (FILTER_SPECIAL_REFIEF.equals(type)) {
            typeId = FILTER_TYPE_SPECIAL_REFIEF;
        } else if (FILTER_SPECIAL_SKETCH.equals(type)) {
            typeId = FILTER_TYPE_SPECIAL_SKETCH;
        } else if (FILTER_SPECIAL_BW.equals(type)) {
            typeId = FILTER_TYPE_SPECIAL_BW;
        } else if (FILTER_SPECIAL_INK.equals(type)) {
            typeId = FILTER_TYPE_SPECIAL_INK;
        } else if(FILTER_FUNNY_PINCH.equals(type)) {
            typeId = FILTER_TYPE_FUNNY_PINCH;
        } else if (FILTER_FUNNY_SPHERE.equals(type)) {
            typeId =  FILTER_TYPE_FUNNY_SPHERE;
        } else if (FILTER_FUNNY_WAVE.equals(type)) {
            typeId =  FILTER_TYPE_FUNNY_WAVE;
        } else if (FILTER_FUNNY_MIRROR.equals(type)) {
            typeId =  FILTER_TYPE_FUNNY_MIRROR;
        } else if (FILTER_FUNNY_MAGIC.equals(type)) {
            typeId =  FILTER_TYPE_FUNNY_MAGIC;
        } else if (Filter_LOMO_WRAM.equals(type)) {
            typeId =  FILTER_TYPE_LOMO_WARM;
        } else if (Filter_LOMO_FILM.equals(type)) {
            typeId = FILTER_TYPE_LOMO_FILM;
        } else if (Filter_LOMO_FALL.equals(type)) {
            typeId =  FILTER_TYPE_LOMO_FALL;
        } else if (Filter_LOMO_COLD.equals(type)) {
            typeId =  FILTER_TYPE_LOMO_COLD;
        } else if (Filter_LOMO_OLD.equals(type)) {
            typeId =  FILTER_TYPE_LOMO_OLD;
        } else if (Filter_LOMO_CYAN.equals(type)) {
            typeId =  FILTER_TYPE_LOMO_CYAN;
        } else if (FILTER_LIGHT_YELLOW.equals(type)) {
            typeId = FILTER_TYPE_LIGHT_YELLOW;
        } else if (FILTER_LIGHT_YELLOWING.equals(type)) {
            typeId = FILTER_TYPE_LIGHT_YELLOWING;
        } else if (FILTER_LIGHT_SUNSET_GLOW.equals(type)) {
            typeId = FILTER_TYPE_LIGHT_SUNSET_GLOW;
        } else if (FILTER_LIGHT_FOG.equals(type)) {
            typeId = FILTER_TYPE_LIGHT_FOG;
        } else if (FILTER_LIGHT_RICH.equals(type)) {
            typeId = FILTER_TYPE_LIGHT_RICH;
        } else if (FILTER_LIGHT_HIGHTLIGHT.equals(type)) {
            typeId = FILTER_TYPE_LIGHT_HIGHTLIGHT;
        } else if (FILTER_LIGHT_LOWLIGHT.equals(type)) {
            typeId = FILTER_TYPE_LIGHT_LOWLIGHT;
        }
        return typeId;
    }

}
