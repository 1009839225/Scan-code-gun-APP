package com.gztd.test01;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import util.Loginconnect;
import util.SharedHelper;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class LoginActiviy extends Activity {
    private EditText a_ed_name;
    private EditText a_ed_pw;
    private EditText a_ed_cd;
    private String pname;
    private String strname;
    private String strpasswd;
    private SharedHelper sh;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_login);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mContext = getApplicationContext();
        sh = new SharedHelper(mContext);
        bindViews();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void bindViews() {
        a_ed_name = findViewById(R.id.a_ed_name);
        a_ed_pw = findViewById(R.id.a_ed_pw);
        a_ed_cd = findViewById(R.id.a_ed_cd);
        Button bt_login = findViewById(R.id.bt_login);
        Button bt_setting = findViewById(R.id.bt_setting);

        bt_login.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bt_login.setBackgroundResource(R.drawable.button_pressed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                bt_login.setBackgroundResource(R.drawable.blue_button);
            }
            return false;
        });
        bt_login.setOnClickListener(v -> {
            strname = a_ed_name.getText().toString();
            strpasswd = a_ed_pw.getText().toString();
            pname = a_ed_cd.getText().toString();
            SharedPreferences sp = mContext.getSharedPreferences("mysp2", Context.MODE_PRIVATE);
            String AccNum = sp.getString("userwb", "");

            String Addressurl = "http://" + AccNum + "/WebService_T_YM.asmx";
            //  Log.e("11", "22222");
            // pname=a_ed_cd.getText().toString();
            String f = null;
            try {

                f = Loginconnect.getland(strname, strpasswd, pname, Addressurl);


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (f != null) {
                Log.e("f", f);
                try {
                    JSONObject json = new JSONObject(f);
                    String res = json.getString("result");
                    JSONArray js = new JSONArray(res);
                    JSONObject json1 = js.getJSONObject(0);
                    String date = json1.getString("data");
//							 String UserName=json1.getString("UserName");

                    if (date.contains("成功")) {

                        long time = (int) (System.currentTimeMillis() / 1000);
                        Long time1 = (long) 1597891025;

                        if (time >= time1) {
                            Toast.makeText(getApplicationContext(), "版本过期", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("11", "11");
                            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                            sh.save(strname, strpasswd, pname);
                            Intent intent = new Intent(LoginActiviy.this, MainActivity.class);
                            finish();
                            startActivity(intent);
                        }

                    } else {
                        //JSONObject js=new JSONObject(json);
                        Log.e("11", "22");
                        Toast.makeText(getApplicationContext(), f, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //String res=

            } else {
                Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
            }

        });
        bt_setting.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActiviy.this, DeployActivity.class);
            finish();
            startActivity(intent);
        });
        bt_setting.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bt_setting.setBackgroundResource(R.drawable.button_pressed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                bt_setting.setBackgroundResource(R.drawable.blue_button);
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Map<String, String> data = sh.read();
        a_ed_name.setText(data.get("strname"));
        a_ed_pw.setText(data.get("strpasswd"));
        a_ed_cd.setText(data.get("pname"));
    }

//    // 第一次按下返回键的事件
//    private long firstPressedTime;
//
//    // System.currentTimeMillis() 当前系统的时间
//    @Override
//    public void onBackPressed() {
//        if (System.currentTimeMillis() - firstPressedTime < 2000) {
//            super.onBackPressed();
//        } else {
//            Toast.makeText(LoginActiviy.this, "再按一次退出", Toast.LENGTH_SHORT).show();
//            firstPressedTime = System.currentTimeMillis();
//        }
//    }
}
