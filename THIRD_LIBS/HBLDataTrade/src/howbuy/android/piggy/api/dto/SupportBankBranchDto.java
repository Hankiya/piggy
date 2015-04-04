package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * 分行
 * 
 * @ClassName: SupportBankBranchDto.java
 * @Description:
 * @author yescpu yes.cpu@gmail.com
 * @date 2013-9-27下午3:17:57
 */
public class SupportBankBranchDto implements Parcelable {

	private String cnapsNo; // 联行号
	private String bankCode; // 银行号
	private String provCode; // 省份号
	private String cityCode; // 城市号
	private String provName; // 省份名
	private String cityName; // 城市名
	private String bankBranchName; // 支行名称
	private String bankBranchFullName; // 支行全称

	public String getCnapsNo() {
		return cnapsNo;
	}

	public void setCnapsNo(String cnapsNo) {
		this.cnapsNo = cnapsNo;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getProvCode() {
		return provCode;
	}

	public void setProvCode(String provCode) {
		this.provCode = provCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getProvName() {
		return provName;
	}

	public void setProvName(String provName) {
		this.provName = provName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getBankBranchName() {
		return bankBranchName;
	}

	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}

	public String getBankBranchFullName() {
		return bankBranchFullName;
	}

	public void setBankBranchFullName(String bankBranchFullName) {
		this.bankBranchFullName = bankBranchFullName;
	}

	@Override
	public String toString() {
		return "SupportBankBranchDto [cnapsNo=" + cnapsNo + ", bankCode=" + bankCode + ", provCode=" + provCode + ", cityCode=" + cityCode + ", provName=" + provName
				+ ", cityName=" + cityName + ", bankBranchName=" + bankBranchName + ", bankBranchFullName=" + bankBranchFullName + "]";
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(cnapsNo);
		dest.writeString(bankCode);
		dest.writeString(provCode);
		dest.writeString(cityCode);
		dest.writeString(provName);
		dest.writeString(cityName);
		dest.writeString(bankBranchName);
		dest.writeString(bankBranchFullName);

	}

	public static final Creator<SupportBankBranchDto> CREATOR = new Creator<SupportBankBranchDto>() {

		@Override
		public SupportBankBranchDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			SupportBankBranchDto _newsListBean = new SupportBankBranchDto();
			_newsListBean.cnapsNo = source.readString();
			_newsListBean.bankCode = source.readString();
			_newsListBean.provCode = source.readString();
			_newsListBean.cityCode = source.readString();
			_newsListBean.provName = source.readString();
			_newsListBean.cityName = source.readString();
			_newsListBean.bankBranchName = source.readString();
			_newsListBean.bankBranchFullName = source.readString();
			return _newsListBean;
		}

		@Override
		public SupportBankBranchDto[] newArray(int size) {
			// TODO Auto-generated method stub
			return new SupportBankBranchDto[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}