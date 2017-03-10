package com.wereading.wereadinghacker;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

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

    private static final String APP_PACKAGE = "com.tencent.weread";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice mDevice;
    List<String> mBooks = new ArrayList<>() ;

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

        prepareBooks();
    }


    private void prepareBooks() {
//        mBooks.add("深入分析Java Web技术内幕") ;
        mBooks.add("大学·中庸·尚书·周易") ;
        mBooks.add("人性的弱点") ;
        mBooks.add("瓦尔登湖") ;
        mBooks.add("人间词话") ;
        mBooks.add("傲慢与偏见") ;
        mBooks.add("国学知识大全") ;
    }

    @Test
    public void hackReading() throws Exception {
        Thread.sleep(1 * 1000);

        startReading();

        int width = InstrumentationRegistry.getTargetContext().getResources().getDisplayMetrics().widthPixels;
        int height = InstrumentationRegistry.getTargetContext().getResources().getDisplayMetrics().heightPixels;

        int min = 60;
        int max = 100;
        int perPage = max - min;
        // 300次翻页, 每次翻页 60 到 100 秒.
        int maxCount = 300 ;
        boolean isDone = false ;
        // 10书币的阅读时长
        for (int i = 0; i < maxCount; i++) {
            UiObject uiObject = mDevice.findObject(new UiSelector().text("全书完"));
            // 如果已经读完, 则向后翻阅
            if ( uiObject.exists() || isDone ) {
                mDevice.click(60, height - 80);
                isDone = true;
            } else {
                // 未读完, 则向前翻页
                mDevice.click(width - 60, height - 80);
            }
            double randomValue = Math.random() * perPage;
            int waitTIme = (int) randomValue + min;
            // 每次翻页等待的时间, 60 ~ 100 秒, 模拟用户真实的阅读耗时
            Thread.sleep(waitTIme * 1000);
        }
    }


    private void startReading() throws Exception {
        // 点击书架tab
        //        UiObject bookTab = mDevice.findObject(new UiSelector().className(RelativeLayout.class).index(1));
        //        if ( bookTab.exists() ) {
        //            bookTab.click();
        //        } else {
        //            Assert.fail("book tab not found!!");
        //        }

        // 点击书架tab
        UiObject bookTab =  mDevice.findObject(new UiSelector().text("书架"));
        if ( bookTab.exists() ) {
            bookTab.click();
        } else {
            Assert.fail("book tab not found!!");
        }
        Thread.sleep(2 * 1000);

        // 进入书籍, 书籍名字可以自选, 从免费书籍中选择即可.
        UiObject oneBook = mDevice.findObject(new UiSelector().text(mBooks.get(0)));
        if (oneBook != null && oneBook.exists()) {
            oneBook.click();
        } else {
            Assert.fail("book not found!!");
        }
        Thread.sleep(1 * 1000);
    }
}