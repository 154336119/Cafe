package com.mj.cafe.retorfit;

import android.content.Context;

import okhttp3.OkHttpClient;

/**
 * 描述：retrofit协议
 * Created by Lee
 * on 2016/9/18.
 */
public interface Iretrofit {
    void attchBaseUrl(String url, Context context, OkHttpClient.Builder clientBuilder);
    <T>T createService(Class<T> clz);
}
