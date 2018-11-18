package com.sunhongbin.noiseDetect.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.sunhongbin.noiseDetect.Entity.Urls;
import com.sunhongbin.noiseDetect.Entity.Value;
import com.sunhongbin.noiseDetect.service.FileUtil;
import com.sunhongbin.noiseDetect.service.HttpDataResponse;
import com.sunhongbin.noiseDetect.service.LocationService;
import com.sunhongbin.noiseDetect.service.MyMediaRecorder;
import com.sunhongbin.noiseDetect.R;
import com.sunhongbin.noiseDetect.service.DoUpload;
import com.sunhongbin.noiseDetect.service.KeepLogin;
import com.sunhongbin.noiseDetect.widget.SoundDiscView;

public class MainActivity extends AppCompatActivity {

    private static String dbUrl = Urls.aliyunUrl + "/tools/db";

    float volume = 10000;
    private SoundDiscView soundDiscView;
    private MyMediaRecorder mRecorder;
    private static final int msgWhat = 0x1001;
    private static final int refreshTime = 100;
    private static final int DATA_UPLOAD_SUCESS = 0;
    private static final int DATA_UPLOAD_FAILURE = 1;
    private static final String TAG = "GpsActivity";
    private Button uploadButton, mStartButton, startLocation;
    private TextView LocationResult;
    private boolean isLocating, isLogin, isStartCollect, isSubmit = false;
    private boolean mShowRequestPermission = true;//用户是否禁用权限
    private Long startTime, collectTime, submitTime;
    private Value value = new Value();
    private int sumOfDb, count, btClkCount = 0;//分贝值总和,采集分贝个数
    private LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //判断各种权限
        init_permission();

        //1、button
        uploadButton = findViewById(R.id.doUpload);
        uploadButton.setOnClickListener(this::Onclick);
        mStartButton = findViewById(R.id.start);
        mStartButton.setOnClickListener(this::Onclick);
        startLocation = findViewById(R.id.locate);
        startLocation.setOnClickListener(this::Onclick);
        LocationResult = findViewById(R.id.textView1);
        LocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());

        //2、recorder
        mRecorder = new MyMediaRecorder();

        //判断是否登陆
        isLogin = (boolean) KeepLogin.getParam(this, KeepLogin.IS_LOGIN, false);
        if (!isLogin) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }

    public void Onclick(View view) {
        if (view.getId() == R.id.start) {
            switch (btClkCount) {
                case 0:
                    isStartCollect = true;
                    startTime = System.currentTimeMillis();
                    value.setCollectTime(startTime);
                    mStartButton.setBackground(getResources().getDrawable(R.drawable.record_pause, null));
                    Toast.makeText(this, "开始采集噪声...", Toast.LENGTH_SHORT).show();
                    btClkCount = 1;//显示暂停图标
                    break;
                case 1:
                    isStartCollect = false;
                    value.setAvgOfDb(sumOfDb / count);
                    value.setUploadDbValue(value.getAvgOfDb());
                    collectTime = (System.currentTimeMillis() - startTime) / 1000;//更新初创建时间
                    Log.i("sun", "平均值： " + value.getAvgOfDb() + " 和：" + sumOfDb + " 个数： " + count);
                    //记录完一次后，初始化值
                    value.setAvgOfDb(0);
                    sumOfDb = 0;
                    count = 1;
                    btClkCount = 0;//显示启动图标
                    mStartButton.setBackground(getResources().getDrawable(R.drawable.record_start, null));
                    break;
            }
        }
        if (view.getId() == R.id.doUpload) {
            if (isSubmit) {
                //不是第一次提交
                if (value.getUploadDbValue() != null) {
                    Log.i("sun", "" + value.getUploadDbValue() + " " + value.getLatitude());
                    uploadData();//上传数据
                } else {
                    Toast.makeText(this, "请采集最新数据", Toast.LENGTH_SHORT).show();
                }
            } else {
                //第一次提交
                if (value.getUploadDbValue() != null) {
                    Log.i("sun", "" + value.getUploadDbValue() + " " + value.getLatitude());
                    uploadData();//上传数据
                } else {
                    Toast.makeText(this, "采集音频数据后提交", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (view.getId() == R.id.locate) {
            if (isLocating) {
                locationService.stop();
                isLocating = false;
                logMsg("点击左侧图标可开启地图定位\n\n点击中间图标开始采集数据\n\n点击右侧图标上传噪声数据");
            } else {
                locationService.start();
                isLocating = true;
                value.setFreshLctTime(System.currentTimeMillis());
            }

        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case msgWhat: {
                    volume = mRecorder.getMaxAmplitude();  //获取声压值
                    if (volume > 0 && volume < 1000000) {
                        Value.setDbCount(20 * (float) (Math.log10(volume)));  //将声压值转为分贝值
                        //进行写文件操作
                        if (isStartCollect == true) {
                            int dbValue = (int) Value.getDbCount();
                            long interval = System.currentTimeMillis() - startTime;
                            count++;
                            sumOfDb += dbValue;
                            Log.i("sun", String.valueOf(dbValue) + " interval: " + String.valueOf(interval) +
                                    " 当前为第" + count + "个数据" + " sumOfDb和为" + sumOfDb + "\n");
                        }
                        soundDiscView.refresh();
                    }
                    handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
                    break;
                }
                case DATA_UPLOAD_SUCESS:
                    Toast.makeText(MainActivity.this, "分贝值：" + value.getUploadDbValue().toString() +
                            "\n音频时长：" + collectTime + "s\n上传成功，谢谢参与！",
                            Toast.LENGTH_SHORT).show();
                    value.setUploadDbValue(null);
                    isSubmit = true;
                    break;
                case DATA_UPLOAD_FAILURE:
                    Toast.makeText(MainActivity.this, "OnFailure: 请检查网络",
                            Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    private void startListenAudio() {
        handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
    }

    /**
     * 开始记录
     *
     * @param fFile
     */
    public void startRecord(File fFile) {
        try {
            mRecorder.setMyRecAudioFile(fFile);
            if (mRecorder.startRecorder()) {
                startListenAudio();
            } else {
                Toast.makeText(this, "启动录音失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "录音机已被占用或录音权限被禁止", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {//protected void onResume()在 Activity 从 Pause 状态转换到 Active 状态时被调用。
        super.onResume();
        soundDiscView = findViewById(R.id.soundDiscView);
        File file = FileUtil.createTempFile("temp.amr");
        if (file != null) {
            startRecord(file);
        } else {
            Toast.makeText(getApplicationContext(), "创建文件失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 停止记录
     */
    @Override
    protected void onPause() {
        super.onPause();
        mRecorder.delete(); //停止记录并删除录音文件
        handler.removeMessages(msgWhat);
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(msgWhat);
        mRecorder.delete();
        super.onDestroy();
    }

    public void uploadData() {

        if (value.getUploadDbValue() == null) {
            Toast.makeText(this, "麦克风不工作，请重新启动应用", Toast.LENGTH_LONG).show();
            return;
        }
        if (System.currentTimeMillis() - value.getFreshLctTime() > 300000) {
            Toast.makeText(this, "距离上次位置刷新已超过30s，请重新定位", Toast.LENGTH_LONG).show();
            return;
        }
//        Map map = new HashMap();
//        map.put("longitude", ""+value.getLongitude());
//        map.put("latitude", ""+value.getLatitude());
//        map.put("db", value.getUploadDbValue().toString());
//        map.put("userPhone", KeepLogin.getParam(this, "loginData", "loginData"));
        value.setLongitude(value.getLongitude());
        value.setLatitude(value.getLatitude());
        value.setCollectTime(startTime);
        String userPhone = KeepLogin.getParam(this, "loginData", "loginData").toString();
        value.setUserPhone(userPhone);
        Log.i("sun", value.getLatitude() + " " + value.getLongitude() + " " + value.getUploadDbValue() +
                " 电话：" + userPhone);
        DoUpload doUpload = new DoUpload();
        doUpload.doUpload_Db(dbUrl, value, new HttpDataResponse() {
            @Override
            public void onDataResponse(String data, String phoneNumber) {
                handler.sendEmptyMessage(DATA_UPLOAD_SUCESS);
            }

            @Override
            public void onNoReceiveDataResponse(IOException e) {
                handler.sendEmptyMessage(DATA_UPLOAD_FAILURE);
            }
        });

    }


    private void init_permission() {
        if (getSdkVersionSix()) {
            String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_NETWORK_STATE};
            List<String> mPermissionList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }
            if (mPermissionList.isEmpty()) {// 全部允许
                mShowRequestPermission = true;
            } else {//存在未允许的权限
                String[] permissionsArr = mPermissionList.toArray(new String[mPermissionList.size()]);
                requestPermissions(permissionsArr, 101);
            }
        }
    }

    public boolean getSdkVersionSix() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    //权限提示框 ，如果点击禁止且不勾选禁止访问，则无线循环弹出申请框
    //如果全部允许则正常操作。如果全部禁止且禁止访问，点击登录按钮时再校验一遍，
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选禁止后不再询问
                        boolean showRequestPermission = shouldShowRequestPermissionRationale(permissions[i]);
                        if (showRequestPermission) {
                            init_permission();
                            return;
                        } else { // false 被禁止了，不在访问
                            mShowRequestPermission = false;//已经禁止了
                        }
                    }
                }
                break;
        }
    }


    /**
     * 显示请求字符串
     *
     * @param str
     */
    public void logMsg(String str) {
        final String s = str;
        try {
            if (LocationResult != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LocationResult.post(new Runnable() {
                            @Override
                            public void run() {
                                LocationResult.setText(s);

                            }
                        });

                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // -----------location config ------------
        locationService = ((LocationApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }


    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {

                StringBuffer sb = new StringBuffer(256);
                /*sb.append("time : ");
                 *//**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 *//*
                sb.append(location.getTime());*/
                sb.append("\n定位类型：");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\n经度：");// 经度
                sb.append(location.getLongitude());
                value.setLongitude(location.getLongitude());
                sb.append("   纬度：");// 纬度
                sb.append(location.getLatitude());
                value.setLatitude(location.getLatitude());
                sb.append("\n半径：");// 半径
                sb.append(location.getRadius());
                sb.append(location.getStreet());
                sb.append("\n地址信息：");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\n用户室内外判断结果：");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
//                sb.append("\n方向(not all devices have value): ");
//                sb.append(location.getDirection());// 方向
//                sb.append("\n位置语义化信息：");
//                sb.append(location.getLocationDescribe());// 位置语义化信息
//                sb.append("\nPOI信息：");// POI信息
//                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
//                    for (int i = 0; i < location.getPoiList().size(); i++) {
//                        Poi poi = (Poi) location.getPoiList().get(i);
//                        sb.append(poi.getName() + ";");
//                    }
//                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\n速度 单位：km/h: ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\n卫星数目：");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\n海拔高度 单位：米：");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps质量判断：");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
//                    if (location.hasAltitude()) {// *****如果有海拔高度*****
//                        sb.append("\nheight : ");
//                        sb.append(location.getAltitude());// 单位：米
//                    }
//                    sb.append("\n运营商信息：");// 运营商信息
//                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的，但建议使用4G网络");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因：1、请检查GPS是否开启 2、关闭飞行模式");
                }
                logMsg(sb.toString());
            }
        }

    };


}
