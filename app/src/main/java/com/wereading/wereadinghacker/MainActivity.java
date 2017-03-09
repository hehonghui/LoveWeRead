package com.wereading.wereadinghacker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.run_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHack();
                Toast.makeText(MainActivity.this, "start hacking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startHack() {
        new Thread() {
            @Override
            public void run() {
                try{
                    Process process = Runtime.getRuntime().exec("am instrument -w -r   -e debug false -e class com.wereading.wereadinghacker.WeReadingHackTest com.wereading.wereadinghacker.test/android.support.test.runner.AndroidJUnitRunner");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
