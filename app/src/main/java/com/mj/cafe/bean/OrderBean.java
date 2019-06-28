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

    public BigDecimal getBigDecimalPrice(int price) {
            return AmountUtils.fen2Yuan(price);
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
        String priceStr = String.valueOf(getBigDecimalPrice(integralMoney));
        SpannableString spanString = new SpannableString("₩"+priceStr);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(ViewUtils.sp2px(MyApp.getInstance(), 45));
        spanString.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spanString;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.integralMoney);
        dest.writeInt(this.goodsMoney);
        dest.writeInt(this.orderId);
        dest.writeString(this.qrcode);
        dest.writeInt(this.couponMoney);
        dest.writeInt(this.pay_money);
    }

    public OrderBean() {
    }

    protected OrderBean(Parcel in) {
        this.integralMoney = in.readInt();
        this.goodsMoney = in.readInt();
        this.orderId = in.readInt();
        this.qrcode = in.readString();
        this.couponMoney = in.readInt();
        this.pay_money = in.readInt();
    }

    public static final Parcelable.Creator<OrderBean> CREATOR = new Parcelable.Creator<OrderBean>() {
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
