package com.mj.cafe.bean;

//语言类型
public class LangTypeBean {
    public static final String KEY_LANG_OBSERVE = "key_lang_observe";
    public static final String CN_HTTP = "cn";
    public static final String EN_HTTP = "en";
    public static final String KO_HTTP = "ko";
    //中文
    public static final int DEFAULT = 0;
    public static final int CN = 0;
    public static final int EN = 1;
    public static final int KO = 2;

    private String userHttpType;
    private int type;

    public LangTypeBean(int type) {
        this.type = type;
    }

    public LangTypeBean() {
    }

    public String getUserHttpType() {
        if(type == 0){
            return CN_HTTP;
        }if(type == 1){
            return EN_HTTP;
        }if(type == 2){
            return KO_HTTP;
        }
        return CN_HTTP;
    }

    public void setUserHttpType(String userHttpType) {
        this.userHttpType = userHttpType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
