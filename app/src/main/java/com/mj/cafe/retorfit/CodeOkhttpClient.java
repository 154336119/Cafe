package com.mj.cafe.retorfit;



import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 描述：
 * Created by Lee
 * on 2016/9/19.
 */
public class CodeOkhttpClient {
    private  static OkHttpClient.Builder mClientBuilder;
    private HttpLoggingInterceptor mHttpLoggingInterceptor;
    private static CodeOkhttpClient mInstance;
    /**
     * 单例
     * @return
     */
    public static synchronized CodeOkhttpClient getInstance(){
        if(mInstance==null){
            mInstance = new CodeOkhttpClient();
        }
        return mInstance;
    }

    public static OkHttpClient.Builder getClientBuilder() {
        if(mInstance==null){
            mInstance = new CodeOkhttpClient();
        }
        String str = "dsad";
        return mClientBuilder;
    }

    public CodeOkhttpClient() {
        initInterceptor();
        mClientBuilder = new OkHttpClient.Builder();
        mClientBuilder.connectTimeout(60, TimeUnit.SECONDS);
        mClientBuilder.readTimeout(60, TimeUnit.SECONDS);
        mClientBuilder.writeTimeout(60, TimeUnit.SECONDS);
        mClientBuilder. addInterceptor(mHttpLoggingInterceptor);
    }
    /**
     * 初始化拦截器
     */
    private void initInterceptor(){
        mHttpLoggingInterceptor = new HttpLoggingInterceptor();
        mHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY
        );
    }


}
