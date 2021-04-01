package com.example.breadtravel_20200408.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.adapter.ContentAdapter;
import com.example.breadtravel_20200408.entity.WorksComment;
import com.example.breadtravel_20200408.entity.WorksContent;
import com.example.breadtravel_20200408.entity.WorksPreview;
import com.example.breadtravel_20200408.httpRequest.RequestCommentDaoImpl;
import com.example.breadtravel_20200408.httpRequest.RequestUserDaoImpl;
import com.example.breadtravel_20200408.httpRequest.RequestWorksDaoImpl;
import com.example.breadtravel_20200408.util.GlideLoadImg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorksContentActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView quit, praise, comment, share, more, myBackGround, headImg,attention;
    private TextView author, title, date, day, love, browse, keyWords;
    private ImageView hdImg,sendComment;
    private EditText commentEdit;
    private TextView nc,commentText,comTime;
    private WorksPreview worksPreview;
    private LinearLayout worksContent,worksComment;
    private List<WorksContent> list;
    private List<WorksComment> commentList;
    private String jsonData = "N",commentJson="N";
    private ImageView photo;
    private TextView contentText, time, location;
    private Map<String, String> params = new HashMap<>();
    private Gson gson = new Gson();
    private SharedPreferences sharedPreferences;
    private ScrollView scrollView;
    private String userName;
    boolean isPraise;
    boolean isAttention;
    static final String HOST = "http://192.168.1.13:8080/BreadTravel_20200408_war_exploded/upload/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works_content);
        //全屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //状态栏高亮显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getWindow().setSoftInputMode
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
        userName=sharedPreferences.getString("userName",null);
        worksPreview = (WorksPreview) getIntent().getSerializableExtra("previewData");
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Map<String, String> params = new HashMap<>();
        params.put("worksId",String.valueOf(worksPreview.getWorksId()));
        params.put("skimNum",String.valueOf(worksPreview.getSkim()+1));
        params.put("userName",userName);
        params.put("worksUser",worksPreview.getUserName());
        String result=new RequestWorksDaoImpl().initRelation(params);
        try {
            JSONObject jsonObject=new JSONObject(result);
            if (jsonObject.getString("isPraise").equals("T")){
                praise.setImageDrawable(getResources().getDrawable(R.drawable.praise_y));
                isPraise=true;
            }else {
                isPraise=false;
            }
            if (jsonObject.getString("isAttention").equals("T")){
                attention.setImageDrawable(getResources().getDrawable(R.drawable.attention_y));
                isAttention=true;
            }else {
                isAttention=false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        quit = findViewById(R.id.quit);
        scrollView=findViewById(R.id.scrollView);
        attention=findViewById(R.id.attention);
        praise = findViewById(R.id.praise);
        comment = findViewById(R.id.comment);
        share = findViewById(R.id.share);
        more = findViewById(R.id.more);
        myBackGround = findViewById(R.id.myBackGround);
        headImg = findViewById(R.id.headImg);
        worksContent = findViewById(R.id.worksContent);
        worksComment=findViewById(R.id.worksComment);
        author = findViewById(R.id.author);
        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        day = findViewById(R.id.day);
        love = findViewById(R.id.love);
        browse = findViewById(R.id.browse);
        commentEdit=findViewById(R.id.commentEdit);
        sendComment=findViewById(R.id.sendComment);
//        keyWords = findViewById(R.id.keyWords);
        comment.setOnClickListener(this);
        sendComment.setOnClickListener(this);
        quit.setOnClickListener(this);
        author.setText(worksPreview.getNickName());
        title.setText(worksPreview.getTitle());
        date.setText(worksPreview.getDate());
        day.setText(worksPreview.getDay() + "天");
        love.setText(String.valueOf(worksPreview.getPraise()));
        browse.setText(String.valueOf(worksPreview.getSkim()));
//        keyWords.setText(worksPreview.getKeyWords());
        praise.setOnClickListener(this);
        attention.setOnClickListener(this);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RequestOptions options = new RequestOptions().error(R.drawable.default_my_bg).placeholder(R.drawable.default_my_bg);
                Glide.with(WorksContentActivity.this).load(HOST + worksPreview.getUserName() + "/" + worksPreview.getHeadImg()).apply(options).into(headImg);
                Glide.with(WorksContentActivity.this).load(HOST + worksPreview.getUserName() + "/" + worksPreview.getCoverImg()).apply(options).into(myBackGround);
            }
        });
        params.put("worksId", String.valueOf(worksPreview.getWorksId()));
        jsonData = new RequestWorksDaoImpl().worksContent(params);
        commentJson=new RequestCommentDaoImpl().queryComment(params);
        if (jsonData.equals("N")||commentJson.equals("N")) {
            Message message = new Message();
            message.what = 404;
            handler.sendMessage(message);
        } else {
            list = gson.fromJson(jsonData, new TypeToken<List<WorksContent>>() {
            }.getType());
            final RequestOptions options=new RequestOptions().placeholder(R.drawable.logo);
            for (int i = 0; i < list.size(); i++) {
                final View view = View.inflate(WorksContentActivity.this, R.layout.content_list_item, null);
                photo = view.findViewById(R.id.photo);
                contentText = view.findViewById(R.id.contentText);
                time = view.findViewById(R.id.time);
                location = view.findViewById(R.id.location);
                final int finalI = i;
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WorksContentActivity.this).load(HOST + worksPreview.getUserName() + "/" + list.get(finalI).getPhoto()).apply(options).into(photo);
                        contentText.setText(list.get(finalI).getContentText());
                        time.setText(list.get(finalI).getDate()+" "+list.get(finalI).getTime());
                        location.setText(list.get(finalI).getLocation());
                        worksContent.addView(view);
                    }
                });
            }
            commentList=gson.fromJson(commentJson, new TypeToken<List<WorksComment>>() {}.getType());
            for (final WorksComment worksComment1 : commentList) {
                initComment(worksComment1);
            }
        }
    }



    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 404: {
                    Toast.makeText(WorksContentActivity.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.quit: {
                finish();
                break;
            }
            case R.id.comment:{
                scrollView.smoothScrollTo(0,worksContent.getMeasuredHeight());
//                commentEdit.setFocusable(true);
//                commentEdit.setFocusableInTouchMode(true);
//                commentEdit.requestFocus();
//                InputMethodManager inputManager =
//                        (InputMethodManager) commentEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.showSoftInput(commentEdit, 0);

                break;
            }
            case R.id.sendComment:{
                WorksComment worksComment=new WorksComment();
                Map<String,String > params1=new HashMap<>();
                params1.put("userName",userName);
                worksComment.setNickName(new RequestUserDaoImpl().nickName(params1));
                worksComment.setHeadImg(new RequestUserDaoImpl().HeadImg(params1));
                worksComment.setWorksId(worksPreview.getWorksId());
                worksComment.setSendUser(userName);
                worksComment.setReceiveUser(worksPreview.getUserName());
                SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                Date dt = new Date(System.currentTimeMillis());
                worksComment.setTime(df.format(dt));
                worksComment.setCommentText(commentEdit.getText().toString().trim());
                Gson gson=new Gson();
                String str=gson.toJson(worksComment);
                Map<String,String> params=new HashMap<>();
                params.put("commentJson",str);
                initComment(worksComment);
                new RequestCommentDaoImpl().addComment(params);
                commentEdit.setText("");
                commentEdit.clearFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                break;
            }
            case R.id.praise:{
                Map<String,String> params=new HashMap<>();
                params.put("userName",userName);
                params.put("worksId",String.valueOf(worksPreview.getWorksId()));
                params.put("praiseNum",String.valueOf(worksPreview.getPraise()));
                if (isPraise==true){
                    praise.setImageDrawable(getResources().getDrawable(R.drawable.praise));
                    love.setText(String.valueOf(worksPreview.getPraise()-1));
                    params.put("praise","del");
                    //取消点赞
                }else {
                    praise.setImageDrawable(getResources().getDrawable(R.drawable.praise_y));
                    love.setText(String.valueOf(worksPreview.getPraise()+1));
                    params.put("praise","add");
                    //增加点赞
                }
                new RequestWorksDaoImpl().praise(params);
                break;
            }
            case R.id.attention:{
                Map<String,String> params=new HashMap<>();
                params.put("userName",userName);
                params.put("worksUser",worksPreview.getUserName());
                if (isAttention==true){
                    attention.setImageDrawable(getResources().getDrawable(R.drawable.attention));
                    params.put("attention","del");
                    //取消关注
                }else {
                    attention.setImageDrawable(getResources().getDrawable(R.drawable.attention_y));
                    params.put("attention","add");
                    //增加关注
                }
                new RequestWorksDaoImpl().attention(params);
                break;
            }

        }
    }

    private void initComment(final WorksComment worksComment1){
        final RequestOptions options=new RequestOptions().placeholder(R.drawable.logo);
        final View view = View.inflate(WorksContentActivity.this, R.layout.comment_list_item, null);
        hdImg=view.findViewById(R.id.hdImg);
        nc=view.findViewById(R.id.nc);
        commentText=view.findViewById(R.id.commentText);
        comTime=view.findViewById(R.id.comTime);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(WorksContentActivity.this).load(HOST+worksComment1.getSendUser()+"/"+worksComment1.getHeadImg()).apply(options).into(hdImg);
                nc.setText(worksComment1.getNickName()+":");
                commentText.setText(worksComment1.getCommentText());
                comTime.setText(worksComment1.getTime());
                worksComment.addView(view);
            }
        });
    }

}
