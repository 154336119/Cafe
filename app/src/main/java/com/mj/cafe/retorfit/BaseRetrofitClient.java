package com.mj.cafe.retorfit;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * 描述：
 * Created by Lee
 * on 2016/9/18.
 */
public class BaseRetrofitClient implements Iretrofit {
    private Retrofit mRetrofit;

    @Override
    public void attchBaseUrl(String url, Context context, OkHttpClient.Builder clientBuilder) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(clientBuilder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .build();
    }

    @Override
    public <T> T createService(Class<T> clz) {
        return mRetrofit.create(clz);
    }

}
