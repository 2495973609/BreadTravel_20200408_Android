package com.example.breadtravel_20200408.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.bumptech.glide.Glide;
import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.entity.WorksContent;
import com.example.breadtravel_20200408.util.MyLocation;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProcessActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView photo, quit, ok;
    private TextView text, location, time;
    private MapView mMapView = null;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private AMap aMap = null;
    private AMapLocationClient mapLocationClient = null;
    private String local;

    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                local = aMapLocation.getDistrict() + "," + aMapLocation.getPoiName();
                location.setText(local);
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
        setContentView(R.layout.activity_process);
        //全屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //状态栏高亮显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
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
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                Date dt = new Date(System.currentTimeMillis());
                time.setText(df.format(dt));
                Glide.with(ProcessActivity.this).load(getIntent().getData()).into(photo);
                new MyLocation().getMyLocation(aMap, mapLocationClient);
            }
        });
    }

    private void initView() {
        quit = findViewById(R.id.quit);
        ok = findViewById(R.id.ok);
        location = findViewById(R.id.location);
        photo = findViewById(R.id.photo);
        text = findViewById(R.id.text);
        time = findViewById(R.id.time);
        quit.setOnClickListener(this);
        ok.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.quit: {
                finish();
                break;
            }
            case R.id.ok: {
                WorksContent worksContent = new WorksContent();
                String time = this.time.getText().toString();
//                worksContent.setWorksId(-1);
                worksContent.setContentText(text.getText().toString());
                worksContent.setDate(time.substring(0, time.indexOf(" ")));
                worksContent.setTime(time.substring(time.indexOf(" ") + 1));
                worksContent.setLocation(local);
                if (getIntent().getData() != null) {
                    worksContent.setPhoto(getIntent().getData().toString());
                }else {
                    worksContent.setPhoto("");
                }
                Intent intent = new Intent();
                intent.putExtra("worksContent", worksContent);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
        }
    }
}
