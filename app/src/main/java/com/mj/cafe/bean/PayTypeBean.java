package com.mj.cafe.bean;

public class PayTypeBean {

    /**
     * id : 1
     * pay_name : Kakao Pay
     * logo :
     */

    private int id;
    private String pay_name;
    private String logo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPay_name() {
        return pay_name;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
