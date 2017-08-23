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
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

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
    private static final String TARGET_PKG = "com.newsdog";

    private static final int LAUNCH_TIMEOUT = 3000;
    private UiDevice mDevice;

    RequestQueue mRequestQueue;
    private boolean isReport = false ;

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

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(APP_PACKAGE);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
        mRequestQueue = Volley.newRequestQueue(InstrumentationRegistry.getContext()) ;
    }

    private void checkNoCrashDialog() {
        try {
            UiObject installFailedObj = mDevice.findObject(new UiSelector().textContains("Installation failed because"));
            if ( isValidObject(installFailedObj) && !isReport && !isOccurError() ) {
                isReport = true ;
                try {
                    hackOppoHaveError();
//                    // 记录数据
//                    writeFile();
                    InstrumentationRegistry.getTargetContext().getSharedPreferences("config", Context.MODE_PRIVATE).edit().putBoolean("fucked", true).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Thread.sleep(2000);
                    Assert.fail("安装失败, 请管理员清空设备 !!!");
                }
//                Assert.fail("安装失败, 请管理员清空设备 !!!");
                return;
            }
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

//    private void writeFile() {
//        File cacheDir = InstrumentationRegistry.getContext().getExternalCacheDir() ;
//        if ( !cacheDir.exists() ) {
//            cacheDir.mkdir() ;
//        }
//        File markFile = new File(cacheDir, "fucked.txt") ;
//        if ( !markFile.exists() ) {
//            try {
//                markFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        Log.e("", "## dir : " + cacheDir.getAbsolutePath()) ;
//    }
//
    private void hackOppoHaveError() {
        try {
            // "+86-18514255092"
            JSONObject params = new JSONObject("{\"msgtype\":\"text\",\"text\":{\"content\":\"Oppo 应用限制 NewsDog 安装, 请注意 ~ \"}," +
                    "\"at\":{\"atMobiles\":[],\"isAtAll\":false}}") ;
            JsonObjectRequest request = new JsonObjectRequest("https://oapi.dingtalk.com/robot/send?access_token=c3bae1abfe414c35c0237daecdb895883d36cef232bfeebac4f00711b840abad", params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.e("", "### hackOppoHaveError request done !") ;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("", "### hackOppoHaveError request onErrorResponse !") ;
                }
            });
            mRequestQueue.add(request) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean isOccurError() {
        return InstrumentationRegistry.getTargetContext().getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("fucked", false);
    }

    @Test
    public void startAppStore() throws Exception {
//        if ( InstrumentationRegistry.getTargetContext().getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("fucked", false) ) {
//            Assert.fail("Oppo商店限制NewsDog安装,请管理员进行处理.");
//            return;
//        }

        UiObject appTabView = mDevice.findObject(new UiSelector().resourceId("com.oppo.market:id/title").text("Apps"));
        // 等待找到搜索框
        int count = 0 ;
        while ( !isValidObject(appTabView) ) {
            Thread.sleep(500);
            appTabView = mDevice.findObject(new UiSelector().resourceId("com.oppo.market:id/title").text("Apps"));
            if ( count++ >= 10 ) {
                break;
            }
        }
        // 进入搜索页面
        if (isValidObject(appTabView)) {
            appTabView.click();
            Thread.sleep(500);
            checkNoCrashDialog();
            sendRequest();

            UiObject categoriesTab = mDevice.findObject(new UiSelector().text("Categories"));
            if ( isValidObject(categoriesTab)) {
                categoriesTab.click();
            } else {
                Assert.fail("没有找到 Categories tab ");
            }

            int counter = 0 ;
            // 找到一个分类, 即加载成功
            UiObject categoryTitle = mDevice.findObject(new UiSelector().text("Social Apps"));
            while ( !isValidObject(categoryTitle) ) {
                Thread.sleep(100);
                categoryTitle = mDevice.findObject(new UiSelector().text("Social Apps"));
                if (counter++ > 150 ) {
                    break;
                }
            }

            if (!isValidObject(categoryTitle)) {
                Assert.fail("没有 加载到 Categories 数据");
            }

//            counter = 0 ;
            mDevice.swipe(200, 680, 200, 180, 10) ;
            Thread.sleep(500);
            UiObject newsCategoryTab = mDevice.findObject(new UiSelector().text("News & Magazines"));
//            while (!isValidObject(newsCategoryTab)) {
//                newsCategoryTab = mDevice.findObject(new UiSelector().text("News & Magazines"));
//                if ( isValidObject(newsCategoryTab)) {
//                    break;
//                }
//                Thread.sleep(400);
//                if (counter++ > 10 ) {
//                    break;
//                }
//            }
            if (!isValidObject(newsCategoryTab)) {
                Assert.fail("没有加载到新闻分类");
            }
//            // 点击新闻分类
//            newsCategoryTab.click();
//            Thread.sleep(1000);
//
//            UiObject newsDogLiteObj;
//            int times = 0;
//            int factor;
//            while (times++ < 150) {
//                factor = 1 ;
//                newsDogLiteObj = mDevice.findObject(new UiSelector().text("NewsDog").className("android.widget.TextView"));
//                if (isValidObject(newsDogLiteObj)) {
//                    newsDogLiteObj.click();
//                    break;
//                }
//                Thread.sleep(factor * 100);
//            }
//            Thread.sleep(1500);
//            UiObject installAction = mDevice.findObject(new UiSelector().resourceId("com.oppo.market:id/button_download"));
//            if (isValidObject(installAction)) {
//                installAction.click();
//                // 等待下载完成
//                waitingForDownload();
//            } else {
//                Assert.fail("没有找到下载按钮");
//            }
        } else {
            Assert.fail("下载失败!!!");
        }
    }



    private void sendRequest() {
        if (!TARGET_PKG.equals("com.newsdog")) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                Log.e("" , "### send af request") ;
                StringRequest request = new StringRequest("https://app.appsflyer.com/com.newsdog?pid=oppo&c=000&af_r=http://www.baidu.com", new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("", "### request af resp : " + response) ;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("", "### request af error : " + volleyError) ;
                    }
                }) ;
                request.setShouldCache(false) ;
                mRequestQueue.add(request) ;
            }
        }.start();
    }


    private static boolean isPkgInstalled(Context context, String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        return packageInfo != null;
    }

    private void waitingForDownload() throws Exception {
        int times = 0;
        // oppo 逻辑
        while (times++ <= 120) {
            // 自动安装
            if (isPkgInstalled(InstrumentationRegistry.getTargetContext(), TARGET_PKG) ) {
                break;
            }
            checkNoCrashDialog();
            Thread.sleep(1 * 1000);
        }
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