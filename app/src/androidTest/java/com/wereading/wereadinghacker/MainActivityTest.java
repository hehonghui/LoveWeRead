package com.wereading.wereadinghacker;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.widget.Button;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Semaphore;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * 选择一本书, 然后翻页300次, 每次翻页间隔 60 到 100 秒.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final String APP_PACKAGE = "com.wereading.wereadinghacker";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice mDevice;
    PowerManager.WakeLock mWakeLock;

    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        try {
            unlockScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /**
     * 解锁屏幕
     */
    protected void unlockScreen() {
        /**
         * 解锁屏幕，需要权限：android.permission.DISABLE_KEYGUARD
         */
        KeyguardManager keyguardManager = (KeyguardManager) InstrumentationRegistry.getContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
        keyguardLock.disableKeyguard();
        /**
         * 点亮屏幕,需要权限：android.permission.WAKE_LOCK
         */
        PowerManager powerManager = (PowerManager) InstrumentationRegistry.getContext().getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "");
        mWakeLock.acquire();
    }


    @After
    public void destroy() {
        if ( mWakeLock != null ) {
            mWakeLock.release();
        }
    }


    @Test
    public void testGotoDetailActivity() throws Exception {
        UiObject uiObject = mDevice.findObject(new UiSelector().className(Button.class));
        // 10书币的阅读时长
        for (int i = 0; i < 10; i++) {
            // 如果已经读完, 则向后翻阅
            if ( uiObject.exists() ) {
                uiObject.click() ;
                Thread.sleep(i <= 0 ? 2 : 20 * 1000);
                mDevice.pressBack();
            } else {
                Assert.fail("not found button");
            }
        }
    }
}