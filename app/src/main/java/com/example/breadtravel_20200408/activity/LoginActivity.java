package com.example.breadtravel_20200408.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.ArraySet;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.entity.WorksContent;
import com.example.breadtravel_20200408.httpRequest.RequestUserDaoImpl;
import com.example.breadtravel_20200408.util.DestroyActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userName, passWord;
    private Button submit;
    private TextView protocol, forget, register;
    private ImageButton eye;
    private boolean eyeFlag;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //全屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //状态栏高亮显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //初始化控件
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        DestroyActivity.destroyActivity("flashActivity");
        DestroyActivity.destroyActivity("mainActivity");
        DestroyActivity.destroyActivity("settingActivity");
        initView();
    }

    private void initView() {
        userName = findViewById(R.id.userName);
        passWord = findViewById(R.id.passWord);
        eyeFlag = false;
        eye = findViewById(R.id.eye);
        eye.setOnClickListener(this);
        submit = findViewById(R.id.submit);
        forget = findViewById(R.id.forget);
        register = findViewById(R.id.register);
        protocol = findViewById(R.id.protocol);
        submit.setOnClickListener(this);
        protocol.setOnClickListener(this);
        forget.setOnClickListener(this);
        register.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.eye: {
                if (eyeFlag == false) {
                    passWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passWord.setSelection(passWord.getText().toString().length());
                    eye.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye1));
                    eyeFlag = true;
                } else {
                    passWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passWord.setSelection(passWord.getText().toString().length());
                    eye.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye2));
                    eyeFlag = false;
                }
                break;
            }
            case R.id.protocol: {
                Intent intent = new Intent(LoginActivity.this, ProtocolActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.register: {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.submit: {
                final Map<String, String> params = new HashMap<>();
                params.put("account", userName.getText().toString().trim());
                params.put("passWord", passWord.getText().toString().trim());
                String result = new RequestUserDaoImpl().Login(params);
                Message message = new Message();
                if (result.equals("F")) {
                    message.what = -1;
                } else if (result.equals("N")) {
                    message.what = 404;
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            editor.putString("userName", jsonObject.getString("userName"));
                            editor.putString("email", jsonObject.getString("email"));
                            editor.commit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                handler.sendMessage(message);
                break;
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 404: {
                    Toast.makeText(LoginActivity.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
                    break;
                }
                case -1: {
                    Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                }

            }
        }
    };

}
