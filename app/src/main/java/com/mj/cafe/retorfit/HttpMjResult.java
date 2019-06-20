package com.mj.cafe.retorfit;


/**
 * 描述：Entity - 网络请求统一返回格式
 * Created by Lee
 * on 2016/10/13.
 */
public class HttpMjResult<T> {

    /**
     * code : 0
     * msg : 成功
     */

    private Integer code;
    private String msg;
    private T data;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
