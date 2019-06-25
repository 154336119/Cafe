package com.mj.cafe.bean;

public class UserBean {

    /**
     * id : 2
     * prefix_no : 86
     * mobile : 15208305795
     * pin : 123456
     * integral : 0
     * now_uid : 
     * create_time : 2019-06-25 14:23:19
     * token : ef0c2ab0467f1e581d1fe31125d02c018e0d1d76
     */

    private Integer id;
    private Integer prefix_no;
    private String mobile;
    private String pin;
    private Integer integral;
    private String now_uid;
    private String create_time;
    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrefix_no() {
        return prefix_no;
    }

    public void setPrefix_no(Integer prefix_no) {
        this.prefix_no = prefix_no;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public String getNow_uid() {
        return now_uid;
    }

    public void setNow_uid(String now_uid) {
        this.now_uid = now_uid;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
