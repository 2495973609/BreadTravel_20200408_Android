package com.example.breadtravel_20200408.httpRequest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class RequestUserDaoImpl implements RequestUserDao {
    protected static final String HOST = "http://10.70.48.215:8080/BreadTravel_20200408_war_exploded/";

    @Override
    public String Login(Map<String, String> params) {
        String host = HOST + "UserServ?act=login";
        String result = HttpGet.get(host, params);
        return result;
    }

    @Override
    public String CheckEmail(Map<String, String> params) {
        String host = HOST + "UserServ?act=checkEmail";
        String result = HttpGet.get(host, params);
        return result;
    }

    @Override
    public String GetCode(Map<String, String> params) {
        String host = HOST + "UserServ?act=getCode";
        String result = HttpGet.get(host, params);
        return result;
    }

    @Override
    public String Verification(Map<String, String> params) {
        String host = HOST + "UserServ?act=verification";
        String result = HttpGet.get(host, params);
        return result;
    }

    @Override
    public String Register(Map<String, String> params) {
        String host = HOST + "UserServ?act=register";
        String result = HttpGet.get(host, params);
        return result;
    }

    @Override
    public String HeadImg(Map<String, String> params) {
        String host = HOST + "UserServ?act=getHeadImg";
        String result = HttpGet.get(host, params);
        return result;
    }

    @Override
    public String nickName(Map<String, String> params) {
        String host = HOST + "UserServ?act=getNickName";
        String result = HttpGet.get(host, params);
        return result;
    }


}
