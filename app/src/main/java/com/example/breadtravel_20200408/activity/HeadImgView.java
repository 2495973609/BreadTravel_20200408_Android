package com.example.breadtravel_20200408.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.breadtravel_20200408.R;

import com.example.breadtravel_20200408.util.GlideLoadImg;


public class HeadImgView extends AppCompatActivity {

    private ImageView headImg;
    private LinearLayout quit;
    private SharedPreferences sharedPreferences;
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_img_view);
        //全屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        userName = sharedPreferences.getString("userName", null);
        initView();
    }

    private void initView() {
        headImg=findViewById(R.id.headImg);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GlideLoadImg(userName,headImg);
            }
        });
        quit=findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
