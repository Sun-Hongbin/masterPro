package com.sunhongbin.noiseDetect.service;

import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.sunhongbin.noiseDetect.Entity.User;
import com.sunhongbin.noiseDetect.Entity.Value;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.sunhongbin.noiseDetect.service.FileUtil.REC_PATH;

/**
 * Created by SunHongbin on 2018/10/20
 */
public class DoUpload {

    OkHttpClient okHttpClient = new OkHttpClient();    //1、拿到okHttpClient对象

    Gson gson = new Gson();    //1.1 使用gson，用json传数据

    //传db数据和录音文件
    public void doUpload_Db_File(String url, Map<String, String> map,
                                 HttpDataResponse httpDataResponse) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        File file = new File(REC_PATH + "temp.amr");
        if (!file.exists()) {
            System.out.println("文件不存在！");
            return;
        }
        RequestBody fileBody = RequestBody.create(MediaType.parse("octet-stream"), file);//audio/mpeg
        RequestBody requestBody = builder
                .setType(MultipartBody.FORM)
                .addFormDataPart("audioFile", file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()  //3、构造request
                .url(url)
                .post(requestBody)
                .build();
        executeRequest(request, httpDataResponse);
    }

    //只传db数据
    public void doUpload_Db(String url, Value value, HttpDataResponse httpDataResponse) {

        String json = gson.toJson(value);//2、构造Request Body,格式为json
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), json);

        Request.Builder builder = new Request.Builder();//3、构造request
        Request request = builder
                .url(url)
                .post(body)
                .build();
        executeRequest(request, httpDataResponse);
    }

    //登陆时传用户信息
    public void doUpload_Login(String url, HttpDataResponse httpDataResponse) {
        Request.Builder builder = new Request.Builder();
        Request request = builder.get()
                .url(url)
                .build();
        executeRequest(request, httpDataResponse);
    }

    //注册时传用户信息
    public void doUpload_Register(User user, String url, HttpDataResponse httpDataResponse) {
        String json = gson.toJson(user);                     //2、构造Request Body,格式为json
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), json);
        Request.Builder builder = new Request.Builder();     //3、构造request
        Request request = builder
                .url(url)
                .post(body)
                .build();
        executeRequest(request, httpDataResponse);
    }

    public void executeRequest(Request request, HttpDataResponse httpDataResponse) {

        //4、将Request封装为call
        Call call = okHttpClient.newCall(request);
        Log.d("request-call", "execute: " + call.toString());

        //5、执行call，同步回调execute，异步callback
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e("onFailure: " + e.getMessage());
                e.printStackTrace();
                httpDataResponse.onNoReceiveDataResponse(e);
                //TODO: Internet 404 resolve
                //TODO: E/sunhongbin_okHttp: onFailure: expected 1463 bytes but received 1559
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                LogUtils.e("onResponse: " + res);
                httpDataResponse.onDataResponse(res, null);
            }

        });
    }

}
