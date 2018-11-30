package com.sunhongbin.noiseDetect.Utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sunhongbin.noiseDetect.R;

/**
 * Created by SunHongbin on 2018/11/27
 */
public class MsgToastUtils {

    public static void showMessageOnScreen(Context context, String message){

        TextView mTextView;

        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.msg_toast,null);

        //初始化布局控件
        mTextView = toastRoot.findViewById(R.id.msg_toast);

        //为控件设置属性
        mTextView.setText(message);

        //Toast的初始化
        Toast toastStart = new Toast(context);

        //获取屏幕高度
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        //Toast的Y坐标是屏幕高度一半
        toastStart.setGravity(Gravity.TOP, 0, height/2);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(toastRoot);
        toastStart.show();
    }
}
