package com.sunhongbin.noiseDetect.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.sunhongbin.noiseDetect.Entity.TaskRecord;
import com.sunhongbin.noiseDetect.Entity.Urls;
import com.sunhongbin.noiseDetect.R;
import com.sunhongbin.noiseDetect.Utils.MsgToastUtils;
import com.sunhongbin.noiseDetect.service.DoUpload;
import com.sunhongbin.noiseDetect.service.HttpDataResponse;
import com.sunhongbin.noiseDetect.service.KeepLogin;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SunHongbin on 2018/11/19
 * 任务发布者发布任务
 */
public class PublishTaskActivity extends BaseActivity implements View.OnClickListener {

    private TaskRecord taskRecord = new TaskRecord();
    private TimePickerView pvTime;
    private int flag;
    private int year, month, day, hour, minute;

    private String url = Urls.t460Purl + "/task/publish";
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @BindView(R.id.input_task_description_id)
    EditText taskDescription;
    @BindView(R.id.input_task_location_id)
    EditText taskLocation;
    @BindView(R.id.start_time_text_id)
    TextView startTimeText;
    @BindView(R.id.end_time_text_id)
    TextView endTimeText;
    @BindView(R.id.date_create)
    TextView date_view;
    @BindView(R.id.title_toolbar)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.start_time_id)
    Button startTimePicker;
    @BindView(R.id.end_time_id)
    Button endTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_task);

        ButterKnife.bind(this);

        init();
    }


    private void init() {

        title.setText("发布新任务");

        initTimePicker();        //初始化时间选择器
        init_view();
        init_toolbar();
    }

    /**
     * 视图初始化
     */
    private void init_view() {

        date_view.setText(getDetailDate());

        startTimePicker.setOnClickListener(this::onClick);
        endTimePicker.setOnClickListener(this::onClick);

        Button btn_red = (Button) findViewById(R.id.btn_red);
        btn_red.setOnClickListener(this);
        Button btn_orange = (Button) findViewById(R.id.btn_orange);
        btn_orange.setOnClickListener(this);
        Button btn_green = (Button) findViewById(R.id.btn_green);
        btn_green.setOnClickListener(this);
    }

    private void init_toolbar() {
        toolbar.inflateMenu(R.menu.menu_publish);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                publishTask();
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start_time_id && pvTime != null) {
            flag = 1;
            pvTime.show(v);//弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
        } else if (v.getId() == R.id.end_time_id && pvTime != null) {
            flag = 2;
            pvTime.show(v);
        }
    }


    private void initTimePicker() {//Dialog 模式下，在底部弹出

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (flag == 1) {//开始时间选择
                    String sTime = showTime(date);
                    Date now = new Date();
                    int compareToNow = now.compareTo(date);
                    switch (compareToNow) {
                        case 0://=可以
                            startTimeText.setText(sTime);
                            taskRecord.setTaskStartTime(sTime);
                            break;
                        case 1://start>end
                            MsgToastUtils.showMessageOnScreen(PublishTaskActivity.this, "任务开始日期错误");
                            break;
                        case -1://start<end
                            startTimeText.setText(sTime);
                            taskRecord.setTaskEndTime(sTime);
                            break;
                    }
                    taskRecord.setTaskStartTime(sTime);
                } else if (flag == 2) {//结束时间选择
                    String eTime = showTime(date);
                    String getSTime = startTimeText.getText().toString();
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
                                MsgToastUtils.showMessageOnScreen(PublishTaskActivity.this, "请延长任务执行时间");
                                break;
                            case 1://start>end
                                MsgToastUtils.showMessageOnScreen(PublishTaskActivity.this, "截止日期错误");
                                break;
                            case -1:
                                endTimeText.setText(eTime);
                                taskRecord.setTaskEndTime(eTime);
                                break;//start<end
                        }
                    } else {
                        MsgToastUtils.showMessageOnScreen(PublishTaskActivity.this, "先设置起始时间");
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
                .setType(new boolean[]{true, true, true, true, true, true})
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

    public void publishTask() {
        if (!validate()) {
            return;
        }
        String description = taskDescription.getText().toString();
        String location = taskLocation.getText().toString();
        taskRecord.setTaskDescription(description);
        taskRecord.setTaskLocation(location);
        String userPhone = KeepLogin.getParam(this, "loginData", "loginData").toString();
        taskRecord.setUserPhone(Long.valueOf(userPhone));
        DoUpload doUpload = new DoUpload();
        doUpload.doUpload_TaskMessage(taskRecord, url, new HttpDataResponse() {
            @Override
            public void onDataResponse(String data, String phoneNumber) {

            }

            @Override
            public void onNoReceiveDataResponse(IOException e) {

            }
        });
        MsgToastUtils.showMessageOnScreen(PublishTaskActivity.this, "已发布");
        Log.i("sun", "publishTask: " + taskRecord.toString());
        finish();//成功上传后关闭当前页面
    }


    public boolean validate() {
        boolean validate = true;
        String description = taskDescription.getText().toString();
        String location = taskLocation.getText().toString();

        if (TextUtils.isEmpty(description)) {
            taskDescription.setError("任务描述不能为空");
            validate = false;
        } else {
            taskDescription.setError(null);
        }
        if (description.length() > 200) {
            taskDescription.setError("任务描述过长，请精炼");
            validate = false;
        } else {
            taskDescription.setError(null);
        }
        if (TextUtils.isEmpty(location)) {
            taskLocation.setError("任务执行地点不能为空");
            validate = false;
        } else {
            taskLocation.setError(null);
        }
        return validate;
    }


    public String getDetailDate() {

        Calendar cal = Calendar.getInstance();

        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);

        StringBuilder sb = new StringBuilder();
        DecimalFormat format = new DecimalFormat("00");
        sb.append(this.year + "年" + format.format(this.month) + "月" + format.format(this.day) + "日");
        sb.append(" " + format.format(this.hour) + ":" + format.format(this.minute));
        return sb.toString();
    }

}
