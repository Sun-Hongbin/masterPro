package me.daei.soundmeter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.daei.soundmeter.okHttpUtils.DoUpload;
import me.daei.soundmeter.widget.SoundDiscView;

/**
 * 首先你要知道Activity的四种状态：
 * Active/Runing 一个新 Activity 启动入栈后，它在屏幕最前端，处于栈的最顶端，此时它处于可见并可和用户交互的激活状态。
 * Paused 当 Activity 被另一个透明或者 Dialog 样式的 Activity 覆盖时的状态。此时它依然与窗口管理器保持连接，系统继续维护其内部状态，所以它仍然可见，但它已经失去了焦点故不可与用户交互。
 * Stoped 当 Activity 被另外一个 Activity 覆盖、失去焦点并不可见时处于 Stoped 状态。
 * Killed Activity 被系统杀死回收或者没有被启动时处于 Killed 状态。
 * protected void onStart() 该方法在 onCreate() 方法之后被调用，或者在 Activity 从 Stop 状态转换为 Active 状态时被调用，一般执行了onStart()后就执行onResume()。
 * protected void onResume() 在 Activity 从 Pause 状态转换到 Active 状态时被调用。
 */
public class MainActivity extends AppCompatActivity {

    float volume = 10000;
    private SoundDiscView soundDiscView;
    private MyMediaRecorder mRecorder;
    private static final int msgWhat = 0x1001;
    private static final int refreshTime = 100;
    private Button mStopButton, mStartButton;
    private boolean mShowRequestPermission = true;//用户是否禁用权限
    private LocationManager locationManager;//获取地理位置
    private String provider;
    private boolean isCreateFile;//是否写入文件
    private Long startTime;
    private String fileName;
    private StringBuffer buffer = new StringBuffer();
    private Value value = new Value();
    private int sumOfDb;//分贝值总和
    private int count;//计算采集分贝个数


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //判断各种权限
        init_permission();

        //1、button
        mStopButton = findViewById(R.id.doUpload);
        mStopButton.setOnClickListener(this::Onclick);
        mStartButton = findViewById(R.id.start);
        mStartButton.setOnClickListener(this::Onclick);

        //2、recorder
        mRecorder = new MyMediaRecorder();

        //初次启动就检查GPS
        getLocation();
    }

    public void Onclick(View view) {
        if (view.getId() == R.id.start) {
            Toast.makeText(this, "开始采集噪声…耗费时长3s...", Toast.LENGTH_SHORT).show();
            isCreateFile = true;
            //获取系统时间作为文件名
            startTime = System.currentTimeMillis();
            value.setCollectTime(startTime);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("-MM-dd-HH-mm-ss");
            Date date = new Date(startTime);
            fileName = simpleDateFormat.format(date) + ".txt";
        }
        if (view.getId() == R.id.doUpload) {
            if (value.getUploadDbValue() != null) {
                uploadData();//上传数据
            }
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.hasMessages(msgWhat)) {
                return;
            }
            volume = mRecorder.getMaxAmplitude();  //获取声压值
            if (volume > 0 && volume < 1000000) {
                Value.setDbCount(20 * (float) (Math.log10(volume)));  //将声压值转为分贝值
                //进行写文件操作
                if (isCreateFile == true) {
                    int dbValue = (int) Value.getDbCount();
                    long interval = System.currentTimeMillis() - startTime;
                    buffer.append(String.valueOf(dbValue) + " ");
                    count++;
                    sumOfDb += dbValue;
                    System.out.println(String.valueOf(dbValue) + " interval: " + String.valueOf(interval) +
                            " 当前为第" + count + "个数据" + " sumOfDb和为" + sumOfDb + "\n");
                    if (interval > 3000) {
                        FileUtil.writeFileSdCard(fileName, buffer);//将buffer值写入txt文件
                        value.setAvgOfDb(sumOfDb / count);
                        System.out.println("平均值： " + value.getAvgOfDb() + " 和：" + sumOfDb + " 个数： " + count);
                        value.setUploadDbValue(value.getAvgOfDb());
                        System.out.println("设置setUploadDbValue：" + value.getUploadDbValue());
                        initValues(value);
                    }
                }
                soundDiscView.refresh();
            }
            handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
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
            Log.v("file", "file =" + file.getAbsolutePath());
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager != null) {
                locationManager.removeUpdates(locationListener);
            }
        }
    }

    //重新初始化值
    public void initValues(Value value) {
        startTime = System.currentTimeMillis();//更新初创建时间
        buffer.setLength(0);//buffer清空
        value.setAvgOfDb(0);
        sumOfDb = 0;
        count = 0;
        isCreateFile = false;
    }

    public void uploadData() {
        if(value.getUploadDbValue() == null){
            Toast.makeText(this, "麦克风不工作啦，请重新启动应用~" ,Toast.LENGTH_LONG).show();
            return;
        }
        getLocation();
        if(value.getLongtitude() == null){
            Toast.makeText(this, "定位失败，请打开网络和GPS后重试！" ,Toast.LENGTH_LONG).show();
            return;
        }
        value.setUploadTime(System.currentTimeMillis());
        DoUpload doUpload = new DoUpload();
        doUpload.doUploadData(value);//上传
        value.setUploadDbValue(null);
        value.setLongtitude(null);
        value.setLatitude(null);
        Toast.makeText(this, "上传成功，感谢您的参与！", Toast.LENGTH_SHORT).show();
    }

    public void getLocation(){

        //3、location register
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //3、1获取所有可用的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            //当没有可用的位置提供器时，弹出Toast提示用户
            Toast.makeText(this, "定位失败，请您将网络和GPS打开…", Toast.LENGTH_LONG).show();
            return;
        }
        //3、3判断是否有权限
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //3.4获取上一次设备位置信息
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                //getLastKnownLocation这个方法用一次往往不能成功，所以在判断不为空的时候反复获取，即加上下面一句话
                locationManager.requestLocationUpdates("gps", 60000, 1, locationListener);
                //获取当前位置，这里只用到经纬度
                System.out.println("上一次记录的经度为：" + location.getLongitude() + " 纬度为：" + location.getLatitude());
                value.setLongtitude(location.getLongitude());
                value.setLatitude(location.getLatitude());
            }
            //绑定定位事件，监听位置是否改变
            //param1：控制器类型  param2：监听位置变化的时间间隔 param3:位置变化间隔(m) param4: 位置监听器
            locationManager.requestLocationUpdates(provider, 1000, 1, locationListener);
        }
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void init_permission() {
        if (getSdkVersionSix()) {
            String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.RECORD_AUDIO};
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

//    点击登录按钮时 ，获取申请权限的结果 并且这里动态申请了“悬浮窗”的权限
// 1. 如何小于6.0 直接登陆
// 2. 申请悬浮窗权限，如果没有，直接跳转到具体页面进行设置，完成后在onActivityResult 中判断是否允许该权限
//    如果允许再次动态申请一次权限，上面如果点击了全部禁止且勾选了禁止访问请调用 getRequestPermission()方法，
//    自动跳转到应用程序列表中让用户手动设置权限
/*
   if (getSdkVersionSix()) {
        if (Settings.canDrawOverlays(this)) {
            if (getRequestPermission().equals("1")) { // 如果等于1则登录
                init_IntentLogIn(getAccount(), getPwd());
            }
        } else {
            Toast.makeText(this, R.string.login_canDrawOverlays,   Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivityForResult(intent, 2);
        }
    } else {
        init_IntentLogIn(getAccount(), getPwd()); // 6.0以下直接登录
    }

//    处理申请的悬浮窗和其他权限
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (Settings.canDrawOverlays(this)) {
                init_permission();
            } else {
                Toast.makeText(this, R.string.login_canDrawOverlays, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 3) {
            init_permission();
        }
    }


    //申请权限后的操作
    public String getRequestPermission() {
        if (mShowRequestPermission) {
            return "1";
        } else {// 被禁止后提示用户必须到设置中授权，跳转到应用程序列表页面
            Toast.makeText(this, R.string.login_permission, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
            startActivityForResult(intent, 3);
            return "-1";
        }
    }*/


}
