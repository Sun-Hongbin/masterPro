package me.daei.soundmeter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.daei.soundmeter.OkHttpUtils.DoUpload;
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
        mStopButton = findViewById(R.id.doUpload);
        mStopButton.setOnClickListener(this::Onclick);
        mStartButton = findViewById(R.id.start);
        mStartButton.setOnClickListener(this::Onclick);
        mRecorder = new MyMediaRecorder();

    }

    public void Onclick(View view) {
        if (view.getId() == R.id.start) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            Toast.makeText(this, "开始采集噪声…耗费时长3s...", Toast.LENGTH_SHORT).show();
            isCreateFile = true;
            //获取系统时间作为文件名
            startTime = System.currentTimeMillis();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("-MM-dd-HH-mm-ss");
            Date date = new Date(startTime);
            fileName = simpleDateFormat.format(date) + ".txt";
        }
        if (view.getId() == R.id.doUpload) {
            if (value.getUploadDbValue() != 0) {
                uploadData();//上传数据
            }
            Toast.makeText(this, "上传数据", Toast.LENGTH_SHORT).show();
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
//        mRecorder.delete(); //停止记录并删除录音文件
        handler.removeMessages(msgWhat);
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(msgWhat);
//        mRecorder.delete();
        super.onDestroy();
    }

    public String getFileName() {
        return fileName;
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
        DoUpload doUpload = new DoUpload();
        doUpload.doUploadData(value);//上传
        System.out.println("执行uploadData方法");
        value.setUploadDbValue(0);
    }

}
