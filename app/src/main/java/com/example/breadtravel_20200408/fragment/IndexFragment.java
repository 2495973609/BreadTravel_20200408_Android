package com.example.breadtravel_20200408.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.activity.SearchActivity;
import com.example.breadtravel_20200408.activity.WorksContentActivity;
import com.example.breadtravel_20200408.adapter.PreviewAdapter;
import com.example.breadtravel_20200408.entity.WorksPreview;
import com.example.breadtravel_20200408.httpRequest.RequestWorksDaoImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private View view;
    private Context context;
    private String userName;
    private TabHost tabHost;
    private View attentionView, recommendView;
    private ListView listView1, listView2;
    private List<WorksPreview> list1, list2;
    private PullToRefreshView refreshView2;
    private LinearLayout searchBtn;
    private Map<String, String> params = new HashMap<>();

    public IndexFragment(String userName, Context context) {
        // Required empty public constructor
        this.userName = userName;
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_index, container, false);
        initView();
        return view;

    }

    private void initView() {
        searchBtn=view.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);
        tabHost = view.findViewById(R.id.tabHost);
        tabHost.setup();
        LayoutInflater inflater = LayoutInflater.from(context);
        attentionView = inflater.inflate(R.layout.attention_preview, tabHost.getTabContentView());
        recommendView = inflater.inflate(R.layout.recommend_preview, tabHost.getTabContentView());
        tabHost.addTab(tabHost.newTabSpec("attention_preview").setIndicator("关注").setContent(R.id.attention_preview));
        tabHost.addTab(tabHost.newTabSpec("recommend_preview").setIndicator("推荐").setContent(R.id.recommend_preview));
        tabHost.setCurrentTab(1);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getPreviewList(1);
                getPreviewList(2);
            }
        });
    }


    private void getPreviewList(int i) {
        params.put("userName", userName);
        final Gson gson = new Gson();
        final String jsonData = "N";
        switch (i) {
            case 1: {
                listView1 = attentionView.findViewById(R.id.listView1);
                refresh1(jsonData,gson);
                break;
            }
            case 2: {
                listView2 = recommendView.findViewById(R.id.listView2);
                refreshView2=recommendView.findViewById(R.id.refreshView2);
                refresh2(jsonData,gson);
                refreshView2.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshView2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                refresh1(jsonData,gson);
                                refresh2(jsonData,gson);
                                refreshView2.setRefreshing(false);
                            }
                        },500);
                    }
                });
                break;
            }
        }
    }

    private void refresh2(String jsonData,Gson gson){
        jsonData = new RequestWorksDaoImpl().recommendPreview(params);
        Message message = new Message();
        if (jsonData.equals("N")) {
            message.what = 404;
            handler.sendMessage(message);
        } else {
            list2 = gson.fromJson(jsonData, new TypeToken<List<WorksPreview>>() {
            }.getType());
            PreviewAdapter previewAdapter = new PreviewAdapter(list2);
            listView2.setAdapter(previewAdapter);
            listView2.setOnItemClickListener(this);
        }

    }

    private void refresh1(String jsonData,Gson gson){
        jsonData = new RequestWorksDaoImpl().attentionPreview(params);
        Message message = new Message();
        if (jsonData.equals("N")) {
            message.what = 404;
            handler.sendMessage(message);
        } else {
            list1 = gson.fromJson(jsonData, new TypeToken<List<WorksPreview>>() {
            }.getType());
            PreviewAdapter previewAdapter = new PreviewAdapter(list1);
            listView1.setAdapter(previewAdapter);
            listView1.setOnItemClickListener(this);
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 404: {
                    Toast.makeText(getContext(), "无法连接服务器", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), WorksContentActivity.class);
        switch (adapterView.getId()) {
            case R.id.listView1: {
                intent.putExtra("previewData", list1.get(i));
                break;
            }
            case R.id.listView2: {
                intent.putExtra("previewData", list2.get(i));
                break;
            }
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.searchBtn:{
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
