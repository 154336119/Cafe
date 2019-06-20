package com.mj.cafe.retorfit;


/**
 * 描述：与服务器约定的相关异常处理
 * Created by Lee
 * on 2016/10/13.
 */
public class ResultException extends RuntimeException {
    private int mResultCode;
    private String mMessage;
    //状态码，0：成功；999：未知错误；1000：需要登录；1001：资源不存在；1002：非法参数；1003：服务端未授权；2000：强制更新;
    /** 未知错误*/
    public static final int API_EXCEPTION_UNKNOWN = 999;
    /** 需要登录*/
    public static final int API_EXCEPTION_NEED_LOGIN = 500;
    /** 资源不存在*/
    public static final int API_EXCEPTION_RESOURCE_NO_EXIST = 1001;
    /** 非法参数*/
    public static final int API_EXCEPTION_ILLEGE_PARARM = 1002;
    /** 服务端未授权*/
    public static final int API_EXCEPTION_UNAUTHORIZED = 1003;
    /** 强制更新*/
    public static final int API_EXCEPTION_FORCIBLY_UPDATE = 2000;

    public ResultException(int resultCode) {
        this(getResultException(resultCode,null));
        mResultCode = resultCode;
        mMessage = getResultException(resultCode,null);
    }
    public ResultException(int resultCode, String msg) {
        this(getResultException(resultCode,msg));
        mResultCode = resultCode;
        mMessage = getResultException(resultCode,null);
    }
    public ResultException(String detailMessage) {
        super(detailMessage);
    }
    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     * @param code
     * @return
     */
    private static String getResultException(int code, String msg){
        String message = msg;
        switch (code) {
            case API_EXCEPTION_NEED_LOGIN:
//                RxBus.getInstance().post(new ResponseExceptionEventArgs(code,msg));
                break;
            default:
                message = msg;

        }
        return message;
    }

    public int getmResultCode() {
        return mResultCode;
    }

    public String getmMessage() {
        return mMessage;
    }
}
