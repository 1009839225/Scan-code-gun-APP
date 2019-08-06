package com.gztd.deliver;

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

public class BigVerifyActivity extends Activity {

    private EditText et_zhtm;// 纸盒条码框
    private Button bt_hd;//核对按钮
    private Button bt_jx;//继续按钮
    private ImageButton top_back;//返回上一页按钮

    private RelativeLayout rl_zhtm;//纸盒条码
    private LinearLayout tb_zhtm;//纸盒条码表体标题

    private RelativeLayout relativeLayout1;

    private String[] name_zhtm = {"代号", "牌号", "图号", "规格型号", "冲压号", "数量", "件数", "带材批号（复合批号）", "是否匹配入库单", "9", "10"};

    private Handler handler_zhtm = new Handler();
    private String et_str_zhtm;

    private ProgressDialog dialog;

    private Runnable delayRun_zhtm = new Runnable() {

        @Override
        public void run() {
            if (et_str_zhtm.length() > 0) {
                // TODO 调用服务器接口
                load();
            } else {
                handler_zhtm.removeCallbacks(delayRun_zhtm);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_verify_big);
        init();
    }

    protected void load() {
        QueryAddressTask queryAddressTask = new QueryAddressTask();
        dialog.show();
        // 启动后台任务
        queryAddressTask.execute(et_zhtm.getText().toString());

    }

    class QueryAddressTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (isLegal(et_str_zhtm) == false) {
                dialog.dismiss();
                Toast.makeText(BigVerifyActivity.this, "无效数据，请重试", Toast.LENGTH_SHORT).show();
                et_zhtm.setText("");
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

    //返回扫码数据的方法
    public void indata() {
        tb_zhtm.removeAllViews();
        tableview_zhtm();
        String str = et_zhtm.getText().toString();
        List<String> list = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(str, "\\|");
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
        et_zhtm.setText("");
        String str1 = list.get(0);
        et_zhtm.setHint(str1);
        relativeLayout1 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.table_zhtm, null);

        MyTableTextView1 txt = relativeLayout1.findViewById(R.id.list_1_1);
        txt.setText(list.get(1));

        txt = relativeLayout1.findViewById(R.id.list_1_2);
        txt.setText(list.get(2));

        txt = relativeLayout1.findViewById(R.id.list_1_3);
        txt.setText(list.get(3));

        txt = relativeLayout1.findViewById(R.id.list_1_4);
        txt.setText(list.get(4));
//
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

        tb_zhtm.addView(relativeLayout1);

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
        et_zhtm = findViewById(R.id.et_zhtm);
        tb_zhtm = findViewById(R.id.tb_zhtm);
        bt_hd = findViewById(R.id.bt_hd);
        bt_hd.setOnClickListener(v -> {
            //TODO 核对并弹出对话框
            Toast.makeText(BigVerifyActivity.this, "此处要核对，功能建设中...", Toast.LENGTH_SHORT).show();
        });
        bt_hd.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bt_hd.setBackgroundResource(R.drawable.button_pressed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                bt_hd.setBackgroundResource(R.drawable.blue_button);
            }
            return false;
        });
        bt_jx = findViewById(R.id.bt_jx);
        bt_jx.setOnClickListener(v -> {
            Intent intent = new Intent(BigVerifyActivity.this, SmallVerifyActivity.class);
            finish();
            startActivity(intent);
        });
        bt_jx.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bt_jx.setBackgroundResource(R.drawable.button_pressed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                bt_jx.setBackgroundResource(R.drawable.blue_button);
            }
            return false;
        });

        top_back = findViewById(R.id.top_back);
        top_back.setOnClickListener(v -> {
            Intent intent = new Intent(BigVerifyActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        });

        // 纸盒条码的扫描监听
        et_zhtm.addTextChangedListener(new TextWatcher() {

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
                if (delayRun_zhtm != null) {
                    handler_zhtm.removeCallbacks(delayRun_zhtm);
                }
                et_str_zhtm = s.toString();
                handler_zhtm.postDelayed(delayRun_zhtm, 800);
            }
        });
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在下载，请稍后...");
        dialog.setCancelable(false);
        tableview_zhtm();
    }

    //纸盒条码表体标题显
    private void tableview_zhtm() {
        rl_zhtm = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.table_zhtm, null);
        MyTableTextView1 title = rl_zhtm.findViewById(R.id.list_1_1);
        title.setText(name_zhtm[0]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_zhtm.findViewById(R.id.list_1_2);
        title.setText(name_zhtm[1]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_zhtm.findViewById(R.id.list_1_3);
        title.setText(name_zhtm[2]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_zhtm.findViewById(R.id.list_1_4);
        title.setText(name_zhtm[3]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_zhtm.findViewById(R.id.list_1_5);
        title.setText(name_zhtm[4]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_zhtm.findViewById(R.id.list_1_6);
        title.setText(name_zhtm[5]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_zhtm.findViewById(R.id.list_1_7);
        title.setText(name_zhtm[6]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_zhtm.findViewById(R.id.list_1_8);
        title.setText(name_zhtm[7]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_zhtm.findViewById(R.id.list_1_9);
        title.setText(name_zhtm[8]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        tb_zhtm.addView(rl_zhtm);
    }
}
