package com.sunhongbin.noiseDetect.dto;

/**
 * Created by SunHongbin on 2018/10/23
 */

//所有的ajax请求类型，封装json请求的各种Entity类
public class UploadEntity<T> {

    //请求是否成功
    private boolean success;

    private T data;

    //请求是否失败
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
