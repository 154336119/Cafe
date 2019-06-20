package com.mj.cafe.retorfit.exception;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gifford on 2017/12/27.
 */

public class ResponseExceptionEventArgs implements Parcelable {
	private int errCode;
	private String tips;

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public ResponseExceptionEventArgs(int errCode, String tips) {
		this.errCode = errCode;
		this.tips = tips;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.errCode);
		dest.writeString(this.tips);
	}

	protected ResponseExceptionEventArgs(Parcel in) {
		this.errCode = in.readInt();
		this.tips = in.readString();
	}

	public static final Parcelable.Creator<ResponseExceptionEventArgs> CREATOR = new Parcelable.Creator<ResponseExceptionEventArgs>() {
		@Override
		public ResponseExceptionEventArgs createFromParcel(Parcel source) {
			return new ResponseExceptionEventArgs(source);
		}

		@Override
		public ResponseExceptionEventArgs[] newArray(int size) {
			return new ResponseExceptionEventArgs[size];
		}
	};
}
