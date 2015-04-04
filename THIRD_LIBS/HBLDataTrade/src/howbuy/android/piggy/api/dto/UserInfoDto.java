package howbuy.android.piggy.api.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

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
	private String acctIdentifyStat;// 鉴权状态（1-未鉴权；2-鉴权中；3-鉴权失败；4-鉴权通过）
	private String bankAcct;// 银行账户
	private String bankAcctVrfyStat;// 银行账户验证状态（1-未验证，2-验证通过，3-验证失败）
	private String bankName;// 银行名称
	private String custName;// 用户名称
	private String bankCode;// 银行code
	private int bankCardIsBand;// 是否绑定银行卡
	private String holdBalance; //用户资产
	private String income; //用户未结转收益
	private String asset;//用户账户名额
	private String protocalNo;
	private ArrayList<String> unconfirmAmt;//申购未确认
	private ArrayList<String> unconfirmVol;//赎回未确认
	private Map<String, String> hbPmtOpenFlag;//isHbsign（是否好买签约1:是）、bankAcct（银行账号）
	private String issuedEarnings;//已发放收益
	private String yesterdayEarnings;//昨日收益
	private String notice;//通知
	private String lastEarningsDay;//最新收益日期
	

	private String provinceNo;//省份号（省份代码）
	private String subBankCode;//支行号（支行代码）
	private String subBankName;//支行名称
	
	public String getProvinceNo() {
		return provinceNo;
	}


	public void setProvinceNo(String provinceNo) {
		this.provinceNo = provinceNo;
	}


	public String getSubBankCode() {
		return subBankCode;
	}


	public void setSubBankCode(String subBankCode) {
		this.subBankCode = subBankCode;
	}


	public String getSubBankName() {
		return subBankName;
	}


	public void setSubBankName(String subBankName) {
		this.subBankName = subBankName;
	}


	
	
	public String getAcctIdentifyStat() {
		return acctIdentifyStat;
	}


	public void setAcctIdentifyStat(String acctIdentifyStat) {
		this.acctIdentifyStat = acctIdentifyStat;
	}


	public String getBankAcct() {
		return bankAcct;
	}


	public void setBankAcct(String bankAcct) {
		this.bankAcct = bankAcct;
	}


	public String getBankAcctVrfyStat() {
		return bankAcctVrfyStat;
	}


	public void setBankAcctVrfyStat(String bankAcctVrfyStat) {
		this.bankAcctVrfyStat = bankAcctVrfyStat;
	}


	public String getBankName() {
		return bankName;
	}


	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


	public String getCustName() {
		return custName;
	}


	public void setCustName(String custName) {
		this.custName = custName;
	}


	public String getBankCode() {
		return bankCode;
	}


	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}


	public int getBankCardIsBand() {
		return bankCardIsBand;
	}


	public void setBankCardIsBand(int bankCardIsBand) {
		this.bankCardIsBand = bankCardIsBand;
	}


	public String getHoldBalance() {
		return holdBalance;
	}


	public void setHoldBalance(String holdBalance) {
		this.holdBalance = holdBalance;
	}


	public String getIncome() {
		return income;
	}


	public void setIncome(String income) {
		this.income = income;
	}


	public String getAsset() {
		return asset;
	}


	public void setAsset(String asset) {
		this.asset = asset;
	}


	public String getProtocalNo() {
		return protocalNo;
	}


	public void setProtocalNo(String protocalNo) {
		this.protocalNo = protocalNo;
	}


	public ArrayList<String> getUnconfirmAmt() {
		return unconfirmAmt;
	}


	public void setUnconfirmAmt(ArrayList<String> unconfirmAmt) {
		this.unconfirmAmt = unconfirmAmt;
	}


	public ArrayList<String> getUnconfirmVol() {
		return unconfirmVol;
	}


	public void setUnconfirmVol(ArrayList<String> unconfirmVol) {
		this.unconfirmVol = unconfirmVol;
	}


	public Map<String, String> getHbPmtOpenFlag() {
		return hbPmtOpenFlag;
	}


	public void setHbPmtOpenFlag(Map<String, String> hbPmtOpenFlag) {
		this.hbPmtOpenFlag = hbPmtOpenFlag;
	}


	public String getIssuedEarnings() {
		return issuedEarnings;
	}


	public void setIssuedEarnings(String issuedEarnings) {
		this.issuedEarnings = issuedEarnings;
	}


	public String getYesterdayEarnings() {
		return yesterdayEarnings;
	}


	public void setYesterdayEarnings(String yesterdayEarnings) {
		this.yesterdayEarnings = yesterdayEarnings;
	}


	public String getNotice() {
		return notice;
	}


	public void setNotice(String notice) {
		this.notice = notice;
	}
	

	public String getLastEarningsDay() {
		return lastEarningsDay;
	}


	public void setLastEarningsDay(String lastEarningsDay) {
		this.lastEarningsDay = lastEarningsDay;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(contentMsg);
		dest.writeInt(contentCode);
		dest.writeString(acctIdentifyStat);
		dest.writeString(bankAcct);
		dest.writeString(bankAcctVrfyStat);
		dest.writeString(bankName);
		dest.writeString(custName);
		dest.writeString(bankCode);
		dest.writeString(bankCode);
		dest.writeString(holdBalance);
		dest.writeString(income);
		dest.writeString(asset);
		dest.writeString(protocalNo);
		dest.writeString(notice);
		dest.writeString(issuedEarnings);
		dest.writeString(yesterdayEarnings);
		dest.writeString(lastEarningsDay);
		dest.writeString(provinceNo);
		dest.writeString(subBankCode);
		dest.writeString(subBankName);
		dest.writeInt(hbPmtOpenFlag.size());
		  for(String key : hbPmtOpenFlag.keySet()){
			  dest.writeString(key);
			  dest.writeString(hbPmtOpenFlag.get(key));
		  }
	}

	public static final Creator<UserInfoDto> CREATOR = new Creator<UserInfoDto>() {

		@Override
		public UserInfoDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			UserInfoDto oDto = new UserInfoDto();
			oDto.contentCode = source.readInt();
			oDto.contentMsg = source.readString();
			oDto.acctIdentifyStat = source.readString();
			oDto.bankAcct = source.readString();
			oDto.bankAcctVrfyStat = source.readString();
			oDto.bankName = source.readString();
			oDto.custName = source.readString();
			oDto.bankCode = source.readString();
			oDto.notice = source.readString();
			oDto.holdBalance = source.readString();
			oDto.asset = source.readString();
			oDto.income = source.readString();
			oDto.protocalNo=source.readString();
			oDto.issuedEarnings=source.readString();
			oDto.yesterdayEarnings=source.readString();
			oDto.lastEarningsDay=source.readString();
			oDto.provinceNo=source.readString();
			oDto.subBankCode=source.readString();
			oDto.subBankName=source.readString();
			int size = source.readInt();
			if (oDto.hbPmtOpenFlag==null) {
				oDto.hbPmtOpenFlag=new HashMap<String, String>();
			}
			  for(int i = 0; i < size; i++){
			    String key = source.readString();
			    String value = source.readString();
			    oDto.hbPmtOpenFlag.put(key,value);
			  }
			
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
		return "UserInfoDto [acctIdentifyStat=" + acctIdentifyStat + ", bankAcct=" + bankAcct + ", bankAcctVrfyStat=" + bankAcctVrfyStat + ", bankName=" + bankName + ", custName="
				+ custName + ", bankCode=" + bankCode + ", bankCardIsBand=" + bankCardIsBand + ", holdBalance=" + holdBalance + ", income=" + income + ", asset=" + asset
				+ ", protocalNo=" + protocalNo + ", unconfirmAmt=" + unconfirmAmt + ", unconfirmVol=" + unconfirmVol + ", hbPmtOpenFlag=" + hbPmtOpenFlag + ", issuedEarnings="
				+ issuedEarnings + ", yesterdayEarnings=" + yesterdayEarnings + ", notice=" + notice + ", lastEarningsDay=" + lastEarningsDay + ", provinceNo=" + provinceNo 
				+ ", subBankCode=" + subBankCode + ", subBankName=" + subBankName +"]";
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
	
}
