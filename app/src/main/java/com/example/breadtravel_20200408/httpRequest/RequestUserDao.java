package com.example.breadtravel_20200408.httpRequest;

import android.graphics.Bitmap;

import java.util.Map;

public interface RequestUserDao {
    String Login(Map<String, String> params);
    String CheckEmail(Map<String, String> params);
    String GetCode(Map<String, String> params);
    String Verification(Map<String, String> params);
    String Register(Map<String, String> params);
    String HeadImg(Map<String, String> params);
    String nickName(Map<String,String> params);
}
