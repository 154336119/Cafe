package com.mj.cafe.retorfit.rxjava;


import com.mj.cafe.retorfit.HttpMjListDataResutl;
import com.mj.cafe.retorfit.HttpMjListResult;
import com.mj.cafe.retorfit.ResultException;

import rx.functions.Func1;

/**
 * 描述：服务器统一请求返回bean-预处理
 * 魅匠网络专属entity
 * Created by Lee
 * on 2016/10/13.
 */
public class HttpMjListEntityFun<T> implements Func1<HttpMjListResult<T>, HttpMjListDataResutl<T>> {
    @Override
    public HttpMjListDataResutl<T> call(HttpMjListResult<T> taHttpResult) {
        if(taHttpResult.getCode() != 200){
           throw new ResultException(taHttpResult.getCode(),taHttpResult.getMsg());
        }
        return taHttpResult.getData();
    }
}
