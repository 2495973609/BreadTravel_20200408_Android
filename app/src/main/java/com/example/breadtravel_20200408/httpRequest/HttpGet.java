package com.example.breadtravel_20200408.httpRequest;

import android.app.ProgressDialog;

import com.example.breadtravel_20200408.util.BaseApplication;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpGet {

    public static final String Method = "POST";
    private static String result;
    public static String get(final String host, final Map<String, String> params)  {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                String sendUrl = getUrlWithQueryString(host, params);
                URL url = null;
                try {
                    url = new URL(sendUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod(Method);
                    urlConnection.setReadTimeout(8000);//设置读取超时的毫秒数
                    urlConnection.setConnectTimeout(8000);//设置连接超时的毫秒数
                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream in = urlConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder builder = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                        result = builder.toString();
                        close(reader); // 关闭数据流
                        close(in); // 关闭数据流
                        urlConnection.disconnect(); // 断开连接
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    result="N";
                }
            }
        });
       thread.start();
       try{
           thread.join();
       }catch (Exception e){
           e.printStackTrace();
       }
       return result;
    }

    public static String getUrlWithQueryString(String url, Map<String, String> params) {
        if (params == null) {
            return url;
        }

        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        int i = 0;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (value == null) { // 过滤空的key
                continue;
            }

            if (i != 0) {
                builder.append('&');
            }

            builder.append(key);
            builder.append('=');
            builder.append(encode(value));
            i++;
        }

        return builder.toString();
    }

    public static String encode(String input) {
        if (input == null) {
            return "";
        }
        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return input;
    }

    protected static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
