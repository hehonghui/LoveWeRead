package com.wereading.wereadinghacker;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrsimple on 9/3/17.
 */

public class LeakActivity extends Activity {

    private static List<Activity> activities = new ArrayList<>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak);
        activities.add(this) ;
    }
}
