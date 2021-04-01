package com.example.breadtravel_20200408.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.httpRequest.RequestUserDaoImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText passWord, emailAddress, code;
    private Button submit, getCode;
    private TextView login, protocol;
    private ImageButton eye;
    private boolean eyeFlag;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //全屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //状态栏高亮显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        init();
    }

    private void init() {
        passWord = findViewById(R.id.passWord);
        eye = findViewById(R.id.eye);
        eyeFlag = false;
        emailAddress = findViewById(R.id.emailAddress);
        code = findViewById(R.id.code);
        submit = findViewById(R.id.submit);
        getCode = findViewById(R.id.getCode);
        login = findViewById(R.id.login);
        protocol = findViewById(R.id.protocol);
        submit.setOnClickListener(this);
        getCode.setOnClickListener(this);
        getCode.setOnClickListener(this);
        login.setOnClickListener(this);
        protocol.setOnClickListener(this);
        eye.setOnClickListener(this);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long l) {
                            getCode.setClickable(false);
                            getCode.setText("重新获取（" + l / 1000 + "s）");
                            getCode.setBackgroundResource(R.drawable.nosubmit_shape);
                        }

                        @Override
                        public void onFinish() {
                            getCode.setClickable(true);
                            getCode.setText("重新获取");
                            getCode.setBackgroundResource(R.drawable.submit_shape);

                        }
                    }.start();
                    break;
                }
                case -1: {
                    Toast.makeText(RegisterActivity.this, "该邮箱已注册", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 404: {
                    Toast.makeText(RegisterActivity.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
                    break;
                }
                case -10: {
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    break;
                }
                case -100: {
                    Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };


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
            case R.id.submit: {
                if (emailAddress.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请填写邮箱地址", Toast.LENGTH_SHORT).show();
                    emailAddress.requestFocus();
                    break;
                }
                if (passWord.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请填写密码", Toast.LENGTH_SHORT).show();
                    passWord.requestFocus();
                    break;
                }
                if (code.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请填写验证码", Toast.LENGTH_SHORT).show();
                    code.requestFocus();
                    break;
                }
                final Map<String, String> params1 = new HashMap<>();
                params1.put("code", code.getText().toString().trim());
                params1.put("email", emailAddress.getText().toString().trim());
                String result1 = new RequestUserDaoImpl().Verification(params1);
                if (result1.equals("T")) {
                    final Map<String, String> params2 = new HashMap<>();
                    params2.put("passWord", passWord.getText().toString().trim());
                    params2.put("email", emailAddress.getText().toString().trim());
                    String result2 = new RequestUserDaoImpl().Register(params2);
                    Message message = new Message();
                    if (result2.equals("F")) {
                        message.what = -10;
                    } else if (result2.equals("N")) {
                        message.what = 404;
                    } else {
                        message.what = 0;
                        try {
                            JSONArray jsonArray = new JSONArray(result2);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                editor.putString("userName", jsonObject.getString("userName"));
                                editor.putString("email", jsonObject.getString("email"));
                                editor.commit();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    handler.sendMessage(message);
                } else {
                    Message message = new Message();
                    message.what = -100;
                    handler.sendMessage(message);
                }
                break;
            }
            case R.id.getCode: {
                if (emailAddress.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请填写邮箱地址", Toast.LENGTH_SHORT).show();
                    emailAddress.requestFocus();
                    break;
                }
                final Map<String, String> params = new HashMap<>();
                params.put("email", emailAddress.getText().toString().trim());
                String result = new RequestUserDaoImpl().CheckEmail(params);
                Message message = new Message();
                if (result.equals("T")) {
                    message.what = 1;
                    new RequestUserDaoImpl().GetCode(params);
                } else if (result.equals("F")) {
                    message.what = -1;
                } else {
                    message.what = 404;
                }
                handler.sendMessage(message);
                break;
            }
            case R.id.login: {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.protocol: {
                Intent intent = new Intent(RegisterActivity.this, ProtocolActivity.class);
                startActivity(intent);
                break;
            }
        }
    }


}
