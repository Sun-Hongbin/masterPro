package com.sunhongbin.noiseDetect.dataAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sunhongbin.noiseDetect.Entity.TaskRecord;
import com.sunhongbin.noiseDetect.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by SunHongbin on 2018/11/28
 * 获取任务界面的数据列表适配器
 * <p>
 * DataSource  ==>  Adapter  ==>  ListView, GridView等
 * (ArrayList等)
 */
public class TaskListAdapter extends BaseAdapter {


    private List<TaskRecord> mData;
    private Context mContext;

    public TaskListAdapter(List<TaskRecord> mData, Context context) {
        this.mData = mData;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_task, null);
        }

        TaskRecord taskRecord = ((TaskRecord) getItem(position));

        //一条item显示从左到右
        TextView monthTxt = convertView.findViewById(R.id.item_month);
        TextView dayTxt = convertView.findViewById(R.id.item_day);
        TextView location = convertView.findViewById(R.id.item_location);
        TextView txt = convertView.findViewById(R.id.item_description);
        TextView dateTxt = convertView.findViewById(R.id.item_date);

        String startTime = taskRecord.getTaskStartTime();
        Long ctime = taskRecord.getCtime();
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        
        Date cdate = new Date(ctime);
        String month = sdfMonth.format(cdate);
        String day = sdfDay.format(cdate);
        
        Date sdate = new Date(Long.valueOf(startTime));
        String start = sdf.format(sdate);

        monthTxt.setText(month);
        dayTxt.setText(day);
        location.setText(taskRecord.getTaskLocation());
        txt.setText(taskRecord.getTaskDescription());
        dateTxt.setText("任务起始：" + start);

        return convertView;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
