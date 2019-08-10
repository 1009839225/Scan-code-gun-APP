package com.gztd.verify;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.pedaily.yc.ycdialoglib.dialog.loading.ViewLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import util.GetTable_ST_ProductReceive;
import util.MyTableTextView1;

public class DocumentActivity extends AppCompatActivity {

    @BindView(R.id.top_back)
    ImageButton top_back;

    @BindView(R.id.tb_zhtm)
    LinearLayout tb_zhtm;
    @BindView(R.id.bt_hd)
    Button bt_hd;
    @BindView(R.id.bt_jx)
    Button bt_jx;
    @BindView(R.id.et_djtm)
    EditText etDjtm;
    @BindView(R.id.et_ckbm)
    EditText etCkbm;
    @BindView(R.id.et_gfbm)
    EditText etGfbm;

    private Context mContext;
    private String beforeresult;
    private String str1;
    private String[] name_zhtm = {"代号", "牌号", "图号", "生产批号", "数量", "件数", "带材批号", "材料编号"};
    List<String> list = new ArrayList<>();
    List<String> list1 = new ArrayList<>();
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
        setContentView(R.layout.activity_verify_doc);
        ButterKnife.bind(this);
        init();
    }

    protected void load() {
        QueryAddressTask queryAddressTask = new QueryAddressTask();
        //开始loading
        ViewLoading.show(mContext, "加载中");
        // 启动后台任务
        queryAddressTask.execute(etDjtm.getText().toString());
    }

    class QueryAddressTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.e("2", "2");
                SharedPreferences sp = mContext.getSharedPreferences("mysp2", Context.MODE_PRIVATE);
                String AccNum = sp.getString("userwb", "");
                String Addressurl = "http://" + AccNum + "/WebService_T_YM.asmx";
                beforeresult = GetTable_ST_ProductReceive.getland(params[0], Addressurl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 将结果返回给onPostExecute方法
            return beforeresult;
        }

        @Override
        protected void onPostExecute(String result) {
//            if (!isLegal(et_str_zhtm)) {
//                //结束loading
//                ViewLoading.dismiss(mContext);
//                Toast.makeText(DocumentActivity.this, "无效数据，请重试", Toast.LENGTH_SHORT).show();
//                et_zhtm.setText("");
//            } else {
            ViewLoading.dismiss(mContext);
            try {
                JSONObject info1 = new JSONObject(result);
                String main = info1.getString("Main");// 表体
                String details = info1.getString("Details");// 表体
                JSONArray jsonArray = new JSONArray(main);
                if (jsonArray.length() != 0) {
                    indata(main,details);
                } else {
                    Toast.makeText(DocumentActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                    etDjtm.setText("");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//            }

        }
    }

    //返回扫码数据
    public void indata(String main,String details) throws JSONException {
        //表头数据
        JSONArray jsonArray1 = new JSONArray(main);
        JSONObject json = jsonArray1.getJSONObject(0);
        //etCkbm如果为空，就是第一次扫码，不需要判断
        if (etCkbm.getText().toString().equals("")) {
            etDjtm.setText("");
            etDjtm.setHint(json.getString("SourceVoucherCode"));
            etCkbm.setText(json.getString("SourceVoucherCode"));
            etGfbm.setText(json.getString("SourceVoucherCode"));
            //表体数据
            JSONArray jsonArray = new JSONArray(details);
            final JSONObject info = jsonArray.getJSONObject(0);
            RelativeLayout relativeLayout2 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.table_zhtm, null);
            vision(info, relativeLayout2);
            tb_zhtm.addView(relativeLayout2);
            list.add(json.getString("SourceVoucherCode"));
            list1.add(list.get(0));
            list.clear();
        }else {
            list.add(json.getString("SourceVoucherCode"));
            if (isRepeat()){
                Toast.makeText(mContext, "不能重复扫码", Toast.LENGTH_SHORT).show();
                etDjtm.setText("");
                list.clear();
            } else {
                etDjtm.setText("");
                etDjtm.setHint(json.getString("SourceVoucherCode"));
                etCkbm.setText(json.getString("SourceVoucherCode"));
                etGfbm.setText(json.getString("SourceVoucherCode"));
                //表体数据
                JSONArray jsonArray = new JSONArray(details);
                final JSONObject info = jsonArray.getJSONObject(0);
                RelativeLayout relativeLayout2 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.table_zhtm, null);
                vision(info, relativeLayout2);
                tb_zhtm.addView(relativeLayout2);
                list.add(json.getString("SourceVoucherCode"));
                list1.add(list.get(0));
                list.clear();
            }
        }
    }

    private void vision(JSONObject info, RelativeLayout relativeLayout2) throws JSONException {
            MyTableTextView1 txt1 = relativeLayout2.findViewById(R.id.list_1_1);
            txt1.setText(info.getString("InventoryCode"));
            txt1.setOnClickListener(v -> {
                Intent intent2 = new Intent(DocumentActivity.this, BigVerifyActivity.class);
                try {
                    intent2.putExtra("InventoryCode", info.getString("InventoryCode"));//键值对 后面的值为传的内容
                    intent2.putExtra("Inventoryname", info.getString("Inventoryname"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent2);
            });
            txt1.setFocusableInTouchMode(false);

            txt1 = relativeLayout2.findViewById(R.id.list_1_2);
            txt1.setText(info.getString("InventoryName"));
            txt1.setFocusableInTouchMode(false);

            txt1 = relativeLayout2.findViewById(R.id.list_1_3);
            txt1.setText(info.getString("Specifications"));
            txt1.setFocusableInTouchMode(false);

            txt1 = relativeLayout2.findViewById(R.id.list_1_4);
            txt1.setText(info.getString("Quantity"));
            txt1.setFocusableInTouchMode(false);

            txt1 = relativeLayout2.findViewById(R.id.list_1_5);
            txt1.setText(info.getString("Storehouse"));
            txt1.setFocusableInTouchMode(false);

            txt1 = relativeLayout2.findViewById(R.id.list_1_6);
            txt1.setText(info.getString("InventoryCode"));
            txt1.setFocusableInTouchMode(false);

            txt1 = relativeLayout2.findViewById(R.id.list_1_7);
            txt1.setText(info.getString("InventoryCode"));
            txt1.setFocusableInTouchMode(false);

            txt1 = relativeLayout2.findViewById(R.id.list_1_8);
            txt1.setText(info.getString("InventoryCode"));
            txt1.setFocusableInTouchMode(false);

            txt1 = relativeLayout2.findViewById(R.id.list_1_9);
            txt1.setText(info.getString("InventoryCode"));
            txt1.setFocusableInTouchMode(false);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        mContext = DocumentActivity.this;
        bt_hd.setOnClickListener(v -> {
//				//TODO 核对并弹出对话框

        });
        bt_hd.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bt_hd.setBackgroundResource(R.drawable.button_pressed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                bt_hd.setBackgroundResource(R.drawable.blue_button);
            }
            return false;
        });
        bt_jx.setOnClickListener(v -> {
            Intent intent = new Intent(DocumentActivity.this, SmallVerifyActivity.class);
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

        top_back.setOnClickListener(v -> {
            Intent intent = new Intent(DocumentActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        });

        // 纸盒条码的扫描监听
        etDjtm.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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

        tableview_zhtm();
    }

    //正则表达式判断字符输入合法性
    public static boolean isLegal(String content) {
        Pattern pattern = Pattern.compile("\\|");
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
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

    //纸盒条码表体标题显示
    private void tableview_zhtm() {
        //纸盒条码
        RelativeLayout rl_zhtm = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.table_zhtm, null);
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

        tb_zhtm.addView(rl_zhtm);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DocumentActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }
}
