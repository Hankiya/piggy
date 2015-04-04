package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;

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
	 * 验证费用（从银行卡扣除金额）
	 */
	private String checkCount;
	/**
	 * 银行客服电话
	 */
	private String spNumber;
	/**
	 * 单笔支付限额
	 */
	private String limitPerTime;
	/**
	 * 日限额
	 */
	private String limitPerDay;

	public String getCode() {
		return code;
	}

	public String getBankName() {
		return bankName;
	}

	public String getCheckCount() {
		return checkCount;
	}

	public String getSpNumber() {
		return spNumber;
	}

	public String getLimitPerTime() {
		return limitPerTime;
	}

	public String getLimitPerDay() {
		return limitPerDay;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setCheckCount(String checkCount) {
		this.checkCount = checkCount;
	}

	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}

	public void setLimitPerTime(String limitPerTime) {
		this.limitPerTime = limitPerTime;
	}

	public void setLimitPerDay(String limitPerDay) {
		this.limitPerDay = limitPerDay;
	}

	

	@Override
	public String toString() {
		return "SupportBankDto [code=" + code + ", bankName=" + bankName + ", checkCount=" + checkCount + ", spNumber=" + spNumber + ", limitPerTime=" + limitPerTime
				+ ", limitPerDay=" + limitPerDay + "]";
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(code);
		dest.writeString(bankName);
		dest.writeString(checkCount);
		dest.writeString(spNumber);
		dest.writeString(limitPerTime);
		dest.writeString(limitPerDay);
	}

	public static final Creator<SupportBankDto> CREATOR = new Creator<SupportBankDto>() {

		@Override
		public SupportBankDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			SupportBankDto _newsListBean = new SupportBankDto();
			_newsListBean.code = source.readString();
			_newsListBean.bankName = source.readString();
			_newsListBean.checkCount = source.readString();
			_newsListBean.spNumber = source.readString();
			_newsListBean.limitPerTime = source.readString();
			_newsListBean.limitPerDay = source.readString();
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