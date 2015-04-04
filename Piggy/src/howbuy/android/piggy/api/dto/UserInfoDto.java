package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户信息
 * 
 * @ClassName: UserInfoDto.java
 * @Description:
 * @author yescpu yes.cpu@gmail.com
 * @date 2013-10-12上午10:38:43
 */
public class UserInfoDto extends TopHeaderDto implements Parcelable {
	public static final String Flag_ISHBSIGN = "isHbsign";
	public static final String Flag_BANKACCT = "bankAcct";
	public static final String Flag_BANKACCTUN = "1";
	public static final String Flag_BANKACCTSuccess = "2";
	public static final String Flag_BANKACCTClose = "3";
	
//	private String custName;// 用户名称
//	private String holdBalance; //用户资产
//	private String totalIncome;//总资产
//	private String piggyBalance;
//	private String piggyIncome;
//	private ArrayList<String> unconfirmAmt;//申购未确认
//	private ArrayList<String> unconfirmVol;//赎回未确认
//	private String riskLevel;// 用户状态
//  private String lastLoginTime;
//	private String lastLoginLocation;
	
	
	
	private String custName;// 用户名称
	private String balanceAmt;//储蓄罐资产
	private String avaliAmt;//储蓄罐可用余额
	private String unSettleAmt;//未结转收益总额
	private String settledAmt;//已发放收益
	private String yesterdayIncome;//昨日收益
	private String navDt;//最新收益日期
	private String unconfirmAmt;//申购未确认金额
	private String unconfirmAmtCount;//申购未确认笔数
	private String unconfirmVol;//赎回未确认份额
	private String unconfirmVolCount;//赎回未确认笔数
	

	

	public String getCustName() {
		return custName;
	}


	public String getBalanceAmt() {
		return balanceAmt;
	}


	public String getAvaliAmt() {
		return avaliAmt;
	}


	public String getUnSettleAmt() {
		return unSettleAmt;
	}


	public String getSettledAmt() {
		return settledAmt;
	}


	public String getYesterdayIncome() {
		return yesterdayIncome;
	}


	public String getNavDt() {
		return navDt;
	}


	public String getUnconfirmAmt() {
		return unconfirmAmt;
	}


	public String getUnconfirmAmtCount() {
		return unconfirmAmtCount;
	}


	public String getUnconfirmVol() {
		return unconfirmVol;
	}


	public String getUnconfirmVolCount() {
		return unconfirmVolCount;
	}


	public void setCustName(String custName) {
		this.custName = custName;
	}


	public void setBalanceAmt(String balanceAmt) {
		this.balanceAmt = balanceAmt;
	}


	public void setAvaliAmt(String avaliAmt) {
		this.avaliAmt = avaliAmt;
	}


	public void setUnSettleAmt(String unSettleAmt) {
		this.unSettleAmt = unSettleAmt;
	}


	public void setSettledAmt(String settledAmt) {
		this.settledAmt = settledAmt;
	}


	public void setYesterdayIncome(String yesterdayIncome) {
		this.yesterdayIncome = yesterdayIncome;
	}


	public void setNavDt(String navDt) {
		this.navDt = navDt;
	}


	public void setUnconfirmAmt(String unconfirmAmt) {
		this.unconfirmAmt = unconfirmAmt;
	}


	public void setUnconfirmAmtCount(String unconfirmAmtCount) {
		this.unconfirmAmtCount = unconfirmAmtCount;
	}


	public void setUnconfirmVol(String unconfirmVol) {
		this.unconfirmVol = unconfirmVol;
	}


	public void setUnconfirmVolCount(String unconfirmVolCount) {
		this.unconfirmVolCount = unconfirmVolCount;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(contentMsg);
		dest.writeInt(contentCode);
		dest.writeString(custName);
		dest.writeString(balanceAmt);
		dest.writeString(avaliAmt);
		dest.writeString(unSettleAmt);
		dest.writeString(settledAmt);
		dest.writeString(yesterdayIncome);
		dest.writeString(navDt);
		dest.writeString(unconfirmAmt);
		dest.writeString(unconfirmAmtCount);
		dest.writeString(unconfirmVol);
		dest.writeString(unconfirmVolCount);
	}

	public static final Creator<UserInfoDto> CREATOR = new Creator<UserInfoDto>() {

		@Override
		public UserInfoDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			
			UserInfoDto oDto = new UserInfoDto();
			oDto.contentCode = source.readInt();
			oDto.contentMsg = source.readString();
			oDto.custName = source.readString();
			oDto.balanceAmt=source.readString();
			oDto.avaliAmt=source.readString();
			oDto.unSettleAmt=source.readString();
			oDto.settledAmt=source.readString();
			oDto.yesterdayIncome=source.readString();
			oDto.navDt=source.readString();
			oDto.unconfirmAmt=source.readString();
			oDto.unconfirmAmtCount=source.readString();
			oDto.unconfirmVol=source.readString();
			oDto.unconfirmVolCount=source.readString();
			
			
			return oDto;
		}

		@Override
		public UserInfoDto[] newArray(int size) {
			// TODO Auto-generated method stub
			return new UserInfoDto[size];
		}
	};


	

	

	@Override
	public String toString() {
		return "UserInfoDto [custName=" + custName + ", balanceAmt=" + balanceAmt + ", avaliAmt=" + avaliAmt + ", unSettleAmt=" + unSettleAmt + ", settledAmt=" + settledAmt
				+ ", yesterdayIncome=" + yesterdayIncome + ", navDt=" + navDt + ", unconfirmAmt=" + unconfirmAmt + ", unconfirmAmtCount=" + unconfirmAmtCount + ", unconfirmVol="
				+ unconfirmVol + ", unconfirmVolCount=" + unconfirmVolCount + "]";
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
	
}
