package com.gztd.verify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_quantity)
    TextView tvQuantity;

    private Context mContext;
    List<String> list = new ArrayList<>();
    List<String> list1 = new ArrayList<>();
    private String num,quantity;
    private int number = 1;
    private String str1;
    private String[] name_zhtm = {"行号", "唯一编号", "代号", "重量", "件数", "自由项1", "带材批号", "自由项3", "自由项4",
            "自由项5", "自由项6", "自由项7", "自由项8", "生产批号", "自由项10"};

    private Handler handler_zhtm = new Handler();
    private String et_str_zhtm;
    private String cInvCode, cinvname, cEngineerFigNo, cFree9, iQuantity, iNum, cFree2, cFree1;
    private String mcInvCode, mcFree9, mcFree2, mcFree1 , mcinvname ,mcEngineerFigNo;

    private Runnable delayRun_zhtm = new Runnable() {

        @Override
        public void run() {
            if (et_str_zhtm.length() > 0) {
                load();
            } else {
                handler_zhtm.removeCallbacks(delayRun_zhtm);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @SuppressLint("StaticFieldLeak")
    class QueryAddressTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return null;
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
                indata();
            }
        }
    }


    //返回扫码数据并核对
    public void indata() {
        String str = etRkdh.getText().toString();

        StringTokenizer st = new StringTokenizer(str, "\\^");
//        while (st.hasMoreTokens()) {
//            String st1 = st.nextToken();
//            if (st1.equals("")){
//                list.add("");
//            }else {
//                list.add(st1);
//            }
//
//        }
        String[] split = str.split("\\^");
        list.addAll(Arrays.asList(split));
//	    if (list.size() == 9) {
        etRkdh.setText("");
        str1 = list.get(0);
        etRkdh.setHint(str1);

        //码上数据
        RelativeLayout relativeLayout1 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.table_zhtm, null);
        vision(relativeLayout1);

        tb_zhtm.addView(relativeLayout1);

    }

    private void checkMatch() {
        DialogUtils.requestMsgPermission(mContext);//自定义样式调用
        //这里判断是否匹配
        if (str1 != null) {
            if (mcInvCode.equals(cInvCode) &&
//                    mcFree1.equals(cFree1) &&
                    tvNum.getText().toString().equals(iNum) &&
                    tvQuantity.getText().toString().equals(iQuantity) &&
                    mcFree2.equals(cFree2) &&
                    mcFree9.equals(cFree9)) {
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
        } else {
            Toast.makeText(mContext, "你还没有扫码", Toast.LENGTH_SHORT).show();
        }

    }

    private void vision(RelativeLayout relativeLayout1) {
        //判断不重复
        if (!isRepeat()) {
            MyTableTextView1 txt = relativeLayout1.findViewById(R.id.list_1_1);
            txt.setText(String.valueOf(number));
            number++;
            txt.setFocusableInTouchMode(false);

            MyTableTextView1 finalTxt = txt;
            Intent intent3 = new Intent(BigVerifyActivity.this, SmallVerifyActivity.class);
            txt.setOnClickListener(v -> {
                intent3.putExtra("Code0", finalTxt.getText().toString());//行号
                startActivity(intent3);
            });

            txt = relativeLayout1.findViewById(R.id.list_1_2);
            txt.setText(list.get(0));
            txt.setFocusableInTouchMode(false);

            txt = relativeLayout1.findViewById(R.id.list_1_3);
            txt.setText(list.get(1));
            txt.setFocusableInTouchMode(false);
            intent3.putExtra("cInvCode", txt.getText().toString());//代号
            mcInvCode = txt.getText().toString();

            txt = relativeLayout1.findViewById(R.id.list_1_4);
            txt.setText(list.get(2));
            txt.setFocusableInTouchMode(false);
            intent3.putExtra("iQuantity", txt.getText().toString());//重量

            txt = relativeLayout1.findViewById(R.id.list_1_5);
            txt.setText(list.get(3));
            txt.setFocusableInTouchMode(false);
            intent3.putExtra("iNum", txt.getText().toString());//件数

            txt = relativeLayout1.findViewById(R.id.list_1_6);
            txt.setText(list.get(4));
            txt.setFocusableInTouchMode(false);
            intent3.putExtra("cFree1", txt.getText().toString());
            mcFree1 = txt.getText().toString();

            txt = relativeLayout1.findViewById(R.id.list_1_7);
            txt.setText(list.get(5));
            txt.setFocusableInTouchMode(false);
            intent3.putExtra("cFree2", txt.getText().toString());
            mcFree2 = txt.getText().toString();


            txt = relativeLayout1.findViewById(R.id.list_1_8);
            txt.setText(list.get(6));
            txt.setFocusableInTouchMode(false);
            intent3.putExtra("Code3", txt.getText().toString());

            txt = relativeLayout1.findViewById(R.id.list_1_9);
            txt.setText(list.get(7));
            txt.setFocusableInTouchMode(false);
            intent3.putExtra("Code4", txt.getText().toString());

            txt = relativeLayout1.findViewById(R.id.list_1_10);
            txt.setText(list.get(8));
            txt.setFocusableInTouchMode(false);
            intent3.putExtra("Code4", txt.getText().toString());

            txt = relativeLayout1.findViewById(R.id.list_1_11);
            txt.setText(list.get(9));
            txt.setFocusableInTouchMode(false);
            intent3.putExtra("Code4", txt.getText().toString());

            txt = relativeLayout1.findViewById(R.id.list_1_12);
            txt.setText(list.get(10));
            txt.setFocusableInTouchMode(false);
            intent3.putExtra("Code4", txt.getText().toString());

            txt = relativeLayout1.findViewById(R.id.list_1_13);
            txt.setText(list.get(11));
            txt.setFocusableInTouchMode(false);
            intent3.putExtra("Code4", txt.getText().toString());

            txt = relativeLayout1.findViewById(R.id.list_1_14);
            txt.setText(list.get(12));
            txt.setFocusableInTouchMode(false);
            intent3.putExtra("cFree9", txt.getText().toString());
            mcFree9 = txt.getText().toString();

            txt = relativeLayout1.findViewById(R.id.list_1_15);
            txt.setText(list.get(13));
            txt.setFocusableInTouchMode(false);
            intent3.putExtra("Code4", txt.getText().toString());

            quantity = list.get(2);
            num = list.get(3);
            list1.add(list.get(0));
            list.clear();
            //扫码次数合计
            tvCount.setText(String.valueOf(Integer.valueOf(tvCount.getText().toString()) + 1));
            //件数的累加
            double n1 = Double.parseDouble(tvNum.getText().toString());
            double n2 = Double.parseDouble(num);
            double res = n1 + n2;
            DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
            tvNum.setText(String.valueOf(decimalFormat.format(res)));

            //数量的累加
            double n3 = Double.parseDouble(tvQuantity.getText().toString());
            double n4 = Double.parseDouble(quantity);
            double res1 = n3 + n4;
            DecimalFormat decimalFormat1 = new DecimalFormat("###################.###########");
            tvQuantity.setText(String.valueOf(decimalFormat1.format(res1)));
        } else {
            Toast.makeText(mContext, "不能重复扫码", Toast.LENGTH_SHORT).show();
            relativeLayout1.removeAllViews();
            list.clear();
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        mContext = BigVerifyActivity.this;
        bt_hd.setOnClickListener(v -> checkMatch());
        bt_hd.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bt_hd.setBackgroundResource(R.drawable.button_pressed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                bt_hd.setBackgroundResource(R.drawable.blue_button);
            }
            return false;
        });
//        bt_jx.setOnClickListener(v -> {
//            Intent intent = new Intent(BigVerifyActivity.this, SmallVerifyActivity.class);
//            finish();
//            startActivity(intent);
//        });
//
//        bt_jx.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                bt_jx.setBackgroundResource(R.drawable.button_pressed);
//            } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                bt_jx.setBackgroundResource(R.drawable.blue_button);
//            }
//            return false;
//        });

        top_back.setOnClickListener(v -> finish());

        // 入库单号的扫描监听
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
        etCkbm.setText(intent2.getStringExtra("Code0"));//行号
        cInvCode = intent2.getStringExtra("cInvCode");//代号
        cinvname = intent2.getStringExtra("cinvname");//牌号
        cEngineerFigNo = intent2.getStringExtra("cEngineerFigNo");//图号
        cFree9 = intent2.getStringExtra("cFree9");//生产批号
        iQuantity = intent2.getStringExtra("iQuantity");//数量
        iNum = intent2.getStringExtra("iNum");//件数
        cFree2 = intent2.getStringExtra("cFree2");//带材批号
        cFree1 = intent2.getStringExtra("cFree1");//材料编号

    }

    //正则判断字符输入合法性
    public static boolean isLegal(String content) {
        Pattern pattern = Pattern.compile("\\^");
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }

    //转格式方法
    private static <E>  List<E> transferArrayToList(E[] array){
        List<E> transferedList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Arrays.stream(array).forEach(arr -> transferedList.add(arr));
        }
        return transferedList;
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

    //暂存数据
    private void save(){

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
        title = rl_zhtm.findViewById(R.id.list_1_9);
        title.setText(name_zhtm[8]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_zhtm.findViewById(R.id.list_1_10);
        title.setText(name_zhtm[9]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_zhtm.findViewById(R.id.list_1_11);
        title.setText(name_zhtm[10]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_zhtm.findViewById(R.id.list_1_12);
        title.setText(name_zhtm[11]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_zhtm.findViewById(R.id.list_1_13);
        title.setText(name_zhtm[12]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_zhtm.findViewById(R.id.list_1_14);
        title.setText(name_zhtm[13]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        title = rl_zhtm.findViewById(R.id.list_1_15);
        title.setText(name_zhtm[14]);
        title.setTextColor(getResources().getColor(R.color.tabletext_black));
        title.setFocusableInTouchMode(false);
        tb_zhtm.addView(rl_zhtm);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
