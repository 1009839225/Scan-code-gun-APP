package com.gztd.inventory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gztd.test01.MainActivity;
import com.gztd.test01.R;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.MyTableTextView1;

public class InventoryActivity extends Activity {

    private EditText et_djtm;// 单据条码框
    private EditText et_cptm;// 产品条码框
    private Button bt_sc;//上传按钮
    private ImageButton top_back;//返回上一页按钮

    private RelativeLayout rl_smpd;//纸盒条码
    private LinearLayout tb_djtm;//纸盒条码表体标题

    private RelativeLayout relativeLayout1;

    private String[] name_smpd = {"仓库编码", "仓库名称", "批号", "存货编码", "存货名称", "规格型号", "1", "账面数量", "账面件数", "实盘重量", "实盘件数"};

    private Handler handler_smpd = new Handler();
    private String et_str_smpd;

    private ProgressDialog dialog;

    private Runnable delayRun_smpd = new Runnable() {

        @Override
        public void run() {
            if (et_str_smpd.length() > 0) {
                // TODO 调用服务器接口
                load();
            } else {
                handler_smpd.removeCallbacks(delayRun_smpd);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inventory);
        init();
    }

    protected void load() {
        QueryAddressTask queryAddressTask = new QueryAddressTask();
        dialog.show();
        // 启动后台任务
        queryAddressTask.execute(et_djtm.getText().toString());
    }

    class QueryAddressTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (isLegal(et_str_smpd) == false) {
                dialog.dismiss();
                Toast.makeText(InventoryActivity.this, "无效数据，请重试", Toast.LENGTH_SHORT).show();
                et_djtm.setText("");
            } else {
                dialog.dismiss();
                indata();
            }
        }
    }

    //正则表达式判断字符输入合法性
    public static boolean isLegal(String content) {
        Pattern pattern = Pattern.compile("\\|");
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }

    //单据条码的
    public void indata() {
        tb_djtm.removeAllViews();
        tableview_smpd();
        String str = et_djtm.getText().toString();
        List<String> list = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(str, "\\|");
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
        et_djtm.setText("");
        String str1 = list.get(0);
        et_djtm.setHint(str1);
        relativeLayout1 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.table_smpd, null);

        MyTableTextView1 txt = relativeLayout1.findViewById(R.id.list_1_1);
        txt.setText(list.get(1));

        txt = relativeLayout1.findViewById(R.id.list_1_2);
        txt.setText(list.get(2));

        txt = relativeLayout1.findViewById(R.id.list_1_3);
        txt.setText(list.get(3));

        txt = relativeLayout1.findViewById(R.id.list_1_4);
        txt.setText(list.get(4));

//		txt = (MyTableTextView1) relativeLayout1.findViewById(R.id.list_1_6);
//		txt.setText(list.get(5));
//		
//		txt = (MyTableTextView1) relativeLayout1.findViewById(R.id.list_1_7);
//		txt.setText(list.get(6));
//		
//		txt = (MyTableTextView1) relativeLayout1.findViewById(R.id.list_1_8);
//		txt.setText(list.get(7));
//
//		txt = (MyTableTextView1) relativeLayout1.findViewById(R.id.list_1_9);
//		txt.setText(list.get(8));

        tb_djtm.addView(relativeLayout1);

//		txt = (MyTableTextView1) relativeLayout1.findViewById(R.id.list_1_10);
//		txt.setText(String.valueOf(1));
//
//		txt = (MyTableTextView1) relativeLayout1.findViewById(R.id.list_1_11);
//		txt.setText(info.getString("Code"));
//
//		txt = (MyTableTextView1) relativeLayout1.findViewById(R.id.list_1_12);
//		txt.setText(info.getString("SourceVoucherId"));
//
//		txt = (MyTableTextView1) relativeLayout1.findViewById(R.id.list_1_13);
//		txt.setText(info.getString("SourceVoucherDetailId"));
//
//		txt = (MyTableTextView1) relativeLayout1.findViewById(R.id.list_1_14);
//		txt.setText(info.getString("SourceVoucherCode"));

//		if (t1.equals(info.getString("InventoryCode")) && t2.equals(info.getString("InventoryCode"))
//				&& t3.equals(info.getString("InventoryCode"))) {
//		}
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        et_djtm = findViewById(R.id.et_djtm);
        et_cptm = findViewById(R.id.et_cptm);
        tb_djtm = findViewById(R.id.tb_smpd);
        bt_sc = findViewById(R.id.bt_sc);
        bt_sc.setOnClickListener(v -> Toast.makeText(InventoryActivity.this, "功能建设中...", Toast.LENGTH_SHORT).show());
        bt_sc.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bt_sc.setBackgroundResource(R.drawable.button_pressed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                bt_sc.setBackgroundResource(R.drawable.blue_button);
            }
            return false;
        });

        top_back = findViewById(R.id.top_back);
        top_back.setOnClickListener(v -> {
            Intent intent = new Intent(InventoryActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        });

        // 纸盒条码的扫描监听
        et_djtm.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (delayRun_smpd != null) {
                    handler_smpd.removeCallbacks(delayRun_smpd);
                }
                et_str_smpd = s.toString();
                handler_smpd.postDelayed(delayRun_smpd, 800);
            }
        });
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在下载，请稍后...");
        dialog.setCancelable(false);
        tableview_smpd();
    }

    //标题显示
    private void tableview_smpd() {
        rl_smpd = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.table_smpd, null);
        MyTableTextView1 title = rl_smpd.findViewById(R.id.list_1_1);
        title.setText(name_smpd[0]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_smpd.findViewById(R.id.list_1_2);
        title.setText(name_smpd[1]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_smpd.findViewById(R.id.list_1_3);
        title.setText(name_smpd[2]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_smpd.findViewById(R.id.list_1_4);
        title.setText(name_smpd[3]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_smpd.findViewById(R.id.list_1_5);
        title.setText(name_smpd[4]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_smpd.findViewById(R.id.list_1_6);
        title.setText(name_smpd[5]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_smpd.findViewById(R.id.list_1_7);
        title.setText(name_smpd[6]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_smpd.findViewById(R.id.list_1_8);
        title.setText(name_smpd[7]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_smpd.findViewById(R.id.list_1_9);
        title.setText(name_smpd[8]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_smpd.findViewById(R.id.list_1_10);
        title.setText(name_smpd[9]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_smpd.findViewById(R.id.list_1_11);
        title.setText(name_smpd[10]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        tb_djtm.addView(rl_smpd);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InventoryActivity.this,MainActivity.class);
        finish();
        startActivity(intent);
    }
}
