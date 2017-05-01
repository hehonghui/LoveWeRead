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
public class WeReadingHackmTest {

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
//        mBooks.add("旧制度与大革命");
//        mBooks.add("比利战争");
//        mBooks.add("极客与团队");
//        mBooks.add("西游日记");
        mBooks.add("大学·中庸·尚书·周易") ;
        mBooks.add("人性的弱点") ;
        mBooks.add("瓦尔登湖") ;
        mBooks.add("人间词话") ;
        mBooks.add("傲慢与偏见") ;
        mBooks.add("国学知识大全") ;
    }

    @Test
    public void hackReading() throws Exception {
        Thread.sleep(2 * 1000);
        int width = InstrumentationRegistry.getTargetContext().getResources().getDisplayMetrics().widthPixels;
        int height = InstrumentationRegistry.getTargetContext().getResources().getDisplayMetrics().heightPixels;
        int i = 0;
        boolean isDone = false ;

        int min = 10;
        int max = 20;
        int perPage = max - min;
        // 300次翻页, 每次翻页 60 到 100 秒.
        // 5h = 5*60*60 = 18000
        int maxCount = 18000/min ;

        // 图书列表循环
        while (i < maxCount ){
            startReading();
            // 翻页循环
            while (i < maxCount & isDone == false){

                // 如果已经读完, 为isDone置位
                if ( isTheEnd()) {
                    isDone = true;
                } else {
                    double randomValue = Math.random() * perPage;
                    int waitTIme = (int) randomValue + min;
                    // 每次翻页等待的时间, 模拟用户真实的阅读耗时
                    Thread.sleep(waitTIme * 1000);
                    // 未读完, 则向前翻页,翻页计数
                    mDevice.click(width - 60, height - 180);
                    ++i;
                }

            }
            // 退回主界面 移除读完的图书 并为isDone置位
            mDevice.pressBack();
            mBooks.remove(0);
            isDone = false;
        }
        // 完成后锁屏
        mDevice.sleep();

    }
    private boolean isTheEnd(){
        ArrayList<String> mEndFlag = new ArrayList<>();
        mEndFlag.add("点评此书");
        mEndFlag.add("未完待续");
        mEndFlag.add("购买本书");
        mEndFlag.add("讲解本书");
        mEndFlag.add("可能感兴趣的书");
        for (String str :
                mEndFlag) {
            if (mDevice.findObject(new UiSelector().text(str)).exists())
                return true;
        }
        return false;
    }

    private void startReading() throws Exception {
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