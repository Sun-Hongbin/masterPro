package me.daei.soundmeter.OkHttpUtils;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import me.daei.soundmeter.Value;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by SunHongbin on 2018/10/20
 */
public class DoUpload {

    private static String mBaseUrl = "http://219.242.247.189:8080";

    public void doUploadData(Value value) {

        //1、拿到okHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        //1.1 使用gson，用json传数据
        Gson gson = new Gson();
        String json = gson.toJson(value);

        //2、构造Request Body,格式为json
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), json);

  /*      FormBody body = new FormBody
                .Builder()
                .add("db","111")
                .build();
        System.out.println("====="+value.getUploadDbValue());*/

        //3、构造request
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(mBaseUrl+"/tools/db")
                .post(body)
                .build();

        //4、将Request封装为call
        Call call = okHttpClient.newCall(request);
        Log.d("request-call", "execute: " + call.toString());

        //5、执行call，同步回调execute，异步callback
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure: " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                L.e("onResponse: " + res);
            }
        });

    }


}
