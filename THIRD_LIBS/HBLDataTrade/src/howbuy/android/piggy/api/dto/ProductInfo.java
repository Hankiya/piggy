package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductInfo extends TopHeaderDto implements Parcelable {
	// "fundCode":"240001","lowestRedemption":1000.0,"minAcctVol":0.0,"oneYearBenefits":3.77,"sgbz":"1","shbz":"1","wfsy":0.0}
	public static final int flagSHEnable = 0;
	public static final int flagSHDisable = 1;
	public static final int flagSGEnable = 0;
	public static final int flagSGDisable = 1;
	private String fundCode;
	private String lowestRedemption;// 最低赎回份额
	private String minAcctVol;// 最低申购份额
	private String bankInterestRates;// 银行活期利率
	private int sgbz;// 申购状态
	private int shbz;// 赎回状态
	private String wfsy;// 万份收益
	private String qrsy;// 七日年化收益
	private String navDate;//MM-dd 七日年化收益

	
	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public String getLowestRedemption() {
		return lowestRedemption;
	}

	public void setLowestRedemption(String lowestRedemption) {
		this.lowestRedemption = lowestRedemption;
	}

	public String getMinAcctVol() {
		return minAcctVol;
	}

	public void setMinAcctVol(String minAcctVol) {
		this.minAcctVol = minAcctVol;
	}

	public String getBankInterestRates() {
		return bankInterestRates;
	}

	public void setBankInterestRates(String bankInterestRates) {
		this.bankInterestRates = bankInterestRates;
	}

	public int getSgbz() {
		return sgbz;
	}

	public void setSgbz(int sgbz) {
		this.sgbz = sgbz;
	}

	public int getShbz() {
		return shbz;
	}

	public void setShbz(int shbz) {
		this.shbz = shbz;
	}

	public String getWfsy() {
		return wfsy;
	}

	public void setWfsy(String wfsy) {
		this.wfsy = wfsy;
	}

	public String getQrsy() {
		return qrsy;
	}

	public void setQrsy(String qrsy) {
		this.qrsy = qrsy;
	}

	public String getNavDate() {
		return navDate;
	}

	public void setNavDate(String navDate) {
		this.navDate = navDate;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	@Override
	public String toString() {
		return "ProductInfo [fundCode=" + fundCode + ", lowestRedemption=" + lowestRedemption + ", minAcctVol=" + minAcctVol + ", bankInterestRates=" + bankInterestRates
				+ ", sgbz=" + sgbz + ", shbz=" + shbz + ", wfsy=" + wfsy + ", qrsy=" + qrsy + ", navDate=" + navDate + "]";
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(fundCode);
		dest.writeString(lowestRedemption);
		dest.writeString(minAcctVol);
		dest.writeInt(sgbz);
		dest.writeInt(shbz);
		dest.writeString(wfsy);
		dest.writeString(qrsy);
		dest.writeString(bankInterestRates);
		dest.writeString(contentMsg);
		dest.writeString(navDate);
		dest.writeInt(contentCode);
	}

	public static final Creator<ProductInfo> CREATOR = new Creator<ProductInfo>() {

		@Override
		public ProductInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			ProductInfo _bBean = new ProductInfo();
			_bBean.fundCode = source.readString();
			_bBean.lowestRedemption = source.readString();
			_bBean.minAcctVol = source.readString();
			_bBean.sgbz = source.readInt();
			_bBean.shbz = source.readInt();
			_bBean.wfsy = source.readString();
			_bBean.qrsy = source.readString();
			_bBean.navDate = source.readString();
			_bBean.bankInterestRates = source.readString();
			_bBean.contentMsg = source.readString();
			_bBean.contentCode = source.readInt();
			return _bBean;
		}

		@Override
		public ProductInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ProductInfo[size];
		}
	};

}
