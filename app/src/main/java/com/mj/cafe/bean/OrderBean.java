package com.mj.cafe.bean;

public class OrderBean {

    /**
     * integralMoney : 0
     * goodsMoney : 9400
     * orderId : 4
     * qrcode : http://qr.topscan.com/api.php?text=prepare_url=131231231
     * couponMoney : 0
     * pay_money : 9400
     */

    private int integralMoney;
    private int goodsMoney;
    private int orderId;
    private String qrcode;
    private int couponMoney;
    private int pay_money;

    public int getIntegralMoney() {
        return integralMoney;
    }

    public void setIntegralMoney(int integralMoney) {
        this.integralMoney = integralMoney;
    }

    public int getGoodsMoney() {
        return goodsMoney;
    }

    public void setGoodsMoney(int goodsMoney) {
        this.goodsMoney = goodsMoney;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public int getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(int couponMoney) {
        this.couponMoney = couponMoney;
    }

    public int getPay_money() {
        return pay_money;
    }

    public void setPay_money(int pay_money) {
        this.pay_money = pay_money;
    }
}
