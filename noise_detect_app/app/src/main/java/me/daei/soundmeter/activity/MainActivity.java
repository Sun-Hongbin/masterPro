package me.daei.soundmeter.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.daei.soundmeter.Entity.Urls;
import me.daei.soundmeter.Entity.Value;
import me.daei.soundmeter.FileUtil;
import me.daei.soundmeter.MyMediaRecorder;
import me.daei.soundmeter.R;
import me.daei.soundmeter.service.DoUpload;
import me.daei.soundmeter.service.KeepLogin;
import me.daei.soundmeter.widget.SoundDiscView;

public class MainActivity extends AppCompatActivity {

    private static String dbUrl = Urls.localUrl + "/tools/db";

    float volume = 10000;
    private SoundDiscView soundDiscView;
    private MyMediaRecorder mRecorder;
    private static final int msgWhat = 0x1001;
    private static final int refreshTime = 100;
    private static final String TAG = "GpsActivity";
    private Button mStopButton, mStartButton;
    private boolean mShowRequestPermission = true;//用户是否禁用权限
    private LocationManager locationManager;//获取地理位置
    private String provider;
    private boolean isCreateFile;//是否写入文件
    private boolean isLogin = false;//是否已经登陆
    private Long startTime;
//    private StringBuffer buffer = new StringBuffer();
    private Value value = new Value();
    private int sumOfDb;//分贝值总和
    private int count;//计算采集分贝个数
    public LocationListener locationListener;

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

        //判断是否登陆
        isLogin = (boolean) KeepLogin.getParam(this, KeepLogin.IS_LOGIN, false);
        if (!isLogin) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        //3、初次启动就检查GPS
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
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
        getLocation();
    }

    public void Onclick(View view) {
        if (view.getId() == R.id.start) {
            Toast.makeText(this, "开始采集噪声…耗费时长3s...", Toast.LENGTH_SHORT).show();
            isCreateFile = true;
            //获取系统时间作为文件名
            startTime = System.currentTimeMillis();
            value.setCollectTime(startTime);
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
//                    buffer.append(String.valueOf(dbValue) + " ");
                    count++;
                    sumOfDb += dbValue;
                    System.out.println(String.valueOf(dbValue) + " interval: " + String.valueOf(interval) +
                            " 当前为第" + count + "个数据" + " sumOfDb和为" + sumOfDb + "\n");
                    if (interval > 3000) {
//                        FileUtil.writeFileSdCard(fileName, buffer);//将buffer值写入txt文件
                        value.setAvgOfDb(sumOfDb / count);
                        System.out.println("平均值： " + value.getAvgOfDb() + " 和：" + sumOfDb + " 个数： " + count);
                        value.setUploadDbValue(value.getAvgOfDb());
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
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //当GPS定位时，在这里注册requestLocationUpdates监听就非常重要而且必要。没有这句话，定位不能成功。
            locationManager.requestLocationUpdates(provider, 1000, 1, locationListener);
        }
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
        if(locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
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
//        buffer.setLength(0);//buffer清空
        value.setAvgOfDb(0);
        sumOfDb = 0;
        count = 0;
        isCreateFile = false;
    }

    public void uploadData() {
        if (value.getUploadDbValue() == null) {
            Toast.makeText(this, "麦克风不工作啦，请重新启动应用", Toast.LENGTH_LONG).show();
            return;
        }
        getLocation();
        if (value.getLongtitude() == null) {
            Toast.makeText(this, "获取位置失败，请检查网络配置", Toast.LENGTH_LONG).show();
            return;
        }
        Map map = new HashMap();
        map.put("longtitude", value.getLongtitude().toString());
        map.put("latitude", value.getLatitude().toString());
        map.put("db", value.getUploadDbValue().toString());
        map.put("userPhone", KeepLogin.getParam(this, "loginData","loginData"));
        DoUpload doUpload = new DoUpload();
        doUpload.doUpload_Db(dbUrl, map, null);//上传
        Toast.makeText(this, "上传成功，感谢您的参与！分贝大小为：" +
                value.getUploadDbValue().toString(), Toast.LENGTH_SHORT).show();
        value.setUploadDbValue(null);
    }

    public void getLocation() {

        //3、1获取所有可用的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            System.out.println("获取到GPS： " + provider);
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
            System.out.println("获取到4G网络： " + provider);
        } else {
            //当没有可用的位置提供器时，弹出Toast提示用户
            Toast.makeText(this, "请打开网络和GPS", Toast.LENGTH_LONG).show();
            return;
        }
        //3、3判断是否有权限
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //3.4获取上一次设备位置信息
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                System.out.println("==>>> location != null");
                //getLastKnownLocation这个方法用一次往往不能成功，所以在判断不为空的时候反复获取，即加上下面一句话
                locationManager.requestLocationUpdates(provider, 6000, 1, locationListener);
                //获取当前位置，这里只用到经纬度
                System.out.println("上一次记录的经度为：" + location.getLongitude() + " 纬度为：" + location.getLatitude());
                value.setLongtitude(location.getLongitude());
                value.setLatitude(location.getLatitude());
            }
            //绑定定位事件，监听位置是否改变
            //param1：控制器类型  param2：监听位置变化的时间间隔 param3:位置变化间隔(m) param4: 位置监听器
            locationManager.requestLocationUpdates(provider, 1000, 1, locationListener);
            System.out.println("done!!!!!!!!!");
        }
    }



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

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            KeepLogin.setParam(MainActivity.this, KeepLogin.IS_LOGIN, false);
            KeepLogin.removeParam(MainActivity.this, KeepLogin.LOGIN_DATA);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
