package com.mj.cafe.retorfit;


import com.mj.cafe.bean.SeatBean;
import com.mj.cafe.bean.TypeBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ComService {
    /**
     * 食物列表
     */
    @FormUrlEncoded
    @POST("/app/goods/category/list"  )
    Observable<HttpMjResult<List<TypeBean>>> getGoods(@Field("lang") String lang,@Field("storeId") Integer storeId);

    /**
     * 座位列表
     */
    @FormUrlEncoded
    @POST("/app/goods/seat/list"  )
    Observable<HttpMjResult<List<SeatBean>>> getSeatList(@Field("storeId") Integer storeId);

//    /**
//     * 首页 热门商品
//     */
//    @FormUrlEncoded
//    @POST("/app/product/hotList" )
//    Observable<HttpMjListResult<String>> getHotGoods(@Field("pageSize") int pageSize,
//                                                    @Field("pageIndex") int pageNum);

}
