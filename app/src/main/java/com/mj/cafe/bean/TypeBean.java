package com.mj.cafe.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TypeBean implements Parcelable, Serializable {
	private Long id;
	private String logo;
	private String name;
	private List<FoodBean> goodsList;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public List<FoodBean> getGoodsList() {
		for(FoodBean bean : goodsList){
			bean.setType(this.name);
		}
		return goodsList;
	}

	public void setGoodsList(List<FoodBean> goodsList) {
		this.goodsList = goodsList;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.id);
		dest.writeString(this.logo);
		dest.writeString(this.name);
		dest.writeList(this.goodsList);
	}

	public TypeBean() {
	}

	protected TypeBean(Parcel in) {
		this.id = (Long) in.readValue(Long.class.getClassLoader());
		this.logo = in.readString();
		this.name = in.readString();
		this.goodsList = new ArrayList<FoodBean>();
		in.readList(this.goodsList, FoodBean.class.getClassLoader());
	}

	public static final Creator<TypeBean> CREATOR = new Creator<TypeBean>() {
		@Override
		public TypeBean createFromParcel(Parcel source) {
			return new TypeBean(source);
		}

		@Override
		public TypeBean[] newArray(int size) {
			return new TypeBean[size];
		}
	};
}
