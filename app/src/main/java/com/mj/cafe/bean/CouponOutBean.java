package com.mj.cafe.bean;

import java.util.List;

public class CouponOutBean {

    /**
     * id : 2
     * prefix_no : 86
     * mobile : 15208305795
     * pin : 123456
     * integral : 0.0
     * now_uid :
     * create_time : 2019-06-25 14:23:19
     * coupons : [{"id":1,"name":"500韩元无门槛优惠券"},{"id":2,"name":"300韩元无门槛优惠券"},{"id":3,"name":"500韩元无门槛优惠券"},{"id":4,"name":"300韩元无门槛优惠券"},{"id":6,"name":"100韩元无门槛优惠券"}]
     */

    private int id;
    private int prefix_no;
    private String mobile;
    private String pin;
    private double integral;
    private String now_uid;
    private String create_time;
    private List<CouponBean> coupons;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrefix_no() {
        return prefix_no;
    }

    public void setPrefix_no(int prefix_no) {
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

    public double getIntegral() {
        return integral;
    }

    public void setIntegral(double integral) {
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

    public List<CouponBean> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponBean> coupons) {
        this.coupons = coupons;
    }
}
