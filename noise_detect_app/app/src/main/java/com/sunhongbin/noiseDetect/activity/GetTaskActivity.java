package com.sunhongbin.noiseDetect.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.sunhongbin.noiseDetect.Entity.TaskRecord;
import com.sunhongbin.noiseDetect.Entity.Urls;
import com.sunhongbin.noiseDetect.R;
import com.sunhongbin.noiseDetect.Utils.MsgToastUtils;
import com.sunhongbin.noiseDetect.dataAdapter.TaskListAdapter;
import com.sunhongbin.noiseDetect.service.DoUpload;
import com.sunhongbin.noiseDetect.service.HttpDataResponse;
import com.sunhongbin.noiseDetect.view.MainCreator;
import com.sunhongbin.noiseDetect.view.RecordScrollView;
import com.sunhongbin.noiseDetect.view.SwipeListView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SunHongbin on 2018/11/19
 * 刷任务
 */
public class GetTaskActivity extends BaseActivity implements HttpDataResponse {

    private String getAllTaskUrl = Urls.t460Purl + "/task/list";

    private List<TaskRecord> mData;
    private TaskRecord[] taskRecords;

    @BindView(R.id.rl_empty)
    RelativeLayout empty;
    @BindView(R.id.text_empty)
    TextView info;
    @BindView(R.id.list_view)
    SwipeListView mListView;
    @BindView(R.id.record_scroll_view)
    RecordScrollView scrollView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.get_task_title_toolbar)
    TextView title;


    //handler处理UI更新，否则onResponse会闪退
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
                        mListView.setVisibility(View.GONE);
                        empty.setVisibility(View.VISIBLE);
                        info.setText(R.string.record_empty_info);
                    } else {
                        taskRecords = new Gson().fromJson((String) msg.obj, TaskRecord[].class);
                        mData = Arrays.asList(taskRecords);
//                        for(TaskRecord record:mData){
//                            Log.i("sun", "遍历元素: "+record);
//                        }
                        TaskListAdapter adapter = new TaskListAdapter(mData, GetTaskActivity.this);
                        mListView.setAdapter(adapter);

                        mListView.setVisibility(View.VISIBLE);
                        empty.setVisibility(View.GONE);
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_task);
        ButterKnife.bind(this);
        actionbarSet();
        listView_setting();
    }

    public void listView_setting() {

        MainCreator mainCreator = new MainCreator(GetTaskActivity.this);
        mListView.setMenuCreator(mainCreator);//设置item的左滑功能
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        getAllTask();
        view_listener();
    }

    public void getAllTask() {
        DoUpload doUpload = new DoUpload();
        doUpload.getTaskMessage(getAllTaskUrl, this);
    }

    public void view_listener() {
        //点击监听
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MsgToastUtils.showMessageOnScreen(GetTaskActivity.this, "打开文本");
            }
        });
    }

    public void actionbarSet() {
        title.setText("当前任务列表");
        toolbar.inflateMenu(R.menu.menu_search);//设置右上角的搜索
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //不能用if判断语句
                switch (item.getItemId()){
                    case R.id.search_item:
                        Intent intent = new Intent(GetTaskActivity.this,SearchActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                        break;
                }
                return false;
            }
        });
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





































