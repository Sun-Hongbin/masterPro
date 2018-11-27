package com.sunhongbin.noiseDetect.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.sunhongbin.noiseDetect.R;
import com.sunhongbin.noiseDetect.service.KeepLogin;

/**
 * Created by SunHongbin on 2018/11/19
 * 欢迎界面
 */
public class WelcomeActivity extends AppCompatActivity {

    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    /**
     * 初始化
     */

    //当计时结束,跳转至主界面
    private void init() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
                checkHavePassWord();
            }
        }, 2000);
    }

    //判断是否登陆
    private void checkHavePassWord() {
        isLogin = (boolean) KeepLogin.getParam(this, KeepLogin.IS_LOGIN, false);
        if (!isLogin) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
    }
}
