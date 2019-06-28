package com.mj.cafe.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PayTypeBean implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.pay_name);
        dest.writeString(this.logo);
    }

    public PayTypeBean() {
    }

    protected PayTypeBean(Parcel in) {
        this.id = in.readInt();
        this.pay_name = in.readString();
        this.logo = in.readString();
    }

    public static final Parcelable.Creator<PayTypeBean> CREATOR = new Parcelable.Creator<PayTypeBean>() {
        @Override
        public PayTypeBean createFromParcel(Parcel source) {
            return new PayTypeBean(source);
        }

        @Override
        public PayTypeBean[] newArray(int size) {
            return new PayTypeBean[size];
        }
    };
}
