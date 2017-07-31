package com.wereading.wereadinghacker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    TextView mInfoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.run_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LeakActivity.class));
            }
        });


        mInfoTv = (TextView) findViewById(R.id.info_tv);

        mInfoTv.append("; Color ROM: " + getSystemProperty("ro.build.version.opporom") + "\n");
        mInfoTv.append("; Color getRomVersion : " + getSystemProperty("ro.build.display.id") + "\n" );
        mInfoTv.append("; Color Os Version: " + ColorOSVersionUtil.getColorOsVersion() + "\n" );

        new Thread() {
            @Override
            public void run() {
                sendRequest();
            }
        }.start();
    }


    private void sendRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext()) ;
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
        requestQueue.add(request) ;
    }

    public static String getSystemProperty(String property) {
        String value = "";
        try {
            Class clz = Class.forName("android.os.SystemProperties") ;
            Method method = clz.getDeclaredMethod("get", String.class) ;
            method.setAccessible(true);
            value = (String) method.invoke(null, property) ;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }


    /**
     *
     *   ( 2187): ### SystemProperties get => gsm.operator.alpha
     I/Xposed  ( 2187): ### SystemProperties get => ro.build.display.id
     I/Xposed  ( 2187): ### SystemProperties get => ro.build.version.opporom
     I/Xposed  ( 2187): ### SystemProperties get => persist.sys.oppo.region
     I/Xposed  ( 2187): ### SystemProperties get => persist.sys.oppo.region
     I/Xposed  ( 2187): ### SystemProperties get => persist.sys.oppo.region
     I/Xposed  ( 2187): ### SystemProperties get => ro.build.display.id
     I/Xposed  ( 2187): ### SystemProperties get => debug.sqlite.journalmode
     I/Xposed  ( 2187): ### SystemProperties get => debug.sqlite.syncmode
     I/Xposed  ( 2187): ### SystemProperties get => debug.second-display.pkg
     I/Xposed  ( 2187): ### SystemProperties get => ro.config.low_ram
     I/Xposed  ( 2187): ### SystemProperties get => ro.config.low_ram
     I/Xposed  ( 2187): ### SystemProperties get => ro.config.low_ram
     I/Xposed  ( 2187): ### SystemProperties get => ro.config.low_ram
     I/Xposed  ( 2187): ### SystemProperties get => ro.config.low_ram
     I/Xposed  ( 2187): ### SystemProperties get => gsm.operator.alpha
     I/Xposed  ( 2187): ### SystemProperties get => ro.build.display.id
     I/Xposed  ( 2187): ### SystemProperties get => ro.build.version.opporom
     I/Xposed  ( 2187): ### SystemProperties get => persist.sys.oppo.region
     I/Xposed  ( 2187): ### SystemProperties get => persist.sys.oppo.region
     I/Xposed  ( 2187): ### SystemProperties get => persist.sys.oppo.region
     I/Xposed  ( 2187): ### SystemProperties get => ro.yunos.version
     I/Xposed  ( 2187): ### SystemProperties get => java.vm.name
     I/Xposed  ( 2187): ### SystemProperties get => debug.hwui.profile
     I/Xposed  ( 2187): ### SystemProperties get => ro.config.low_ram
     I/Xposed  ( 2187): ### SystemProperties get => debug.sqlite.journalmode
     I/Xposed  ( 2187): ### SystemProperties get => persist.sys.oppo.region
     I/Xposed  ( 2187): ### SystemProperties get => debug.sqlite.syncmode
     I/Xposed  ( 2187): ### SystemProperties get => debug.hwui.profile
     I/Xposed  ( 2187): ### SystemProperties get => gsm.operator.alpha
     I/Xposed  ( 2187): ### SystemProperties get => ro.build.display.id
     I/Xposed  ( 2187): ### SystemProperties get => ro.build.version.opporom
     I/Xposed  ( 2187): ### SystemProperties get => persist.sys.oppo.region
     I/Xposed  ( 2187): ### SystemProperties get => persist.sys.oppo.region
     I/Xposed  ( 2187): ### SystemProperties get => persist.sys.oppo.region
     I/Xposed  ( 2187): ### SystemProperties get => persist.sys.oppo.region
     I/Xposed  ( 2187): ### SystemProperties get => ro.config.low_ram
     I/Xposed  ( 2187): ### SystemProperties get => persist.sys.oppo.region
     */
}
