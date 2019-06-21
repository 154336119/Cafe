package com.mj.cafe.bean;

public class SeatBean {

    /**
     * id : 1
     * store_id : 1
     * seat_no : A
     * is_delete : 0
     * is_occupy : 0
     * coordinate_x : 1
     * coordinate_y : 1
     */

    private Integer id;
    private Integer store_id;
    private String seat_no;
    private Integer is_delete;
    private Integer is_occupy;
    private Integer coordinate_x;
    private Integer coordinate_y;

    private Boolean isEmpty = false; //是否为空数据

    public Boolean getEmpty() {
        return isEmpty;
    }

    public void setEmpty(Boolean empty) {
        isEmpty = empty;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStore_id() {
        return store_id;
    }

    public void setStore_id(Integer store_id) {
        this.store_id = store_id;
    }

    public String getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(String seat_no) {
        this.seat_no = seat_no;
    }

    public Integer getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(Integer is_delete) {
        this.is_delete = is_delete;
    }

    public Integer getIs_occupy() {
        return is_occupy;
    }

    public void setIs_occupy(Integer is_occupy) {
        this.is_occupy = is_occupy;
    }

    public Integer getCoordinate_x() {
        return coordinate_x;
    }

    public void setCoordinate_x(Integer coordinate_x) {
        this.coordinate_x = coordinate_x;
    }

    public Integer getCoordinate_y() {
        return coordinate_y;
    }

    public void setCoordinate_y(Integer coordinate_y) {
        this.coordinate_y = coordinate_y;
    }
}
