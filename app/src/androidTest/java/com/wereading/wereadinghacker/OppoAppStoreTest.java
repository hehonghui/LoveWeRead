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

    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(APP_PACKAGE);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }


    @Test
    public void startAppStore() throws Exception {
        if (isPkgInstalled(InstrumentationRegistry.getTargetContext(), TARGET_PKG)) {
            // 先确保删除老的包
            mDevice.executeShellCommand("pm uninstall " + TARGET_PKG);
        }
        Thread.sleep(3 * 1000);

        UiObject searchTv = mDevice.findObject(new UiSelector().resourceId("com.oppo.market:id/ll_bg"));
        if (isValidObject(searchTv)) {
            searchTv.click();

            Thread.sleep(1000);

            UiObject searchKeywordTv = mDevice.findObject(new UiSelector().resourceId("com.oppo.market:id/et_search"));
            searchKeywordTv.setText("NewsDog");

            UiObject goSearch = mDevice.findObject(new UiSelector().resourceId("com.oppo.market:id/iv_search"));
            // click to search
            goSearch.click();

            Thread.sleep(3000);
            UiObject newsDogLiteObj;
            int times = 0;
            while (times++ < 10) {
                newsDogLiteObj = mDevice.findObject(new UiSelector().text("NewsDog - Daily News"));
                if (isValidObject(newsDogLiteObj)) {
                    newsDogLiteObj.click();
                    break;
                }
                Thread.sleep(1500);
            }
            UiObject installAction = mDevice.findObject(new UiSelector().resourceId("com.oppo.market:id/button_download"));
            if (isValidObject(installAction)) {
                installAction.click();
                // 等待下载完成
                waitingForDownload();
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
        while (times++ <= 20) {
            // 外部安装
            isInstalledFromOutside();
            // 自动安装
            if (isPkgInstalled(InstrumentationRegistry.getTargetContext(), TARGET_PKG) ) {
                break;
            }
            Thread.sleep(6 * 1000);
        }

//        UiObject uiObject = mDevice.findObject(new UiSelector().resourceId("com.oppo.market:id/button_download"));
//        if (isValidObject(uiObject)) {
//            // 打开应用
//            uiObject.click();
//        }

        // open newsdog
        openNewsDog("com.android.packageinstaller:id/launch_button");
        openNewsDog("com.oppo.market:id/button_download");

        Thread.sleep(2 * 1000);

        // 打开过NewsDog之后再卸载, 然后重复这个过程
        mDevice.executeShellCommand("pm uninstall " + TARGET_PKG);

        // force stop market app
        mDevice.executeShellCommand("am force-stop com.oppo.market");
    }

    private void openNewsDog(String openUiObjName) throws UiObjectNotFoundException {
        UiObject installObject = mDevice.findObject(new UiSelector().resourceId(openUiObjName));
        if ( isValidObject(installObject) ) {
            installObject.click();
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
                Thread.sleep(200);
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


    private boolean isValidObject(UiObject object) {
        return object != null && object.exists();
    }
}