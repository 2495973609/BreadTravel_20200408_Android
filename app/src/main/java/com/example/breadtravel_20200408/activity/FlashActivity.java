package com.example.breadtravel_20200408.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.util.DestroyActivity;

public class FlashActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        //全屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //状态栏高亮显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPreferences.getString("userName",null)!=null){
                    Intent intent=new Intent(FlashActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(FlashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                DestroyActivity.addDestroyActivityToMap(FlashActivity.this,"flashActivity");
            }
        },1000);
    }
}
