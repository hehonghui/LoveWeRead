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
import android.widget.ImageView;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

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

        // Start from the home screen
        mDevice.pressHome();

        checkNoCrashDialog();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        checkRemoveAd();

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
        try {
            UiObject okObj = mDevice.findObject(new UiSelector().resourceId("android:id/button1").text("OK"));
            while (isValidObject(okObj)) {
                okObj = mDevice.findObject(new UiSelector().resourceId("android:id/button1").text("OK"));
                if ( isValidObject(okObj)) {
                    okObj.click();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkRemoveAd()  {
        UiObject okObj = mDevice.findObject(new UiSelector().text("推广"));
        try {
            // 是否有广告
            if (isValidObject(okObj)) {
                UiObject closeObj = mDevice.findObject(new UiSelector().className(ImageView.class).index(1));
                // 关闭广告
                if ( isValidObject(closeObj)) {
                    closeObj.click();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void startAppStore() throws Exception {
//        if (isPkgInstalled(InstrumentationRegistry.getTargetContext(), TARGET_PKG)) {
//            // 先确保删除老的包
//            mDevice.executeShellCommand("pm uninstall " + TARGET_PKG);
//        }
        Thread.sleep(2000);

        checkNoCrashDialog();

        UiObject searchTv = mDevice.findObject(new UiSelector().resourceId("com.oppo.market:id/ll_bg"));
        // 等待找到搜索框
        if ( !isValidObject(searchTv)) {
            Thread.sleep(3000);
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
            int delay ;
            while (times++ < 10) {
                delay = 0 ;
                newsDogLiteObj = mDevice.findObject(new UiSelector().text("NewsDog - Daily News"));
                if (isValidObject(newsDogLiteObj)) {
                    newsDogLiteObj.click();
                    break;
                } else if ( times % 3 == 0 && isValidObject(goSearch) ){ // 没有搜索成功, 则重新搜索
                    // click to search
                    goSearch.click();
                    delay = 1500 ;
                }
                Thread.sleep(1500 + delay);
            }
            Thread.sleep(1500);
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
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    private void waitingForDownload() throws Exception {
        int times = 0;

        // 模拟器测试逻辑
//        while (times++ <= 12) {
//            // 外部安装
//            isInstalledFromOutside();
//            // 自动安装
//            if (isPkgInstalled(InstrumentationRegistry.getTargetContext(), TARGET_PKG) ) {
//                break;
//            }
//            Thread.sleep(10 * 1000);
//        }
//        openNewsDog("com.android.packageinstaller:id/launch_button");

        // oppo 逻辑
        while (times++ <= 20) {
            // 自动安装
            if (isPkgInstalled(InstrumentationRegistry.getTargetContext(), TARGET_PKG) ) {
                break;
            }
            Thread.sleep(5 * 1000);
        }

        checkNoCrashDialog();

        Thread.sleep(5 * 1000);
        // open newsdog
        openNewsDog("com.oppo.market:id/button_download");

        try {
            allowPermissionsForCoolpadF1();

            // todo : coolpad 大神的包中执行不了卸载功能, 因此转移到shell 中.
            // 打开过NewsDog之后再卸载, 然后重复这个过程
//            mDevice.executeShellCommand("pm uninstall " + TARGET_PKG);
//
//            // force stop market app
//            mDevice.executeShellCommand("am force-stop com.oppo.market");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void openNewsDog(String openUiObjName) throws UiObjectNotFoundException {
        UiObject openObject = mDevice.findObject(new UiSelector().resourceId(openUiObjName));
        if ( isValidObject(openObject) ) {
            openObject.click();
        }
    }

    /**
     * 可以分出两套代码执行.
     * 1. 自动安装.
     * 2. 外部安装.
     *
     * @throws UiObjectNotFoundException
     */
    private void isInstalledFromOutside() throws UiObjectNotFoundException {
        try {
            UiObject marketInstallObject = mDevice.findObject(new UiSelector().resourceId("com.oppo.market:id/button_download"));
            // 如果下载完成会跳转到外部安装程序, 此时点击外部安装程序的"Next"按钮进行安装; 否则再点击一下 下载按钮,继续下载.
            if ( isValidObject(marketInstallObject) ) {
                marketInstallObject.click();
                Thread.sleep(100);
                // 查看是否安装完成
                UiObject outsideInstallObject = mDevice.findObject(new UiSelector().resourceId("com.android.packageinstaller:id/ok_button").text("Next"));
                if ( !isValidObject(outsideInstallObject)) {
                    // 没有下载完成, 再次点击继续下载. 否则跳出 if 条件 ,继续往下执行, 点击"Next"进行安装
                    marketInstallObject.click();
                    return;
                }
            }

            // 外部安装程序
            UiObject outsideInstallObject = mDevice.findObject(new UiSelector().resourceId("com.android.packageinstaller:id/ok_button").text("Next"));
            // 外部安装程序
            if ( isValidObject(outsideInstallObject) ) {
                outsideInstallObject.click();
                Thread.sleep(1000);
                // install
                UiObject installObject = mDevice.findObject(new UiSelector().resourceId("com.android.packageinstaller:id/ok_button").text("Install"));
                if ( isValidObject(installObject) ) {
                    installObject.click();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 允许权限
     * @throws UiObjectNotFoundException
     */
    private void allowPermissionsForCoolpadF1() throws UiObjectNotFoundException {
        try {
//            UiObject allowObject = mDevice.findObject(new UiSelector().resourceId("android:id/button1").text("Allow"));
//            if ( isValidObject(allowObject) ) {
//                allowObject.click();
//                Thread.sleep(1000);
//            }
//
//            UiObject allowObject2 = mDevice.findObject(new UiSelector().resourceId("android:id/button1").text("Allow"));
//            if ( isValidObject(allowObject2) ) {
//                allowObject2.click();
//                Thread.sleep(2000);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isValidObject(UiObject object) {
        return object != null && object.exists();
    }
}