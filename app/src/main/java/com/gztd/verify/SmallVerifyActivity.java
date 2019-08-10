package com.gztd.verify;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.gztd.test01.MainActivity;
import com.gztd.test01.R;
import com.pedaily.yc.ycdialoglib.dialog.loading.ViewLoading;
import com.pedaily.yc.ycdialoglib.fragment.CustomDialogFragment;
import com.pedaily.yc.ycdialoglib.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import util.MyTableTextView1;

public class SmallVerifyActivity extends AppCompatActivity {
    @BindView(R.id.et_xh)
    EditText etXh;
    @BindView(R.id.tv_count)
    TextView tvCount;
    private EditText et_wlbq;// 物料标签框
    private Button bt_hd;// 核对按钮
    private Button bt_exit;// 退出按钮
    private ImageButton top_back;// 返回上一页按钮
    private String str1;
    private String inventoryCode;
    private RelativeLayout rl_wlbq;// 物料标签
    private LinearLayout tb_wlbq;// 物料标签表体标题
    List<String> list = new ArrayList<>();
    List<String> list1 = new ArrayList<>();
    private RelativeLayout relativeLayout1;
    private Context mContext;
    private String[] name_wlbq = {"代号", "牌号", "图号", "规格型号", "冲压号", "数量", "件数",
            "带材批号（复合批号）", "累计扫描数量", "累计扫描件数", "10"};

    private ProgressDialog dialog;
    private String et_str_wlbq;

    private Handler handler_wlbq = new Handler();

    private Runnable delayRun_wlbq = new Runnable() {

        @Override
        public void run() {
            if (et_str_wlbq.length() > 0) {
                // TODO 调用服务器接口
                load();
            } else {
                handler_wlbq.removeCallbacks(delayRun_wlbq);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_verify_small);
        ButterKnife.bind(this);
        init();
    }

    protected void load() {
        QueryAddressTask queryAddressTask = new QueryAddressTask();
        //开始loading
        ViewLoading.show(mContext, "加载中");
        // 启动后台任务
        queryAddressTask.execute(et_wlbq.getText().toString());
    }

    class QueryAddressTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (isLegal(et_str_wlbq) == false) {
                //结束loading
                ViewLoading.dismiss(mContext);
                Toast.makeText(SmallVerifyActivity.this, "无效数据，请重试", Toast.LENGTH_SHORT).show();
                et_wlbq.setText("");
            } else {
                //结束loading
                ViewLoading.dismiss(mContext);
                indata();
            }
        }
    }

    private void vision(RelativeLayout relativeLayout1) {
        //relativeLayout1如果为空，就是第一次扫码，不需要判断
        if (relativeLayout1 == null) {
            MyTableTextView1 txt = relativeLayout1.findViewById(R.id.list_1_1);
            txt.setText(list.get(0));
            txt.setFocusableInTouchMode(false);
            list1.add(list.get(0));
            list.clear();
            checkMatch();
            tvCount.setText(String.valueOf(Integer.valueOf(tvCount.getText().toString()) + 1));
        } else {
            //判断不重复
            if (!isRepeat()) {
                MyTableTextView1 txt = relativeLayout1.findViewById(R.id.list_1_1);
                txt.setText(list.get(0));
                txt.setFocusableInTouchMode(false);
                list1.add(list.get(0));
                txt.setOnClickListener(v -> {
                    Intent intent3 = new Intent(SmallVerifyActivity.this, SmallVerifyActivity.class);

                    intent3.putExtra("InventoryCode", list1.get(0));//键值对 后面的值为传的内容
                    intent3.putExtra("Inventoryname", list1.get(0));

                    startActivity(intent3);
                });
                list.clear();
                checkMatch();
                tvCount.setText(String.valueOf(Integer.valueOf(tvCount.getText().toString()) + 1));
            } else {
                Toast.makeText(mContext, "不能重复扫码", Toast.LENGTH_SHORT).show();
                relativeLayout1.removeAllViews();
                list.clear();
            }
        }
    }

    //判断是否有重复
    public boolean isRepeat() {
        boolean r = false;
        for (int i = 0; i < list1.size(); i++) {
            if (list.get(0).contains(list1.get(i))) {
                r = true;
            }
        }
        return r;
    }

    //正则表达式判断字符输入合法性
    public static boolean isLegal(String content) {
        Pattern pattern = Pattern.compile("\\|");
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }

    private void checkMatch() {
        DialogUtils.requestMsgPermission(this);//自定义样式调用

        //这里判断是否匹配
        if (str1.equals(inventoryCode)) {
            CustomDialogFragment
                    .create(getSupportFragmentManager())
                    .setTitle("系统提示：")
                    .setContent("当前数据匹配，请继续！")
                    .setDimAmount(0.2f)
                    .setTag("dialog")
                    .setCancelOutside(true)
                    .setOkListener(v -> CustomDialogFragment.dismissDialogFragment())
                    .show();
        } else {
            CustomDialogFragment
                    .create(getSupportFragmentManager())
                    .setTitle("系统提示：")
                    .setContent("当前数据不匹配，请检查！")
                    .setDimAmount(0.2f)
                    .setTag("dialog")
                    .setCancelOutside(true)
                    .setOkListener(v -> CustomDialogFragment.dismissDialogFragment())
                    .show();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        mContext = SmallVerifyActivity.this;
        et_wlbq = findViewById(R.id.et_wlbq);
        tb_wlbq = findViewById(R.id.tb_wlbq);
        bt_hd = findViewById(R.id.bt_hd);
        bt_hd.setOnClickListener(v -> {
            // TODO 核对
            Toast.makeText(SmallVerifyActivity.this, "此处要核对，功能建设中...", Toast.LENGTH_SHORT).show();
        });

        bt_hd.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bt_hd.setBackgroundResource(R.drawable.button_pressed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                bt_hd.setBackgroundResource(R.drawable.blue_button);
            }
            return false;
        });
        bt_exit = findViewById(R.id.bt_exit);
        bt_exit.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(SmallVerifyActivity.this, MainActivity.class);
            startActivity(intent);
        });
        bt_exit.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bt_exit.setBackgroundResource(R.drawable.button_pressed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                bt_exit.setBackgroundResource(R.drawable.blue_button);
            }
            return false;
        });

        top_back = findViewById(R.id.top_back);
        top_back.setOnClickListener(v -> {
            finish();
        });

        // 物料标签的扫描监听
        et_wlbq.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (delayRun_wlbq != null) {
                    handler_wlbq.removeCallbacks(delayRun_wlbq);
                }
                et_str_wlbq = s.toString();
                handler_wlbq.postDelayed(delayRun_wlbq, 800);
            }
        });
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在下载，请稍后...");
        dialog.setCancelable(false);

        tableview_wlbq();
        Intent intent3 = getIntent();
        etXh.setText(intent3.getStringExtra("InventoryCode"));
        inventoryCode = intent3.getStringExtra("InventoryCode");
    }

    //返回扫码数据的方法
    public void indata() {
        tb_wlbq.removeAllViews();
        tableview_wlbq();
        String str = et_wlbq.getText().toString();

        StringTokenizer st = new StringTokenizer(str, "\\|");
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
        et_wlbq.setText("");
        str1 = list.get(0);
        et_wlbq.setHint(str1);
        relativeLayout1 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.table_wlbq, null);

        MyTableTextView1 txt = relativeLayout1.findViewById(R.id.list_1_1);
        txt.setText(list.get(1));

//        txt = relativeLayout1.findViewById(R.id.list_1_2);
//        txt.setText(list.get(2));
//
//        txt = relativeLayout1.findViewById(R.id.list_1_3);
//        txt.setText(list.get(3));
//
//        txt = relativeLayout1.findViewById(R.id.list_1_4);
//        txt.setText(list.get(4));
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

        tb_wlbq.addView(relativeLayout1);

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

    // 物料标签表体标题显示
    private void tableview_wlbq() {
        rl_wlbq = (RelativeLayout) LayoutInflater.from(this).inflate(
                R.layout.table_wlbq, null);
        MyTableTextView1 title = rl_wlbq
                .findViewById(R.id.list_1_1);
        title.setText(name_wlbq[0]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_wlbq.findViewById(R.id.list_1_2);
        title.setText(name_wlbq[1]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_wlbq.findViewById(R.id.list_1_3);
        title.setText(name_wlbq[2]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_wlbq.findViewById(R.id.list_1_4);
        title.setText(name_wlbq[3]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_wlbq.findViewById(R.id.list_1_5);
        title.setText(name_wlbq[4]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_wlbq.findViewById(R.id.list_1_6);
        title.setText(name_wlbq[5]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_wlbq.findViewById(R.id.list_1_7);
        title.setText(name_wlbq[6]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_wlbq.findViewById(R.id.list_1_8);
        title.setText(name_wlbq[7]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_wlbq.findViewById(R.id.list_1_9);
        title.setText(name_wlbq[8]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_wlbq.findViewById(R.id.list_1_10);
        title.setText(name_wlbq[9]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        tb_wlbq.addView(rl_wlbq);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
