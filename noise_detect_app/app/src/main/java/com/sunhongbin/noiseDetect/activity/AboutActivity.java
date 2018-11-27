package com.sunhongbin.noiseDetect.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sunhongbin.noiseDetect.Dialog.ChooseDialog;
import com.sunhongbin.noiseDetect.Dialog.MyOnClickListener;
import com.sunhongbin.noiseDetect.R;
import com.sunhongbin.noiseDetect.Utils.MsgToastUtils;
import com.sunhongbin.noiseDetect.Utils.ShareUtil;

/**
 * 关于界面
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        init_toolbar();
        init_table();
    }

    /**
     * toolbar初始化
     */
    private void init_toolbar(){
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);


        mToolbar.setNavigationIcon(R.drawable.pic_back);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });
    }

    /**
     * 初始化列表
     */
    private void init_table(){

        //share
        findViewById(R.id.t1_table).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //"我在使用Mcs Share你也来试试吧"
                ShareUtil.shareText(AboutActivity.this,
                        AboutActivity.this.getResources().getString(R.string.shareApp));
            }
        });

        //history
        findViewById(R.id.t2_table).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, UpdateHistoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        //contact me
        findViewById(R.id.content_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCopyContent();
            }
        });
        //contact me
        findViewById(R.id.info_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCopyContent();
            }
        });
    }

    /**
     * 选择复制e-mail或者git-hub到剪贴板
     */
    private void chooseCopyContent(){

        //多选对话框
        final ChooseDialog dialog = new ChooseDialog(this);

        dialog.show();
        dialog.setTitle("请选择");
        dialog.setInfo("复制到剪贴板");


        dialog.setChoose1("e-mail");
        dialog.setListener_1(new MyOnClickListener() {
            @Override
            public void onClick() {
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                assert manager != null;
                manager.setPrimaryClip(ClipData.newPlainText(null,"420498251@qq.com"));
                if(manager.hasPrimaryClip()){
                    manager.getPrimaryClip().getItemAt(0).getText();
                }
                MsgToastUtils.showMessageOnScreen(AboutActivity.this,"复制e-mail成功!");

            }
        });

        dialog.setChoose2("源码地址");
        dialog.setListener_2(new MyOnClickListener() {
            @Override
            public void onClick() {
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                assert manager != null;
                manager.setPrimaryClip(ClipData.newPlainText(null,"https://github.com/Sun-Hongbin/masterPro"));
                if(manager.hasPrimaryClip()){
                    manager.getPrimaryClip().getItemAt(0).getText();
                }
                MsgToastUtils.showMessageOnScreen(AboutActivity.this,"复制源码地址成功!");
            }
        });


        dialog.setChoose3("取消");
    }

}
