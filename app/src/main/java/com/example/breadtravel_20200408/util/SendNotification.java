package com.example.breadtravel_20200408.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import com.example.breadtravel_20200408.R;

import java.io.File;

public class SendNotification {
    public void setNotification(int progress, File file){
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(BaseApplication.getmContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(BaseApplication.getmContext().getPackageName(), R.layout.notification);
        remoteViews.setImageViewBitmap(R.id.bg, BitmapFactory.decodeFile(file.getPath()));
        remoteViews.setTextViewText(R.id.content,"当前进度："+progress+"%");
        remoteViews.setProgressBar(R.id.pb, 100, progress, false);
        NotificationUtils notificationUtils = new NotificationUtils(BaseApplication.getmContext());
        NotificationManager manager = notificationUtils.getManager();
        Notification notification = notificationUtils.setContentIntent(pendingIntent)
                .setContent(remoteViews)
                .setFlags(Notification.FLAG_AUTO_CANCEL)
                .setOnlyAlertOnce(true)
                .getNotification("作品上传", "上传中", R.drawable.notification_icon);
        //下载成功或者失败
//        if (progress == 100 || progress == -1) {
//            manager.notify(1, notification);
//        } else {
//            manager.notify(1, notification);
//        }
        manager.notify(1,notification);

    }
}
