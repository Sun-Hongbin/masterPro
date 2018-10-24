package me.daei.soundmeter.service;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import me.daei.soundmeter.Entity.Urls;
import me.daei.soundmeter.Entity.User;
import me.daei.soundmeter.Entity.Value;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by SunHongbin on 2018/10/20
 */
public class DoUpload {

    OkHttpClient okHttpClient = new OkHttpClient();    //1、拿到okHttpClient对象

    Gson gson = new Gson();    //1.1 使用gson，用json传数据

    //传db数据
    public void doUpload_Db(Value value, String url, HttpDataResponse httpDataResponse) {

        String json = gson.toJson(value);        //2、构造Request Body,格式为json
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), json);

        Request.Builder builder = new Request.Builder();        //3、构造request
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
                L.e("onFailure: " + e.getMessage());
                e.printStackTrace();
                httpDataResponse.onNoReceiveDataResponse(e);
                //TODO:Internet 404 resolve

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                L.e("onResponse: " + res);
                httpDataResponse.onDataResponse(res, null);
            }

        });
    }

}
