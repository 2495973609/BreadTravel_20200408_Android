package com.example.breadtravel_20200408.util;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DestroyActivity {
    private static Map<String, Activity> destroyMap = new HashMap<>();

    //将Activity添加到队列中
    public static void addDestroyActivityToMap(Activity activity, String activityName) {
        destroyMap.put(activityName, activity);
    }

    //根据名字销毁制定Activity
    public static void destroyActivity(String activityName) {
        Set<String> keySet = destroyMap.keySet();
        if (keySet.size() > 0) {
            for (String key : keySet) {
                if (activityName.equals(key)) {
                    destroyMap.get(key).finish();
                }
            }
        }
    }

}
