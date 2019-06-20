package com.mj.cafe.retorfit.exception;

/**
 * 描述：
 * Created by Lee
 * on 2016/10/13.
 */
public class ApiException extends RuntimeException {
    /*错误码*/
    private int code;
    /*显示的信息*/
    private String displayMessage;

    public ApiException(Throwable e) {
        super(e);
    }
    public ApiException(String detailMessage) {
        super(detailMessage);
        this.displayMessage = detailMessage;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }
}
