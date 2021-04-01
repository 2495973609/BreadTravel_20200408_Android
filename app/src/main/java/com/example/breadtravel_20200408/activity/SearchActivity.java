package com.example.breadtravel_20200408.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.adapter.SearchAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private SearchView searchView;
    private ListView listView;
    private List<Tip> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //全屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //状态栏高亮显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initView();
    }

    private void initView() {
        searchView=findViewById(R.id.searchView);
        listView=findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        searchView.setIconified(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //单机搜索按钮时激发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                //实际应用中应该在该方法内执行实际查询，此处仅使用Toast显示用户输入的查询内容
                PoiSearch.Query queryPoi=new PoiSearch.Query(query,"110000","");
                queryPoi.setPageSize(10);// 设置每页最多返回多少条poiitem
                queryPoi.setPageNum(1);//设置查询页码
                PoiSearch poiSearch=new PoiSearch(SearchActivity.this,queryPoi);
                poiSearch.searchPOIAsyn();
                poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                    @Override
                    public void onPoiSearched(PoiResult poiResult, int i) {
                        List<PoiItem> list=poiResult.getPois();
                        Intent intent=new Intent(SearchActivity.this,PoiSearchActivity.class);
                        intent.putParcelableArrayListExtra("poiItem", (ArrayList<? extends Parcelable>) list);
                        startActivity(intent);
                    }

                    @Override
                    public void onPoiItemSearched(PoiItem poiItem, int i) {
                        System.out.println(poiItem.toString()+"!!!");
                    }
                });
                return false;
            }

            //用户输入字符时激发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                //如果newText不是长度为0的字符串
                if (TextUtils.isEmpty(newText)) {
                    listView.setAdapter(null);
                } else {
                    //使用用户输入的内容对ListView的列表项进行过滤
//                    listView.setFilterText(newText);
                    InputtipsQuery inputquery = new InputtipsQuery(newText, "");
                    inputquery.setCityLimit(true);//限制在当前城市
                    Inputtips inputTips = new Inputtips(SearchActivity.this, inputquery);
                    inputTips.requestInputtipsAsyn();
                    inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
                        @Override
                        public void onGetInputtips(List<Tip> list, int i) {
                            addressList=list;
                            SearchAdapter searchAdapter=new SearchAdapter(list);
                            listView.setAdapter(searchAdapter);
                        }
                    });
                }
                return true;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(SearchActivity.this,SearchContentActivity.class);
        intent.putExtra("address",addressList.get(i));
        startActivity(intent);
    }
}
