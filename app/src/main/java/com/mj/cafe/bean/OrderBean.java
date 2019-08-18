package com.mj.cafe.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;

import com.mj.cafe.MyApp;
import com.mj.cafe.utils.AmountUtils;
import com.mj.cafe.utils.ViewUtils;

import java.math.BigDecimal;

public class OrderBean implements Parcelable {

    /**
     * integralMoney : 0
     * goodsMoney : 9400
     * orderId : 4
     * qrcode : http://qr.topscan.com/api.php?text=prepare_url=131231231
     * couponMoney : 0
     * pay_money : 9400
     */

    private String integralMoney;
    private String goodsMoney;
    private int orderId;
    private String qrcode;
    private String couponMoney;
    private String payMoney;

    public String getTaxMoney() {
        return taxMoney;
    }

    public void setTaxMoney(String taxMoney) {
        this.taxMoney = taxMoney;
    }

    private String taxMoney;
    public String getCancelBankInfo() {
        return cancelBankInfo;
    }

    public void setCancelBankInfo(String cancelBankInfo) {
        this.cancelBankInfo = cancelBankInfo;
    }

    //确认号码  +  销售日期  + 销售时间   26位 （52个数字）
    private String cancelBankInfo;




    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    private String orderCode;
    public String getIntegralMoney() {
        return integralMoney;
    }

    public void setIntegralMoney(String integralMoney) {
        this.integralMoney = integralMoney;
    }

    public String getGoodsMoney() {
        return goodsMoney;
    }

    public void setGoodsMoney(String goodsMoney) {
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

    public String getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(String couponMoney) {
        this.couponMoney = couponMoney;
    }


    public BigDecimal getBigDecimalPrice(String price) {
            return AmountUtils.toBigyuan(price);
    }

    public SpannableString getStringIntegralMoney() {
        String priceStr = String.valueOf(getBigDecimalPrice(integralMoney));
        SpannableString spanString = new SpannableString("-₩"+priceStr);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(ViewUtils.sp2px(MyApp.getInstance(), 35));
        spanString.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spanString;
    }

    public SpannableString getStringGoodsMoneyMoney() {
        String priceStr = String.valueOf(getBigDecimalPrice(goodsMoney));
        SpannableString spanString = new SpannableString("₩"+priceStr);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(ViewUtils.sp2px(MyApp.getInstance(), 35));
        spanString.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spanString;
    }

    public SpannableString getStringCouponMoneyMoney() {
        String priceStr = String.valueOf(getBigDecimalPrice(couponMoney));
        SpannableString spanString = new SpannableString("-₩"+priceStr);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(ViewUtils.sp2px(MyApp.getInstance(), 35));
        spanString.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spanString;
    }

    public SpannableString getStringPay_moneyMoney() {
        String priceStr = String.valueOf(getBigDecimalPrice(payMoney));
        SpannableString spanString = new SpannableString("₩"+priceStr);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(ViewUtils.sp2px(MyApp.getInstance(), 45));
        spanString.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spanString;
    }



    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.integralMoney);
        dest.writeString(this.goodsMoney);
        dest.writeInt(this.orderId);
        dest.writeString(this.qrcode);
        dest.writeString(this.couponMoney);
        dest.writeString(this.payMoney);
        dest.writeString(this.taxMoney);
        dest.writeString(this.cancelBankInfo);
        dest.writeString(this.orderCode);
    }

    public OrderBean() {
    }

    protected OrderBean(Parcel in) {
        this.integralMoney = in.readString();
        this.goodsMoney = in.readString();
        this.orderId = in.readInt();
        this.qrcode = in.readString();
        this.couponMoney = in.readString();
        this.payMoney = in.readString();
        this.taxMoney = in.readString();
        this.cancelBankInfo = in.readString();
        this.orderCode = in.readString();
    }

    public static final Creator<OrderBean> CREATOR = new Creator<OrderBean>() {
        @Override
        public OrderBean createFromParcel(Parcel source) {
            return new OrderBean(source);
        }

        @Override
        public OrderBean[] newArray(int size) {
            return new OrderBean[size];
        }
    };
}
