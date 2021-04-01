package com.example.breadtravel_20200408.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.fragment.CityFragment;
import com.example.breadtravel_20200408.adapter.FragmentAdapter;
import com.example.breadtravel_20200408.fragment.IndexFragment;
import com.example.breadtravel_20200408.fragment.MessageFragment;
import com.example.breadtravel_20200408.fragment.MyFragment;
import com.example.breadtravel_20200408.util.DestroyActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout index_btn,city_btn,add_btn,message_btn,my_btn;
    private ImageView index_img,city_img,add_img,message_img,my_img;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private FragmentAdapter fragmentAdapter;
    private SharedPreferences sharedPreferences;
    private String userName;
    private long exitTime = 0;
    private IndexFragment indexFragment;
    private CityFragment cityFragment;
    private MessageFragment messageFragment;
    private MyFragment myFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //全屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //状态栏高亮显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
        userName=sharedPreferences.getString("userName",null);
        DestroyActivity.addDestroyActivityToMap(this,"mainActivity");
        DestroyActivity.destroyActivity("flashActivity");
        initView();
        initDate();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 按下的如果是BACK，同时没有重复.
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次返回桌面", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
//                finish();
            }
        }
        return true;
    }

    private void initView() {
        index_btn=findViewById(R.id.index_btn);
        city_btn=findViewById(R.id.city_btn);
        add_btn=findViewById(R.id.add_btn);
        message_btn=findViewById(R.id.message_btn);
        my_btn=findViewById(R.id.my_btn);
        index_img=findViewById(R.id.index_img);
        city_img=findViewById(R.id.city_img);
        add_img=findViewById(R.id.add_img);
        message_img=findViewById(R.id.message_img);
        my_img=findViewById(R.id.my_img);
        index_btn.setOnClickListener(this);
        city_btn.setOnClickListener(this);
        add_btn.setOnClickListener(this);
        message_btn.setOnClickListener(this);
        my_btn.setOnClickListener(this);
        viewPager=findViewById(R.id.viewPage);
        indexFragment=new IndexFragment(userName,this);
        cityFragment=new CityFragment();
        messageFragment=new MessageFragment();
        myFragment=new MyFragment(this);
        fragments=new ArrayList<>();
        fragments.add(indexFragment);
        fragments.add(cityFragment);
        fragments.add(messageFragment);
        fragments.add(myFragment);
        fragmentAdapter=new FragmentAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:{
                        index_img.setImageDrawable(getResources().getDrawable(R.drawable.index_select));
                        city_img.setImageDrawable(getResources().getDrawable(R.drawable.map_not_select));
                        message_img.setImageDrawable(getResources().getDrawable(R.drawable.message_not_select));
                        my_img.setImageDrawable(getResources().getDrawable(R.drawable.my_not_select));
                        break;
                    }
                    case 1:{
                        index_img.setImageDrawable(getResources().getDrawable(R.drawable.index_not_select));
                        city_img.setImageDrawable(getResources().getDrawable(R.drawable.map_select));
                        message_img.setImageDrawable(getResources().getDrawable(R.drawable.message_not_select));
                        my_img.setImageDrawable(getResources().getDrawable(R.drawable.my_not_select));
                        break;
                    }
                    case 2:{
                        index_img.setImageDrawable(getResources().getDrawable(R.drawable.index_not_select));
                        city_img.setImageDrawable(getResources().getDrawable(R.drawable.map_not_select));
                        message_img.setImageDrawable(getResources().getDrawable(R.drawable.message_select));
                        my_img.setImageDrawable(getResources().getDrawable(R.drawable.my_not_select));
                        break;
                    }
                    case 3:{
                        index_img.setImageDrawable(getResources().getDrawable(R.drawable.index_not_select));
                        city_img.setImageDrawable(getResources().getDrawable(R.drawable.map_not_select));
                        message_img.setImageDrawable(getResources().getDrawable(R.drawable.message_not_select));
                        my_img.setImageDrawable(getResources().getDrawable(R.drawable.my_select));
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initDate() {

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.index_btn:{
                viewPager.setCurrentItem(0);
                break;
            }
            case R.id.city_btn:{
                viewPager.setCurrentItem(1);
                break;
            }
            case R.id.message_btn:{
                viewPager.setCurrentItem(2);
                break;
            }
            case R.id.my_btn:{
                viewPager.setCurrentItem(3);
                break;
            }
            case R.id.add_btn:{
                Intent intent=new Intent(MainActivity.this,ReleaseWorksActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

}
