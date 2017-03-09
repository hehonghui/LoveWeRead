package com.wereading.wereadinghacker;

import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

import java.lang.reflect.Method;

/**
 * Created by mrsimple on 9/3/17.
 */

public class TestApplication extends Application {
    private static boolean isTestMode = true;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        setupLeakCanary();
    }

    /**
     * see : http://wetest.qq.com/lab/view/175.html
     */
    private void setupLeakCanary() {
        if (isTestMode) {
            try {
                Log.e("", "### setupLeakCanary") ;
                Class canaryClz = Class.forName("com.simple.leakfortest.LeakCanaryForTest") ;
                Method method = canaryClz.getDeclaredMethod("install", Application.class) ;
                method.setAccessible(true);
                method.invoke(null, this) ;
                Log.e("", "### install invoked.") ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
