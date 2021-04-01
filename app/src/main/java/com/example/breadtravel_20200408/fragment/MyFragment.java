package com.example.breadtravel_20200408.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.breadtravel_20200408.activity.EditActivity;
import com.example.breadtravel_20200408.activity.HeadImgView;
import com.example.breadtravel_20200408.activity.SettingActivity;
import com.example.breadtravel_20200408.activity.WorksContentActivity;
import com.example.breadtravel_20200408.adapter.PreviewAdapter;
import com.example.breadtravel_20200408.entity.WorksContent;
import com.example.breadtravel_20200408.entity.WorksPreview;
import com.example.breadtravel_20200408.httpRequest.DownLoadFile;
import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.httpRequest.RequestUserDaoImpl;
import com.example.breadtravel_20200408.httpRequest.RequestWorksDaoImpl;
import com.example.breadtravel_20200408.util.BaseApplication;
import com.example.breadtravel_20200408.util.GlideLoadImg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View view;
    private ImageView headImg, myBackGround, more;
    private TextView nickName, idNum;
    private Button editBtn;
    private String userName;
    public TabHost tabHost;
    private Context context;
    private Bitmap bitmap;
    private SharedPreferences sharedPreferences;
    private View myWorksView, praiseView;
    private ListView listView3, listView4;
    private List<WorksPreview> list3, list4;
    private Map<String, String> params = new HashMap<>();
    PreviewAdapter previewAdapter3;

    public MyFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my, container, false);
        initView();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Map<String, String> params = new HashMap<>();
        params.put("userName", userName);
        idNum.setText(userName);
        nickName.setText(new RequestUserDaoImpl().nickName(params));
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GlideLoadImg(userName, headImg);
            }
        });

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    headImg.setImageBitmap(bitmap);
                    break;
                }
                case 404: {
                    Toast.makeText(getContext(), "无法连接服务器", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };

    /*
    初始化布局控件
     */
    private void initView() {
        sharedPreferences = context.getSharedPreferences("userInfo", MODE_PRIVATE);
        userName = sharedPreferences.getString("userName", null);
        params.put("userName", userName);
        myBackGround = view.findViewById(R.id.myBackGround);
        headImg = view.findViewById(R.id.headImg);
        more = view.findViewById(R.id.more);
        nickName = view.findViewById(R.id.nickName);
        idNum = view.findViewById(R.id.idNum);
        editBtn = view.findViewById(R.id.editBtn);
        more.setOnClickListener(this);
        myBackGround.setOnClickListener(this);
        headImg.setOnClickListener(this);
        editBtn.setOnClickListener(this);
        tabHost = view.findViewById(R.id.tabHost);
        tabHost.setup();
        LayoutInflater inflater = LayoutInflater.from(context);
        myWorksView = inflater.inflate(R.layout.my_works_preview, tabHost.getTabContentView());
        praiseView = inflater.inflate(R.layout.praise_preview, tabHost.getTabContentView());
        tabHost.addTab(tabHost.newTabSpec("my_works_preview").setIndicator("作品").setContent(R.id.my_works_preview));
        tabHost.addTab(tabHost.newTabSpec("praise_preview").setIndicator("喜欢").setContent(R.id.praise_preview));
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getPreviewList(1);
                getPreviewList(2);
            }
        });
    }

    private void getPreviewList(int i) {
        Gson gson = new Gson();
        String jsonData = "N";
        switch (i) {
            case 1: {
                listView3 = myWorksView.findViewById(R.id.listView3);
                jsonData = new RequestWorksDaoImpl().myWorksPreview(params);
                Message message = new Message();
                if (jsonData.equals("N")) {
                    message.what = 404;
                    handler.sendMessage(message);
                } else {
                    list3 = gson.fromJson(jsonData, new TypeToken<List<WorksPreview>>() {
                    }.getType());
                    previewAdapter3 = new PreviewAdapter(list3);
                    listView3.setAdapter(previewAdapter3);
                    listView3.setOnItemClickListener(this);
                    listView3.setOnCreateContextMenuListener(this);
                }
                break;
            }
            case 2: {
                listView4 = praiseView.findViewById(R.id.listView4);
                jsonData = new RequestWorksDaoImpl().praisePreview(params);
                Message message = new Message();
                if (jsonData.equals("N")) {
                    message.what = 404;
                    handler.sendMessage(message);
                } else {
                    list4 = gson.fromJson(jsonData, new TypeToken<List<WorksPreview>>() {
                    }.getType());
                    PreviewAdapter previewAdapter = new PreviewAdapter(list4);
                    listView4.setAdapter(previewAdapter);
                    listView4.setOnItemClickListener(this);
                }
                break;
            }
        }

    }

    //设置长按弹出菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 1, 0, "删除");
        menu.add(0, 3, 0, "取消");
        super.onCreateContextMenu(menu, v, menuInfo);
    }


    //完成长按菜单的功能
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        ContextMenu.ContextMenuInfo menuInfo = item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int i = (int) info.id;
        switch (item.getItemId()) {
            case 1: {
                Map<String, String> params = new HashMap<>();
                params.put("worksId", String.valueOf(list3.get(i).getWorksId()));
                String result = new RequestWorksDaoImpl().deleteWorks(params);
                if (result.equals("T")) {
                    Toast.makeText(getContext(), "作品:" + list3.get(i).getTitle() + " 删除成功", Toast.LENGTH_SHORT).show();
                    list3.remove(i);
                    previewAdapter3.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "作品:" + list3.get(i).getTitle() + " 删除失败", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more: {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.editBtn: {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.headImg: {
                Intent intent = new Intent(getActivity(), HeadImgView.class);
                startActivity(intent);
                break;

            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), WorksContentActivity.class);
        switch (adapterView.getId()) {
            case R.id.listView3: {
                intent.putExtra("previewData", list3.get(i));
                break;
            }
            case R.id.listView4: {
                intent.putExtra("previewData", list4.get(i));
                break;
            }
        }
        startActivity(intent);
    }

}
