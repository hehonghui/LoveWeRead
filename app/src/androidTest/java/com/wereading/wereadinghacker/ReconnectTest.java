package com.wereading.wereadinghacker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

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
public class ReconnectTest {

    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Start from the home screen
        mDevice.pressHome();
    }


    @Test
    public void reconnectWifi() throws Exception {
        mDevice.openNotification();
        Thread.sleep(1000);
        UiObject wifiSwitch = mDevice.findObject(new UiSelector().resourceId("com.android.systemui:id/statebutton0").index(0)) ;
        if ( wifiSwitch != null && wifiSwitch.exists() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wifiSwitch.click() ;
            Thread.sleep(500);
            wifiSwitch.click() ;
            Thread.sleep(1000);
            while ( !isConnectedWifi(InstrumentationRegistry.getContext())) {
                Thread.sleep(500);
            }
            mDevice.pressBack();
            Log.e("", "### reconnect ==> " + isConnectedWifi(InstrumentationRegistry.getContext())) ;
        } else {
            mDevice.pressBack();
            Assert.fail("没有找到wifi控件 !!!");
        }
    }


    private static boolean isConnectedWifi(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.getType() ==
                ConnectivityManager.TYPE_WIFI;
    }
}