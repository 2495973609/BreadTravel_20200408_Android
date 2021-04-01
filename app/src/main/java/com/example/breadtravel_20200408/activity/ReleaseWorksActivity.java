package com.example.breadtravel_20200408.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.breadtravel_20200408.BuildConfig;
import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.adapter.ContentAdapter;
import com.example.breadtravel_20200408.entity.WorksContent;
import com.example.breadtravel_20200408.entity.WorksPreview;
import com.example.breadtravel_20200408.fragment.MyFragment;
import com.example.breadtravel_20200408.httpRequest.RequestWorksDaoImpl;
import com.example.breadtravel_20200408.httpRequest.UploadFile;
import com.example.breadtravel_20200408.util.FileUtil;
import com.example.breadtravel_20200408.util.GlideLoadImg;
import com.example.breadtravel_20200408.util.MyLocation;
import com.example.breadtravel_20200408.util.NotificationUtils;
import com.example.breadtravel_20200408.util.SPUtil;
import com.google.gson.Gson;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.breadtravel_20200408.util.FileUtil.getRealFilePathFromUri;

public class ReleaseWorksActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView myBackGround, quit, headImg;
    private TextView title, date;
    private LinearLayout text, camera, video, img, worksContents, sendPost;
    private String userName;
    private MapView mMapView = null;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private SharedPreferences sharedPreferences;
    private ListView contents_item;

    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //更换背景
    private static final int CHANGE_BACKGROUND_CAMERA = 105;
    private static final int CHANGE_BACKGROUND_PHOTO = 106;
    private File tempFile;
    private String location;
    private AMap aMap = null;
    private AMapLocationClient mapLocationClient = null;
    private WorksPreview worksPreview = new WorksPreview();
    private List<WorksContent> worksContentList = new ArrayList<>();
    private ContentAdapter contentAdapter;
    private NotificationManager notificationManager;
    private Notification.Builder builder;


    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                try {
                    LatLng latLng=new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                    location = aMapLocation.getCountry() + "," + aMapLocation.getCity()+" "+latLng.toString();
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_works);
        //全屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.showIndoorMap(true);//显示室内地图
            mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
            mUiSettings.setZoomControlsEnabled(false);
            mUiSettings.setScaleControlsEnabled(true);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);//定位图标
        }
        mapLocationClient = new AMapLocationClient(this);
        mapLocationClient.setLocationListener(mLocationListener);
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        userName = sharedPreferences.getString("userName", null);
        initView();
    }


    private void initView() {
        myBackGround = findViewById(R.id.myBackGround);
        quit = findViewById(R.id.quit);
        headImg = findViewById(R.id.headImg);
        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        text = findViewById(R.id.text);
        camera = findViewById(R.id.camera);
        video = findViewById(R.id.video);
        img = findViewById(R.id.img);
        sendPost = findViewById(R.id.sendPost);
        contents_item = findViewById(R.id.contents_item);
//        worksContents = findViewById(R.id.worksContents);
        quit.setOnClickListener(this);
        text.setOnClickListener(this);
        camera.setOnClickListener(this);
        video.setOnClickListener(this);
        img.setOnClickListener(this);
        sendPost.setOnClickListener(this);
        myBackGround.setOnCreateContextMenuListener(this);
        contents_item.setOnCreateContextMenuListener(this);
    }

    //设置长按弹出菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.myBackGround: {
                menu.add(0, 1, 0, "拍一张");
                menu.add(0, 2, 0, "从相册选择");
                menu.add(0, 3, 0, "取消");
                break;
            }
            case R.id.contents_item: {
                menu.add(0, 4, 0, "删除");
                menu.add(0, 5, 0, "取消");
                break;
            }
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case 1: {
                //权限判断
                if (ContextCompat.checkSelfPermission(ReleaseWorksActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(ReleaseWorksActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统相机
                    gotoCamera(2);
                }
                break;
            }
            case 2: {
                //权限判断
                if (ContextCompat.checkSelfPermission(ReleaseWorksActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(ReleaseWorksActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统相机
                    gotoPhoto(2);
                }
                break;
            }
            case 4: {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int i = (int) info.id;
                worksContentList.remove(i);
                contentAdapter.notifyDataSetChanged();
                new SPUtil().setDataList(this, "worksContent", worksContentList, "SP_WorksContent");
                break;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        worksPreview = new SPUtil().getObject(this, "worksPreview", "SP_WorksPreview");
        worksContentList = new SPUtil().getDataList(this, "worksContent", "SP_WorksContent");
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        Date dt = new Date(System.currentTimeMillis());
        date.setText(df.format(dt));
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GlideLoadImg(userName, headImg);
                new MyLocation().getMyLocation(aMap, mapLocationClient);
                if (worksPreview != null) {
                    Glide.with(ReleaseWorksActivity.this).load(worksPreview.getCoverImg()).into(myBackGround);
                    title.setText(worksPreview.getTitle());
                }
                contentAdapter = new ContentAdapter(worksContentList);
                contents_item.setAdapter(contentAdapter);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (worksPreview.getCoverImg() != null || worksContentList.size() != 0) {
                askSave();
            } else {
                finish();
            }
        }
        return true;
    }

    public void askSave() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView save = (TextView) view.findViewById(R.id.save);
        TextView noSave = (TextView) view.findViewById(R.id.noSave);
        TextView exit = (TextView) view.findViewById(R.id.exit);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_release_works, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveResources();
                new SPUtil().setObject(ReleaseWorksActivity.this, "worksPreview", worksPreview, "SP_WorksPreview");
                popupWindow.dismiss();
                ReleaseWorksActivity.this.finish();
            }
        });
        noSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SPUtil().clearSP(ReleaseWorksActivity.this, "SP_WorksPreview");
                new SPUtil().clearSP(ReleaseWorksActivity.this, "SP_WorksContent");
                popupWindow.dismiss();
                ReleaseWorksActivity.this.finish();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }


    @Override
    public void onClick(View view1) {
        switch (view1.getId()) {
            case R.id.quit: {
                if (worksPreview.getCoverImg() != null || worksContentList.size() != 0) {
                    askSave();
                } else {
                    finish();
                }
                break;
            }
            case R.id.sendPost: {
                if (worksPreview.getCoverImg() == null) {
                    Toast.makeText(this, "发布内容不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                saveResources();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            upLoad();
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                }).start();

                new SPUtil().clearSP(this, "SP_WorksPreview");
                new SPUtil().clearSP(this, "SP_WorksContent");
                finish();
                break;
            }
            case R.id.text: {
                Intent intent = new Intent();
                intent.setClass(ReleaseWorksActivity.this, ProcessActivity.class);
                intent.setData(null);
                startActivityForResult(intent, REQUEST_CROP_PHOTO);
                break;
            }
            case R.id.camera: {
                //权限判断
                if (ContextCompat.checkSelfPermission(ReleaseWorksActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(ReleaseWorksActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统相机
                    gotoCamera(1);
                }
                break;
            }
//            case R.id.video:{
//                //权限判断
//                if (ContextCompat.checkSelfPermission(ReleaseWorksActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    //申请WRITE_EXTERNAL_STORAGE权限
//                    ActivityCompat.requestPermissions(ReleaseWorksActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
//                } else {
//                    //跳转到调用系统相机
//                    gotoVideo();
//                }
//                break;
//            }
            case R.id.img: {
                //权限判断
                if (ContextCompat.checkSelfPermission(ReleaseWorksActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(ReleaseWorksActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到相册
                    gotoPhoto(1);
                }
                break;
            }
        }
    }

    private void setNotification(int progress){
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.content,"当前进度："+progress+"%");
        remoteViews.setProgressBar(R.id.pb, 100, progress, false);
        NotificationUtils notificationUtils = new NotificationUtils(this);
        NotificationManager manager = notificationUtils.getManager();
        Notification notification = notificationUtils.setContentIntent(pendingIntent)
                .setContent(remoteViews)
                .setFlags(Notification.FLAG_AUTO_CANCEL)
                .setOnlyAlertOnce(true)
                .getNotification("作品上传", "上传中", R.drawable.camera);
        //下载成功或者失败
        if (progress == 100 || progress == -1) {
            notificationUtils.clearNotification();
        } else {
            manager.notify(1, notification);
        }

    }

    private void upLoad() {
        Gson gson = new Gson();
        Uri uri1 = Uri.parse(worksPreview.getCoverImg());
        String imgPath1 = getRealFilePathFromUri(getApplicationContext(), uri1);
        File file1 = new File(imgPath1);
        worksPreview.setCoverImg(file1.getName());
        String previewJSON = gson.toJson(worksPreview);
        Map<String, String> previewParams1 = new HashMap<>();
        previewParams1.put("worksPreview", previewJSON);
        String worksId = new RequestWorksDaoImpl().upLoadWorksPreview(previewParams1);
        if (worksId.equals("F")) {
            Toast.makeText(this,"发布失败",Toast.LENGTH_SHORT).show();
            //插入失败
        } else if (worksId.equals("N")) {
            Toast.makeText(this,"无法连接到服务器",Toast.LENGTH_SHORT).show();
            //无法连接服务器
        } else {
            Map<String, String> previewParams2 = new HashMap<>();
            previewParams2.put("userName", userName);
            new UploadFile(file1, previewParams2, "WorksServ?act=upLoadFile");
            for (WorksContent worksContent : worksContentList) {
                Uri uri2 = Uri.parse(worksContent.getPhoto());
                String imgPath2 = getRealFilePathFromUri(getApplicationContext(), uri2);
                File file2 = new File(imgPath2);
                worksContent.setPhoto(file2.getName());
                worksContent.setWorksId(Long.valueOf(worksId));
                String contentJSON = gson.toJson(worksContent);
                Map<String, String> worksContent1 = new HashMap<>();
                worksContent1.put("worksContent", contentJSON);
                new RequestWorksDaoImpl().upLoadWorksContent(worksContent1);
                Map<String, String> worksContent2 = new HashMap<>();
                worksContent2.put("userName", userName);
                new UploadFile(file2, worksContent2, "WorksServ?act=upLoadFile");
            }
        }

    }

    private void saveResources() {
        worksPreview.setUserName(userName);
        worksPreview.setTitle(title.getText().toString());
        if (worksContentList.size() == 0) {
            worksPreview.setDate(date.getText().toString());
            worksPreview.setDay(0);
        } else {
            worksPreview.setDate(worksContentList.get(0).getDate());
            int day = daysBetween(worksContentList.get(0).getDate(), worksContentList.get(worksContentList.size() - 1).getDate());
            worksPreview.setDay(day + 1);
        }
        worksPreview.setRegion(location);
    }

    //计算天数
    public static int daysBetween(String smdate, String bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 跳转到照相机
     */
    private void gotoCamera(int i) {
        Log.d("evan", "*****************打开相机********************");
        //创建拍照存储的图片文件
        tempFile = new File(FileUtil.checkDirPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/"), "IMG_" + System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(ReleaseWorksActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        if (i == 1) {
            startActivityForResult(intent, REQUEST_CAPTURE);
        } else {
            startActivityForResult(intent, CHANGE_BACKGROUND_CAMERA);
        }
    }

    /**
     * 跳转到相册
     */
    private void gotoPhoto(int i) {
        Log.d("evan", "*****************打开图库********************");
        //跳转到调用系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (i == 1) {
            startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
        } else {
            startActivityForResult(Intent.createChooser(intent, "请选择图片"), CHANGE_BACKGROUND_PHOTO);
        }
    }


    /**
     * 跳转到照相机
     */
    private void gotoVideo() {
        Log.d("evan", "*****************打开摄像机********************");
        //创建摄像机存储的视频文件
        tempFile = new File(FileUtil.checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera"), System.currentTimeMillis() + ".mp4");
        //跳转到调用系统摄像机
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(ReleaseWorksActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, REQUEST_CAPTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case REQUEST_CAPTURE: { //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            }
            case REQUEST_PICK: {  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            }
            case CHANGE_BACKGROUND_CAMERA: {
                if (resultCode == RESULT_OK) {
                    File file = new File(Uri.fromFile(tempFile).toString());
                    worksPreview.setCoverImg(file.toString());
                    new SPUtil().setObject(this, "worksPreview", worksPreview, "SP_WorksPreview");
                }
                break;
            }
            case CHANGE_BACKGROUND_PHOTO: {
                if (resultCode == RESULT_OK) {
                    String imgPath = getRealFilePathFromUri(getApplicationContext(), intent.getData());
                    File file = new File(imgPath);
                    worksPreview.setCoverImg(file.toString());
                    new SPUtil().setObject(this, "worksPreview", worksPreview, "SP_WorksPreview");
                }
                break;
            }
            case REQUEST_CROP_PHOTO: {
                if (resultCode == RESULT_OK) {
                    final WorksContent worksContent = (WorksContent) intent.getSerializableExtra("worksContent");
                    worksContentList.add(worksContent);
                    new SPUtil().setDataList(this, "worksContent", worksContentList, "SP_WorksContent");
                }
            }
        }
    }

    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(ReleaseWorksActivity.this, ProcessActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }
}
