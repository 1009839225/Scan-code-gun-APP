package com.gztd.test01;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import util.SharedHelper;

@SuppressLint("NewApi")
public class DeployActivity extends Activity {
    private Button d_bt_commint;
    private Button bt_clear;
    private ImageButton top_back;
    private EditText d_ed_wb;
    private SharedHelper sh;
    private Context mContext;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_deploy);
        mContext = getApplicationContext();
        sh = new SharedHelper(mContext);
        into();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void into() {
        CurrentEvent event = new CurrentEvent();
        // TODO Auto-generated method stub
        top_back = findViewById(R.id.top_back);
        d_ed_wb = findViewById(R.id.d_ed_wb);
        bt_clear = findViewById(R.id.button_clear);
        bt_clear.setOnClickListener(v -> d_ed_wb.setText(""));
        top_back.setOnClickListener(event);
        d_bt_commint = findViewById(R.id.d_bt_commint);
        d_bt_commint.setOnClickListener(event);
        d_bt_commint.setOnTouchListener((v, event1) -> {
            if (event1.getAction() == MotionEvent.ACTION_DOWN) {
                d_bt_commint.setBackgroundResource(R.drawable.button_pressed);
            } else if (event1.getAction() == MotionEvent.ACTION_UP) {
                d_bt_commint.setBackgroundResource(R.drawable.blue_button);
            }
            return false;
        });
        bt_clear.setOnTouchListener((v, event1) -> {
            if (event1.getAction() == MotionEvent.ACTION_DOWN) {
                bt_clear.setBackgroundResource(R.drawable.button_pressed);
            } else if (event1.getAction() == MotionEvent.ACTION_UP) {
                bt_clear.setBackgroundResource(R.drawable.blue_button);
            }
            return false;
        });
    }

    class CurrentEvent implements OnClickListener {

        private CurrentEvent() {

        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.top_back:
                    back();
                    break;
                case R.id.d_bt_commint:
                    commit();
                    break;
                default:
                    break;
            }
        }

        private void commit() {
            // TODO Auto-generated method stub

            String userwb = d_ed_wb.getText().toString();
            String add = "http://" + userwb;
            boolean flag = testWsdlConnection(add);
            System.out.println(flag);
            if (flag == true) {
                sh.save(userwb);
                Intent int2 = new Intent(DeployActivity.this,
                        LoginActiviy.class);
                finish();
                startActivity(int2);
            } else {
                Toast.makeText(getApplicationContext(), "访问失败",
                        Toast.LENGTH_SHORT).show();
            }

        }

        public boolean testWsdlConnection(String address) {
            boolean flag = false;
            try {
                URL urlObj = new URL(address);
                HttpURLConnection oc = (HttpURLConnection) urlObj
                        .openConnection();
                oc.setUseCaches(false);
                oc.setConnectTimeout(3000);
                int status = oc.getResponseCode();
                if (200 == status) {
                    return flag = true;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("请求地址不通。。GjcbdwxxUtil。", address);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("请求地址不通。。GjcbdwxxUtil。", address);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("请求地址不通。。GjcbdwxxUtil。", address);
            }
            return flag;
        }

        private void back() {
            // TODO Auto-generated method stub
            Intent intent = new Intent(DeployActivity.this, LoginActiviy.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Map<String, String> data = sh.read2();

        d_ed_wb.setText(data.get("userwb"));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DeployActivity.this,LoginActiviy.class);
        finish();
        startActivity(intent);
    }

}
