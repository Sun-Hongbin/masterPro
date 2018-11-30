package com.sunhongbin.noiseDetect.dataAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.sunhongbin.noiseDetect.Entity.TaskRecord;

import java.util.List;

/**
 * Created by SunHongbin on 2018/11/29
 */
public class SearchAdapter extends ArrayAdapter<TaskRecord> {

    public SearchAdapter(@NonNull Context context, int resource, @NonNull List<TaskRecord> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
