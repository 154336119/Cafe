package com.mj.cafe.retorfit;


import com.mj.cafe.bean.CouponBean;
import com.mj.cafe.bean.CouponOutBean;
import com.mj.cafe.bean.OrderBean;
import com.mj.cafe.bean.OrderStateEntity;
import com.mj.cafe.bean.PayTypeBean;
import com.mj.cafe.bean.PrintEntity;
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
    Observable<HttpMjResult<CouponOutBean>> getCouponList(@Field("lang") String lang, @Field("token") String token);

    /**
     * 创建订单
     */
    @FormUrlEncoded
    @POST("/app/order/create"  )
    Observable<HttpMjResult<OrderBean>> orderCreate(@Field("lang") String lang, @Field("token") String token
                                                    ,@Field("storeId") Integer storeId, @Field("seatId") String seatId
                                                    ,@Field("enjoyway") Integer enjoyway, @Field("integral") Integer integral
                                                    ,@Field("couponId") Integer couponId, @Field("payType") Integer payType
                                                      ,@Field("goodsArray") String goodsArray);

    /**
     * 支付状态 轮询
     * 查询订单支付状态,返回状态码为200即支付成功，其他状态码都表示没完成支付
     */
    @FormUrlEncoded
    @POST("/app/order/query/payStatus"  )
    Observable<HttpMjResult<Object>> getPayStatus(@Field("orderCode") String orderCode);
//    /**
//     * 首页 热门商品
//     */
//    @FormUrlEncoded
//    @POST("/app/product/hotList" )
//    Observable<HttpMjListResult<String>> getHotGoods(@Field("pageSize") int pageSize,
//                                                    @Field("pageIndex") int pageNum);


    /**
     * 打印小票相关数据
     */
    @FormUrlEncoded
    @POST("/app/order/query/print"  )
    Observable<HttpMjResult<PrintEntity>> getPrintData(@Field("orderCode") String orderCode ,@Field("lang") String lang);

    /**
     * 订单状态 轮询
     * 查询订单支付状态,返回状态码为200即支付成功，其他状态码都表示没完成支付
     */
    @FormUrlEncoded
    @POST("/app/order/query/orderStatus"  )
    Observable<HttpMjResult<OrderStateEntity>> getOrderStatus(@Field("orderCode") String orderCode);

}
