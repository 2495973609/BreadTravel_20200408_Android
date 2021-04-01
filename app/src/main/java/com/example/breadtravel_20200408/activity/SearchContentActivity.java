package com.example.breadtravel_20200408.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.services.help.Tip;
import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.adapter.RecyclerViewAdapter;
import com.example.breadtravel_20200408.entity.WorksPreview;
import com.example.breadtravel_20200408.httpRequest.RequestWorksDaoImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SearchContentActivity extends AppCompatActivity {

    private AMap aMap;
    private MapView mMapView;
    private Tip tip;
    private TextView title, address;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<WorksPreview> list = new ArrayList<>();
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_content);
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
        tip = (Tip) getIntent().getParcelableExtra("address");
        initView();
    }

    private void initView() {
        title = findViewById(R.id.title);
        address = findViewById(R.id.address);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        title.setText(tip.getName());
        address.setText(tip.getDistrict() + " " + tip.getAddress());
        if (tip.getPoint() != null) {
            aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            latLng = getLat("(" + tip.getPoint().toString() + ")");
            final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(tip.getName()).snippet(tip.getDistrict()));
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));

        }
        initResources(tip.getDistrict());
    }

    private void initResources(String city) {
        Map<String, String> params = new HashMap<>();
        Gson gson = new Gson();
        if (!city.equals("")) {
            city = city.substring(city.indexOf("市") - 2, city.indexOf("市") + 1);
        }else {
            city=tip.getName();
        }
        params.put("city", city);
        String jsonData = new RequestWorksDaoImpl().cityPreview(params);
        list = gson.fromJson(jsonData, new TypeToken<List<WorksPreview>>() {
        }.getType());

        if (tip.getPoint() != null) {
            Iterator<WorksPreview> iterator = list.iterator();
            while (iterator.hasNext()) {
                WorksPreview worksPreview = iterator.next();
                LatLng latLng = getLat(worksPreview.getRegion());
                float dis = AMapUtils.calculateLineDistance(this.latLng, latLng) / 1000;
                if (dis > 5) {
                    iterator.remove();
                }
            }
        }
        recyclerViewAdapter = new RecyclerViewAdapter(list, latLng, this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public static LatLng getLat(String str) {
        String jwd = str.substring(str.indexOf("("), str.indexOf(")") + 1);
        String wd = jwd.substring(1, jwd.indexOf(","));
        String jd = jwd.substring(jwd.indexOf(",") + 1, jwd.lastIndexOf(")"));
        LatLng latLng = new LatLng(Double.valueOf(wd), Double.valueOf(jd));
        return latLng;
    }

}
