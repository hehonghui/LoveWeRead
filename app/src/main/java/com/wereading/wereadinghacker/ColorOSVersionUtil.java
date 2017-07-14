package com.wereading.wereadinghacker;

import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by mrsimple on 13/7/17.
 */

public final class ColorOSVersionUtil {

    private ColorOSVersionUtil() {
    }

    public static int getColorOsVersion() {
        int intValue;
        try {
            Class cls = Class.forName("com.color.os.ColorBuild");
            if (cls == null) {
                return 0;
            }
            Method method = cls.getDeclaredMethod("getColorOSVERSION", new Class[0]) ;
            method.setAccessible(true);
            intValue = ((Integer) method.invoke(cls, new Object[0])).intValue();
            return intValue;
        } catch (Exception e) {
            Log.e("ColorOSVersionUtil", "ColorOSVersionUtil failed. error = " + e.getMessage());
            intValue = 0;
        }
        return intValue;
    }
}
