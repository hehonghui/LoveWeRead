package com.simple.leakfortest;

import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.internal.DisplayLeakActivity;
import com.squareup.leakcanary.internal.LeakCanaryInternals;

/**
 * see : http://wetest.qq.com/lab/view/175.html
 * Created by mrsimple on 9/3/17.
 */
public final class LeakCanaryForTest {

    public static String sAppPackageName = "";

    public static void install(Application application) {
        Log.e("", "### LeakCanaryForTest install invoked.") ;
        sAppPackageName = application.getPackageName();
        // 设置定制的 LeakDumpService , 将 leak 信息输出到指定的目录
        LeakCanary.refWatcher(application).listenerServiceClass(LeakDumpService.class).excludedRefs(AndroidExcludedRefs.createAppDefaults().build())
                .buildAndInstall();
        // disable DisplayLeakActivity
        LeakCanaryInternals.setEnabled(application, DisplayLeakActivity.class, false);
    }
}
