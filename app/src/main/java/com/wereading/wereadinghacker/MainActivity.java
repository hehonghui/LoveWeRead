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

//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//
//        mTextView1 = new TextView(this) ;
//        mTextView1.setText("Hello");
//
//        // 添加到视图层级中
//        ((ViewGroup)getWindow().getDecorView()).addView(mTextView1);
//
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                new Thread() {
//                    @Override
//                    public void run() {
//                        mTextView2 = new TextView(MainActivity.this) ;
//                        mTextView2.setText("This is text view 2");
//
//                        mTextView1.setText("Changed");
//                    }
//                }.start();
//            }
//        }, 1000);
//    }
//
//    TextView mTextView1 ;
//    TextView mTextView2 ;

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

}
