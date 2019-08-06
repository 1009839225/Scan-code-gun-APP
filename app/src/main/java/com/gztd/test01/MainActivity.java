package com.gztd.test01;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gztd.inventory.InventoryActivity;
import com.gztd.verify.BigVerifyActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private TextView other_label;
    private TextView list_label;
    private TextView vouchar_label;
    private ViewPager viewPager;
    private List<View> views = new ArrayList<View>();
    private View list;
    private View vouchar;
    private View other;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private Lists listClass;
    private Vouchar voucharClass;
    private other otherClass;
    private FrameLayout o_fl_wdyw;
    private FrameLayout o_fl_wdyw1;
    private FrameLayout l_fl_wdyw;

    private FrameLayout v_fl_smhd;
    private FrameLayout v_fl_smfh;
    private FrameLayout v_fl_smpd;
    private Button bt_logout;
    private Context context;
    private EditText a_ed_name;
    private EditText a_ed_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        // mysqlite=new MySQLiteOpenHelper(this,"CREATE_BOOK",null,1);
        init();

    }

    public void init() {
        CurrentEvent event = new CurrentEvent();
        list_label = findViewById(R.id.list_label);
        vouchar_label = findViewById(R.id.vouchar_label);
        other_label = findViewById(R.id.other_label);

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);

        list_label.setOnClickListener(event);
        vouchar_label.setOnClickListener(event);
        other_label.setOnClickListener(event);

        viewPager = findViewById(R.id.external_viewPage);
        LayoutInflater inflater = LayoutInflater.from(this);
        list = inflater.inflate(R.layout.list, null);
        vouchar = inflater.inflate(R.layout.vouchar_main, null);
        other = inflater.inflate(R.layout.activity_logout, null);
        views.add(list);
        views.add(vouchar);
        views.add(other);

        viewPager.setAdapter(event);
        viewPager.setOnPageChangeListener(event);

        vouchar();

    }

    /**
     * 切换到列表界面
     */
    public void list() {
        viewPager.setCurrentItem(0);
//        list_label.setTextColor(Color.YELLOW);
        imageView1.setImageDrawable(getResources().getDrawable(
                R.mipmap.tab_message_pressed));

        imageView2.setImageDrawable(getResources().getDrawable(
                R.mipmap.tab_my_normal));
        // ue(Color.GR
        imageView3.setImageDrawable(getResources().getDrawable(
                R.mipmap.tab_better_normal));
        // list_label.setBackgroundColor(Color.GRAY);
        // list_line.setBackgroundColor(Color.GREEN);
        vouchar_label.setTextColor(Color.BLACK);
        // vouchar_label.setBackg/roundColor(Color.WHITE);
        // vouchar_line.setBackgroundColor(Color.WHITE);
        other_label.setTextColor(Color.BLACK);
        // other_label.setBackgroundColor(Color.WHITE);
        // other_line.setBackgroundColor(Color.WHITE);
        if (listClass == null) {
            listClass = new Lists();
            // //
        }

    }

    class Lists implements OnClickListener {

        public Lists() {
            l_fl_wdyw = list.findViewById(R.id.l_fl_wdyw);
            l_fl_wdyw.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.l_fl_wdyw:
                    // sao();
                    break;

                default:
                    break;
            }
        }

    }

    /**
     * 切换到单据界面
     */
    public void vouchar() {
        viewPager.setCurrentItem(1);
//        vouchar_label.setTextColor(Color.YELLOW);
        imageView1.setImageDrawable(getResources().getDrawable(
                R.mipmap.tab_message_normal));
        imageView2.setImageDrawable(getResources().getDrawable(
                R.mipmap.tab_my_pressed));
        // ue(Color.GR
        imageView3.setImageDrawable(getResources().getDrawable(
                R.mipmap.tab_better_normal));

        // vouchar_line.setBackgroundColor(Color.GREEN);
        list_label.setTextColor(Color.BLACK);

        // list_label.setBackgroundColor(Color.WHITE);
        // list_line.setBackgroundColor(Color.WHITE);
        other_label.setTextColor(Color.BLACK);
        // other_label.setBackgroundColor(Color.WHITE);
        // other_line.setBackgroundColor(Color.WHITE);

        if (voucharClass == null) {
            voucharClass = new Vouchar();
        }
    }

    /**
     * 切换到其他界面
     */
    public void other() {
        viewPager.setCurrentItem(2);
//        other_label.setTextColor(Color.YELLOW);
        imageView3.setImageDrawable(getResources().getDrawable(
                R.mipmap.tab_better_pressed));
        // other_label.setBackgroundColor(Color.GRAY);
        // other_line.setBackgroundColor(Color.GREEN);
        imageView1.setImageDrawable(getResources().getDrawable(
                R.mipmap.tab_message_normal));

        imageView2.setImageDrawable(getResources().getDrawable(
                R.mipmap.tab_my_normal));
        // ue(Color.GR

        vouchar_label.setTextColor(Color.BLACK);
        // vouchar_label.setBackgroundColor(Color.WHITE);
        // vouchar_line.setBackgroundColor(Color.WHITE);
        list_label.setTextColor(Color.BLACK);

        // list_label.setBackgroundColor(Color.WHITE);
        // list_line.setBackgroundColor(Color.WHITE);
        // other_label.setTextColor(Color.BLACK);
        // other_label.setBackgroundColor(Color.WHITE);
        // list_line.setBackgroundColor(Color.WHITE);
        if (otherClass == null) {
            otherClass = new other();
        }
    }

    class other implements OnClickListener {

        @SuppressLint("ClickableViewAccessibility")
        public other() {
            o_fl_wdyw = findViewById(R.id.o_fl_wdyw);
            o_fl_wdyw1 = findViewById(R.id.o_fl_wdyw1);
            bt_logout = findViewById(R.id.bt_login);
            a_ed_name = findViewById(R.id.a_ed_name);
            a_ed_pw = findViewById(R.id.a_ed_pw);
            SharedPreferences sp = context.getSharedPreferences("mysp1",
                    Context.MODE_PRIVATE);
            String strpasswd = sp.getString("strpasswd", "");
            String strname = sp.getString("strname", "");
            a_ed_name.setText(strname);
            a_ed_pw.setText(strpasswd);
            bt_logout.setOnClickListener(this);
            o_fl_wdyw.setOnClickListener(this);
            o_fl_wdyw1.setOnClickListener(this);
            bt_logout.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bt_logout.setBackgroundResource(R.drawable.button_pressed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    bt_logout.setBackgroundResource(R.drawable.blue_button);
                }
                return false;
            });

        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.o_fl_wdyw:
                    yw();
                    break;

                case R.id.o_fl_wdyw1:
                    yw1();
                    break;
                case R.id.bt_login:
                    home();
                    break;
                default:
                    break;
            }

        }

        private void yw() {
            // TODO Auto-generated method stub
            Intent int1 = new Intent(MainActivity.this, DeployActivity.class);
            startActivityForResult(int1, 0);
        }

        private void yw1() {
            // TODO Auto-generated method stub
            // Intent int1=new Intent(SecondActivity.this,ThreeActivity.class);
            // startActivityForResult(int1, 0);
        }
    }

    class CurrentEvent extends PagerAdapter implements OnClickListener,
            OnPageChangeListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.list_label:

                    list();
                    break;
                case R.id.vouchar_label:
                    vouchar();
                    break;
                case R.id.other_label:
                    other();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageSelected(int index) {
            switch (index) {
                case 0:
                    list();
                    break;
                case 1:
                    vouchar();
                    break;
                case 2:
                    other();
                    break;
                default:
                    break;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    // voucher页面
    class Vouchar implements OnClickListener {

        public Vouchar() {
            v_fl_smhd = vouchar.findViewById(R.id.v_fl_smhd);
            v_fl_smfh = vouchar.findViewById(R.id.v_fl_smfh);
            v_fl_smpd = vouchar.findViewById(R.id.v_fl_smpd);
            v_fl_smhd.setOnClickListener(this);
            v_fl_smfh.setOnClickListener(this);
            v_fl_smpd.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.v_fl_smhd:
                    smhd();
                    break;
                case R.id.v_fl_smfh:
                    smfh();
                    break;
                case R.id.v_fl_smpd:
                    smpd();
                    break;
                default:
                    break;
            }
        }

        private void smhd() {
            Intent intent2 = new Intent(MainActivity.this, BigVerifyActivity.class);
            finish();
            startActivityForResult(intent2, 0);
        }

        private void smfh() {
            Intent intent2 = new Intent(MainActivity.this, com.gztd.deliver.BigVerifyActivity.class);
            finish();
            startActivityForResult(intent2, 0);
        }

        private void smpd() {
            Intent intent2 = new Intent(MainActivity.this, InventoryActivity.class);
            finish();
            startActivityForResult(intent2, 0);
        }
    }

    public void home() {
        Intent startMain = new Intent(context, SplashActivity.class);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        System.exit(0);

    }
    // 第一次按下返回键的事件
    private long firstPressedTime;

    // System.currentTimeMillis() 当前系统的时间
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime < 2000) {
            super.onBackPressed();
        } else {
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            firstPressedTime = System.currentTimeMillis();
        }
    }
}
