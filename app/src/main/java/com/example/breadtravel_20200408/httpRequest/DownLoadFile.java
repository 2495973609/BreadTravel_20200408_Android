package com.example.breadtravel_20200408.httpRequest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadFile {
    static final String METHOD = "POST";
    protected static final String HOST = "http://10.70.48.215:8080/BreadTravel_20200408_war_exploded/";
    Bitmap bitmap=null;
    public Bitmap getHeadImgFile(final String path){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sendUrl = HOST + path;
                    URL url = new URL(sendUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(METHOD);
                    connection.setReadTimeout(10000);
                    connection.setReadTimeout(10000);
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream=connection.getInputStream();
                        bitmap= BitmapFactory.decodeStream(inputStream);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
