package com.mj.cafe.retorfit;

import java.util.List;

/**
 * 描述：Entity - 服务器返回data里面的内容
 * Created by Lee
 * on 2016/10/13.
 */
public class HttpMjListDataResutl<T> {
    private List<T> list;
    private int total;


    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
