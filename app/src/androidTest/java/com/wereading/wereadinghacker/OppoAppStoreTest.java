package com.wereading.wereadinghacker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumentation test, which will execute on an Android device.
 * <p>
 * 选择一本书, 然后翻页300次, 每次翻页间隔 60 到 100 秒.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class OppoAppStoreTest {

    private static final String APP_PACKAGE = "com.oppo.market";
    private static final String TARGET_PKG = "com.newsdog.daily";

    private static final int LAUNCH_TIMEOUT = 3000;
    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

//        // Start from the home screen
//        mDevice.pressHome();
//
//        checkNoCrashDialog();
//
//        // Wait for launcher
//        final String launcherPackage = mDevice.getLauncherPackageName();
//        assertThat(launcherPackage, notNullValue());
//        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(APP_PACKAGE);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    private void checkNoCrashDialog() {
//        try {
//            UiObject okObj = mDevice.findObject(new UiSelector().resourceId("android:id/button1").text("OK"));
//            while (isValidObject(okObj)) {
//                okObj = mDevice.findObject(new UiSelector().resourceId("android:id/button1").text("OK"));
//                if ( isValidObject(okObj)) {
//                    okObj.click();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    @Test
    public void startAppStore() throws Exception {
        Thread.sleep(1500);

        checkNoCrashDialog();

        UiObject searchTv = mDevice.findObject(new UiSelector().resourceId("com.oppo.market:id/ll_bg"));
        // 等待找到搜索框
        if ( !isValidObject(searchTv)) {
            Thread.sleep(2000);
            searchTv = mDevice.findObject(new UiSelector().resourceId("com.oppo.market:id/ll_bg"));
        }
        // 进入搜索页面
        if (isValidObject(searchTv)) {
            searchTv.click();
            Thread.sleep(1000);

            UiObject searchKeywordTv = mDevice.findObject(new UiSelector().resourceId("com.oppo.market:id/et_search"));
            searchKeywordTv.setText("NewsDog");

            UiObject goSearch = mDevice.findObject(new UiSelector().resourceId("com.oppo.market:id/iv_search"));
            if ( isValidObject(goSearch)) {
                // click to search
                goSearch.click();
            }
            Thread.sleep(1000);
            UiObject newsDogLiteObj;
            int times = 0;
            int factor;
            while (times++ < 10) {
                factor = 1 ;
                newsDogLiteObj = mDevice.findObject(new UiSelector().text("NewsDog - Daily News"));
                if (isValidObject(newsDogLiteObj)) {
                    newsDogLiteObj.click();
                    break;
                } else if ( times % 3 == 0 && isValidObject(goSearch) ){ // 没有搜索成功, 则重新搜索
                    // click to search
                    goSearch.click();
                    factor = 3 ;
                }
                Thread.sleep(factor * 1000);
            }
            Thread.sleep(1000);
            UiObject installAction = mDevice.findObject(new UiSelector().resourceId("com.oppo.market:id/button_download"));
            if (isValidObject(installAction)) {
                installAction.click();
                // 等待下载完成
                waitingForDownload();
            } else {
                Assert.fail("没有找到下载按钮");
            }
        } else {
            Assert.fail("没有找到搜索框");
        }
    }


    private static boolean isPkgInstalled(Context context, String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
//            e.printStackTrace();
        }
        return packageInfo != null;
    }

    private void waitingForDownload() throws Exception {
        int times = 0;
        // oppo 逻辑
        while (times++ <= 90) {
            // 自动安装
            if (isPkgInstalled(InstrumentationRegistry.getTargetContext(), TARGET_PKG) ) {
                break;
            }
            Thread.sleep(1 * 1000);
        }

        checkNoCrashDialog();

        Thread.sleep(1000);
        // open newsdog
        openNewsDog("com.oppo.market:id/button_download");
    }


    private void openNewsDog(String openUiObjName) throws UiObjectNotFoundException {
        UiObject openObject = mDevice.findObject(new UiSelector().resourceId(openUiObjName));
        if ( isValidObject(openObject) ) {
            openObject.click();
        }
    }


    private boolean isValidObject(UiObject object) {
        return object != null && object.exists();
    }
}