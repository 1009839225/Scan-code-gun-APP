package com.gztd.test01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        RelativeLayout layoutSplash = (RelativeLayout) findViewById(R.id.activity_splash);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(1000);//设置动画播放时长1000毫秒（1秒）
        layoutSplash.startAnimation(alphaAnimation);
        //设置动画监听
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            //动画结束
            @Override
            public void onAnimationEnd(Animation animation) {
                //页面的跳转
                Intent intent = new Intent(SplashActivity.this, LoginActiviy.class);
                finish();
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }
}