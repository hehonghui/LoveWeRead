package com.wereading.wereadinghacker;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    /**
     * 1. 过滤 xposed 安装 以及相关 module 的package
     * 2. 添加其他应用
     */
    private void test() {
        try {
            List<PackageInfo> pkgs = new ArrayList<>();

            Iterator<PackageInfo> iterator = pkgs.iterator() ;
            while ( iterator.hasNext() ) {
                PackageInfo item = iterator.next() ;
                // remove
                if ( item.packageName.contains("xposed") ) {
                    iterator.remove();
                }
            }

            List<String> extra = new ArrayList<>() ;
            extra.add("com.whatsapp") ;
            extra.add("com.facebook.katana") ;

            for (String pkgName : extra) {
                PackageInfo packageInfo = new PackageInfo();
                packageInfo.packageName = pkgName ;
                packageInfo.applicationInfo = new ApplicationInfo() ;
                packageInfo.applicationInfo.packageName = pkgName ;
                // add to
                pkgs.add(packageInfo) ;
            }

//            for (PackageInfo info : pkgs) {
//                XposedBridge.log("### getInstalledPackages item 1 --> " + item.packageName);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            List<ApplicationInfo> pkgs = new ArrayList<ApplicationInfo>();

            Iterator<ApplicationInfo> iterator = pkgs.iterator() ;
            while ( iterator.hasNext() ) {
                ApplicationInfo item = iterator.next() ;

//                XposedBridge.log("### getInstalledPackages item 1 --> " + item.packageName);
                // remove
                if ( item.packageName.contains("xposed") ) {
                    iterator.remove();
                }
            }

            List<String> extra = new ArrayList<String>() ;
            extra.add("com.whatsapp") ;
            extra.add("com.facebook.katana") ;

            for (String pkgName : extra) {
                ApplicationInfo applicationInfo = new ApplicationInfo() ;
                applicationInfo.packageName = pkgName ;
                // add to
                pkgs.add(applicationInfo) ;
            }

            for (ApplicationInfo info : pkgs) {
                //                XposedBridge.log("### getInstalledPackages item  2 --> " + item.packageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
