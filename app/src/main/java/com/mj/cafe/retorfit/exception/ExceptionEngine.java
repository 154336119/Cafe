package com.mj.cafe.retorfit.exception;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * 描述：处理异常的驱动器
 * Created by Lee
 * on 2017/1/23.
 */
public class ExceptionEngine {
    private static final String HttpException_MSG = "网络错误";
    private static final String ConnectException_MSG = "连接失败";
    private static final String JSONException_MSG = "Json解析失败";
    private static final String UnknownHostException_MSG = "无法解析该域名";

    public static Exception analysisExcetpion(Throwable e){
        ApiException exception = null;
        if(e instanceof HttpException){
            /*网络异常*/
            exception = new ApiException(HttpException_MSG);
            exception.setCode(CodeException.HTTP_ERROR);
        }else if (e instanceof ConnectException ||e instanceof SocketTimeoutException) {
             /*链接异常*/
            exception = new ApiException(ConnectException_MSG);
            exception.setCode(CodeException.HTTP_ERROR);
        }else if(e instanceof JSONException || e instanceof ParseException){
            /*解析失败*/
            exception = new ApiException(JSONException_MSG);
            exception.setCode(CodeException.JSON_ERROR);
        }else if (e instanceof UnknownHostException){
            /*无法解析该域名异常*/
            exception = new ApiException(UnknownHostException_MSG);
            exception.setCode(CodeException.UNKOWNHOST_ERROR);
        }else if (e instanceof ResultException){
            /*服务器返回错误*/
            ResultException exception1 = (ResultException)e;
            exception = new ApiException(exception1.getmMessage());
            exception.setCode(exception1.getmResultCode());
        }else {
            /*未知异常*/
            exception = new ApiException(e.getMessage());
            exception.setCode(CodeException.UNKNOWN_ERROR);
        }
        return exception;
    }
}
