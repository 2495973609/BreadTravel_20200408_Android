package com.example.breadtravel_20200408.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.example.breadtravel_20200408.entity.WorksContent;
import com.example.breadtravel_20200408.entity.WorksPreview;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

public class SPUtil {


    public void setObject(Context context, String key, Object object, String SP_NAME) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        //创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //创建字节对象输出流
        ObjectOutputStream out = null;
        try {
            //然后通过将字对象进行64转码，写入sp中
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectValue = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, objectValue);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }

                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  <T> WorksPreview getObject(Context context, String key,String SP_NAME) {
        WorksPreview worksPreview=new WorksPreview();
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if (sp.contains(key)) {
            String objectValue = sp.getString(key, null);
            byte[] buffer = Base64.decode(objectValue, Base64.DEFAULT);
            //一样通过读取字节流，创建字节流输入流，写入对象并作强制转换
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                 worksPreview = (WorksPreview) ois.readObject();
                return worksPreview;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }

                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return worksPreview;
    }

    public  <T> void setDataList(Context context, String key, List<WorksContent> dataList, String SP_NAME) {
        if (null == dataList || dataList.size() < 0) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(dataList);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, strJson);
        editor.commit();
    }

    public  <T> List<WorksContent> getDataList(Context context, String key,String SP_NAME) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        List<WorksContent> dataList =new ArrayList();
        String strJson = sp.getString(key, null);
        if (null == strJson) {
            return dataList;
        }

        Gson gson = new Gson();
        //使用泛型解析数据会出错，返回的数据类型是LinkedTreeMap
        dataList = gson.fromJson(strJson, new TypeToken<List<WorksContent>>() {
        }.getType());

        return  dataList;
    }


    public void clearSP(Context context,String SP_NAME){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }


}
