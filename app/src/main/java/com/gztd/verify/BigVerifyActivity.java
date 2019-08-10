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
import android.widget.TextView;
import android.widget.Toast;

import com.gztd.test01.R;
import com.pedaily.yc.ycdialoglib.dialog.loading.ViewLoading;
import com.pedaily.yc.ycdialoglib.fragment.CustomDialogFragment;
import com.pedaily.yc.ycdialoglib.utils.DialogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import util.GetTable_ST_ProductReceive;
import util.MyTableTextView1;

public class BigVerifyActivity extends AppCompatActivity {

    @BindView(R.id.top_back)
    ImageButton top_back;
    @BindView(R.id.tb_zhtm)
    LinearLayout tb_zhtm;
    @BindView(R.id.bt_hd)
    Button bt_hd;
    @BindView(R.id.bt_jx)
    Button bt_jx;
    @BindView(R.id.et_zhtm)
    EditText etRkdh;
    @BindView(R.id.et_ckbm)
    EditText etCkbm;
    @BindView(R.id.et_ghdw)
    EditText etGhdw;
    @BindView(R.id.et_btz4)
    EditText etBtz4;
    @BindView(R.id.tv_count)
    TextView tvCount;

    private Context mContext;
    List<String> list = new ArrayList<>();
    List<String> list1 = new ArrayList<>();
    private String beforeresult;
    private String str1;
    private String inventoryCode;
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
        ButterKnife.bind(this);
        init();
    }

    protected void load() {
        QueryAddressTask queryAddressTask = new QueryAddressTask();
        //开始loading
        ViewLoading.show(mContext, "加载中");
        // 启动后台任务
        queryAddressTask.execute(etRkdh.getText().toString());
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
            if (!isLegal(et_str_zhtm)) {
                //结束loading
                ViewLoading.dismiss(mContext);
                Toast.makeText(BigVerifyActivity.this, "无效数据，请重试", Toast.LENGTH_SHORT).show();
                etRkdh.setText("");
            } else {
                ViewLoading.dismiss(mContext);
                try {
                    JSONObject info1 = new JSONObject(result);
                    String s = info1.getString("Details");// 表体
                    JSONArray jsonArray = new JSONArray(s);
                    if (jsonArray.length() != 0) {
                        indata(s);
                    } else {
                        Toast.makeText(BigVerifyActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                        etRkdh.setText("");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }

    //返回扫码数据并核对
    public void indata(String s) throws JSONException {

//        tableview_zhtm();
        String str = etRkdh.getText().toString();

        StringTokenizer st = new StringTokenizer(str, "\\|");
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }

//	    if (list.size() == 9) {
        etRkdh.setText("");
        str1 = list.get(0);
        etRkdh.setHint(str1);

        //码上数据
        RelativeLayout relativeLayout1 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.table_zhtm, null);

        vision(relativeLayout1);


//			txt = (MyTableTextView1) relativeLayout1.findViewById(R.id.list_1_2);
//			txt.setText(list.get(2));
//
//			txt = (MyTableTextView1) relativeLayout1.findViewById(R.id.list_1_3);
//			txt.setText(list.get(3));
//
//			txt = (MyTableTextView1) relativeLayout1.findViewById(R.id.list_1_4);
//			txt.setText(list.get(4));
//
//			txt = (MyTableTextView1) relativeLayout1.findViewById(R.id.list_1_5);
//			txt.setText(list.get(5));
//
//			txt = (MyTableTextView1) relativeLayout1.findViewById(R.id.list_1_6);
//			txt.setText(list.get(6));
//			
//			txt = (MyTableTextView1) relativeLayout1.findViewById(R.id.list_1_7);
//			txt.setText(list.get(7));
//
//			txt = (MyTableTextView1) relativeLayout1.findViewById(R.id.list_1_8);
//			txt.setText(list.get(8));

        tb_zhtm.addView(relativeLayout1);
//		} else {
//			Toast.makeText(BigVerifyActivity.this, "��Ч���ݣ�������", Toast.LENGTH_SHORT).show();
//			et_zhtm.setText("");
//		}


//        //开始获取大盒子数据
//        JSONArray jsonArray = new JSONArray(s);
//        final JSONObject info = jsonArray.getJSONObject(0);
////	    if (str1.length()>0) {
//
//        RelativeLayout relativeLayout2 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.table_zhtm, null);
//
//        MyTableTextView1 txt1 = relativeLayout2.findViewById(R.id.list_1_1);
//        txt1.setText(info.getString("InventoryCode"));
//
//        txt1 = relativeLayout2.findViewById(R.id.list_1_2);
//        txt1.setText(info.getString("InventoryName"));
//
//        txt1 = relativeLayout2.findViewById(R.id.list_1_3);
//        txt1.setText(info.getString("Specifications"));
//
//        txt1 = relativeLayout2.findViewById(R.id.list_1_4);
//        txt1.setText(info.getString("Quantity"));
//
//        txt1 = relativeLayout2.findViewById(R.id.list_1_5);
//        txt1.setText(info.getString("Storehouse"));
//
//        txt1 = relativeLayout2.findViewById(R.id.list_1_6);
//        txt1.setText(info.getString("InventoryCode"));
//
//        txt1 = relativeLayout2.findViewById(R.id.list_1_7);
//        txt1.setText(info.getString("InventoryCode"));
//
//        txt1 = relativeLayout2.findViewById(R.id.list_1_8);
//        txt1.setText(info.getString("InventoryCode"));
//
//        txt1 = relativeLayout2.findViewById(R.id.list_1_9);
//        txt1.setText(info.getString("InventoryCode"));
//
//        tb_zhtm.addView(relativeLayout2);
//	    }


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
                    Intent intent3= new Intent(BigVerifyActivity.this, SmallVerifyActivity.class);

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

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        mContext = BigVerifyActivity.this;
        bt_hd.setOnClickListener(v -> {
//				//TODO 核对并弹出对话框
////				check();
//				builder = new AlertDialog.Builder(BigVerifyActivity.this);
//				alert = builder
//						.setIcon(R.mipmap.alert)
//						.setTitle("ϵͳ��ʾ��")
//						.setMessage("����ƥ�䣬�������")
//						.setPositiveButton("ȷ��",
//								new DialogInterface.OnClickListener() {
//									@Override
//									public void onClick( DialogInterface dialog, int which) {
//									}
//								}).create();
//				alert.show();
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

        top_back.setOnClickListener(v -> {
            finish();
        });

        // 纸盒条码的扫描监听
        etRkdh.addTextChangedListener(new TextWatcher() {

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
        //Intent传值存储单据界面传过来的数据
        Intent intent2 = getIntent();
        etCkbm.setText(intent2.getStringExtra("InventoryCode"));
        etGhdw.setText(intent2.getStringExtra("inventory"));
        etBtz4.setText(intent2.getStringExtra("inventory"));

        inventoryCode = intent2.getStringExtra("InventoryCode");
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

//    // 动态文本工具方法
//    public void DynamicSetTextTool(int stringId, Object changeText, int viewId) {
//        String RefreshTime = getResources().getString(stringId);
//        String FinalRefreshTime = String.format(RefreshTime, changeText);
//        TextView RefreshTextObject = findViewById(viewId);
//        RefreshTextObject.setText(FinalRefreshTime);
//    }

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
        title = rl_zhtm.findViewById(R.id.list_1_9);
        title.setText(name_zhtm[8]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        tb_zhtm.addView(rl_zhtm);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
