package com.sunhongbin.noiseDetect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by SunHongbin on 2018/11/28
 * 任务页面的展示滑动
 */
public class RecordScrollView extends ScrollView {

    private ScrollViewListener scrollViewListener = null;

    //构造方法
    public RecordScrollView(Context context) {
        super(context);
    }

    public RecordScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RecordScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //setter
    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }


    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {

        super.onScrollChanged(x, y, oldx, y);

        if (scrollViewListener != null) {

            //滑动距离超过15像素 控件向上滑
            if (oldx < y && ((y - oldy)) > 15) {
                scrollViewListener.onScroll(y - oldy);

                //向下滑
            } else if (oldy > y && ((oldy - y)) > 15) {
                scrollViewListener.onScroll(y - oldy);
            }
        }
    }

    public  interface ScrollViewListener{//dy Y轴滑动距离
        void onScroll(int dy);
    }
}
