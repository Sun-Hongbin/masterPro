package com.sunhongbin.noiseDetect.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Gradient;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.google.gson.Gson;
import com.sunhongbin.noiseDetect.Entity.NoiseMessage;
import com.sunhongbin.noiseDetect.Entity.Urls;
import com.sunhongbin.noiseDetect.R;
import com.sunhongbin.noiseDetect.Utils.MsgToastUtils;
import com.sunhongbin.noiseDetect.service.DoUpload;
import com.sunhongbin.noiseDetect.service.HttpDataResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SunHongbin on 2018/11/30
 */
public class HeatMapActivity extends AppCompatActivity implements View.OnClickListener, HttpDataResponse {

    private String url = Urls.t460Purl + "/tools/map";

    private Long startTime, endTime;

    private List<NoiseMessage> noiseMessageList;

    private NoiseMessage[] noiseArray;

    private TimePickerView pvTime;

    private int flag;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int key = msg.what;
            switch (key) {
                case 0:
                    break;
                case 1:
                    if ((msg.obj).equals("select no results")) {
                        MsgToastUtils.showMessageOnScreen(HeatMapActivity.this, "无噪声数据");
                    } else {
                        noiseArray = new Gson().fromJson((String) msg.obj, NoiseMessage[].class);
                        noiseMessageList = Arrays.asList(noiseArray);
                        for (NoiseMessage data : noiseMessageList) {
                            double lat = data.getLatitude();
                            double lng = data.getLongitude();
                            LatLng ll = new LatLng(lat, lng);
                            randomList.add(ll);
                        }

//        Random r = new Random();
//        for (int i = 0; i < 500; i++) {
//            // 116.220000,39.780000 116.570000,40.150000
//            int rlat = r.nextInt(370000);
//            int rlng = r.nextInt(370000);
//            int lat = 39780000 + rlat;
//            int lng = 116220000 + rlng;
//            LatLng ll = new LatLng(lat / 1E6, lng / 1E6);
//            randomList.add(ll);
//        }
                        buildMap(randomList);
                    }
                    break;
            }
        }
    };

    //百度地图API使用
    private BaiduMap mBaiduMap;
    //1、设置颜色变化
    //设置渐变颜色值
    int[] DEFAULT_GRADIENT_COLORS = {Color.rgb(102, 225, 0), Color.rgb(255, 0, 0)};
    //设置渐变颜色起始值
    float[] DEFAULT_GRADIENT_START_POINTS = {0.2f, 1f};
    //构造颜色渐变对象
    Gradient gradient = new Gradient(DEFAULT_GRADIENT_COLORS, DEFAULT_GRADIENT_START_POINTS);

    //2、准备数据
    List<LatLng> randomList = new ArrayList<>();

    @BindView(R.id.map_search)
    Button formMapButton;
    @BindView(R.id.map_search_stime_id)
    Button mSearchStartTime;
    @BindView(R.id.map_search_etime_id)
    Button mSearchEndTime;
    @BindView(R.id.bMapView)
    MapView mapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_heatmap);

        ButterKnife.bind(this);

        mSearchStartTime.setOnClickListener(this::onClick);
        mSearchEndTime.setOnClickListener(this::onClick);
        formMapButton.setOnClickListener(this::onClick);

        mBaiduMap = mapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
        LatLng bjtu = new LatLng(39.9575557139, 116.3498688946);//以学校为中心
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(bjtu));


        initTimePicker();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_search:
                formMap();
                break;
            case R.id.map_search_stime_id:
                flag = 1;
                pvTime.show();
                break;
            case R.id.map_search_etime_id:
                flag = 2;
                pvTime.show();
                break;
        }
    }


    public void formMap() {
        if (startTime == null || endTime == null) {
            MsgToastUtils.showMessageOnScreen(HeatMapActivity.this, "请先选择时间范围");
            return;
        }
        DoUpload doUpload = new DoUpload();
        doUpload.get_HeatMapData(url + "?startTime=" + startTime + "&endTime=" + endTime + "&taskId=", this);

    }

    public void buildMap(List<LatLng> dbData) {
        HeatMap heatMap = new HeatMap.Builder()
                .data(dbData)
                .gradient(gradient)
                .build();
        mBaiduMap.addHeatMap(heatMap);
    }


    private void initTimePicker() {//Dialog 模式下，在底部弹出

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (flag == 1) {//开始时间选择
                    String sTime = showTime(date);
                    mSearchStartTime.setText(sTime);
                    startTime = date.getTime();
                } else if (flag == 2) {//截止时间选择
                    String eTime = showTime(date);
                    String getSTime = mSearchStartTime.getText().toString();
                    if (!TextUtils.isEmpty(getSTime)) {
                        Date date1 = null;
                        Date date2 = null;
                        try {
                            date1 = format.parse(getSTime);
                            date2 = format.parse(eTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int compareTo = date1.compareTo(date2);
                        switch (compareTo) {
                            case 0://=
                                MsgToastUtils.showMessageOnScreen(HeatMapActivity.this, "请延长任务执行时间");
                                break;
                            case 1://start > end
                                MsgToastUtils.showMessageOnScreen(HeatMapActivity.this, "截止日期错误");
                                break;
                            case -1:
                                mSearchEndTime.setText(eTime);
                                endTime = date.getTime();
                                break;//start<end
                        }
                    } else {
                        MsgToastUtils.showMessageOnScreen(HeatMapActivity.this, "先设置起始时间");
                    }
                }
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("sun", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{
                        true, true, true, true, true, true
                })
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
    }


    private String showTime(Date date) {//可根据需要自行截取数据显示
        Log.d("sun", "choice date millis: " + date.getTime());
        return format.format(date);
    }

    @Override
    public void onDataResponse(String data, String phoneNumber) {
        Message message = handler.obtainMessage();
        message.what = 1;
        message.obj = data;
        handler.sendMessage(message);
    }

    @Override
    public void onNoReceiveDataResponse(IOException e) {

    }
}
