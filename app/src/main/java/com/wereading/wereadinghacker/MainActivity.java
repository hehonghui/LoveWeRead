package com.wereading.wereadinghacker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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
