package com.example.breadtravel_20200408.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.util.DestroyActivity;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout quitLogin;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ImageView quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //全屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //状态栏高亮显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        initView();
    }

    private void initView() {
        quit=findViewById(R.id.quit);
        quitLogin=findViewById(R.id.quitLogin);
        quitLogin.setOnClickListener(this);
        quit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.quitLogin:{
                editor.putString("userName",null);
                editor.commit();
                Intent intent=new Intent(SettingActivity.this,LoginActivity.class);
                startActivity(intent);
                DestroyActivity.addDestroyActivityToMap(this,"settingActivity");
                finish();
                break;
            }
            case R.id.quit:{
                this.finish();
                break;
            }
        }
    }
}
