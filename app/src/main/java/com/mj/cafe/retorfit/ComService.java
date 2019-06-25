package com.mj.cafe.retorfit;


import com.mj.cafe.bean.CouponBean;
import com.mj.cafe.bean.OrderBean;
import com.mj.cafe.bean.PayTypeBean;
import com.mj.cafe.bean.SeatBean;
import com.mj.cafe.bean.TypeBean;
import com.mj.cafe.bean.UserBean;

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

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("/app/user/register"  )
    Observable<HttpMjResult<UserBean>> register(@Field("lang") String lang, @Field("prefixNo") Integer prefixNo
                                                , @Field("mobile") String mobile, @Field("pin") String pin);

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("/app/user/login"  )
    Observable<HttpMjResult<UserBean>> login(@Field("lang") String lang,@Field("prefixNo") Integer prefixNo
                                 ,@Field("mobile") String mobile,@Field("pin") String pin);

    /**
     * 支付类型列表
     */
    @FormUrlEncoded
    @POST("/app/order/paytype"  )
    Observable<HttpMjResult<List<PayTypeBean>>> getPaytypeList(@Field("storeId") Integer storeId);

    /**
     * 优惠券列表
     */
    @FormUrlEncoded
    @POST("/app/user/points/coupons"  )
    Observable<HttpMjResult<List<CouponBean>>> getCouponList(@Field("lang") String lang,@Field("token") String token);

    /**
     * 创建订单
     */
    @FormUrlEncoded
    @POST("/app/order/create"  )
    Observable<HttpMjResult<OrderBean>> orderCreate(@Field("lang") String lang, @Field("token") String token
                                                    ,@Field("storeId") Integer storeId, @Field("seatArray") String seatArray
                                                    ,@Field("enjoyway") Integer enjoyway, @Field("integral") Integer integral
                                                    ,@Field("couponId") Integer couponId, @Field("payType") Integer payType
                                                      ,@Field("goodsArray") String goodsArray);


//    /**
//     * 首页 热门商品
//     */
//    @FormUrlEncoded
//    @POST("/app/product/hotList" )
//    Observable<HttpMjListResult<String>> getHotGoods(@Field("pageSize") int pageSize,
//                                                    @Field("pageIndex") int pageNum);

}
