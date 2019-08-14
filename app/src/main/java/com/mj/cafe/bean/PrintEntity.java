package com.mj.cafe.bean;

import java.util.List;

public class PrintEntity {


    /**
     * meal_code : 0015
     * meal_pwd : 9482
     * create_time : 2019-07-31 21:47:47
     * license_number : 282-34-00719
     * tel : 010-2505-8844
     * store_name : Smart Green Cafe (澳林广场店)
     * address : 大田广域市儒城区Guryongdaljeon路396号1层（屯股洞）
     * contact : 李东载
     * invoice_date : 2019-08-05 11:18:58
     * invoice_code : 19073174585941
     * total_money : 0
     * tax_money : 0.02
     * pay_money : 0.22
     * device_no : 34892343
     * vip_card : 2039485445
     * integral : 200
     */

    private String meal_code;
    private String meal_pwd;
    private String create_time;
    private String license_number;
    private String tel;
    private String store_name;
    private String address;
    private String contact;
    private String invoice_date;
    private String invoice_code;
    private String total_money;
    private String tax_money;
    private String pay_money;
    private String device_no;
    private String vip_card;
    private String integral;

    private String card_number;
    private String card_company;
    private String stage_month;
    private String approval_number;

    private List<PrintGoodsEntity> goodsList;

    public String getMeal_code() {
        return meal_code;
    }

    public void setMeal_code(String meal_code) {
        this.meal_code = meal_code;
    }

    public String getMeal_pwd() {
        return meal_pwd;
    }

    public void setMeal_pwd(String meal_pwd) {
        this.meal_pwd = meal_pwd;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getInvoice_code() {
        return invoice_code;
    }

    public void setInvoice_code(String invoice_code) {
        this.invoice_code = invoice_code;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getTax_money() {
        return tax_money;
    }

    public void setTax_money(String tax_money) {
        this.tax_money = tax_money;
    }

    public String getPay_money() {
        return pay_money;
    }

    public void setPay_money(String pay_money) {
        this.pay_money = pay_money;
    }

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }

    public String getVip_card() {
        return vip_card;
    }

    public void setVip_card(String vip_card) {
        this.vip_card = vip_card;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public List<PrintGoodsEntity> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<PrintGoodsEntity> goodsList) {
        this.goodsList = goodsList;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getCard_company() {
        return card_company;
    }

    public void setCard_company(String card_company) {
        this.card_company = card_company;
    }

    public String getStage_month() {
        return stage_month;
    }

    public void setStage_month(String stage_month) {
        this.stage_month = stage_month;
    }

    public String getApproval_number() {
        return approval_number;
    }

    public void setApproval_number(String approval_number) {
        this.approval_number = approval_number;
    }
}
