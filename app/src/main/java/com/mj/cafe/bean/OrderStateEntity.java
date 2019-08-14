package com.mj.cafe.bean;

public class OrderStateEntity {

    /**
     * id : 16
     * order_code : 19080544286420
     * state : 1
     * record_type_text : 支付成功
     * 2确认可制作（即下单成功）
     * 5确认不可制作
     * 1支付成功（还没有确认完）
     */

    private Long id;
    private String order_code;
    private Long state;
    private String record_type_text;

    public String getMeal_code() {
        return meal_code;
    }

    public void setMeal_code(String meal_code) {
        this.meal_code = meal_code;
    }

    private String meal_code;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public String getRecord_type_text() {
        return record_type_text;
    }

    public void setRecord_type_text(String record_type_text) {
        this.record_type_text = record_type_text;
    }
}
