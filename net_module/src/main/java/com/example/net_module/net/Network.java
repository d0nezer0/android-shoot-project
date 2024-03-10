package com.example.net_module.net;


import com.example.net_module.Common;
import com.example.net_module.Factory;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 功能   ：网络请求的封装
 */

public class Network {

    private static Network instance;
    private Retrofit mRetrofit;
    private OkHttpClient mClient;

    static {
        instance = new Network();
    }

    private Network() {

    }

    public static OkHttpClient getOkHttpClient() {
        if (instance.mClient != null) {
            return instance.mClient;
        }
        //创建一个client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        //得到我们发起请求的request
                        Request request = chain.request();
                        //重新封装builder
                        Request.Builder builder = request.newBuilder();
//                        if (!TextUtils.isEmpty(Account.getToken()))
//                            //注入一个Token
//                            builder.addHeader("token", Account.getToken());
                        builder.addHeader("Content-Type", "application/json");//添加数据格式 不是必须的retrofit2已经帮我们完成了
                        Request newRequest = builder.build();
                        //返回封装Header之后的Request
                        return chain.proceed(newRequest);
                    }
                })
                .build();
        instance.mClient = client;
        return instance.mClient;
    }

    private static Retrofit getRetrofit() {
        if (instance.mRetrofit != null) {
            return instance.mRetrofit;
        }
        getOkHttpClient();
        //Retrofit
        Retrofit.Builder builder = new Retrofit.Builder();
        instance.mRetrofit = builder.baseUrl(Common.API_URL)
                //设置我们自己的Json解析器
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .client(instance.mClient)
                .build();
        return instance.mRetrofit;
    }

    /**
     * 返回一个请求代理
     *
     * @return RemoteService代理
     */
    public static RemoteService remote() {
        return Network.getRetrofit().create(RemoteService.class);
    }
}
