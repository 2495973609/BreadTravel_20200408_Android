package com.example.breadtravel_20200408.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.PoiItem;
import com.bumptech.glide.Glide;
import com.example.breadtravel_20200408.R;

import java.util.List;

public class PoiSearchActivity extends AppCompatActivity {

    private List<PoiItem> list;
    private MapView mMapView;
    private AMap aMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);
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
            UiSettings mUiSettings;//定义一个UiSettings对象
            mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
            mUiSettings.setZoomControlsEnabled(false);
        }
        list=getIntent().getParcelableArrayListExtra("poiItem");
        for (int i = 0; i < list.size(); i++) {
            aMap.moveCamera(CameraUpdateFactory.zoomTo(10));
            LatLng latLng=getLat("("+list.get(i).getLatLonPoint().toString()+")");
            final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(list.get(i).getTitle()).snippet(list.get(i).getSnippet()));
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        }
    }

    public static LatLng getLat(String str) {
        String jwd = str.substring(str.indexOf("("), str.indexOf(")") + 1);
        String wd = jwd.substring(1, jwd.indexOf(","));
        String jd = jwd.substring(jwd.indexOf(",") + 1, jwd.lastIndexOf(")"));
        LatLng latLng = new LatLng(Double.valueOf(wd), Double.valueOf(jd));
        return latLng;
    }
}
