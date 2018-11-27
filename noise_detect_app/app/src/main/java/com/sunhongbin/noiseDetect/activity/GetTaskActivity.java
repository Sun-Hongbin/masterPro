package com.sunhongbin.noiseDetect.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.sunhongbin.noiseDetect.Entity.Urls;
import com.sunhongbin.noiseDetect.R;
import com.sunhongbin.noiseDetect.service.DoUpload;

/**
 * Created by SunHongbin on 2018/11/19
 * 刷任务
 */
public class GetTaskActivity extends BaseActivity {

    private TextView taskMessage;

    private String getAllTaskUrl = Urls.t460Purl + "/task/list";
    private String publishTaskUrl = Urls.t460Purl + "/task/publish";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_task);
        getAllTask();
    }

    public void getAllTask(){
        DoUpload doUpload = new DoUpload();
        doUpload.getTaskMessage(getAllTaskUrl);
    }
}
