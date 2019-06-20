package com.mj.cafe.bean;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;

import com.mj.cafe.utils.AmountUtils;
import com.mj.cafe.utils.ViewUtils;

import java.io.Serializable;
import java.math.BigDecimal;

public class FoodBean implements Parcelable, Serializable {


	/**
	 * id : 1
	 * name : 美式咖啡
	 * logo : http://s8.sinaimg.cn/mw690/005OCzq7gy6UoJ6VcGz77
	 * price : 2500
	 * discount_price : 2200
	 * stock : 2341
	 */

	private Long id;
	private String name;
	private String logo;
	private Long price;
	private Long discount_price;
	private Long stock;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(long selectCount) {
		this.selectCount = selectCount;
	}

	private String type;//类
	private long selectCount;

	public BigDecimal getBigDecimalPrice() {
		if(discount_price!=null && discount_price!= 0){
			return AmountUtils.fen2Yuan(discount_price);
		}else{
			return AmountUtils.fen2Yuan(price);
		}
	}

	public SpannableString getStrPrice(Context context) {
		String priceStr = String.valueOf(getBigDecimalPrice());
		SpannableString spanString = new SpannableString("¥" + priceStr);
		AbsoluteSizeSpan span = new AbsoluteSizeSpan(ViewUtils.sp2px(context, 30));
		spanString.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		return spanString;
	}

	public SpannableString getStrPrice(Context context, BigDecimal price) {
		String priceStr = String.valueOf(price);
		SpannableString spanString = new SpannableString("¥" + priceStr);
		AbsoluteSizeSpan span = new AbsoluteSizeSpan(ViewUtils.sp2px(context, 30));
		spanString.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		return spanString;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getDiscount_price() {
		return discount_price;
	}

	public void setDiscount_price(Long discount_price) {
		this.discount_price = discount_price;
	}

	public Long getStock() {
		return stock;
	}

	public void setStock(Long stock) {
		this.stock = stock;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.id);
		dest.writeString(this.name);
		dest.writeString(this.logo);
		dest.writeValue(this.price);
		dest.writeValue(this.discount_price);
		dest.writeValue(this.stock);
		dest.writeString(this.type);
		dest.writeLong(this.selectCount);
	}

	public FoodBean() {
	}

	protected FoodBean(Parcel in) {
		this.id = (Long) in.readValue(Long.class.getClassLoader());
		this.name = in.readString();
		this.logo = in.readString();
		this.price = (Long) in.readValue(Long.class.getClassLoader());
		this.discount_price = (Long) in.readValue(Long.class.getClassLoader());
		this.stock = (Long) in.readValue(Long.class.getClassLoader());
		this.type = in.readString();
		this.selectCount = in.readLong();
	}

	public static final Creator<FoodBean> CREATOR = new Creator<FoodBean>() {
		@Override
		public FoodBean createFromParcel(Parcel source) {
			return new FoodBean(source);
		}

		@Override
		public FoodBean[] newArray(int size) {
			return new FoodBean[size];
		}
	};
}
