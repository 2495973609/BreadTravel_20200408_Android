package com.example.breadtravel_20200408.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.adapter.RecyclerViewAdapter;
import com.example.breadtravel_20200408.entity.WorksPreview;
import com.example.breadtravel_20200408.httpRequest.RequestWorksDaoImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.phoenix.PullToRefreshView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityFragment extends Fragment implements AMapLocationListener {

    public View view;
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    public TextView location;
    private RecyclerView recyclerView;
    private PullToRefreshView refresh;
    private RecyclerView.LayoutManager layoutManager;
    private List<WorksPreview> list = new ArrayList<>();
    private RecyclerViewAdapter recyclerViewAdapter;
    private LatLng latLng1;
    private String city;

    public CityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_city, container, false);
        initView();
        getMyLocation();
        return view;
    }

    private void initView() {
        location = view.findViewById(R.id.location);
        refresh = view.findViewById(R.id.refresh);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        refresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initResources(city);
                        getMyLocation();
                        refresh.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }

    private void getMyLocation() {
        //声明mlocationClient对象
        mlocationClient = new AMapLocationClient(getContext());
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
//设置定位参数
        mLocationOption.setOnceLocation(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mlocationClient.setLocationOption(mLocationOption);
            }
        }).start();
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
        mlocationClient.startLocation();

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            latLng1 = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            city = aMapLocation.getCity();
            location.setText("自动定位：" + city);
            initResources(city);
        } else {
            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
            Log.e("AmapError", "location Error, ErrCode:"
                    + aMapLocation.getErrorCode() + ", errInfo:"
                    + aMapLocation.getErrorInfo());
        }
    }

    private void initResources(String city) {
        Map<String, String> params = new HashMap<>();
        Gson gson = new Gson();
        params.put("city", city);
        String jsonData = new RequestWorksDaoImpl().cityPreview(params);
        list = gson.fromJson(jsonData, new TypeToken<List<WorksPreview>>() {
        }.getType());
        recyclerViewAdapter = new RecyclerViewAdapter(list, latLng1,getActivity());
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}
