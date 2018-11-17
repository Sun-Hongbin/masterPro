package com.sunhongbin.noiseDetect.service;

import java.io.IOException;

/**
 * Created by SunHongbin on 2018/10/23
 */
public interface HttpDataResponse {

    void onDataResponse(String data, String phoneNumber);

    void onNoReceiveDataResponse(IOException e);
}
