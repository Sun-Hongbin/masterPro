package com.sunhongbin.noiseDetect.dataAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.sunhongbin.noiseDetect.Entity.TaskRecord;

/**
 * Created by SunHongbin on 2018/11/29
 */
public class HistoryAdapter extends ArrayAdapter<TaskRecord>{

    public HistoryAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
