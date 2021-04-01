package com.example.breadtravel_20200408.util;

import android.app.Activity;
import android.app.Application;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.httpRequest.RequestUserDaoImpl;

import java.util.HashMap;
import java.util.Map;


public class GlideLoadImg extends AppCompatActivity {
    protected static final String HOST = "http://192.168.43.154:8080/BreadTravel_20200408_war_exploded/";
    private Map<String, String> params = new HashMap<>();
    private String url;
    private String headImg;

    public GlideLoadImg(String userName,ImageView imageView) {
        params.put("userName", userName);
        headImg=new RequestUserDaoImpl().HeadImg(params);
        if (headImg.equals("logo.jpg")){
            url=HOST+"/logo.jpg";
        }else {
            url=HOST+userName+"/"+headImg;
        }
        Glide.with(BaseApplication.getmContext()).load(url).apply(new RequestOptions().placeholder(R.drawable.logo)).into(imageView);
    }
}
