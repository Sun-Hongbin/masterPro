package com.sunhongbin.noiseDetect.activity;

import android.os.Bundle;
import android.widget.ListView;
import com.quinny898.library.persistentsearch.SearchBox;
import com.sunhongbin.noiseDetect.Entity.TaskRecord;
import com.sunhongbin.noiseDetect.R;
import com.sunhongbin.noiseDetect.dataAdapter.HistoryAdapter;
import com.sunhongbin.noiseDetect.dataAdapter.SearchAdapter;
import com.sunhongbin.noiseDetect.service.HttpDataResponse;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by SunHongbin on 2018/11/29
 */
public class SearchActivity extends BaseActivity implements HttpDataResponse {

    private SearchBox searchBox;

    private ArrayList<TaskRecord> list_search, list_result;

    private ListView mSearchResults;
    private SearchAdapter mResultAdapter;

    private ListView mHistory;
    private HistoryAdapter mHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

    }

    @Override
    public void onDataResponse(String data, String phoneNumber) {

    }

    @Override
    public void onNoReceiveDataResponse(IOException e) {

    }
}
