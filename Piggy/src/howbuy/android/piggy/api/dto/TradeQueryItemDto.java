package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 交易查询item
 * 
 * @ClassName: TradeQueryItemDto.java
 * @Description:
 * @author yescpu yes.cpu@gmail.com
 * @date 2013-10-25下午5:13:52
 */
public class TradeQueryItemDto implements Parcelable {
	private String contractNo;// 交易Id(合同号)
	private String tradeType;// 交易类型
	private String tradeBal;// 交易金额
	private String taTradeDt;// 交易日期2
	private String tradeDt;// 交易日期1
	
	private String optDt;// 操作日期
	private String txPmtFlag;// 付款状态
	private String txAckFlag;// 确认状态
	private String txStatus;// 交易状态
	private String bankAcct;// 银行账号
	private String bankName;// 银行名称
	private String arriveDay;// 到账时间
	private String incomeDate;// 收益产生日期(申购)
	private String toAccountDate;// 预计到账时间（赎回）
	private String bankCode;//银行卡code
	private String tradeTm;//交易时间
	

	
	

	@Override
	public String toString() {
		return "TradeQueryItemDto [contractNo=" + contractNo + ", tradeType=" + tradeType + ", tradeBal=" + tradeBal + ", taTradeDt=" + taTradeDt + ", tradeDt=" + tradeDt
				+ ", optDt=" + optDt + ", txPmtFlag=" + txPmtFlag + ", txAckFlag=" + txAckFlag + ", txStatus=" + txStatus + ", bankAcct=" + bankAcct + ", bankName=" + bankName
				+ ", arriveDay=" + arriveDay + ", incomeDate=" + incomeDate + ", toAccountDate=" + toAccountDate + ", bankCode=" + bankCode + ", tradeTm=" + tradeTm + "]";
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeBal() {
		return tradeBal;
	}

	public void setTradeBal(String tradeBal) {
		this.tradeBal = tradeBal;
	}

	public String getTaTradeDt() {
		return taTradeDt;
	}

	public void setTaTradeDt(String taTradeDt) {
		this.taTradeDt = taTradeDt;
	}

	public String getOptDt() {
		return optDt;
	}

	public void setOptDt(String optDt) {
		this.optDt = optDt;
	}

	public String getTxPmtFlag() {
		return txPmtFlag;
	}

	public void setTxPmtFlag(String txPmtFlag) {
		this.txPmtFlag = txPmtFlag;
	}

	public String getTxAckFlag() {
		return txAckFlag;
	}

	public void setTxAckFlag(String txAckFlag) {
		this.txAckFlag = txAckFlag;
	}

	public String getTxStatus() {
		return txStatus;
	}

	public void setTxStatus(String txStatus) {
		this.txStatus = txStatus;
	}

	public String getBankAcct() {
		return bankAcct;
	}

	public void setBankAcct(String bankAcct) {
		this.bankAcct = bankAcct;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getArriveDay() {
		return arriveDay;
	}

	public void setArriveDay(String arriveDay) {
		this.arriveDay = arriveDay;
	}

	public String getIncomeDate() {
		return incomeDate;
	}

	public void setIncomeDate(String incomeDate) {
		this.incomeDate = incomeDate;
	}

	public String getToAccountDate() {
		return toAccountDate;
	}

	public void setToAccountDate(String toAccountDate) {
		this.toAccountDate = toAccountDate;
	}
	
	

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	

	public String getTradeTm() {
		return tradeTm;
	}




	public void setTradeTm(String tradeTm) {
		this.tradeTm = tradeTm;
	}


	


	public String getTradeDt() {
		return tradeDt;
	}

	public void setTradeDt(String tradeDt) {
		this.tradeDt = tradeDt;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.contractNo);
		dest.writeString(this.tradeType);
		dest.writeString(this.tradeBal);
		dest.writeString(this.taTradeDt);
		dest.writeString(this.optDt);
		dest.writeString(this.txPmtFlag);
		dest.writeString(this.txAckFlag);
		dest.writeString(this.txStatus);
		dest.writeString(this.bankAcct);
		dest.writeString(this.bankName);
		dest.writeString(this.arriveDay);
		dest.writeString(this.incomeDate);
		dest.writeString(this.toAccountDate);
		dest.writeString(this.bankCode);
		dest.writeString(this.tradeTm);
		dest.writeString(this.tradeDt);
	}

	public TradeQueryItemDto() {
	}

	private TradeQueryItemDto(Parcel in) {
		this.contractNo = in.readString();
		this.tradeType = in.readString();
		this.tradeBal = in.readString();
		this.taTradeDt = in.readString();
		this.optDt = in.readString();
		this.txPmtFlag = in.readString();
		this.txAckFlag = in.readString();
		this.txStatus = in.readString();
		this.bankAcct = in.readString();
		this.bankName = in.readString();
		this.arriveDay = in.readString();
		this.incomeDate = in.readString();
		this.toAccountDate = in.readString();
		this.bankCode=in.readString();
		this.tradeTm=in.readString();
		this.tradeDt=in.readString();
	}

	public static final Parcelable.Creator<TradeQueryItemDto> CREATOR = new Parcelable.Creator<TradeQueryItemDto>() {
		public TradeQueryItemDto createFromParcel(Parcel source) {
			return new TradeQueryItemDto(source);
		}

		public TradeQueryItemDto[] newArray(int size) {
			return new TradeQueryItemDto[size];
		}
	};
}
