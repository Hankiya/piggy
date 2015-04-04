package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SupportBankDto implements Parcelable {

	/**
	 * 银行代码
	 */
	private String code;

	/**
	 * 银行名称
	 */
	private String bankName;

	/**
	 * 单笔最大限额
	 */
	private String maxSingleQuota;

	/**
	 * 日累计
	 */
	private String dayTotal;

	// start:add by renzh
	/**
	 * 使用的验卡通道
	 */
	private String payChannel = null;

	// end:add by renzh

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName
	 *            the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @return the maxSingleQuota
	 */
	public String getMaxSingleQuota() {
		return maxSingleQuota;
	}

	/**
	 * @param maxSingleQuota
	 *            the maxSingleQuota to set
	 */
	public void setMaxSingleQuota(String maxSingleQuota) {
		this.maxSingleQuota = maxSingleQuota;
	}

	/**
	 * @return the dayTotal
	 */
	public String getDayTotal() {
		return dayTotal;
	}

	/**
	 * @param dayTotal
	 *            the dayTotal to set
	 */
	public void setDayTotal(String dayTotal) {
		this.dayTotal = dayTotal;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	 
	@Override
	public String toString() {
		return "SupportBankDto [code=" + code + ", bankName=" + bankName + ", maxSingleQuota=" + maxSingleQuota + ", dayTotal=" + dayTotal + ", payChannel=" + payChannel + "]";
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(code);
		dest.writeString(bankName);
		dest.writeString(maxSingleQuota);
		dest.writeString(dayTotal);
		dest.writeString(payChannel);
	}

	public static final Creator<SupportBankDto> CREATOR = new Creator<SupportBankDto>() {

		@Override
		public SupportBankDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			SupportBankDto _newsListBean = new SupportBankDto();
			_newsListBean.code = source.readString();
			_newsListBean.bankName = source.readString();
			_newsListBean.maxSingleQuota = source.readString();
			_newsListBean.dayTotal = source.readString();
			_newsListBean.payChannel=source.readString();
			return _newsListBean;
		}

		@Override
		public SupportBankDto[] newArray(int size) {
			// TODO Auto-generated method stub
			return new SupportBankDto[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}