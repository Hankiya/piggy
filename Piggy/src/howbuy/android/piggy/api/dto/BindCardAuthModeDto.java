package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 绑卡和鉴权返回值
 * 
 * @author Administrator
 * 
 */

public class BindCardAuthModeDto implements Parcelable {
	/**
	 * b2c鉴权 cp
	 */
	public static final String Auth_B2c = "00";
	
	/**
	 * 小额打款
	 */
	public static final String Auth_DaKuang = "01";
	/**
	 * 微信鉴权
	 */
	public static final String Auth_WeChat = "02";
	
	
	public static final String DEFAULT1 = "1";

	private String authMode;// 鉴权方式:00-B2C, 01-小额打款, 02-微信
	private String payChlCode;// 验证渠道
	private String isDefault;// 1:默认渠道；0或不传为非默认结构

	public String getAuthMode() {
		return authMode;
	}

	public void setAuthMode(String authMode) {
		this.authMode = authMode;
	}

	public String getPayChlCode() {
		return payChlCode;
	}

	public void setPayChlCode(String payChlCode) {
		this.payChlCode = payChlCode;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.authMode);
		dest.writeString(this.payChlCode);
		dest.writeString(this.isDefault);
	}

	public BindCardAuthModeDto() {
	}

	private BindCardAuthModeDto(Parcel in) {
		this.authMode = in.readString();
		this.payChlCode = in.readString();
		this.isDefault = in.readString();
	}

	public static final Parcelable.Creator<BindCardAuthModeDto> CREATOR = new Parcelable.Creator<BindCardAuthModeDto>() {
		public BindCardAuthModeDto createFromParcel(Parcel source) {
			return new BindCardAuthModeDto(source);
		}

		public BindCardAuthModeDto[] newArray(int size) {
			return new BindCardAuthModeDto[size];
		}
	};
	

	@Override
	public String toString() {
		return "BindCardAuthModeDto [authMode=" + authMode + ", payChlCode=" + payChlCode + ", isDefault=" + isDefault + "]";
	}}
